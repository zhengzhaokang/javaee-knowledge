### Ribbon

#### 1. 简介：Ribbon原理解析

```
Ribbon是Netflix开源的一个客户端负载均衡库，也是Spring CloudNetflix项目的核心组件之一。以下是对Ribbon原理的详细解析：

一、负载均衡原理
客户端负载均衡：Ribbon通过LoadBalancerClient来实现客户端负载均衡。ILoadBalancer通过配置IRule（负载均衡规则）和IPing（服务可用性检测）来实现具体的负载均衡策略。
服务列表获取：ILoadBalancer会定期（如每10秒）从Eureka等注册中心获取服务提供者的注册列表。
负载均衡策略：获取到服务列表后，ILoadBalancer会根据配置的IRule规则（如轮询、随机、可用性过滤、重试等）来选择具体的服务实例进行请求分发。
二、核心原理：拦截器
代理机制：Ribbon的核心是代理机制，它通过拦截器的方式对请求进行拦截和处理。
请求拦截：拦截器会拦截客户端发起的请求，获取请求的URL，并解析出hostname。
服务实例选择：根据hostname，拦截器会从Eureka等注册中心获取对应的服务实例列表，并按照配置的负载均衡规则选择具体的服务实例。
URL转换：将原始请求中的hostname替换为选中服务实例的IP和端口，然后构建新的请求进行发送。
三、代码实现过程
在Spring Cloud中，Ribbon通常与RestTemplate结合使用来实现服务调用和负载均衡。以下是代码实现的基本过程：

RestTemplate注入：通过@Bean向Spring中注入RestTemplate对象。
拦截器注入：通过@LoadBalanced注解，将自定义的拦截器（如LoadBalancerInterceptor）注入到RestTemplate的拦截器链中。
HTTP调用：当客户端通过RestTemplate调用目标服务接口时，拦截器会拦截请求，进行URL转换和负载均衡策略处理，然后发送请求到选中的服务实例。
四、自定义负载均衡算法
Ribbon允许用户自定义负载均衡算法。用户可以通过实现IRule接口并重写choose方法来自定义负载均衡策略。自定义的负载均衡算法需要注入到Spring容器中，并通过配置指定为Ribbon的负载均衡算法。

五、注意事项
版本兼容性：在使用Ribbon时，需要注意与Spring Cloud版本的兼容性。从Spring Cloud 2020.0版本开始，Spring Cloud官方已经将Ribbon标记为过时（deprecated），推荐使用Spring Cloud LoadBalancer作为替代方案。
算法选择：在选择负载均衡算法时，需要根据实际业务场景和需求进行选择。不同的算法有不同的优缺点和适用场景。
性能监控：在使用Ribbon进行负载均衡时，需要对服务的性能和可用性进行监控和评估，以确保负载均衡的有效性和稳定性。
综上所述，Ribbon通过客户端负载均衡和拦截器机制实现了对微服务架构中服务调用的负载均衡处理。通过合理配置和自定义负载均衡算法，可以实现对服务调用的有效分发和性能优化。
```

#### 2、Ribbon负载均衡算法的使用业务场景

```
RoundRobinRule：适用于服务实例性能相近，无特殊要求的场景。通过轮询的方式，确保每个服务实例都能被均匀访问。
RandomRule：适用于需要随机分配请求的场景，可以分散请求压力，避免某个服务实例被过度访问。
AvailabilityFilteringRule：适用于需要过滤掉不稳定或故障服务实例的场景。通过过滤掉这些服务实例，可以提高系统的稳定性和可靠性。
WeightedResponseTimeRule：适用于服务实例性能存在差异的场景。通过根据响应时间分配权重，可以优先访问性能较好的服务实例，提高系统的整体性能。
RetryRule：适用于需要重试获取服务的场景。当某个服务实例无法访问时，可以在指定时间内进行重试，确保请求能够成功被处理。
BestAvailableRule：适用于需要选择并发量最小的服务实例的场景。通过选择并发量最小的服务实例，可以优化系统的资源利用和响应时间。
ZoneAvoidanceRule：适用于需要考虑服务实例所在区域性能和可用性的场景。通过选择性能和可用性较好的服务实例，可以提高系统的稳定性和响应速度。
```

#### 3、核心接口

主要有ILoadBalancer、IRule、IPing、IClientConfig

```
Ribbon通过ILoadBalancer接口对外提供统一的选择服务器(Server)的功能，此接口会根据不同的负载均衡策略(IRule)选择合适的Server返回给使用者。

IRule是负载均衡策略的抽象，ILoadBalancer通过调用IRule的choose()方法返回Server.

IPing用来检测Server是否可用，ILoadBalancer的实现类维护一个Timer每隔10s检测一次Server的可用状态.

IClientConfig主要定义了用于初始化各种客户端和负载均衡器的配置信息，器实现类为DefaultClientConfigImpl。
```

