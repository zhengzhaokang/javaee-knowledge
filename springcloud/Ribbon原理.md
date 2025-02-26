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

##### 5.1 当实例化LoadBalancerAutoConfiguration时，

给所有标记了@LoadBalanced的RestTemplate Bean设置了拦截器LoadBalancerInterceptor，此实例保存在了RestTemplate的父类InterceptingHttpAccessor的集合List interceptors中。

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

##### 5.2 LoadBalancerClientLoadBalancerRequestFactory

从上面源码可以看出LoadBalancerInterceptor的构造函数接受两个参数：LoadBalancerClientLoadBalancerRequestFactory.

LoadBalancerRequestFactory的实例在此Configuration中被注入类，而LoadBalancerClient的实例却没有。那么LoadBalancerClient的实例是在哪里实例化的呢？答案是RibbonAutoConfiguration，这个Configuration注入了LoadBalancerClient的实现类RibbonLoadBalancerClient的实例和SpringClientFactory的实例，相关源码如下：

```
@Bean
public SpringClientFactory springClientFactory() {
    SpringClientFactory factory = new SpringClientFactory();
    factory.setConfigurations(this.configurations);
    return factory;
}

@Bean
@ConditionalOnMissingBean(LoadBalancerClient.class)
public LoadBalancerClient loadBalancerClient() {
    return new RibbonLoadBalancerClient(springClientFactory());
}

```

##### 5.3 LoadBalancerInterceptor

至此拦截器LoadBalancerInterceptor创建完成并且保存在了RestTemplate的集合属性中，那么RestTemplate是如何利用此拦截器的呢？

当我们使用RestTemplate发起请求时最终会调用到RestTemplate的doExecute()方法，此方法会创建ClientHttpRequest对象并调用其execute()方法发起请求，源码如下：

```
protected <T> T doExecute(URI url, @Nullable HttpMethod method, @Nullable RequestCallback requestCallback,
        @Nullable ResponseExtractor<T> responseExtractor) throws RestClientException {

    ClientHttpResponse response = null;
    try {
        // 1. 创建ClientHttpRequest。
        ClientHttpRequest request = createRequest(url, method);
        if (requestCallback != null) {
            requestCallback.doWithRequest(request);
        }
        // 2. 执行其execute()方法获取结果。
        response = request.execute();
        handleResponse(url, method, response);
        return (responseExtractor != null ? responseExtractor.extractData(response) : null);
    }
    catch (IOException ex) {
        String resource = url.toString();
        String query = url.getRawQuery();
        resource = (query != null ? resource.substring(0, resource.indexOf('?')) : resource);
        throw new ResourceAccessException("I/O error on " + method.name() +
                " request for \"" + resource + "\": " + ex.getMessage(), ex);
    }
    finally {
        if (response != null) {
            response.close();
        }
    }
}

protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
    ClientHttpRequest request = getRequestFactory().createRequest(url, method);
    if (logger.isDebugEnabled()) {
        logger.debug("HTTP " + method.name() + " " + url);
    }
    return request;
}

@Override
public ClientHttpRequestFactory getRequestFactory() {
    List<ClientHttpRequestInterceptor> interceptors = getInterceptors();
    if (!CollectionUtils.isEmpty(interceptors)) {
        ClientHttpRequestFactory factory = this.interceptingRequestFactory;
        if (factory == null) {
            factory = new InterceptingClientHttpRequestFactory(super.getRequestFactory(), interceptors);
            this.interceptingRequestFactory = factory;
        }
        return factory;
    }
    else {
        return super.getRequestFactory();
    }
}

```

##### 5.4 InterceptingClientHttpRequestFactory

从上面的getRequestFactory()方法可以看到当集合interceptors不为空的时候ClientHttpRequest对象是由类InterceptingClientHttpRequestFactory的createRequest()方法创建出来的，并且集合interceptors作为参数传递到了InterceptingClientHttpRequestFactory中，深入InterceptingClientHttpRequestFactory的createRequest()方法，如下：

```
public class InterceptingClientHttpRequestFactory extends AbstractClientHttpRequestFactoryWrapper {

    private final List<ClientHttpRequestInterceptor> interceptors;

    public InterceptingClientHttpRequestFactory(ClientHttpRequestFactory requestFactory,
            @Nullable List<ClientHttpRequestInterceptor> interceptors) {

        super(requestFactory);
        this.interceptors = (interceptors != null ? interceptors : Collections.emptyList());
    }

    @Override
    protected ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod, ClientHttpRequestFactory requestFactory) {
        // 直接返回InterceptingClientHttpRequest对象。
        return new InterceptingClientHttpRequest(requestFactory, this.interceptors, uri, httpMethod);
    }

}

```

