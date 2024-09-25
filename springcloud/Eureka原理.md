Eureka原理

#### 1、背景

```
分布式系统是由多个应用程序协同工作来完成任务的一种工作模式。
如何管理多个应用程序？服务注册与发现应运而生。
```

#### 2、服务发现解决的痛点

##### 2.1 解耦、屏蔽程序之间IP和端口依赖

```
分布式系统中，程序之间通过一次或者多次远程调用或者数据传输完成任务，调用程序我们首先需要知道被调用的程序在网络中的位置，在网络中地位程序的位置通过IP+端口号。但是这种方式存在两个问题：
1、IP地址没有任务特殊含义，不容易记忆和理解；
2、当被调用的程序IP和端口号改变，调用者也需要同步修改地址；
通过服务发现，可以将程序之间对于IP+端口号的依赖转化为服务名的依赖，服务名可以根据业务功能等进行命名。因此，解耦、屏蔽服务之间IP+端口号的依赖是分布式系统需要解决的第一个问题。
```

##### 2.2 动态管理服务状态

```
在分布式系统重，我们说程序或者服务有可能宕机，磁盘可能损坏、线程可能全部被占用、网络可能不稳定。因此，在分布式系统中程序的状态是随时变化的，不可预测的，谁也无法保证某个程序下一秒是否可以正常运行。如果某个程序挂掉，调用者不能及时知道，就会出现不可控的连锁反应。

服务发现可以对服务的状态进行管理，当程序状态发生改变，可以第一时间通知程序的调用者，可以从两方面理解：
1、程序之间需要知道彼此对方的信息与状态。
2、对方程序状态发生改变能及时知晓。
```

#### 3、Eureka是如何设计服务发现

##### 3.1 统一管理中心

```
服务发现要做的是抽象程序标识达到解耦、屏蔽IP+端口号依赖以及程序状态的实时管理，要进行管理，首先会想到集权、集中、统一、设置机构等，Eureka也是如此，要管理程序，首先要有一个统一的管理中心--注册中心。

因此，注册中心就是Eureka的大脑，负责Eureka各项管理、协调的职能。
```

##### 3.2 基本概念

```
将注册中心作为服务发现中的管理者，是一个不错的想法。就如一个公司，出钱的是老板、找活的是销售经历、监督干活的是项目经理、干活的是程序员。

Eureka也对管理者与非管理者进行了区分，负责管理的-注册中心(服务端)，负责干活的程序(客户端),对应EurekaServer和EurekaClient.
```

##### 3.3 基本运行流程

- 客户端发起服务注册
- 服务端保存注册信息到注册表
- 客户端定时发送心跳检测
- 服务端服务剔除及自我保护
- 客户端发起服务下线
- 客户端定时向服务端获取注册列表信息，并保存到本地内存
- 客户端整合服务发现

```
1.客户端向服务端发起注册
2、服务端注册表（双字典结构），保存注册信息
3、客户端定时续约心跳，并获取注册表信息，保存到本地内存，并整合服务发现
4、客户端下线通知
5、服务端自我保护机制，剔除服务定时器，定时剔除客户端
```

###### 3.3.1 客户端发起服务注册

```
客户端向服务端发生请求，将自身相关的信息提交给服务端

对于这一步操作很容易理解，客户端启动要加入分布式系统中，首先就是要提供自身的相关信息，告诉服务端我来了。

Eureka服务端提供一个接口，用来接收客户端服务注册的请求，服务端会一直监听这个接口，等待客户端调用。

### 关键点，调用注册接口
客户端在启动时首先找到服务端以及自身的信息（分区、服务名称、IP、端口等），调用服务端提供的服务注册接口，将自身的信息发送过去。
###

关于配置信息可能在客户端的配置文件中，可能在配置中心统一配置。
```

##### 3.3.2.服务端保存注册信息

