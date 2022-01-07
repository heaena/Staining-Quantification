#### call the im_process function
source("./processing-wrap4.R")
library(logging)

# log
logReset()
basicConfig(level='FINEST')
addHandler(writeToFile, file="./logs/processing.log", level='INFO')
with(getLogger(), names(handlers))

########################################################################
############  for loop for large amount of images
########################################################################
#####
# note that, for red stained color, the stained.thr = "auto";
# for black stained color, the stained.thr = 0.7
# ROI.fill.thr usually set to 7 (can be adjusted)

batch_process <- function(inputPath,outputPath,ROI.fill.thr,stained.thr){
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
  files <- list.files(path = inputPath, pattern = NULL, all.files = FALSE,
                      full.names = TRUE, recursive =FALSE,
                      include.dirs =FALSE, no.. = FALSE)
  for (inputFile in files) {
    im_process(inputFile, outputPath, ROI.fill.thr, stained.thr)
  }
}

