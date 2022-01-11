### 启动app

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

