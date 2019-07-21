# FileDownloader

## 前言

> 在我的任务清单中，很早就有了一个文件下载器，但一直忙着没空去写。最近刚好放假，便抽了些时间完成了下文中的这个下载器。

## 介绍

同样的，还是先上效果图吧。

![Image](https://tva2.sinaimg.cn/large/007DFXDhgy1g57h330c4bj30m40bcwi7.jpg)

Jar包地址位于 [FileDownloader](https://github.com/asche910/FileDownloader/releases)

目前实现的主要功能有：

* 多线程下载
* 断点续传
* 自定义头部等

即将完成的包括：

* 添加代理功能
* **...**

感觉做了回标题党，代理功能由于时间关系，将在下次更新加入。
关于设置代理，我这篇文章 [Java实现Ip代理池](https://www.cnblogs.com/asche/p/10291701.html) 中有具体的设置方法。
另外除了这个代理功能，我也实在不知道下载器能加些啥功能了。。

## 使用说明
jar包的运行方式不用多说了吧，直接
```bash
java -jar FileDownloader.jar

```
这样不加任何参数的话会输出下文中的内容：

```
   _____ __    ___                  __             __
  / __(_) /__ / _ \___ _    _____  / /__  ___ ____/ /__ ____
 / _// / / -_) // / _ \ |/|/ / _ \/ / _ \/ _ `/ _  / -_) __/
/_/ /_/_/\__/____/\___/__,__/_//_/_/\___/\_,_/\_,_/\__/_/
                                                            
usage: FileDownloader [options ...] <url>
 -c                      加上表明关闭断点续传，默认开启
 -H,--Header <arg>       添加请求头部，格式："header=value"，多个可叠加使用该H参数
 -h,--help               使用说明
 -n,--num <arg>          开启的线程数量，默认为8
 -U,--User-Agent <arg>   添加User-Agent标识头
```

于是，我们可以加上一些参数，比如这样
```
java -jar FileDownloader.jar  -c -U "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0" -H Accept=* -H Cookie=value -n 10 https://www.picpick.org/releases/latest/picpick_inst.exe
```
这样的话，我们就是 关闭了断点续传功能，带上了 “Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0” 的User-Agent， “*”的Accept头部，value的Cookie，同时线程数量设置为10，然后便开始下载我们后面的资源了。其中注意如果某项值含有空格的话，要用双引号圈起来，比如上面的user-agent，不然可能会解析错误。