##### 3.1 ILoadBalancer

[Ribbon](https://so.csdn.net/so/search?q=Ribbon&spm=1001.2101.3001.7020)通过ILoadBalancer接口对外提供统一的选择服务器(Server)的功能，此接口会根据不同的负载均衡策略(IRule)选择合适的Server返回给使用者。其核心方法如下:

```
public interface ILoadBalancer {

    public void addServers(List<Server> newServers);

    public Server chooseServer(Object key);
    
    public void markServerDown(Server server);
    
    public List<Server> getReachableServers();

    public List<Server> getAllServers();
}

```

此接口默认实现类为ZoneAwareLoadBalancer

##### 3.2 IRule

IRule是[负载均衡](https://so.csdn.net/so/search?q=负载均衡&spm=1001.2101.3001.7020)策略的抽象，ILoadBalancer通过调用IRule的choose()方法返回Server，其核心方法如下：

```
public interface IRule{
    
    public Server choose(Object key);
    
    public void setLoadBalancer(ILoadBalancer lb);
    
    public ILoadBalancer getLoadBalancer();    
}

```

##### 3.3 IPing

IPing用来检测Server是否可用，ILoadBalancer的实现类维护一个Timer每隔10s检测一次Server的可用状态，其核心方法有：

```
public interface IPing {
    public boolean isAlive(Server server);
}
```

##### 3.4 IClientConfig

IClientConfig主要定义了用于初始化各种客户端和负载均衡器的配置信息，器实现类为DefaultClientConfigImpl。



#### 4、负载均衡的逻辑实现

##### 4.1Server的选择

ILoadBalancer接口的主要实现类为BaseLoadBalancer和ZoneAwareLoadBalancer，ZoneAwareLoadBalancer为BaseLoadBalancer的子类并且其也重写了chooseServer方法，ZoneAwareLoadBalancer从其名称可以看出这个实现类是和Spring Cloud的分区有关的，当分区的数量为1(默认配置)时它直接调用父类BaseLoadBalancer的chooseServer()方法，源码如下：

```
@Override
public Server chooseServer(Object key) {
    if (!ENABLED.get() || getLoadBalancerStats().getAvailableZones().size() <= 1) {
        // 调用父类BaseLoadBalancer的chooseServer()方法
        return super.chooseServer(key);
    }
    // 略
}

```

类BaseLoadBalancer的chooseServer()方法直接调用IRule接口的choose()方法，源码如下：

```
public Server chooseServer(Object key) {
    if (counter == null) {
        counter = createCounter();
    }
    counter.increment();
    if (rule == null) {
        return null;
    } else {
        try {
            return rule.choose(key);
        } catch (Exception e) {
            logger.warn("LoadBalancer [{}]:  Error choosing server for key {}", name, key, e);
            return null;
        }
    }
}
```

这里IRule的实现类为ZoneAvoidanceRule，choose()方法的实现在其父类PredicateBasedRule中，如下

```
@Override
public Server choose(Object key) {
    ILoadBalancer lb = getLoadBalancer();
    Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
    if (server.isPresent()) {
        return server.get();
    } else {
        return null;
    }       
}
```

从上面源码可以看出，其先调用ILoadBalancer的getAllServers()方法获取所有Server列表，

getAllServers()方法的实现在BaseLoadBalancer类中，此类维护了一个List类型的属性allServerList，所有Server都缓存至此集合中。

获取Server列表后调用chooseRoundRobinAfterFiltering()方法返回Server对象。chooseRoundRobinAfterFiltering()方法会根据loadBalancerKey筛选出候选的Server，然后通过轮询的负载均衡策略选出Server，相关源码如下：

```
public Optional<Server> chooseRoundRobinAfterFiltering(List<Server> servers, Object loadBalancerKey) {
    List<Server> eligible = getEligibleServers(servers, loadBalancerKey);
    if (eligible.size() == 0) {
        return Optional.absent();
    }
    return Optional.of(eligible.get(incrementAndGetModulo(eligible.size())));
}

private int incrementAndGetModulo(int modulo) {
    for (;;) {
        int current = nextIndex.get();
        int next = (current + 1) % modulo;
        if (nextIndex.compareAndSet(current, next) && current < modulo)
            return current;
    }
}
```

可以看到其轮询选择Server的策略为获取次数加1然后对Server数量取余得到。



4.2 Server的状态检测

BaseLoadBalancer类的集合allServerList缓存了所有Server信息，但是这些Server的状态有可能发生变化，比如Server不可用了，Ribbon就需要及时感知到，那么Ribbon是如何感知Server可用不可用的呢？

BaseLoadBalancer的构造函数中初始化了一个任务调度器Timer，这个调度器每隔10s执行一次PingTask任务，相关源码如下:

```
public BaseLoadBalancer(String name, IRule rule, LoadBalancerStats stats,
            IPing ping, IPingStrategy pingStrategy) {
    
    this.name = name;
    this.ping = ping;
    this.pingStrategy = pingStrategy;
    setRule(rule);
    setupPingTask();
    lbStats = stats;
    init();
}
    
void setupPingTask() {
    if (canSkipPing()) {
        return;
    }
    if (lbTimer != null) {
        lbTimer.cancel();
    }
    lbTimer = new ShutdownEnabledTimer("NFLoadBalancer-PingTimer-" + name,
            true);
    lbTimer.schedule(new PingTask(), 0, pingIntervalSeconds * 1000);
    forceQuickPing();
}

class PingTask extends TimerTask {
    public void run() {
        try {
            new Pinger(pingStrategy).runPinger();
        } catch (Exception e) {
            logger.error("LoadBalancer [{}]: Error pinging", name, e);
        }
    }
}
```

深入Pinger和SerialPingStrategy的源码可知，最终通过NIWSDiscoveryPing这一IPing实现类判断Server是否可用，NIWSDiscoveryPing的isAlive()方法通过判断与Server关联的InstanceInfo的status是否为UP来判断Server是否可用，其isAlive()方法源码如下：

```
NIWSDiscoveryPing

public boolean isAlive(Server server) {
    boolean isAlive = true;
    if (server!=null && server instanceof DiscoveryEnabledServer){
        DiscoveryEnabledServer dServer = (DiscoveryEnabledServer)server;	            
        InstanceInfo instanceInfo = dServer.getInstanceInfo();
        if (instanceInfo!=null){	                
            InstanceStatus status = instanceInfo.getStatus();
            if (status!=null){
                isAlive = status.equals(InstanceStatus.UP);
            }
        }
    }
    return isAlive;
}
```



#### 5、Ribbon的使用姿势

RestTemplate + @LoadBalanced

提供一个标记@LoadBalanced的RestTemplate Bean，然后直接使用此Bean发起请求即可，如下：

```
@Configuration
public class Config {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        // 提供一个标记@LoadBalanced的RestTemplat Bean
        return new RestTemplate();
    }
}

@RestController
public class HelloController {

    @Resource
    private RestTemplate restTemplate;
    
    @GetMapping("/hi")
    public String hi() {
        // 直接使用即可
        return restTemplate.getForEntity("http://Eureka-Producer/hello", String.class).getBody();
    }
}
```

##### 5.1 当实例化LoadBalancerAutoConfiguration时，给所有标记了@LoadBalanced的RestTemplate Bean设置了拦截器LoadBalancerInterceptor，此实例保存在了RestTemplate的父类InterceptingHttpAccessor的集合List interceptors中。

设置拦截器LoadBalancerInterceptor源码如下：

```
@Configuration
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnBean(LoadBalancerClient.class)
@EnableConfigurationProperties(LoadBalancerRetryProperties.class)
public class LoadBalancerAutoConfiguration {

    // 1. 收集到所有标记了@LoadBalanced的RestTemplate
    @LoadBalanced
    @Autowired(required = false)
    private List<RestTemplate> restTemplates = Collections.emptyList();

    @Bean
    public SmartInitializingSingleton loadBalancedRestTemplateInitializerDeprecated(
            final ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers) {
        return () -> restTemplateCustomizers.ifAvailable(customizers -> {
            for (RestTemplate restTemplate : LoadBalancerAutoConfiguration.this.restTemplates) {
                for (RestTemplateCustomizer customizer : customizers) {
                    // 3. 对于每一个RestTemplate执行customize()方法
                    customizer.customize(restTemplate);
                }
            }
        });
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadBalancerRequestFactory loadBalancerRequestFactory(
            LoadBalancerClient loadBalancerClient) {
        return new LoadBalancerRequestFactory(loadBalancerClient, transformers);
    }

    @Configuration
    @ConditionalOnMissingClass("org.springframework.retry.support.RetryTemplate")
    static class LoadBalancerInterceptorConfig {
        @Bean
        public LoadBalancerInterceptor ribbonInterceptor(
                LoadBalancerClient loadBalancerClient,
                LoadBalancerRequestFactory requestFactory) {
            // 2. 注入LoadBalancerInterceptor
            return new LoadBalancerInterceptor(loadBalancerClient, requestFactory);
        }

        @Bean
        @ConditionalOnMissingBean
        public RestTemplateCustomizer restTemplateCustomizer(
                final LoadBalancerInterceptor loadBalancerInterceptor) {
            return restTemplate -> {
                // 4. customize()方法给RestTemplate设置LoadBalancerInterceptor
                List<ClientHttpRequestInterceptor> list = new ArrayList<>(
                        restTemplate.getInterceptors());
                list.add(loadBalancerInterceptor);
                restTemplate.setInterceptors(list);
            };
        }
    }
    // 略
}
```

