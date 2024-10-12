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

![](D:\2024\code\javaee-knowledge\springcloud\image-20241012153611875.png)

##### 2.2集群模式

###### 2.2.1配置集群配置文件

```
在nacos的解压目录nacos/的conf目录下，有配置文件cluster.conf，请每行配置成ip:port。（请配置3个或3个以上节点）

# ip:port
200.8.9.16:8848
200.8.9.17:8848
200.8.9.18:8848
```

###### 2.2.2确定数据源

```
使用内置数据源,无需进行任何配置
Nacos默认自带的是嵌入式数据库derby
使用外置数据源
生产使用建议至少主备模式，或者采用高可用数据库。
初始化 MySQL 数据库
sql语句源文件
application.properties 配置
application.properties配置文件
```

[sql语句源文件](https://link.csdn.net/?target=https%3A%2F%2Fgithub.com%2Falibaba%2Fnacos%2Fblob%2Fmaster%2Fdistribution%2Fconf%2Fmysql-schema.sql%3Flogin%3Dfrom_csdn)

[application.properties配置文件](https://link.csdn.net/?target=https%3A%2F%2Fgithub.com%2Falibaba%2Fnacos%2Fblob%2Fmaster%2Fdistribution%2Fconf%2Fapplication.properties%3Flogin%3Dfrom_csdn)

###### 2.2.3启动服务器

```
单机模式
sh startup.sh -m standalone

集群模式 
 使用内置数据源 sh startup.sh -p embedded
 使用外置数据源 sh startup.sh
```

#### 三、服务注册&发现和配置管理

OPen API指南: [Open API 指南](https://link.csdn.net/?target=https%3A%2F%2Fnacos.io%2Fzh-cn%2Fdocs%2Fopen-api.html%3Flogin%3Dfrom_csdn)

```
服务注册
curl -X PUT 'http://101.43.xx.xx:8848/nacos/v1/ns/instance?serviceName=ITPortal&ip=20.18.7.10&port=8080'

服务发现
curl -X GET 'http://101.43.xx.xx:8848/nacos/v1/ns/instance/list?serviceName=ITPortal'

发布配置
curl -X POST "http://101.43.xx.xx:8848/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test&content=helloWorld"

获取配置
curl -X GET "http://101.43.xx.xx:884/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test"
```

#### 四、SpringCloud集成Nacos

##### 4.1 注册中心

```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

使用 `@EnableDiscoveryClient` 注解开启服务注册与发现功能

```
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
@EnableRedisHttpSession
public class GulimallSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallSearchApplication.class, args);
    }

}
```

（1）注册服务nacos-payment-provider，存在9001，9002两个实例

```
server:
  port: 9001
spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: 101.43.xx.xx:8848 #配置Nacos地址
```

```
server:
  port: 9002
spring:
  application:
    name: nacos-payment-provider
  cloud:
    nacos:
      discovery:
        server-addr: 101.43.xx.xx:8848 #配置Nacos地址
```

 这样Nacos上就注册了一个服务nacos-payment-provider，有2个实例分别是9001和9002

（2）注册一个客户端

```
server:
  port: 83

spring:
  application:
    name: nacos-order-consumer
  cloud:
    nacos:
      discovery:
        server-addr: 101.43.xx.xx:8848
        metadata:
          preserved.heart.beat.interval: 1000
          preserved.heart.beat.timeout: 1000
          preserved.ip.delete.timeout: 1000
ribbon:
  ServerListRefreshInterval: 1000

#消费者将要去访问的微服务名称（成功注册进nacos的微服务提供者），在这配置了访问的服务，业务类就不用在定义常量了
service-url:
  nacos-user-service: http://nacos-payment-provider
```

（3）测试接口

```
@RestController
public class OrderNacosController {
    /*
    因为在yml中配置了service-url.nacos-user-service，
    这里不需要再定义要访问微服务名常量，而是通过boot直接读出来
     */
    @Value("${service-url.nacos-user-service}")
    private String serverURL;

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/nacos/{id}")
    public String paymentInfo(@PathVariable("id") Long id){
        return restTemplate.getForObject(serverURL+"/payment/nacos/"+id,String.class);
    }
}
```

（4）访问接口

[http://localhost:83/consumer/payment/nacos/1](https://link.csdn.net/?target=http%3A%2F%2Flocalhost%3A83%2Fconsumer%2Fpayment%2Fnacos%2F1%3Flogin%3Dfrom_csdn) 可以看出轮询调用服务9001和9002（默认使用的ribbon做为负载工具）

下线9001再次访问上述接口就会只有9002提供服务



##### 4.2配置中心

```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