```
服务端保存客户端请求发送的服务注册信息到本地内存注册表

注册表是服务发现的核心，基本所有的操作都是围绕注册表进行操作，注册表的添加、获取、更新、删除、同步等一系列操作。

Eureka的注册表是一个双字典结构的数据，服务发现的目的是标识服务和服务状态的管理，所以注册表中有服务标识、服务基本信息、服务状态信息等

ConcurrentHashMap<String, Map<String, Lease<InstanceInfo>>> registry = new ConcurrentHashMap();
```

```
	@Override
	public void register(final InstanceInfo info, final boolean isReplication) {
		handleRegistration(info, resolveInstanceLeaseDuration(info), isReplication);
		super.register(info, isReplication);
	}

	@Override
	public boolean cancel(String appName, String serverId, boolean isReplication) {
		handleCancelation(appName, serverId, isReplication);
		return super.cancel(appName, serverId, isReplication);
	}

	@Override
	public boolean renew(final String appName, final String serverId, boolean isReplication) {
	}
```

##### 3.3.3 客户端定时发送心跳检测

```
客户端定时向服务端发送请求，告诉服务端自己运行正常

客户端只是在启动时注册服务信息，后续运行过程服务端不知道客户端是否运行正常，就无法对无法状态进行实时的管理。因此客户端定时不断的向服务端发送请求，告诉服务端自己运行正常，这种主动上报状态的过程，在Eureka中叫做--服务续约

具体实现，服务端提供一个续约接口，客户端通过定时任务不断的调用续约接口，服务端收到请求后，更新注册表中服务续约时间
```

涉及源码

```
    /**
     * Marks the given instance of the given app name as renewed, and also marks whether it originated from
     * replication.
     *
     * @see com.netflix.eureka.lease.LeaseManager#renew(java.lang.String, java.lang.String, boolean)
     */
    public boolean renew(String appName, String id, boolean isReplication) {
        RENEW.increment(isReplication);
        Map<String, Lease<InstanceInfo>> gMap = registry.get(appName);
        Lease<InstanceInfo> leaseToRenew = null;
        if (gMap != null) {
            leaseToRenew = gMap.get(id);
        }
        if (leaseToRenew == null) {
            RENEW_NOT_FOUND.increment(isReplication);
            logger.warn("DS: Registry: lease doesn't exist, registering resource: {} - {}", appName, id);
            return false;
        } else {
            InstanceInfo instanceInfo = leaseToRenew.getHolder();
            if (instanceInfo != null) {
                // touchASGCache(instanceInfo.getASGName());
                InstanceStatus overriddenInstanceStatus = this.getOverriddenInstanceStatus(
                        instanceInfo, leaseToRenew, isReplication);
                if (overriddenInstanceStatus == InstanceStatus.UNKNOWN) {
                    logger.info("Instance status UNKNOWN possibly due to deleted override for instance {}"
                            + "; re-register required", instanceInfo.getId());
                    RENEW_NOT_FOUND.increment(isReplication);
                    return false;
                }
                if (!instanceInfo.getStatus().equals(overriddenInstanceStatus)) {
                    logger.info(
                            "The instance status {} is different from overridden instance status {} for instance {}. "
                                    + "Hence setting the status to overridden status", instanceInfo.getStatus().name(),
                                    instanceInfo.getOverriddenStatus().name(),
                                    instanceInfo.getId());
                    instanceInfo.setStatusWithoutDirty(overriddenInstanceStatus);

                }
            }
            renewsLastMin.increment();
            leaseToRenew.renew();
            return true;
        }
    }
```

##### 3.3.4.服务端服务剔除和自我保护

```
服务端在一段时间没有收到客户端的心跳请求，就从注册表移除该客户端

上面说的心跳检测续约客户端定时发送心跳请求，服务端收到请求将注册表中服务续约时间进行更新，目的是服务端能实时知道客户端运行状态

如果服务端在一段时间，默认（90s）没有收到客户端心跳请求，服务端任务客户端挂掉了，就会从注册表中移除给客户端信息，这个过程叫做--服务剔除

有时候由于网络原因，客户端与服务端无法进行正常通信，但是客户端仍然运行正常，可以进行相互的访问，如果按照续约，那么所有的客户端就会从注册表中被移除，这样影响了那些正常运行的客户端

因此，服务端有自我保护机制处理这种问题

服务端判断在15分钟内，有超过85%的客户端都没有进行服务续约，则进入自我保护；

进入自我保护机制，服务端不在剔除没有续约的客户端；

进入自我保护机制，服务端只接收新客户端的注册和服务查询；
```

