source("./batch-processing.R")
#source("./batch-processing.R")
# 通过命令行调用，并传参数
args<-commandArgs(TRUE)
inputPath <- args[1]
outputPath <- args[2]
ROI.fill.thr <- args[3]
stained.thr <- args[4]
batch_process(inputPath,outputPath,ROI.fill.thr,stained.thr)
