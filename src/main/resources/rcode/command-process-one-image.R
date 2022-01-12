source("./process-one-image.R")
# 通过命令行调用，并传参数
args<-commandArgs(TRUE)
inputPath <- args[1]
outputPath <- args[2]
param <- args[3]
im_process(inputPath,outputPath,param)
