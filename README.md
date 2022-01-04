https://zhuanlan.zhihu.com/p/358568363


Rscript执行命令
```text
//安装依赖
Rscript -e 'install.packages("readxl",repos="https://mirrors.tuna.tsinghua.edu.cn/CRAN/")'
//查询已安装包
Rscript -e 'installed.packages()'
```



```text
Usage: jpackage <options>

Sample usages:
--------------
    Generate an application package suitable for the host system:
        For a modular application:
            jpackage -n name -p modulePath -m moduleName/className
        For a non-modular application:
            jpackage -i inputDir -n name \
                --main-class className --main-jar myJar.jar
        From a pre-built application image:
            jpackage -n name --app-image appImageDir
    Generate an application image:
        For a modular application:
            jpackage --type app-image -n name -p modulePath \
                -m moduleName/className
        For a non-modular application:
            jpackage --type app-image -i inputDir -n name \
                --main-class className --main-jar myJar.jar
        To provide your own options to jlink, run jlink separately:
            jlink --output appRuntimeImage -p modulePath \
                --add-modules moduleName \
                --no-header-files [<additional jlink options>...]
            jpackage --type app-image -n name \
                -m moduleName/className --runtime-image appRuntimeImage
    Generate a Java runtime package:
        jpackage -n name --runtime-image <runtime-image>

Generic Options:
  @<filename> 
          Read options and/or mode from a file 
          This option can be used multiple times.
  --type -t <type> 
          The type of package to create
          Valid values are: {"app-image", "dmg", "pkg"} 
          If this option is not specified a platform dependent
          default type will be created.
  --app-version <version>
          Version of the application and/or package
  --copyright <copyright string>
          Copyright for the application
  --description <description string>
          Description of the application
  --help -h 
          Print the usage text with a list and description of each valid
          option for the current platform to the output stream, and exit
  --icon <file path>
          Path of the icon of the application package
          (absolute path or relative to the current directory)
  --name -n <name>
          Name of the application and/or package
  --dest -d <destination path>
          Path where generated output file is placed
          (absolute path or relative to the current directory)
          Defaults to the current working directory.
  --temp <directory path>
          Path of a new or empty directory used to create temporary files
          (absolute path or relative to the current directory)
          If specified, the temp dir will not be removed upon the task
          completion and must be removed manually.
          If not specified, a temporary directory will be created and
          removed upon the task completion.
  --vendor <vendor string>
          Vendor of the application
  --verbose
          Enables verbose output
  --version
          Print the product version to the output stream and exit.

Options for creating the runtime image:
  --add-modules <module name>[,<module name>...]
          A comma (",") separated list of modules to add
          This module list, along with the main module (if specified)
          will be passed to jlink as the --add-module argument.
          If not specified, either just the main module (if --module is
          specified), or the default set of modules (if --main-jar is 
          specified) are used.
          This option can be used multiple times.
  --module-path -p <module path>...
          A : separated list of paths
          Each path is either a directory of modules or the path to a
          modular jar.
          (Each path is absolute or relative to the current directory.)
          This option can be used multiple times.
  --jlink-options <jlink options> 
          A space separated list of options to pass to jlink 
          If not specified, defaults to "--strip-native-commands 
          --strip-debug --no-man-pages --no-header-files". 
          This option can be used multiple times.
  --runtime-image <directory path>
          Path of the predefined runtime image that will be copied into
          the application image
          (absolute path or relative to the current directory)
          If --runtime-image is not specified, jpackage will run jlink to
          create the runtime image using options:
          --strip-debug, --no-header-files, --no-man-pages, and
          --strip-native-commands.

Options for creating the application image:
  --input -i <directory path>
          Path of the input directory that contains the files to be packaged
          (absolute path or relative to the current directory)
          All files in the input directory will be packaged into the
          application image.

Options for creating the application launcher(s):
  --add-launcher <launcher name>=<file path>
          Name of launcher, and a path to a Properties file that contains
          a list of key, value pairs
          (absolute path or relative to the current directory)
          The keys "module", "main-jar", "main-class",
          "arguments", "java-options", "app-version", "icon", and
          "win-console" can be used.
          These options are added to, or used to overwrite, the original
          command line options to build an additional alternative launcher.
          The main application launcher will be built from the command line
          options. Additional alternative launchers can be built using
          this option, and this option can be used multiple times to
          build multiple additional launchers. 
  --arguments <main class arguments>
          Command line arguments to pass to the main class if no command
          line arguments are given to the launcher
          This option can be used multiple times.
  --java-options <java options>
          Options to pass to the Java runtime
          This option can be used multiple times.
  --main-class <class name>
          Qualified name of the application main class to execute
          This option can only be used if --main-jar is specified.
  --main-jar <main jar file>
          The main JAR of the application; containing the main class
          (specified as a path relative to the input path)
          Either --module or --main-jar option can be specified but not
          both.
  --module -m <module name>[/<main class>]
          The main module (and optionally main class) of the application
          This module must be located on the module path.
          When this option is specified, the main module will be linked
          in the Java runtime image.  Either --module or --main-jar
          option can be specified but not both.
  --mac-package-identifier <ID string>
          用来唯一地标识 macOS 应用程序的标识符
          默认为主类名称。
          只能使用字母数字（A-Z、a-z、0-9）、连字符 (-) 和
          句点 (.) 字符。
  --mac-package-name <name string>
          出现在菜单栏中的应用程序名称
          这可以与应用程序名称不同。
          此名称的长度必须小于 16 个字符，适合
          显示在菜单栏中和应用程序“信息”窗口中。
          默认为应用程序名称。
  --mac-package-signing-prefix <prefix string>
          在对应用程序包签名时，会在所有需要签名
          但当前没有程序包标识符的组件的
          前面加上此值。
  --mac-sign
          请求对程序包进行签名
  --mac-signing-keychain <keychain name>
          Name of the keychain to search for the signing identity
          If not specified, the standard keychains are used.
  --mac-signing-key-user-name <team name>
          Team or user name portion of Apple signing identities.
  --mac-app-store
          Indicates that the jpackage output is intended for the
          Mac App Store.
  --mac-entitlements <file path>
          Path to file containing entitlements to use when signing
          executables and libraries in the bundle.
  --mac-app-category <category string>
          String used to construct LSApplicationCategoryType in
          application plist.  The default value is "utilities".

Options for creating the application package:
  --about-url <url>
          URL of the application's home page
  --app-image <directory path>
          Location of the predefined application image that is used
          to build an installable package
          (absolute path or relative to the current directory)
  --file-associations <file path>
          Path to a Properties file that contains list of key, value pairs
          (absolute path or relative to the current directory)
          The keys "extension", "mime-type", "icon", and "description"
          can be used to describe the association.
          This option can be used multiple times.
  --install-dir <directory path>
          应用程序安装目录的绝对路径
  --license-file <file path>
          Path to the license file
          (absolute path or relative to the current directory)
  --resource-dir <directory path>
          Path to override jpackage resources
          Icons, template files, and other resources of jpackage can be
          over-ridden by adding replacement resources to this directory.
          (absolute path or relative to the current directory)
  --runtime-image <directory path>
          Path of the predefined runtime image to install
          (absolute path or relative to the current directory)
          Option is required when creating a runtime package.
              
```

