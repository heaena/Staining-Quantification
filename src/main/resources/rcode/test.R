library(imager)
args<-commandArgs(TRUE)
test <- function(){
  im <- load.image("/Users/zhangkai/IdeaProjects/picture-analysis-admin/tmp/input/pic1.jpg")
  #print(im)
  print(args[2])
}
test()

print(getwd())
