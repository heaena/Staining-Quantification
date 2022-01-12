library(imager)
library(opencv)
library(secr)
library(plyr)
library(wvtool)
library(TSP)
library(pracma)
library(EBImage)
library(rjson)

########################################################################
############  create user-defined function to image processing in loop
########################################################################
im_process <- function(inputFile, outputPath, param){

  ### check param
  print(param)
  jsonParam <- fromJSON(param)
  ROI.fill.thr <- jsonParam$ROI.fill.thr
  stained.thr <- jsonParam$stained.thr
  if(is.null(ROI.fill.thr)){
    ROI.fill.thr <- 7
  }else{
    ROI.fill.thr <- as.numeric(ROI.fill.thr)
  }
  if(is.null(stained.thr)){
    stained.thr <- 0.7
  }else{
    stained.thr <- as.numeric(stained.thr)
  }
  sourceFileName <- basename(inputFile)

  ### load image
  im <- load.image(inputFile)

  ### make image grayscaled
  im.g <- grayscale(im)
  ### invert
  im.g <- 1-im.g
  ### extract 2D matrix
  im.g2d <- im.g[,,1,1]
  ### rescale from 0-1 to 0-255
  im.g_rescale <- im.g * 255

  ##############################################################
  ############  ROI: select the whole interest area
  ##############################################################
  ### thresholding
  ROI <- thresh(im.g)
  # fill ROI area for a little bit
  ROI.fill <- fill(ROI, ROI.fill.thr)

  ### make a contour that contains the whole ROI area (as much as possible)
  cont_list <- contours(ROI.fill, nlevels=1)

  get_max_area <- function(cont_list){
    purrr::map(cont_list, function(li, x, y) abs(pracma::polyarea(li$x, li$y)))
  }

  areas <- get_max_area(cont_list)

  # get 1st contour
  cont1 <- data.frame(cont_list[[which.max(areas)]])[,c("x","y")]

  # side value
  x_left = 1
  y_upper = 1
  x_right = max(nrow(im.g2d))
  y_bottom = max(ncol(im.g2d)) # y-axis is inverse in plot

  ###step1 judge how many contours needed to be added up
  t1 <- data.frame(c(any(cont1[,"x"]==1),
                     any(cont1[,"y"]==1),
                     any(cont1[,"x"]==x_right),
                     any(cont1[,"y"]==y_bottom)))
  if (length(which(t1==FALSE))==4){ #case1:enclosed
    cont <- cont1
  } else if (length(which(t1==TRUE))==1){ #case2.1:one contour, one side
    cont <- cont1
  } else if (length(which(t1==TRUE))==2){ #case2.2:one contour, two sides (cont_add needed)
    # find cont_add by looking for contour(s) touches two sides from cont_list2
    t1.2 <- matrix(rep(NA, 4*length(cont_list)), ncol=4)
    num_touched_side <- NULL
    cont_list2 <- cont_list
    cont_list2[[which.max(areas)]] <- NULL #remove cont1 from cont_list
    for (k in 1:length(cont_list2)){
      t1.2[k,] <- c(any(as.data.frame(cont_list2[[k]]["x"])==1),
                    any(as.data.frame(cont_list2[[k]]["y"])==1),
                    any(as.data.frame(cont_list2[[k]]["x"])==x_right),
                    any(as.data.frame(cont_list2[[k]]["y"])==y_bottom))

      num_touched_side[k] <- length(which(t1.2[k,]==TRUE))
    }
    cont_add_list <- cont_list2[which(num_touched_side==2)]
    cont_add <- ldply(cont_add_list, data.frame)[,c("x","y")]
    cont <- rbind(cont1, cont_add)
  }


  ###Step2 add edge lines to cont based on the judgment made for "cont" at Step1
  # 1. left side
  if (min(cont$x) == 1){
    y1l <- as.integer(min(cont[which(cont$x== 1), "y"])) # round it down
    y2l <- as.integer(max(cont[which(cont$x== 1), "y"])) # round it down

    l1 <- data.frame(x = 1, y = seq(y1l+1, y2l))
  } else {l1 <- NULL}

  # 2. upper line
  if (min(cont$y) == 1) {
    x1u <- as.integer(min(cont[which(cont$y== 1), "x"])) # round it down
    x2u <- as.integer(max(cont[which(cont$y== 1), "x"])) # round it down

    l2 <- data.frame(x = seq(x2u, x1u+1), y = 1)
  } else {l2 <-NULL}

  # 3. right side
  if (max(cont$x) == x_right){
    y1r <- as.integer(min(cont[which(cont$x== x_right), "y"])) # round it down
    y2r <- as.integer(max(cont[which(cont$x== x_right), "y"])) # round it down

    l3 <- data.frame(x = x_right, y = seq(y2r, y1r+1))
  } else {l3 <- NULL}

  # 4. bottom line
  if (max(cont$y) == y_bottom) {
    x1b <- as.integer(min(cont[which(cont$y== y_bottom), "x"])) # round it down
    x2b <- as.integer(max(cont[which(cont$y== y_bottom), "x"])) # round it down

    l4 <- data.frame(x = seq(x1b+1, x2b), y = y_bottom)
  } else {l4 <- NULL}

  # rbind cont and lines
  contline <- rbind(cont, l1,l2,l3,l4)
  # reorder contline by nearest point for each point (traveling salesman problem)
  xytsp <- ETSP(contline)
  colnames(xytsp) <- c("x","y")
  xytour <- solve_TSP(xytsp)
  reorder_contline <- data.frame(xytsp[xytour,])

  ### create index matrix for original image
  x <- rep(seq(1,nrow(im.g2d)), ncol(im.g2d))
  x <- sort(x)
  y <- rep(seq(1,ncol(im.g2d)), nrow(im.g2d))
  ind <- data.frame(x, y)

  ### judge if points fall inside of the enclosed polygon
  if_select <- inpolygon(ind$x, ind$y, reorder_contline$x, reorder_contline$y, boundary = TRUE)
  if_select2 <- matrix(if_select, nrow=nrow(im.g2d), byrow=T)

  ### try to plot and see if the selected area is correct
  if_select3 <- ifelse(if_select2==T, 1, 0)
  im.select3 <- as.cimg(if_select3)
  save.image(im.select3, paste0(outputPath, "/total-", sourceFileName))

  ### make pixel values outside ROI equal to 0 for original image
  ROI2d <- im.g_rescale[,,1,1]
  ROI2d[which(if_select2==F)] <- 0

  ### number of pixels in ROI area
  ROI_area <- length(which(ROI2d!=0))
  ### sum of intensities in ROI area
  ROI_intensity <- sum(ROI2d[which(ROI2d!=0)])

  ### reverse ROI2d to cimg
  ROI <- as.cimg(ROI2d)

  ##############################################################
  ############  positive stained area
  ##############################################################
  ### thresholding
  stained <- as.cimg(threshold(im.g, stained.thr))   #threshold retains same for the entire batch of image processing
  stained2d <- stained[,,1,1]
  stained2d[which(ROI==0)] <- 0
  stained_out <- as.cimg(stained2d)
  save.image(stained_out, paste0(outputPath, "/stained-", sourceFileName))

  ### number of pixels in stained area (excluding area outside of ROI)
  stained_area <- length(which(ROI!=0 & stained==1))
  ### sum of intensities in stained area
  stained_intensity <- sum(ROI[which(ROI!=0 & stained==1)])



  ##### combine result from ROI and stained area
  stats <- data.frame(ROI_area, ROI_intensity,
                      stained_area, stained_intensity)

  return(stats)
}  # end of im_process
