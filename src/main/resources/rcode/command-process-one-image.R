source("./process-one-image.R")
# 通过命令行调用，并传参数
args<-commandArgs(TRUE)
im_process(args[1],args[2],args[3],args[4],args[5],args[6],args[7],args[8])