##### 5.5 InterceptingClientHttpRequest

可以看到拦截器最终传递到了InterceptingClientHttpRequest中

上面说了RestTemplate的doExecute()方法创建了InterceptingClientHttpRequest对象且调用了其execute()方法获取响应结果，深入其execute()方法发现在execute()中直接调用了拦截器的intercept()方法，也即InterceptingClientHttpRequest的intercept()方法，源码如下：

```
public ClientHttpResponse execute(HttpRequest request, byte[] body) throws IOException {
    if (this.iterator.hasNext()) {
        ClientHttpRequestInterceptor nextInterceptor = this.iterator.next();
        // 这里调用InterceptingClientHttpRequest的intercept()方法
        return nextInterceptor.intercept(request, body, this);
    }
    // 略
}

```

也就是说RestTemplate的请求最终是委托给InterceptingClientHttpRequest来处理。那么InterceptingClientHttpRequest是如何利用Ribbon相关接口处理请求的呢？且看InterceptingClientHttpRequest的intercept()方法：

```
public class LoadBalancerInterceptor implements ClientHttpRequestInterceptor {

    private LoadBalancerClient loadBalancer;
    private LoadBalancerRequestFactory requestFactory;

    public LoadBalancerInterceptor(LoadBalancerClient loadBalancer, LoadBalancerRequestFactory requestFactory) {
        this.loadBalancer = loadBalancer;
        this.requestFactory = requestFactory;
    }

    public LoadBalancerInterceptor(LoadBalancerClient loadBalancer) {
        // for backwards compatibility
        this(loadBalancer, new LoadBalancerRequestFactory(loadBalancer));
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
            final ClientHttpRequestExecution execution) throws IOException {
        final URI originalUri = request.getURI();
        String serviceName = originalUri.getHost();
        // 直接调用LoadBalancerClient的execute()方法。
        return this.loadBalancer.execute(serviceName, requestFactory.createRequest(request, body, execution));
    }
}
```

可以看到InterceptingClientHttpRequest的intercept()方法直接调用LoadBalancerClient的execute()方法，LoadBalancerClient是一个接口，这里其实现类为RibbonLoadBalancerClient，上面创建InterceptingClientHttpRequest时提到LoadBalancerAutoConfiguration注入了RibbonLoadBalancerClient Bean，此Bean通过构造函数保存在了LoadBalancerClient中。那么接下来就是LoadBalancerClient的execute()方法了.

##### 5.6 LoadBalancerClient的execute()方法

```
LoadBalancerClient的execute()方法首先会通过调用SpringClientFactory的getLoadBalancer()方法获取ILoadBalancer，那么此方法是如何返回ILoadBalancer呢？很简单，就是从Spring上下文中获取，那么Spring上下文中的ILoadBalancer时何时注入的呢？答案是RibbonClientConfiguration，此Configuration向Spring上下文注入了以下Bean：

ILoadBalancer的实现类ZoneAwareLoadBalancer。
IRule的实现类ZoneAvoidanceRule。
IClientConfig的实现类DefaultClientConfigImpl。

另外EurekaRibbonClientConfiguration还注入了：
ServerList的实现类DomainExtractingServerList和DiscoveryEnabledNIWSServerList。
IPing的实现类NIWSDiscoveryPing。
```

源码如下:

```
@Bean
@ConditionalOnMissingBean
public IClientConfig ribbonClientConfig() {
    DefaultClientConfigImpl config = new DefaultClientConfigImpl();
    config.loadProperties(this.name);
    config.set(CommonClientConfigKey.ConnectTimeout, DEFAULT_CONNECT_TIMEOUT);
    config.set(CommonClientConfigKey.ReadTimeout, DEFAULT_READ_TIMEOUT);
    config.set(CommonClientConfigKey.GZipPayload, DEFAULT_GZIP_PAYLOAD);
    return config;
}

@Bean
@ConditionalOnMissingBean
public IRule ribbonRule(IClientConfig config) {
    if (this.propertiesFactory.isSet(IRule.class, name)) {
        return this.propertiesFactory.get(IRule.class, config, name);
    }
    ZoneAvoidanceRule rule = new ZoneAvoidanceRule();
    rule.initWithNiwsConfig(config);
    return rule;
}

@Bean
@ConditionalOnMissingBean
public ServerList<Server> ribbonServerList(IClientConfig config) {
    if (this.propertiesFactory.isSet(ServerList.class, name)) {
        return this.propertiesFactory.get(ServerList.class, config, name);
    }
    ConfigurationBasedServerList serverList = new ConfigurationBasedServerList();
    serverList.initWithNiwsConfig(config);
    return serverList;
}

@Bean
@ConditionalOnMissingBean
public ILoadBalancer ribbonLoadBalancer(IClientConfig config,
        ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
        IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
    if (this.propertiesFactory.isSet(ILoadBalancer.class, name)) {
        return this.propertiesFactory.get(ILoadBalancer.class, config, name);
    }
    return new ZoneAwareLoadBalancer<>(config, rule, ping, serverList,
            serverListFilter, serverListUpdater);
}

@Bean
@ConditionalOnMissingBean
public IPing ribbonPing(IClientConfig config) {
    if (this.propertiesFactory.isSet(IPing.class, serviceId)) {
        return this.propertiesFactory.get(IPing.class, config, serviceId);
    }
    NIWSDiscoveryPing ping = new NIWSDiscoveryPing();
    ping.initWithNiwsConfig(config);
    return ping;
}

@Bean
@ConditionalOnMissingBean
public ServerList<?> ribbonServerList(IClientConfig config, Provider<EurekaClient> eurekaClientProvider) {
    if (this.propertiesFactory.isSet(ServerList.class, serviceId)) {
        return this.propertiesFactory.get(ServerList.class, config, serviceId);
    }
    DiscoveryEnabledNIWSServerList discoveryServerList = new DiscoveryEnabledNIWSServerList(
            config, eurekaClientProvider);
    DomainExtractingServerList serverList = new DomainExtractingServerList(
            discoveryServerList, config, this.approximateZoneFromHostname);
    return serverList;
}

```

##### 5.7  ZoneAwareLoadBalancer

ZoneAwareLoadBalancer的构造函数通过调用DiscoveryEnabledNIWSServerList的getUpdatedListOfServers()方法获取Server集合

DiscoveryEnabledNIWSServerList维护了一个Provider类型的属性eurekaClientProvider，eurekaClientProvider缓存了EurekaClient的实现类CloudEurekaClient的实例，getUpdatedListOfServers()方法通过调用CloudEurekaClient的getInstancesByVipAddress()方法从Eureka Client缓存中获取应用对应的所有InstanceInfo列表。源码如下：

```
// 缓存了EurekaClient的实现类CloudEurekaClient的实例
private final Provider<EurekaClient> eurekaClientProvider;

@Override
public List<DiscoveryEnabledServer> getUpdatedListOfServers(){
    return obtainServersViaDiscovery();
}

private List<DiscoveryEnabledServer> obtainServersViaDiscovery() {
    List<DiscoveryEnabledServer> serverList = new ArrayList<DiscoveryEnabledServer>();

    if (eurekaClientProvider == null || eurekaClientProvider.get() == null) {
        logger.warn("EurekaClient has not been initialized yet, returning an empty list");
        return new ArrayList<DiscoveryEnabledServer>();
    }

    EurekaClient eurekaClient = eurekaClientProvider.get();
    if (vipAddresses!=null){
        for (String vipAddress : vipAddresses.split(",")) {
            // if targetRegion is null, it will be interpreted as the same region of client
            List<InstanceInfo> listOfInstanceInfo = eurekaClient.getInstancesByVipAddress(vipAddress, isSecure, targetRegion);
            for (InstanceInfo ii : listOfInstanceInfo) {
                if (ii.getStatus().equals(InstanceStatus.UP)) {

                    if(shouldUseOverridePort){
                        if(logger.isDebugEnabled()){
                            logger.debug("Overriding port on client name: " + clientName + " to " + overridePort);
                        }

                        InstanceInfo copy = new InstanceInfo(ii);

                        if(isSecure){
                            ii = new InstanceInfo.Builder(copy).setSecurePort(overridePort).build();
                        }else{
                            ii = new InstanceInfo.Builder(copy).setPort(overridePort).build();
                        }
                    }

                    DiscoveryEnabledServer des = createServer(ii, isSecure, shouldUseIpAddr);
                    serverList.add(des);
                }
            }
            if (serverList.size()>0 && prioritizeVipAddressBasedServers){
                break; // if the current vipAddress has servers, we dont use subsequent vipAddress based servers
            }
        }
    }
    return serverList;
}
```