```text
jlink --help
用法: jlink <选项> --module-path <模块路径> --add-modules <模块>[,<模块>...]
可能的选项包括:
      --add-modules <mod>[,<mod>...]    除了初始模块之外要解析的
                                        根模块。<mod> 还可以为 ALL-MODULE-PATH。
      --bind-services                   链接服务提供方模块及其
                                        被依赖对象
  -c, --compress=<0|1|2>                Enable compression of resources:
                                          Level 0: No compression
                                          Level 1: Constant string sharing
                                          Level 2: ZIP
      --disable-plugin <pluginname>     Disable the plugin mentioned
      --endian <little|big>               所生成 jimage
                                          的字节顺序 (默认值: native)
  -h, --help, -?                        输出此帮助消息
      --ignore-signing-information        在映像中链接已签名模块化
                                          JAR 的情况下隐藏致命错误。
                                          已签名模块化 JAR 的签名
                                          相关文件将不会复制到
                                          运行时映像。
      --launcher <名称>=<模块>[/<主类>]
                                        为模块和主类添加给定
                                        名称的启动程序命令
                                        (如果指定)  
      --limit-modules <模块>[,<模块>...]  限制可观察模块的领域
      --list-plugins                    List available plugins
  -p, --module-path <path>              模块路径。
                                        如果未指定，将使用 JDK 的 jmods 
                                        目录（如果存在该目录）。如果指定，
                                        但它不包含 java.base 模块，
                                        则将添加 JDK 的 jmods 目录
                                        （如果存在该目录）。
      --no-header-files                 Exclude include header files
      --no-man-pages                    Exclude man pages
      --output <路径>                     输出路径的位置
      --save-opts <文件名>                将 jlink 选项保存在指定文件中
  -G, --strip-debug                     Strip debug information
      --suggest-providers [<名称>,...]  建议可从模块路径中实现
                                        给定服务类型的提供方
  -v, --verbose                         启用详细跟踪
      --version                           版本信息
      @<文件名>                           从文件中读取选项
```
