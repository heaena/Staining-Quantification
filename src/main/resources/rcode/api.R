library(rjson)
source("./batch-processing.R")
#detectCores函数可以告诉你你的CPU可使用的核数
#' start processing task
#' 运行 plumber::plumb("./api.R")$run()
#' 测试 http://127.0.0.1:8997/startTask?param={"ROI.fill.thr":"7","inputFilePath":"/Users/zhangkai/IdeaProjects/picture-analysis-admin/tmp/input","outputFilePath":"/Users/zhangkai/IdeaProjects/picture-analysis-admin/tmp/output","stained.thr":"0.7"}
#' @param msg The message to echo back.
#' @get /startTask
function (param){
  parser <- newJSONParser()
  parser$addData( param )
  data <- parser$getObject()
  print(is.null(data$ROI.fill.thr))
  batch_process(data$inputFilePath, data$outputFilePath, data$ROI.fill.thr, data$stained.thr)
  list(code=0)
}