LoadBalancerClient的execute()方法在通过调用SpringClientFactory的getLoadBalancer()方法获取ILoadBalancer后调用其chooseServer()返回一个Server对象，如下：

```
public <T> T execute(String serviceId, LoadBalancerRequest<T> request, Object hint) throws IOException {
    // 1. 获取ILoadBalancer
    ILoadBalancer loadBalancer = getLoadBalancer(serviceId);
    
    // 2. 通过ILoadBalancer选择一个Server
    Server server = getServer(loadBalancer, hint);
    if (server == null) {
        throw new IllegalStateException("No instances available for " + serviceId);
    }
    RibbonServer ribbonServer = new RibbonServer(serviceId, server, isSecure(server,
            serviceId), serverIntrospector(serviceId).getMetadata(server));

    // 3. 对Server发起请求
    return execute(serviceId, ribbonServer, request);
}

protected Server getServer(ILoadBalancer loadBalancer, Object hint) {
    if (loadBalancer == null) {
        return null;
    }
    // Use 'default' on a null hint, or just pass it on?
    return loadBalancer.chooseServer(hint != null ? hint : "default");
}

```

##### 5.8  ZoneAwareLoadBalancer的chooseServer

ZoneAwareLoadBalancer的chooseServer()方法会通过调用ZoneAvoidanceRule的choose()方法返回一个Server

ZoneAvoidanceRule继承类ClientConfigEnabledRoundRobinRule，所以其会根据ZoneAwareLoadBalancer获取的Server列表采用轮询的负载均衡策略选择一个Server返回；最后根据此Server的地址等向其发起请求.



#### 6、Feign接口

##### 6.1 Feign基本使用

相对于RestTemplate+@Loadbalance的方式，我们在使用Spring Cloud的时候使用更多的是Feign接口，因为Feign接口使用起来会更加简单，下面就是一个使用Feign接口调用服务的例子：

```
// 定义Feign接口
@FeignClient(value = "Eureka-Producer", fallbackFactory = HelloClientFallbackFactory.class)
public interface HelloClient {

    @GetMapping("/hello")
    String hello();
}

// 订单熔断快速失败回调
@Component
public class HelloClientFallbackFactory implements FallbackFactory<HelloClient>, HelloClient {

    @Override
    public HelloClient create(Throwable throwable) {
        return this;
    }

    @Override
    public String hello() {
        return "熔断";
    }
}

// 使用
@RestController
public class HelloController {

    @Resource
    private HelloClient helloClient;

    @GetMapping("/hello")
    public String hello() {
        return helloClient.hello();
    }
}

```

##### 6.2 执行请求时机 LoadBalancerFeignClient

与RestTemplate的通过RibbonLoadBalancerClient获取Server并执行请求类似，

Feign接口通过LoadBalancerFeignClient获取Server并执行请求。DefaultFeignLoadBalancedConfiguration会注入LoadBalancerFeignClient Bean，源码如下：

```
@Configuration
class DefaultFeignLoadBalancedConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory) {
        return new LoadBalancerFeignClient(new Client.Default(null, null),
                cachingFactory, clientFactory);
    }
}
```

##### 6.3 如何利用Ribbon做负载均衡

那么LoadBalancerFeignClient的execute()方法又是如何利用Ribbon做负载均衡的呢？其通过调用CachingSpringLoadBalancerFactory的create()方法获取FeignLoadBalancer对象，FeignLoadBalancer对象持有一个ILoadBalancer的对象实例，此ILoadBalancer对象实例是CachingSpringLoadBalancerFactory通过调用SpringClientFactory的getLoadBalancer()方法从Spring上下文中获取的，源码如下：