涉及源码

```
public void evict(long additionalLeaseMs) {
        logger.debug("Running the evict task");

        if (!isLeaseExpirationEnabled()) {
            logger.debug("DS: lease expiration is currently disabled.");
            return;
        }

        // We collect first all expired items, to evict them in random order. For large eviction sets,
        // if we do not that, we might wipe out whole apps before self preservation kicks in. By randomizing it,
        // the impact should be evenly distributed across all applications.
        List<Lease<InstanceInfo>> expiredLeases = new ArrayList<>();
        for (Entry<String, Map<String, Lease<InstanceInfo>>> groupEntry : registry.entrySet()) {
            Map<String, Lease<InstanceInfo>> leaseMap = groupEntry.getValue();
            if (leaseMap != null) {
                for (Entry<String, Lease<InstanceInfo>> leaseEntry : leaseMap.entrySet()) {
                    Lease<InstanceInfo> lease = leaseEntry.getValue();
                    if (lease.isExpired(additionalLeaseMs) && lease.getHolder() != null) {
                        expiredLeases.add(lease);
                    }
                }
            }
        }

        // To compensate for GC pauses or drifting local time, we need to use current registry size as a base for
        // triggering self-preservation. Without that we would wipe out full registry.
        int registrySize = (int) getLocalRegistrySize();
        int registrySizeThreshold = (int) (registrySize * serverConfig.getRenewalPercentThreshold());
        int evictionLimit = registrySize - registrySizeThreshold;

        int toEvict = Math.min(expiredLeases.size(), evictionLimit);
        if (toEvict > 0) {
            logger.info("Evicting {} items (expired={}, evictionLimit={})", toEvict, expiredLeases.size(), evictionLimit);

            Random random = new Random(System.currentTimeMillis());
            for (int i = 0; i < toEvict; i++) {
                // Pick a random item (Knuth shuffle algorithm)
                int next = i + random.nextInt(expiredLeases.size() - i);
                Collections.swap(expiredLeases, i, next);
                Lease<InstanceInfo> lease = expiredLeases.get(i);

                String appName = lease.getHolder().getAppName();
                String id = lease.getHolder().getId();
                EXPIRED.increment();
                logger.warn("DS: Registry: expired lease for {}/{}", appName, id);
                internalCancel(appName, id, false);
            }
        }
    }
```



##### 3.3.5.客户端发送服务下线请求

```
客户端正常关闭，向服务端发送服务下线请求，服务会直接从注册表移除该客户端

Eureka通过心跳续约、服务剔除来排查异常的服务，那么对于一下正常关闭的服务，例如进行一下新的功能发布等，可以通过发送服务下线请求，服务端从注册表移除客户端，这个过程叫做--服务下线
```

##### 3.3.6.客户端定时获取注册表信息

```
客户端定时向服务端发送请求，获取注册表信息，保存到本地内存。

在设计中，客户端本地也有一个注册表，还有一个定时器，定时从服务端更新注册表信息，保存到客户端本地内存
```

##### 3.3.7.客户端整合服务发现

```
客户端消费者从本地注册表中获取客户方生产者服务信息，并进行后续的操作。
```



#### 4.Eureka如何保证高可用和一致性

```
Eureka支持集群部署，不同区域、机房部署多个服务端实例，这样可以横向扩展服务发现的规模，当有一个服务端挂掉，其他服务端仍然可以正常运行，保证系统的高可用性，但是集群多个节点部署，必须要考虑的一个问题，就是数据一致性。
```

##### 4.1 CAP理论

