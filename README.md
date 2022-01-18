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


1、app 电脑独立运行
java 集成（前端UI、R脚本）=》app （xxx.dmg, xxx.exe）
2、app安装及使用
四、todo list
李
1、提供参数及说明 *****
2、csv文件已提供
3、持久化R脚本执行的时间、状态、错误日志，文件名称：log-[name].csv；数据文件名称：data-[name].csv
4、按照/src/main/resources/rcode/process-one-image.R格式编写单张图片的处理函数 *****
5、按照src/main/resources/script/install.packages格式提供安装依赖的脚本 *****

张凯
1、在分析结果页面，根据分析结果，调整参数，再次执行分析 ****
2、在图片页面，按照图片名称和分析任务，导出分析结果 ***
3、展示数据结果 *****
4、页面增加参数 *****（参数支持滑动***）
5、分析执行的状态、消耗时间、执行日志(包括错误日志) ***
6、在windows系统打包app测试 *****