```
public class CachingSpringLoadBalancerFactory {

	protected final SpringClientFactory factory;

	protected LoadBalancedRetryFactory loadBalancedRetryFactory = null;

	private volatile Map<String, FeignLoadBalancer> cache = new ConcurrentReferenceHashMap<>();

	public CachingSpringLoadBalancerFactory(SpringClientFactory factory) {
		this.factory = factory;
	}

	public CachingSpringLoadBalancerFactory(SpringClientFactory factory,
			LoadBalancedRetryFactory loadBalancedRetryPolicyFactory) {
		this.factory = factory;
		this.loadBalancedRetryFactory = loadBalancedRetryPolicyFactory;
	}

	public FeignLoadBalancer create(String clientName) {
		FeignLoadBalancer client = this.cache.get(clientName);
		if (client != null) {
			return client;
		}
		IClientConfig config = this.factory.getClientConfig(clientName);
		ILoadBalancer lb = this.factory.getLoadBalancer(clientName);
		ServerIntrospector serverIntrospector = this.factory.getInstance(clientName,
				ServerIntrospector.class);
		client = this.loadBalancedRetryFactory != null
				? new RetryableFeignLoadBalancer(lb, config, serverIntrospector,
						this.loadBalancedRetryFactory)
				: new FeignLoadBalancer(lb, config, serverIntrospector);
		this.cache.put(clientName, client);
		return client;
	}

}
```

##### 6.4 AbstractLoadBalancerAwareClient 执行executeWithLoadBalancer

FeignLoadBalancer 父类 AbstractLoadBalancerAwareClient 执行executeWithLoadBalancer,

创建完FeignLoadBalancer后紧接着接着调用了LoadBalancerFeignClient的executeWithLoadBalancer()方法，如下:

```
@Override
public Response execute(Request request, Request.Options options) throws IOException {
    URI asUri = URI.create(request.url());
    String clientName = asUri.getHost();
    URI uriWithoutHost = cleanUrl(request.url(), clientName);
    FeignLoadBalancer.RibbonRequest ribbonRequest = new FeignLoadBalancer.RibbonRequest(
            this.delegate, request, uriWithoutHost);

    IClientConfig requestConfig = getClientConfig(options, clientName);
    // 执行FeignLoadBalancer的executeWithLoadBalancer()方法。
    return lbClient(clientName).executeWithLoadBalancer(ribbonRequest,
            requestConfig).toResponse();
    // 略
}
// 创建FeignLoadBalancer对象并返回
private FeignLoadBalancer lbClient(String clientName) {
    return this.lbClientFactory.create(clientName);
}
```

executeWithLoadBalancer()方法的具体实现在类FeignLoadBalancer的父类AbstractLoadBalancerAwareClient中，如下：

```
public T executeWithLoadBalancer(final S request, final IClientConfig requestConfig) throws ClientException {
    LoadBalancerCommand<T> command = buildLoadBalancerCommand(request, requestConfig);

    try {
        return command.submit(
            new ServerOperation<T>() {
                @Override
                public Observable<T> call(Server server) {
                    URI finalUri = reconstructURIWithServer(server, request.getUri());
                    S requestForServer = (S) request.replaceUri(finalUri);
                    try {
                        return Observable.just(AbstractLoadBalancerAwareClient.this.execute(requestForServer, requestConfig));
                    } 
                    catch (Exception e) {
                        return Observable.error(e);
                    }
                }
            })
            .toBlocking()
            .single();
    } catch (Exception e) {
        // 略
    }
}
```

##### 6.5 LoadBalancerCommand 的 submit方法

executeWithLoadBalancer()方法创建了LoadBalancerCommand对象并且向提交(submit()方法)了一个ServerOperation对象，跟踪LoadBalancerCommand的submit()方法发现其调用了selectServer()方法获取Server，而selectServer()方法则委托给了FeignLoadBalancer的父类LoadBalancerContext的getServerFromLoadBalancer()方法获取Server，如下：