```
C（一致性）：所有的节点上的数据时刻保持同步
A（可用性）：每个请求都能接受到一个响应，无论响应成功或失败
P（分区容错）：系统应该能持续提供服务，即使系统内部有消息丢失（分区）

要保证一致性，就要进行所有节点数据的同步，同步的过程中无法保证系统可用性，会出现超时，如果要保证可用性，那么就无法保证一致性；分区容错在分布式系统中必须存在，所以必须在一致性和可用性两者进行取舍衡量。

Eureka的设计中认为系统的可用性优于一致性，采用AP，同样作为对比zookeeper使用的是CP

例如现在有两个机房，机房A中有一个主节点和一个从节点，机房B有一个从节点，当出现某些原因，两个机房无法进行通信，那么当机房2中生产者服务B进行服务注册时，从节点要同步到机房A中的主节点才算注册成功，由于zk CP使用的是强一致性，所以会注册失败。在机房2中生产者与消费者直接进行调用是没有问题的，但是zk为了一致性，牺牲了机房2中的部分可用性。

二对于Eureka，采用的是AP，当网络出现分区，机房2中的服务仍然可以进行注册，服务之间也可以正常调用，但是两个机房的数据会不一致，为了可用性，牺牲了一致性。

Eureka采用AP，主要是服务发现中注册表信息不会涉及到业务逻辑，保存的是一些服务器信息，这些信息不会经常发送变化，所以导致不一致的几率也比较小，所以采用了AP。
```

##### 4.2 服务端数据同步

```
Peer To Peer同步模式
分布式系统中数据同步模式一般分为两种：主从模式、对等模式。

主从模式：集群中有一个主副本和多个从副本，主副本负责数据的写操作，然后将数据同步到其他从副本，从副本负责读操作。该模式主副本面临所有的写操作压力，可能会成为瓶颈，但是可以保证一致性。

对等模式：在集群中不存在主从副本，任何一个节点都可以进行读写操作，然后节点之间进行相互数据同步，优点是没有单点的写操作压力，缺点是进行数据同步和数据冲突是一个需要解决的问题。

Eureka中集群节点进行数据同步，采用的是 对等模式
```

涉及源码

```
    /**
     * Replicates all instance changes to peer eureka nodes except for
     * replication traffic to this node.
     *
     */
    private void replicateInstanceActionsToPeers(Action action, String appName,
                                                 String id, InstanceInfo info, InstanceStatus newStatus,
                                                 PeerEurekaNode node) {
        try {
            InstanceInfo infoFromRegistry;
            CurrentRequestVersion.set(Version.V2);
            switch (action) {
                case Cancel:
                    node.cancel(appName, id);
                    break;
                case Heartbeat:
                    InstanceStatus overriddenStatus = overriddenInstanceStatusMap.get(id);
                    infoFromRegistry = getInstanceByAppAndId(appName, id, false);
                    node.heartbeat(appName, id, infoFromRegistry, overriddenStatus, false);
                    break;
                case Register:
                    node.register(info);
                    break;
                case StatusUpdate:
                    infoFromRegistry = getInstanceByAppAndId(appName, id, false);
                    node.statusUpdate(appName, id, newStatus, infoFromRegistry);
                    break;
                case DeleteStatusOverride:
                    infoFromRegistry = getInstanceByAppAndId(appName, id, false);
                    node.deleteStatusOverride(appName, id, infoFromRegistry);
                    break;
            }
        } catch (Throwable t) {
            logger.error("Cannot replicate information to {} for action {}", node.getServiceUrl(), action.name(), t);
        } finally {
            CurrentRequestVersion.remove();
        }
    }
```



#### 5.Eureka分区

```
region：可以理解为地理上的分区，例如亚洲分区、华北地区等等，地区没有具体大小限制，根据实际情况合理划分。

zone：可以理解为region下的具体分区（机房），例如北京分区下有两个机房，可以划分出两个区域zone1、zone2.

通过分区管理，可以实现不同区域不同机房的服务进行就近调用，降低延迟。

一个机房内的服务优先调用同一个机房内的服务，当同一个机房的服务不可用的时候，再去调用其它机房的服务，以达到减少延时的作用。
```

