# FiFuSky-Rebuild

#### 介绍
FiFuSky-Rebuild
对于原先的空岛插件用kotlin进行重构

[FiFu服务器官网](https://www.fifu.fun)

#### 软件架构
采用高版本CraftBukkit核心进行开发

采用Kotlin作为语言

采用Hutool作为工具库

核心性质的数据使用数据库存储，辅助性质的数据库采用json文件存储

#### 安装教程

1.  clone
2.  把服务器核心、插件之类的依赖放入libs文件夹
3.  gradle build

#### 使用说明

1.  db.setting必须解压到服务器核心jar包的根目录里，否则无法读取数据库
2.  服务端把依赖文件放到服务端核心同级目录，然后将其添加进classpath
3.  参考启动命令（Paper）：java -javaagent:paper-1.16.5-471.jar -cp * io.papermc.paperclip.Paperclip --nogui

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
