source("./process-one-image.R")
# 通过命令行调用，并传参数
args<-commandArgs(TRUE)
inputPath <- args[1]
outputPath <- args[2]
ROI.fill.thr <- args[3]
stained.thr <- args[4]
im_process(inputPath,outputPath,ROI.fill.thr,stained.thr)
