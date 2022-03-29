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
