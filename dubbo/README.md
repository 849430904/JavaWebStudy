#####  [dubbo.docx](doc/尚硅谷-dubbo.docx)
#####  [dubbo 官方文档](http://dubbo.apache.org/zh-cn/)

* 分布式应用架构演变：单一应用->垂直应用(按模块拆分成多个应用)
* 单一应用缺点：打包不方便、不好扩展
* 垂直应用优点：易扩展
* 垂直应用缺点：一个模块包括界面、业务逻辑实现，两者没有分开（改了页面需要重新打包）；应用不可能完全独立，大量的应用之间需要交互
* 分布式架构：把核心业务逻辑与用户业务分别抽取出来 ![](img/001.png),这样的话，用户业务与核心业务之间的调用需要借助RPC（远程过程调用）
* 分布式架构流动计算架构：![](img/002.png)，根据模块的访问量动态配置服务器的数目；


## RPC简介
* 基本原理：![](img/003.png),例如：![](img/004.png)

````
  评判RPC框架：
  1，AB建立连接的时间要短
  2，序列化与反序列化要快
````

* dubbo
![](img/005.png)
* 灰度发布：![](img/006.png)

* dubbo架构：注册中心、服务提供者、服务消费者、框架窗口(Container)、监控中心 ![](img/007.png)

````
  运行流程：
  1，dubbo架构启动，容器启动
  2，服务提供者将服务注册到注册中心
  3，服务消费者启动后，从注册中心订阅服务
  4，如果某个服务下线了，注册中心可以与服务消费者保持长连接，注册中心将消息推送到消费者
  5，消费者同步调用提供者的功能
  6，每隔一分钟将信息到监控中心
  
  开发步骤：
  1，开发提供者
  2，注册中心注册
  3，消费者
````

#### 开发环境搭建
* [搭建注册中心](http://dubbo.apache.org/zh-cn/docs/user/references/registry/introduction.html)  、 [zookeeper](http://zookeeper.apache.org/) 、 [zookeeper mac安装](https://blog.csdn.net/qi49125/article/details/60779877)
* [搭建监控中心](https://github.com/apache/incubator-dubbo-ops)

````
  注意：
    注意安装Mavean环境
    可能需要修改zookeeper地址与端口号
````