```
public T executeWithLoadBalancer(final S request, final IClientConfig requestConfig) throws ClientException {
    LoadBalancerCommand<T> command = buildLoadBalancerCommand(request, requestConfig);

    try {
        return command.submit(
            new ServerOperation<T>() {
                @Override
                public Observable<T> call(Server server) {
                    URI finalUri = reconstructURIWithServer(server, request.getUri());
                    S requestForServer = (S) request.replaceUri(finalUri);
                    try {
                        return Observable.just(AbstractLoadBalancerAwareClient.this.execute(requestForServer, requestConfig));
                    } 
                    catch (Exception e) {
                        return Observable.error(e);
                    }
                }
            })
            .toBlocking()
            .single();
    } catch (Exception e) {
        Throwable t = e.getCause();
        if (t instanceof ClientException) {
            throw (ClientException) t;
        } else {
            throw new ClientException(e);
        }
    }
    
}

public Observable<T> submit(final ServerOperation<T> operation) {
        final ExecutionInfoContext context = new ExecutionInfoContext();
    // 略
    
    // 这里当server为null时调用selectServer()获取Server。
    Observable<T> o = 
            (server == null ? selectServer() : Observable.just(server))
            .concatMap(new Func1<Server, Observable<T>>() {
                @Override
                // Called for each server being selected
                public Observable<T> call(Server server) {
                    context.setServer(server);
                    final ServerStats stats = loadBalancerContext.getServerStats(server);
                    
                    // Called for each attempt and retry
                    Observable<T> o = Observable
                            .just(server)
                            .concatMap(new Func1<Server, Observable<T>>() {
                                @Override
                                public Observable<T> call(final Server server) {
                                    // 略
                }
            });
        // 略
}

private Observable<Server> selectServer() {
    return Observable.create(new OnSubscribe<Server>() {
        @Override
        public void call(Subscriber<? super Server> next) {
            try {
                // 调用LoadBalancerContext的getServerFromLoadBalancer()获取Server
                Server server = loadBalancerContext.getServerFromLoadBalancer(loadBalancerURI, loadBalancerKey);   
                next.onNext(server);
                next.onCompleted();
            } catch (Exception e) {
                next.onError(e);
            }
        }
    });
}
```

##### 6.6 LoadBalancerContext的getServerFromLoadBalancer()方法

FeignLoadBalancer和LoadBalancerCommand互相依赖、彼此调用，最终FeignLoadBalancer的父类LoadBalancerContext的getServerFromLoadBalancer()方法返回了Server，此方法通过调用其持有的ILoadBalancer对象的chooseServer()方法获取Server，源码如下：

```
public Server getServerFromLoadBalancer(@Nullable URI original, @Nullable Object loadBalancerKey) throws ClientException {
        String host = null;
    int port = -1;
    if (original != null) {
        host = original.getHost();
    }
    if (original != null) {
        Pair<String, Integer> schemeAndPort = deriveSchemeAndPortFromPartialUri(original);        
        port = schemeAndPort.second();
    }
    // 获取ILoadBalancer
    ILoadBalancer lb = getLoadBalancer();
    // 调用ILoadBalancer的chooseServer()方法获取Server。
    Server svc = lb.chooseServer(loadBalancerKey);
    if (svc == null){
        throw new ClientException(ClientException.ErrorType.GENERAL,
                "Load balancer does not have available server for client: "
                        + clientName);
    }
    host = svc.getHost();
    if (host == null){
        throw new ClientException(ClientException.ErrorType.GENERAL,
                "Invalid Server for :" + svc);
    }
    logger.debug("{} using LB returned Server: {} for request {}", new Object[]{clientName, svc, original});
    return svc;
}
```

#### 7、总结

##### 7.1 ILoadBalancer、IRule、IPing

```
Ribbon通过ILoadBalancer接口提供负载均衡服务，其实现原理为：
ILoadBalancer依赖ServerList通过DiscoveryClient从Eureka Client处获取Server列表并缓存这些Server列表。
IRule接口是负载均衡策略的抽象，ILoadBalancer通过IRule选出一个Server。
IPing接口定时对ILoadBalancer缓存的Server列表进行检测，判断其是否可用。

当使用RestTemplate+@LoadBalanced的方式进行服务调用时，LoadBalancerInterceptor和RibbonLoadBalancerClient作为桥梁结合Ribbon提供负载均衡服务。
当使用Feign接口调用服务时，LoadBalancerFeignClient和FeignLoadBalancer作为调用Ribbon的入口为Feign接口提供负载均衡服务。
不管使用那种姿势，最终都会通过Ribbon的ILoadBalancer接口实现负载均衡。
```

##### 7.2 Ribbon相关Configuration以及注入的Bean

```
RibbonAutoConfiguration
注入了 LoadBalancerClient的实现类RibbonLoadBalancerClient。
注入了SpringClientFactory。

LoadBalancerAutoConfiguration
注入了LoadBalancerInterceptor。
给RestTemplate设置LoadBalancerInterceptor。

RibbonClientConfiguration
注入了ILoadBalancer的实现类ZoneAwareLoadBalancer。
注入了IRule的实现类ZoneAvoidanceRule。
注入了IClientConfig的实现类DefaultClientConfigImpl。

EurekaRibbonClientConfiguration
注入了IPing的实现类NIWSDiscoveryPing。
注入了ServerList的实现类DiscoveryEnabledNIWSServerList。
```

