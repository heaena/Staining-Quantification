### 打包App
```text
1、执行package
2、自行jpackage
```


### 启动App

### 安装环境
```text
1.安装R语言SDK
国内镜像地址https://www.runoob.com/r/r-setup.html

2.安装依赖
执行脚本/src/main/resources/script/install.packages
或者手动安装依赖
或者点击app的按钮'安装依赖'

3.检查已安装包
执行脚本/src/main/resources/script/installed.packages
```

### 验证R脚本运行环境

```text
测试方法1：运行测试脚本： /src/main/resources/rcode/test.sh(或test.bat)
测试方法2：运行执行脚本的method：image.analysis.cloud.app.infra.rpc.RemoteAnalysisPlatformService.main()
```



启动app成功后


### 异常解决

```text
Mac M1 Pro异常
Error: package or namespace load failed for 'imager' in dyn.load(file, DLLpath = DLLpath, ...):
 unable to load shared object '/Library/Frameworks/R.framework/Versions/4.1/Resources/library/imager/libs/imager.so':
  dlopen(/Library/Frameworks/R.framework/Versions/4.1/Resources/library/imager/libs/imager.so, 0x0006): Library not loaded: /opt/X11/lib/libX11.6.dylib
  Referenced from: /Library/Frameworks/R.framework/Versions/4.1/Resources/library/imager/libs/imager.so
  
解决：
todo
```


```text
异常
Attaching package: ‘imager’

The following object is masked from ‘package:magrittr’:
    add

The following objects are masked from ‘package:stats’:

    convolve, spectrum

The following object is masked from ‘package:graphics’:

    frame

The following object is masked from ‘package:base’:

    save.image


Attaching package: ‘plyr’

The following object is masked from ‘package:imager’:

    liply


Attaching package: ‘EBImage’

The following objects are masked from ‘package:imager’:

    channel, dilate, display, erode, resize, watershed

以上错误解释（函数被屏蔽）及处理方法,可以通过library（）的参数warn.conflicts，去除警告, 如下  https://blog.csdn.net/ouyangk1026/article/details/122165567
library(igraph,warn.conflicts = F)

Error in bdilate_square(px, x) : 
  Not compatible with requested type: [type=character; target=integer].
Calls: im_process ... %>% -> shrink -> berode_square -> grow -> bdilate_square
Execution halted
把字符串转为数字 as.numeric
```

```text
Error in file(file, ifelse(append, "a", "w")) : 
  cannot open the connection
Calls: im_process ... write.csv -> eval.parent -> eval -> eval -> <Anonymous> -> file
In addition: Warning message:
In file(file, ifelse(append, "a", "w")) :
  cannot open file '/Users/zhangkai/IdeaProjects/image-analysis-app/temp/.out_stats/test.jpg-out_stats.csv': No such file or directory
Execution halted
文件夹不存在，需要先创建
```
