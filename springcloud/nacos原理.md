#### Nacos 简介和原理

#### 一、Nacos简介

##### 1.1 **Nacos是什么**

```
Nacos提供了统一配置管理、服务发现与注册。其中服务注册和发现的功能，相当于dubbo里面使用到 的zookeeper、或者spring cloud里面应用到的consoul以及eureka
```

##### 1.2 **Nacos特性**

###### 1.2.1 服务发现和服务健康检测

```
Nacos提供了基于RPC的服务发现，服务提供者可以将自身的服务通过原生API或者openApi来实现服务 的注册，服务消费者可以使用API或者Http来查找和发现服务

同时，Nacos提供了对服务的实时监控检查，当发现服务不可用时，可以实现对服务的动态下线从而阻止服务消费者向不健康的服务发送请求
```

###### 1.2.2**配置管理**

```
传统的配置管理，是基于项目中的配置文件来实现，当出现配置文件变更时需要重新部署，而动态配置 中心可以将配置进行统一的管理，是的配置变得更加灵活以及高效。

动态配置中心可以实现路由规则的动态配置、限流规则的动态配置、动态数据源、开关、动态UI等场景
```

##### 1.3 Nacos数据模型

###### 1.3.1 ***\*命名空间(Namespace)\**** 

```
可用于进行不同环境的配置隔离。

例如：
1.可以隔离开发环境——测试环境和生产环境，因为它们的配置可能各不相同；
2.可以隔离不同的用户——不同的开发人员使用同一个nacos管理各自的配置，可通过namespace隔离。 不同的命名空间下可以存在相同名称的配置分组(Group) 或配置集。
```

###### 1.3.2 ***\*配置分组(Group)\**** 

```
配置分组是对配置集进行分组。 通过一个有意义的字符串（如 Buy 或 Trade ）来表示。 不同的配置分组下可以有相同的当您在 Nacos 上创建一个配置时，如果未填写配置分组的名称，则配置分组的名称默认采用 DEFAULT_GROUP 配置分组的常见场景——可用于区分不同的项目或应用
```

###### 1.3.3 ***\*配置集（Data ID）\****

```
配置集(Data ID) 在系统中，一个配置文件通常就是一个配置集。 一个配置集可以包含了系统的各种配置信息。
```

##### 1.4 Nacos官网

[Nacos支持三种部署模式](https://nacos.io/zh-cn/docs/v2/guide/admin/deployment.html?login=from_csdn)



#### 二，Nacos部署

```
Nacos支持三种部署模式
单机模式 - 用于测试和单机试用。
集群模式 - 用于生产环境，确保高可用。
多集群模式 - 用于多数据中心场景
```

##### **2.1** **单机模式支持mysql**

```
1.安装数据库，版本要求：5.6.5+ 
2.初始化mysql数据库，数据库初始化文件：mysql-schema.sql 
3.修改conf/application.properties文件，增加支持mysql数据源配置（目前只支持mysql），添加mysql数据源的url、用户名和密码。 
spring.datasource.platform=mysql 
db.num=1 
db.url.0=jdbc:mysql://11.162.196.16:3306/nacos_devtest?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true db.user=nacos_devtest db.password=youdontknow ​ 
4.启动nacos sh startup.sh -m standalone 
5.访问nacos localhost:8848/nacos
```

![image-20241012153611875](D:\2024\code\javaee-knowledge\springcloud\image-20241012153611875.png)
