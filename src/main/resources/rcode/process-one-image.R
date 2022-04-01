library(imager)
library(plyr)
library(EBImage)
library(imagerExtra)
library(dbscan)
memory.limit(10000000000000)

HOS <- function(channel){
  rescale <- channel *255

  # calculate number of row and column
  a = nrow(rescale); b = ncol(rescale)

  m_xy <- matrix(data=NA, nrow=a, ncol=b)
  system.time(
    for (i in 2:(a-1)){
      for (j in 2:(b-1)){
        I_xy <- c(rescale[i-1,j-1], rescale[i,j-1], rescale[i+1,j-1],
                  rescale[i-1,j], rescale[i+1,j],
                  rescale[i-1,j+1], rescale[i,j+1], rescale[i+1,j+1])
        m_xy[i,j] <- 1/length(I_xy) * sum((I_xy - mean(I_xy))^4)
      }
    }
  )

  for (i in 2:(a-1)){
    ## four sides
    # left
    I_left <- c(rescale[i-1,1],rescale[i-1,2],rescale[i,2],rescale[i+1,2],rescale[i+1,1])
    m_xy[i,1] <- 1/length(I_left)* sum((I_left - mean(I_left))^4)
    # right
    I_right <- c(rescale[i-1,b],rescale[i-1,b-1],rescale[i,b-1],rescale[i+1,b-1],rescale[i+1,b])
    m_xy[i,b] <- 1/length(I_right)* sum((I_right - mean(I_right))^4)
  }

  for (j in 2:(b-1)){
    ## four sides
    # upper
    I_upper <- c(rescale[1,j-1],rescale[2,j-1],rescale[2,j],rescale[2,j+1],rescale[1,j+1])
    m_xy[1,j] <- 1/length(I_upper)* sum((I_upper - mean(I_upper))^4)
    # bottom
    I_bottom <- c(rescale[a,j-1],rescale[a-1,j-1],rescale[a-1,j],rescale[a-1,j+1],rescale[a,j+1])
    m_xy[a,j] <- 1/length(I_bottom)* sum((I_bottom - mean(I_bottom))^4)
  }

  # four corners
  I_corner1 <- c(rescale[2,1],rescale[1,2],rescale[2,2])
  m_xy[1,1] <- 1/length(I_corner1)* sum((I_corner1 - mean(I_corner1))^4)
  I_corner2 <- c(rescale[a-1,1],rescale[a-1,2],rescale[a,2])
  m_xy[a,1] <- 1/length(I_corner2)* sum((I_corner2 - mean(I_corner2))^4)
  I_corner3 <- c(rescale[1,b-1],rescale[2,b-1],rescale[2,b])
  m_xy[1,b] <- 1/length(I_corner3)* sum((I_corner3 - mean(I_corner3))^4)
  I_corner4 <- c(rescale[a-1,b-1],rescale[a,b-1],rescale[a-1,b])
  m_xy[a,b] <- 1/length(I_corner4)* sum((I_corner4 - mean(I_corner4))^4)

  return(m_xy)
}

## suffix ： .jpg .png
im_process <- function(label, suffix, load.path, out.path, d.thr, flood, fill, obj.thr, stained.thr){

    im <- load.image(paste0(load.path, "/" ,label, suffix))

    im.g <- grayscale(im)
    invert <- 1-im.g
    g2d1 <- invert[,,1,1]

    a = nrow(g2d1); b = ncol(g2d1)

    # HOS
    m_xy <- HOS(channel= g2d1)
    for (i in 1:nrow(m_xy)){
      for (j in 1:ncol(m_xy)){
        m_xy[i,j] <- min(255, m_xy[i,j]/300)
      }
    }

    ### compute coordinate index given a pixset
    m_ind <- coord.index(as.cimg(m_xy), which(as.cimg(m_xy) > d.thr))
    m_ind <- m_ind[,c("x","y")]

    ### calculate local density
    d <- pointdensity(m_ind, eps = 2, type = "density")
    # normalize it to 0-1
    dnorm <- (d - min(d))/(max(d) - min(d))
    # filter noise if the normalized local density less than 10%
    m_nonoise <- m_ind[which(dnorm >= 0.95), ]

    ### create new pixset which all values =0
    d.select <- matrix(rep(0, a*b), nrow=a, ncol=b)
    # compute vector index of a pixel given its coordinates
    px <- index.coord(as.cimg(d.select), m_nonoise)
    d.select[px] <- 1


    ROI.connect <- imager::fill(as.cimg(d.select), fill)

    # whether fill connected area or not
    if (flood=="Y"){
      ROI.fill <- EBImage::fillHull(ROI.connect)
    } else if (flood=="N"){
      ROI.fill <- ROI.connect
      }


    ROI_label <- bwlabel(ROI.fill)
    table <- table(ROI_label)

    # select objects (their index) whose area account for more than x% of the whole objects area
    cutoff <- sum(table[-1])*obj.thr
    obj_select <- table[which(table > cutoff & table != table[1])]
    obj_select_index <- as.numeric(names(obj_select))

    # select objects in original ROI_label according to their index
    selectROI <- list()
    for (k in 1:length(obj_select_index)){
      selectROI[[k]] <- ROI_label==obj_select_index[k]
    }
    ## compile the list of selectROI
    #1. turn True/False to 1/0
    selectROI2d <- list()
    if_select <- list()
    for (k in 1:length(selectROI)){
      selectROI2d[[k]] <- selectROI[[k]][,,1,1]
      if_select[[k]] <- ifelse(selectROI2d[[k]]==T, 1, 0)
    }
    #2. sum the list of matrices to one
    if_select2 <- Reduce('+', if_select)
    # convert it to cimg and display
    im.select <- as.cimg(if_select2)
    save.image(im.select, paste0(out.path, label, "-total.jpg"))

    ### make pixel values outside ROI equal to 0 on original rescaled image
    ROI2d <- g2d1 * 255
    ROI2d[which(if_select2==0)] <- 0

    ### number of pixels in ROI area
    ROI_area <- length(which(ROI2d!=0))
    ### sum of intensities in ROI area
    ROI_intensity <- sum(ROI2d[which(ROI2d!=0)])


    ##############################################################
    ############  positive stained area
    ##############################################################
    ### thresholding within ROI area
    # Alizarin Red: >100; Von Kossa: > 140
    stained <- as.cimg(ifelse(g2d1> stained.thr/255 & ROI2d!=0, 1, 0))
    save.image(stained, paste0(out.path, label, "-stained.jpg"))

    ### number of pixels in stained area (excluding area outside of ROI)
    stained_area <- length(which(stained!=0))
    ### sum of intensities in stained area
    stained_intensity <- sum(ROI2d[which(stained!=0)])

    ##### combine result from ROI and stained area
    stats <- data.frame(ROI_area, ROI_intensity,
                         stained_area, stained_intensity)
    singleStats <- cbind(label, stats)
    ##### 创建out_stats
    write.csv(singleStats, paste0(out.Path, "/out_stats/", label, "-out_stats.csv"))
    return(singleStats)
}
