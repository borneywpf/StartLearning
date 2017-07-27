---
layout: post
title: "android终端可交互的lint脚步slint"
date: 2017-05-26 17:06:03 +0800
comments: true
published: true
categories: script
---

<!--more-->

最近对自己的一些项目用lint检查，发现终端的展示效果很不友好，就写了这个脚本，虽然studio也有类似的功能，但大爱终端的我决定自己搞个好看点的，如果大家觉得有用可以收藏，发现错误可以给我提[issues](https://github.com/borneywpf/script/issues)
   
# 默认的lint在终端的展示
   
可以发现，真的很难看，而且当数据量大的时候更加难看了，查看自己需要的东西时都没什么心情了  
![1](/images/slint/1.png)
   
  
# slint的优化效果
   
   我通过这个slint脚步对lint检查的结果进行二次解析，分类和展示美化，效果如下：
   
## 主交互界面
   
super lint对数据进行分类展示，并且可交互的，主交互界面  
![2](/images/slint/2.png)

## 单个种类的错误查询
   
super lint会对错误类型进行归类，可按照你的意向对相应问题类型进行展示(如下图的UsesMinSdkAttributes)  
![3](/images/slint/3.png)
    
## 根据错误类型查询
    
super lint也会对错误类型进行分类展示（如下图的Error）  
![4](/images/slint/4.png)
    
## 展示所有数据

super lint也会对所有数据进行展示

## 删除无用资源

脚本也可以对无用资源进行删除，包括无用的文件，无用的string、dimen、color等值资源  
**注意** ：当对lint检查的无用资源删除后，再用lint检查还有无用资源，这个是资源相互依赖导致的，所以删除操作需要多进行几次，不过删除的很精准  
![5](/images/slint/5.png)
    
## slint使用方法
如下是super lint的使用方法  
![6](/images/slint/6.png)  


# 后记

刚开始学习python，写这个脚步主要是为了讲自己学的东西学以致用，同时也优化了自己的工作环境，一举两得脚本目录在我的github中 **[[script](https://github.com/borneywpf/script)]** 工程中，该工程存储了我工作和学习中自己为了方便而写的脚本，会不断更新，欢迎大家star