```
server:
  port: 3377

spring:
  application:
    name: nacos-config-client
  cloud:
    nacos:
      //注册
      discovery:
        server-addr: 101.43.xx.xx:8848 #Nacos服务注册中心地址
      //配置
      config:
        server-addr: 101.43.xx.xx:8848 #Nacos作为配置中心地址
        file-extension: yaml  #指定yaml格式的配置
```

（1）测试接口

```
@RestController
@RefreshScope   //SpringCloud原生注解 支持Nacos的动态刷新功能
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info")
    public String getConfigInfo(){
        return configInfo;
    }
}
```

(2)Nacos中增加配置文件

命名格式

![image-20241012160645939](D:\2024\code\javaee-knowledge\springcloud\config命名规则)

(3)访问测试

[http://localhost:3377/config/info](https://link.csdn.net/?target=http%3A%2F%2Flocalhost%3A3377%2Fconfig%2Finfo%3Flogin%3Dfrom_csdn)输出结果：NACOS CONFIG DEV

再次修改配置内容  [http://localhost:3377/config/info](https://link.csdn.net/?target=http%3A%2F%2Flocalhost%3A3377%2Fconfig%2Finfo%3Flogin%3Dfrom_csdn)输出结果：NACOS CONFIG DEV222



#### 五、Nacos原理

##### 5.1 原理

![image-20241012161853754](D:\2024\code\javaee-knowledge\springcloud\nacos 原理 鱼骨图)

```
Nacos注册中心分为server与client，server采用Java编写，为client提供注册发现服务与配置服务。而client可以用多语言实现，client与微服务嵌套在一起，nacos提供sdk和openApi，如果没有sdk也可以根据openApi手动写服务注册与发现和配置拉取的逻辑

Nacos注册概括来说有6个步骤：
0、服务容器负责启动，加载，运行服务提供者。
1、服务提供者在启动时，向注册中心注册自己提供的服务。
2、服务消费者在启动时，向注册中心订阅自己所需的服务。
3、注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。
4、服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。
5、服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。
Nacos 服务注册与订阅的完整流程
Nacos 客户端进行服务注册有两个部分组成，一个是将服务信息注册到服务端，另一个是像服务端发送心跳包，这两个操作都是通过 NamingProxy 和服务端进行数据交互的。
Nacos 客户端进行服务订阅时也有两部分组成，一个是不断从服务端查询可用服务实例的定时任务，另一个是不断从已变服务队列中取出服务并通知 EventListener 持有者的定时任务。
```

![image-20241012162531960](D:\2024\code\javaee-knowledge\springcloud\nacos四大功能)

##### 5.2 **Nacos 的关键特性包括:**

```
5.2.1服务发现和服务健康监测

Nacos 支持基于 DNS 和基于 RPC 的服务发现。服务提供者使用
原生SDK、OpenAPI、或一个独立的Agent TODO注册 Service 后，服务消费者可以使用DNS TODO
或HTTP&API查找和发现服务。

Nacos 提供对服务的实时的健康检查，阻止向不健康的主机或服务实例发送请求。Nacos 支持传输层 (PING 或 TCP)和应用层(如 HTTP、MySQL、用户自定义）的健康检查。对于复杂的云环境和网络拓扑环境中（如 VPC、边缘网络等）服务的健康检查，Nacos提供了 agent 上报模式和服务端主动检测2种健康检查模式。Nacos还提供了统一的健康检查仪表盘，帮助您根据健康状态管理服务的可用性及流量。

5.2.2 动态配置服务

动态配置服务可以让您以中心化、外部化和动态化的方式管理所有环境的应用配置和服务配置。
动态配置消除了配置变更时重新部署应用和服务的需要，让配置管理变得更加高效和敏捷。
配置中心化管理让实现无状态服务变得更简单，让服务按需弹性扩展变得更容易。
Nacos 提供了一个简洁易用的UI (控制台样例 Demo) 帮助您管理所有的服务和应用的配置。Nacos 还提供包括配置版本跟踪、金丝雀发布、一键回滚配置以及客户端配置更新状态跟踪在内的一系列开箱即用的配置管理特性，帮助您更安全地在生产环境中管理配置变更和降低配置变更带来的风险。

5.2.3 动态 DNS 服务 动态 DNS
服务支持权重路由，让您更容易地实现中间层负载均衡、更灵活的路由策略、流量控制以及数据中心内网的简单DNS解析服务。动态DNS服务还能让您更容易地实现以DNS 协议为基础的服务发现，以帮助您消除耦合到厂商私有服务发现 API 上的风险。Nacos 提供了一些简单的 DNS APIs TODO 帮助您管理服务的关联域名和可用的 IP:PORT 列表.

5.2.4 服务及其元数据管理 Nacos
能让您从微服务平台建设的视角管理数据中心的所有服务及元数据，包括管理服务的描述、生命周期、服务的静态依赖分析、服务的健康状态、服务的流量管理、路由及安全策略、服务的SLA 以及最首要的 metrics 统计数据。
```



#### 六、Nacos 服务发现产品对比

![image-20241012162702553](D:\2024\code\javaee-knowledge\springcloud\Nacos 服务发现产品对比)

```
Nacos除了服务的注册发现之外，还支持动态配置服务。动态配置服务可以让您以中心化、外部化和动态化的方式管理所有环境的应用配置和服务配置。动态配置消除了配置变更时重新部署应用和服务的需要，让配置管理变得更加高效和敏捷。配置中心化管理让实现无状态服务变得更简单，让服务按需弹性扩展变得更容易。

一句话概括就是Nacos = Spring Cloud注册中心 + Spring Cloud配置中心。
```

面试分析

```
为什么要将服务注册到nacos?(为了更好的查找这些服务)

在Nacos中服务提供者是如何向Nacos注册中心(Registry)续约的？(5秒心跳)

对于Nacos服务来讲它是如何判定服务实例的状态？(检测心跳包，15,30)

服务启动时如何找到服务启动注册配置类?(NacosNamingService)

服务消费方是如何调用服务提供方的服务的？(RestTemplate)
```



#### 七、详细比较Nacos与Eureka的区别

```
Nacos和Eureka整体结构类似，服务注册、服务拉取、心跳等待，但是也存在一些差异：

Nacos与eureka的共同点:
都支持服务注册和服务拉取
都支持服务提供者心跳方式做健康检测

Nacos与Eureka的区别:
Nacos支持服务端主动检测提供者状态：临时实例采用心跳模式，非临时实例采用主动检测模式
临时实例心跳不正常会被剔除，非临时实例则不会被剔除
Nacos支持服务列表变更的消息推送模式，服务列表更新更及时
Nacos集群默认采用AP方式，当集群中存在非临时实例时，采用CP模式；Eureka采用AP方式

1、范围不同。
Nacos的阈值是针对某个具体Service的，而不是针对所有服务的；但Eureka的自我保护阈值是针对所有服务的。nacos支持CP和AP两种；eureka只支持AP。nacos使用netty，是长连接；eureka是短连接，定时发送
2、保护方式不同。
Eureka保护方式：当在短时间内，统计续约失败的比例，如果达到一定阈值，则会触发自我保护的机制，在该机制下，Eureka Server不会剔除任何的微服务，等到正常后，再退出自我保护机制。自我保护开关（eureka.server.enable-self-preservation: false)。
Nacos保护方式：当域名健康实例（Instance)占总服务实例（Instance)的比例小于阈值时，无论实例（Instance)是否健康，都会将这个实例（Instance)返回给客户端。这样做虽然损失了一部分流量，但是保证了集群的剩余健康实例（Instance)能正常工作。
3、连接方式不同。
nacos支持动态刷新，在控制器（controller）上加＠RefreshScope注解即可，采用Netty连接，是长连接；eureka本身不支持动态刷新，需要配合MQ完成动态刷新，且是短连接，是定时发送。
4、Nacos中的CAP模式切换
Nacos 支持 AP 和 CP 模式的切换，这意味着 Nacos 同时支持两者一致性协议。这样，Nacos能够以一个注册中心管理这些生态的服务。不过在Nacos中，AP模式和CP模式的具体含义，还需要再说明下。

AP模式为了服务的可能性而减弱了一致性，因此AP模式下只支持注册临时实例。AP 模式是在网络分区下也能够注册实例。在AP模式下也不能编辑服务的元数据等非实例级别的数据，但是允许创建一个默认配置的服务。同时注册实例前不需要进行创建服务的操作，因为这种模式下，服务其实降级成一个简单的字符创标识，不在存储任何属性，会在注册实例的时候自动创建。

CP模式下则支持注册持久化实例，此时则是以 Raft 协议为集群运行模式，因此网络分区下不能够注册实例，在网络正常情况下，可以编辑服务器别的配置。改模式下注册实例之前必须先注册服务，如果服务不存在，则会返回错误。

MIXED 模式可能是一种比较让人迷惑的模式，这种模式的设立主要是为了能够同时支持临时实例和持久化实例的注册。这种模式下，注册实例之前必须创建服务，在服务已经存在的前提下，临时实例可以在网络分区的情况下进行注册。
```

