### 设置R镜像
options(BioC_mirror="https://mirrors.ustc.edu.cn/bioc/") ##指定镜像，这个是中国科技大学镜像

options("repos" = c(CRAN="https://mirrors.tuna.tsinghua.edu.cn/CRAN/")) ##指定install.packages安装镜像，这个是清华镜像

### 启动http服务

pr <- plumber::plumb("./api.R")
pr$run()

### 安装特殊的包
install.packages("BiocManager")

BiocManager::install("EBImage")

imager https://cran.r-project.org/web/packages/imager/index.html
https://github.com/dahtah/imager
install.packages('https://cran.r-project.org/src/contrib/Archive/imager/imager_0.42.1.tar.gz', repos = NULL, type = 'source')
install.packages("/Users/zhangkai/IdeaProjects/picture-analysis-admin/tmp/imager_0.42.1.tar.gz", repos = NULL)

### mac安装conda 
https://www.jianshu.com/p/2e1986296e15?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation
安装完成后配置export PATH=/Users/zhangkai/anaconda/bin:$PATH
~/.bashrc 中配置环境变量

### 使用devtools
install.packages('devtools')
devtools::install_github("dahtah/imager")

require(devtools)
install_version('imager', version='0.42.3')

### R 包 rgl 安装失败, 报错 X11 not found but required, configure aborted 及解决方法
https://www.cnblogs.com/shuaihe/articles/11386088.html

### python和R的选择
https://zhuanlan.zhihu.com/p/43502885
