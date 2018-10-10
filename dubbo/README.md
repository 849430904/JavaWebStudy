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
    需要先启动zookeeper服务器
````

##### 创建服务提供者、消费者，需求：![](img/008.png)  ，[服务的编写规范可以参考](http://dubbo.apache.org/zh-cn/docs/user/best-practice.html)
* [服务提供者代码参考](code/user-service-provider)
* [服务消费者代码参考](code/order-service-consumer)
* [接品制取](code/gmall-interface)，将消费者与提供者需要的公共接口，单独抽到一个工程中；

````
  1，将服务提供者注册到注册中心核心步骤
      1）,导入dubbo依赖。可以在pom文件中引入
      2），由于注册中心我们使用的是zookeeper，所以我们要引入操作zookeeper的客户端的依赖。可以在pom中导入
      3），配置服务提供者，参考后面；
      4)，启动spring容器，到监控中心页面可以看对应的服务与应用
  2，让服务消费者去注册中心订阅服务提供者的服务地址核心步骤
      1），导入dubbo依赖。可以在pom文件中引入
      2），消费者中心配置，参考后面；注意：接口名称要与提供者的接口一样
      3），启动spring容器，测试
      
  System.in.read();测试的时候让程序阻塞，否则程序执行完马上就退出了，到控制台就看不到效果了    
     
````

* [dubbo服务提供者配置参考](code/user-service-provider/src/main/resources/provider.xml)
*  [dubbo消费中心配置](code/order-service-consumer/src/main/resources/consumer.xml)

##### 安装监控中心 dubbo-monitor-smiple
* dubbo-monitor-smiple监控服务的调用情况,[安装地址](https://github.com/apache/incubator-dubbo-ops) ，[参考](https://blog.csdn.net/goldenfish1919/article/details/69502898)；最后配置在消费者、提供者配置文件里面配置监控中心

##### 整合spring boot,[提供者工程代码](code/boot-user-service-provider) [消费者工程代码](code/boot-order-service-consumer)

````
 Spring boot的配置方式：
  1，将服务提供者注册到注册中心核心步骤
      1）,导入dubbo依赖。可以在pom文件中引入
      2），由于注册中心我们使用的是zookeeper，所以我们要引入操作zookeeper的客户端的依赖。可以在pom中导入
      3），导入dubbo-spring-boot-starter依赖 ；如果要暴露的服务很多，也可以通过注解配置要暴露的服务
      4)，在主程序中开启@EnableDubbo，开启基于注解的dubbo功能；启动spring容器，到监控中心页面可以看对应的服务与应用
  2，让服务消费者去注册中心订阅服务提供者的服务地址核心步骤
      1），导入dubbo依赖。可以在pom文件中引入
      2），消费者中心配置，参考后面；需要消费哪个服务采用@Reference注解方式在代码里面配置
      3），启动spring容器，测试
      
  System.in.read();测试的时候让程序阻塞，否则程序执行完马上就退出了，到控制台就看不到效果了    
     
````

* [spring boot dubbo服务提供者配置参考](code/boot-user-service-provider/src/main/resources/provider.xml)
* [spring boot dubbo消费中心配置](code/boot-order-service-consumer/src/main/resources/application.properties)


##### [dubbo配置文件 ](http://dubbo.apache.org/zh-cn/docs/user/references/xml/introduction.html)

````
   配置优先级：虚拟机参数>appliction配置>公共配置
````

* [启动检查](http://dubbo.apache.org/zh-cn/docs/user/demos/preflight-check.html)

````
   默认情况下，消费者启动的时候会去检查与它相关的服务提供者；如果服务提供者没有开启，消费者启动就不会报错
````

* [超时配置](http://dubbo.apache.org/zh-cn/docs/user/references/xml/dubbo-reference.html),参考链接中的timeout属性
* [配置覆盖关系(优先级关系)](http://dubbo.apache.org/zh-cn/docs/user/configuration/xml.html)

````
   如果服务消费者调用服务提供者的时候，服务提供者可能阻塞、忙不过来。这时候可以在消费者方配置一个超时时间；默认超时为1s
   
   优先级：
     1），精确优先（方法级优先，接口级次之，全局配置再次之）
     2），消费者设置优先（如果级别一样，则消费者优先，提供方次之）
````

* 重试次数

````
  当我们调用服务，由于各种原因，导致失败；我们可以配置重试次数（不包括第一次）
  
  幂等方法（可设置重试次数）【查询、删除、修改】，方法不管运行多少次，都是一个结果；
  非幂等方法（不能重试次数）【增加】，方法每次运行的结果都会产生一个新的结果；如数据库增加操作就不能让用户重试
  怎么设置幂等？设置retries为0就OK了
````

* [多版本](http://dubbo.apache.org/zh-cn/docs/user/demos/multi-versions.html)

````
 当一个接口实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用。
 可以先让一部份人用新版本，一部份人用旧版本，等版本稳定了再切换过来
````

* [本地存根](http://dubbo.apache.org/zh-cn/docs/user/demos/local-stub.html)

````

  消费者 调用 服务提供者
  可以在调用服务提供者之前，利用本地的存根代码检查一下参数再调用远程服务；
````

* Springboot的方式配置

````
   一般可以通过注解配置
   
   Springboot与dubbo整合的三种方式 ：
   1），导入dubbo-starter,在application.properties配置属性，使用@service暴露服务，使用@Reference
   2),如果想进行方法级别的配置，可以 保留dubbo xml配置文件 ，@ImportResource（location:xxxx）,导入配置文件。
      在配置文件中，以前怎么写，现在就怎么写
   3），使用注解API的方式
      http://dubbo.apache.org/zh-cn/docs/user/configuration/annotation.html
      专门搞一个注解类，将每一个组件手动创建到容器中；  相当于用它替换了application.properties配置文件 （什么标签都有什么样的config类）
   
````

* 高可用：zeekeeper宕机与dubbo直连

````
   注册中心是用zeekeeper搞的，如果注册中心挂了呢？服务提供者与消费者有缓存，可以通过缓存找到对方，利用缓存实现调用；
   直连：直接指定服务提供者的地址
   
   结论：注册中心宕机了，消费者也能调用服务提供者。通过直连或缓存
````

* 高可用：[负载均衡机制](http://dubbo.apache.org/zh-cn/docs/user/demos/loadbalance.html)

````
   基于权重的随机负载均衡机制（概率分布）：如 A B 权重分别为30 70，则访问A的概率分别为：3/10 7/10；不用考虑顺序
   基于权重轮询负载均衡机制：要考虑顺序，顺序又要考虑权限
   最少活跃数据的负载均衡机制：先问一下上一次处理花了多少时间，挑个最小的访问
   一致性hash负载均衡机制：通过id算出一个hash值，通过hash访问某台机器

````
* 高可用：[服务降级](http://dubbo.apache.org/zh-cn/docs/user/demos/service-downgrade.html)

````
  当服务器压力剧增的情况下，根据实际业务情况及流量，对一些服务和页面有策略的不处理或换种简单的方式处理，从而释放服务器资源
  以保证核心交易正常动作或高效率运作；
  可以通过服务降级功能临时屏蔽某个出错的非关键服务，并定义降级后的返回策略
  
  1）mock=force:return+null 表示消费方对该服务的方法调用都直接返回 null 值，不发起远程调用。用来屏蔽不重要服务不可用时对调用方的影响。
还可以改为 
  2）mock=fail:return+null 表示消费方对该服务的方法调用在失败后，再返回 null 值，不抛异常。用来容忍不重要服务不稳定时对调用方的影响。
````