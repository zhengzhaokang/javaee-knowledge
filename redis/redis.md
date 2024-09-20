Redis通常是用来作缓存的，当然也可以用来做数据库。
1.redis 缓存击穿和缓存穿透、缓存雪崩 ### 
1.1 缓存击穿：是指缓存中没有但数据库中有的数据（一般是缓存时间到期），这时由于并发用户特别多，同时读缓存没读到数据，又都去数据库去取数据，引起数据库压力瞬间增大，造成过大压力。
解决方法：设置热点数据永不过期，布隆过滤器,使用互斥锁
1.2 缓存穿透: 是指用户查询数据，在数据库没有，自然在缓存中也不会有。这样就导致用户查询的时候，在缓存中找不到，每次都要去数据库再查询一遍，然后返回空（相当于进行了两次无用的查询）。这样请求就绕过了缓存，直接查数据库，这也是经常说的缓存命中率失效的情况，当有大量这样的请求，数据库压力会非常大。
解决方法: 使用布隆过滤器进行快速判断，如果数据不存在，则直接返回，不进行数据库查询; 对于数据库查询结果为空的数据，也将其缓存起来，但设置一个较短的过期时间; 对用户请求的参数进行校验，非法请求直接返回。
1.3 缓存雪崩: 是指由于缓存层承载着大量请求，当缓存服务器重启或者大量缓存集中在某一个时间段失效，所有的请求都会直接打在数据库上，造成数据库压力骤增，引起数据库崩溃。
解决方法:缓存过期时间设置随机, 避免大量缓存同时失效; 服务熔断或限流;二级缓存;使用缓存降级,当缓存服务器挂掉或缓存不可用时，可以直接访问数据库，但此时可能不是全量访问数据库，而是采用一些降级手段，比如只返回部分数据或返回预设的默认值。
2. redis做为缓存，mysql的数据如何与redis进行同步呢？（双写一致性）### 
1. 应用程序层面同步
在应用程序代码中显式地管理MySQL和Redis之间的数据同步。这通常涉及以下几个步骤：
读取数据：首先，从MySQL数据库中读取数据。
写入Redis：然后，将读取到的数据写入Redis缓存中。
更新数据：当MySQL中的数据发生更新时，需要同时更新Redis中的数据。这可以通过在更新MySQL数据库的逻辑之后，立即更新Redis缓存来实现。
删除或失效缓存：在更新MySQL数据时，如果更新逻辑复杂，可能会选择删除或使Redis中的相关缓存失效，以便下次访问时重新从MySQL加载数据。
这种方法简单直接，但要求开发者在每个数据更新点都手动管理缓存的同步，容易出错且维护成本高。
2. 使用消息队列
引入消息队列（如RabbitMQ、Kafka等）来处理数据同步的逻辑。当MySQL中的数据发生变化时，不是直接更新Redis，而是发送一个消息到队列中。然后，有一个或多个消费者监听这个队列，当接收到消息时，从MySQL读取最新的数据并更新到Redis中。
这种方法的好处是解耦了数据产生和消费的过程，提高了系统的可扩展性和可靠性。但是，它增加了系统的复杂性，需要维护额外的消息队列服务。
3 .定时任务
在某些场景下，可以使用定时任务（如Cron作业）来定期从MySQL中读取数据并更新Redis缓存。这种方法适用于数据变更不频繁的场景，但它会导致Redis中的数据与MySQL中的数据之间存在一定的时间差。

 3. 持久化的方式：1、RDB 2、AOF  ###
redis的持久化方式主要包括两种：RDB（Redis Database）和AOF（Append Only File）。这两种方式各有特点，可以单独使用，也可以结合使用，以提供更强大的数据保护能力。
1. RDB持久化
概述：
RDB持久化是一种快照式的持久化方法，它将某一时刻的内存数据快照保存到磁盘文件中，默认的文件名为dump.rdb。这种方式适用于灾难恢复，可以快速恢复大量数据。
触发机制：
手动触发：通过执行save或bgsave命令。save命令会阻塞Redis服务器进程，直到RDB文件创建完毕；而bgsave命令则会创建一个子进程来负责RDB文件的创建，从而避免阻塞主进程。
自动触发：根据配置文件中的save指令和Redis的键值变化率自动触发。例如，可以配置“900秒内如果有1次键值变化，则自动触发RDB持久化”。
save 900 1
save 300 10
save 60 10000
In the example below the behavior will be to save:
#   after 900 sec (15 min) if at least 1 key changed
#   after 300 sec (5 min) if at least 10 keys changed
#   after 60 sec if at least 10000 keys changed
优缺点：
优点：RDB文件是一个紧凑的单一文件，便于传输和备份；恢复速度快，对于大数据集恢复尤其有效。
缺点：RDB是定时快照，可能会丢失最后一次快照后的数据；在数据量较大时，fork子进程可能会消耗较多CPU资源，并可能导致短暂阻塞。
2. AOF持久化
概述：
AOF持久化是一种追加式的持久化方法，它将Redis执行的写命令以日志的形式追加到文件中，默认的文件名为appendonly.aof。当Redis重启时，它会重新执行AOF文件中的命令来恢复数据。
配置：
AOF持久化可以通过配置文件中的appendonly指令来开启，并可以通过appendfsync指令来配置同步策略，包括always（每次写操作都同步）、everysec（每秒同步一次）和no（由操作系统决定何时同步）。appendonly no  appendfilename "appendonly.aof"  
# appendfsync always  appendfsync everysec  # appendfsync no
auto-aof-rewrite-percentage 100    auto-aof-rewrite-min-size 64mb
优缺点：
优点：提供了更好的数据持久化保证，可以最大限度地减少数据丢失；AOF文件以命令的形式记录，易于理解和修改。
缺点：AOF文件会随着写操作的增加而不断增大，需要定期进行重写（BGREWRITEAOF命令）来压缩文件；在某些情况下，AOF的恢复速度可能比RDB慢。
总结
Redis的RDB和AOF持久化方式各有优势，在实际应用中可以根据具体需求选择使用。如果需要快速恢复大量数据，并且对数据的实时性要求不是特别高，可以选择RDB持久化；如果需要最大限度地减少数据丢失，并且可以接受一定的性能开销，可以选择AOF持久化。同时，也可以将RDB和AOF结合使用，以获得更好的数据保护效果。
4.Redis的数据过期策略有哪些?   ###
答：1.定时删除: 对键设置过期时间时，同步设置一个定时器，当达到定时时间时，立即删除键。
优点: 省内存，立即删除，释放内存。
缺点:CPU使用率高，容易造成系统卡顿。因此，Redis并不推荐这种方式作为主要的过期策略。
2.惰性删除: 不主动删除key，在每次读写之前，调用expireIfNeeded函数确定key是否过期，如果过期则删除，否则不做处理，之后执行命令。
优点：直接删除效率大部分非常快，只有非常大的对象回收时可能存在卡顿；CPU处理次数少。
缺点: 很多到了过期时间的key还占用着内存，如果某个key永远不会被访问，即使设置了过期时间，它也不会被自动删除。
3.定期删除: 定时删除和惰性删除的折中做法。Redis将设置了过期时间的key放入一个特定的字典中，周期性调用activeExpireCycle函数，周期时间可以设定，默认100ms执行一次。执行频率默认为10hz(0.1s)，每次不超过25ms.
综合策略: Redis实际使用的是定期删除+惰性删除的策略，在合理使用CPU和避免内存浪费之间平衡。当某个key过期后，如果定期删除没删除成功，并且也没有再次去请求这个key，即惰性删除也没生效，那么随着时间的推移，如果大量过期的key堆积在内存中，Redis的内存会越来越高，这时就可能需要采用内存淘汰机制来进一步管理内存。

5.Redis的数据淘汰策略有哪些  ###
Redis的数据淘汰策略主要用来处理当Redis的内存使用达到最大允许的内存值，且没有更多的内存空间容纳新数据时的情况。根据配置的淘汰策略，Redis会选择删除合适的键以释放内存空间。以下是Redis支持的主要数据淘汰策略：
1.Noeviction(默认)
描述:不淘汰任何数据，如果内存已经满了，不支持客户端写入新的数据，会直接发出拒绝，返回错误。
适用场景：适用于对数据完整性要求极高的场景，但可能导致新的写操作失败。
2.Allkeys-lru
描述: 从所有key中使用LRU（最近最少使用）算法进行淘汰。即，从所有键中淘汰最久未被使用的键。
适用场景：适用于希望保留最常访问的数据的场景。
3.Volatile-lru
描述:从设置了过期时间的key中使用LRU算法进行淘汰。即，从设置了过期时间的键中淘汰最久未被使用的键。
适用场景：适用于有大量临时数据，且希望保留最常访问的临时数据的场景。
4.Allkeys-random
描述: 从设置了过期时间的key中随机淘汰。
适用场景: 适用于没有明确访问模式，或者所有数据访问频率相近的场景。
5.Volatile-random
描述:从设置了过期时间的key中随机淘汰。
适用场景: 与Volatile-lru类似，但采用随机方式，适用于访问模式不明确，但有大量临时数据的场景。
6.Volatile-ttl
描述: 在设置了过期时间的key中，淘汰过期时间剩余最短的。即，越早过期的键优先被淘汰。
适用场景:适用于需要根据数据的过期时间来决定淘汰顺序的场景。
7.Volatile-lfu
描述：针对设置了过期时间的key，按照一段时间内的访问频次，淘汰掉访问频次最低的键。
适用场景: 适用于需要保留高频访问的临时数据的场景。
8.Allkeys-lfu
描述：与Allkeys-lru类似，但不区分是否设置过期时间，而是根据一段时间内的访问频次来决定淘汰顺序，访问频次低的键优先被淘汰。
适用场景: 适用于希望保留高频访问数据的场景，且不关心数据是否设置了过期时间。
注意事项:
这些淘汰策略只在Redis的内存使用达到一定阈值时才会触发。可以通过maxmemory参数来设置Redis的最大内存限制。如：maxmemory 100mb
可以通过maxmemory-policy参数来设置Redis的淘汰策略。如：maxmemory-policy allkeys-lru
当使用volatile-lru、volatile-random、volatile-ttl这三种策略时，如果没有可被淘汰的设置了过期时间的key，则和noeviction一样返回错误。
6. Redis的内存用完了会发生什么 ###
这个要看redis的数据淘汰策略是什么，如果是默认的配置，redis内存用完以后则直接报错。我们当时设置的 allkeys-lru 策略。把最近最常访问的数据留在缓存中。数据GET可读，但不可新增PUT Lpush
为了避免潜在的问题，建议仔细规划Redis的内存使用，并设置适当的监控和警报机制。
7. Redis分布式锁 ###
Redis分布式锁如何实现？
在redis中提供了一个命令setnx(SET if not exists)，由于redis的单线程的，用了命令之后，只能有一个客户端对某一个key设置值，在没有过期或删除key的时候是其他客户端是不能设置这个key的。Key得是唯一的，比如:订单号+status.
那你如何控制Redis实现分布式锁有效时长呢？
嗯，的确，redis的setnx指令不好控制这个问题，我们当时采用的redis的一个框架redisson实现的。
在redisson中需要手动加锁，并且可以控制锁的失效时间和等待时间，当锁住的一个业务还没有执行完成的时候，在redisson中引入了一个看门狗机制，就是说每隔一段时间就检查当前业务是否还持有锁，如果持有就增加加锁的持有时间，当业务执行完成之后需要使用释放锁就可以了。还有一个好处就是，在高并发下，一个业务有可能会执行很快，先客户1持有锁的时候，客户2来了以后并不会马上拒绝，它会自旋不断尝试获取锁，如果客户1释放之后，客户2就可以马上持有锁，性能也得到了提升。
redisson实现的分布式锁是可重入的吗？
是可以重入的。这样做是为了避免死锁的产生。这个重入其实在内部就是判断是否是当前线程持有的锁，如果是当前线程持有的锁就会计数，如果释放锁就会在计算上减一。在存储数据的时候采用的hash结构，大key可以按照自己的业务进行定制，其中小key是当前线程的唯一标识，value是当前线程重入的次数.
redisson实现的分布式锁能解决主从一致性的问题吗
这个是不能的，比如，当线程1加锁成功后，master节点数据会异步复制到slave节点，此时当前持有Redis锁的master节点宕机，slave节点被提升为新的master节点，假如现在来了一个线程2，再次加锁，会在新的master节点上加锁成功，这个时候就会出现两个节点同时持有一把锁的问题。
我们可以利用redisson提供的红锁来解决这个问题，在多个redis实例上创建锁，并且要求在大多数redis节点上都成功创建锁，红锁中要求是redis的节点数量要过半。这样就能避免线程1加锁成功后master节点宕机导致线程2成功加锁到新的master节点上的问题了。但是，如果使用了红锁，因为需要同时在多个节点上都添加锁，性能就变的很低了，并且运维维护成本也非常高，所以，我们一般在项目中也不会直接使用红锁.
如果业务非要保证数据的强一致性，这个该怎么解决呢？
edis本身就是支持高可用的，做到强一致性，就非常影响性能，所以，如果有强一致性要求高的业务，建议使用zookeeper实现的分布式锁，它是可以保证强一致性的。
8.Redis集群有哪些方案? ###
Redis中提供的集群方案总共有三种：主从复制、哨兵模式、Redis分片集群
9.Redis主从同步 ###
一般都是一主多从，主节点负责写数据，从节点负责读数据，主节点写入数据之后，需要把数据同步到从节点中
主从同步分为了两个阶段，一个是全量同步，一个是增量同步
全量同步通常发生在以下几种情况：
主从节点第一次建立连接时：当从节点（slave）第一次与主节点（master）建立连接时，由于从节点上没有任何数据，因此需要进行全量同步，即主节点会将其全部数据发送给从节点。
从节点重启后，且无法通过增量同步恢复数据：如果从节点在重启后与主节点的数据差异过大，或者由于某些原因（如复制缓冲区repl_backlog_buffer中的数据已被覆盖）导致无法进行增量同步，那么也会触发全量同步。
全量同步的过程大致如下：
从节点向主节点发送SYNC或PSYNC命令（Redis 2.8及以后版本推荐使用PSYNC命令，因为它支持增量同步）。
如果是第一次同步或无法进行增量同步，主节点会执行bgsave命令生成RDB快照文件，并将该文件发送给从节点。
从节点接收并加载RDB文件中的数据，完成数据的初始同步。
增量同步发生在全量同步之后，或者在从节点与主节点已经同步过一次数据，但之后主节点上又有新的数据变更时。主要信息：replication id和offset及增量数据。增量同步的过程大致如下：
从节点继续监听主节点的命令传播（基于长连接的命令传播）。
当主节点接收到新的写命令时，它会将这些命令发送给所有已连接的从节点。
从节点接收并执行这些命令，从而保持与主节点数据的一致性。
10. 怎么保证Redis的高并发高可用###
保证Redis的高并发高可用需要从多个方面入手，包括架构设计、配置优化、监控和维护等。
架构设计：
首先可以搭建主从集群，再加上使用redis中的哨兵模式，哨兵模式可以实现主从集群的自动故障恢复，里面就包含了对主从服务的监控、自动故障恢复、通知；如果master故障，Sentinel会将一个slave提升为master。当故障实例恢复后也以新的master为主；同时Sentinel也充当Redis客户端的服务发现来源，当集群发生故障转移时，会将最新信息推送给Redis的客户端，所以一般项目都会采用哨兵的模式来保证redis的高并发高可用。Cluster模式.
配置优化:
最大内存限制：设置maxmemory以限制Redis实例使用的最大内存，避免因内存不足导致实例崩溃。
内存淘汰策略：设置适当的内存淘汰策略（如volatile-lru、allkeys-lru），确保在达到最大内存限制时能够优雅地淘汰不需要的数据。
持久化：配置RDB快照和AOF日志，以确保数据的持久性。注意合理设置持久化策略以平衡性能和数据安全。
连接配置：优化连接配置（如timeout、tcp-keepalive）以确保网络连接的稳定性。
高并发优化:
使用连接池：在客户端应用中使用连接池（如JedisPool），以减少频繁创建和销毁连接的开销。
批量操作：使用批量操作（如MSET、MGET）减少网络往返次数。
管道(Pipeline)：利用管道技术将多条命令一起发送，减少网络延迟。
监控与维护:
监控工具：使用监控工具（如Prometheus、Grafana、Redis Exporter）监控Redis的性能指标（如内存使用、连接数、命中率等）。
日志监控：定期检查Redis日志，发现潜在问题。
自动告警：配置告警系统，及时通知管理员异常情况（如节点故障、内存使用率过高等）。
定期备份：定期进行数据备份，确保在发生严重故障时可以恢复数据。
升级和补丁：及时升级Redis版本，应用安全补丁和性能优化。
灾难恢复演练：定期进行灾难恢复演练，确保在实际故障发生时能够迅速恢复服务。
11. redis集群脑裂，该怎么解决呢？###
有的时候由于网络等原因可能会出现脑裂的情况，就是说，由于redis master节点和redis salve节点和sentinel处于不同的网络分区，使得sentinel没有能够心跳感知到master，所以通过选举的方式提升了一个salve为master，这样就存在了两个master，就像大脑分裂了一样，这样会导致客户端还在old master那里写入数据，新节点无法同步数据，当网络恢复后，sentinel会将old master降为salve，这时再从新master同步数据，这会导致old master中的大量数据丢失。
关于解决的话，我记得在redis的配置中可以设置：第一可以设置最少的salve节点个数，比如设置至少要有一个从节点才能同步数据，第二个可以设置主从数据复制和同步的延迟时间，达不到要求就拒绝请求，就可以避免大量的数据丢失
Redis 为我们提供了以下两个配置，通过以下两个配置可以尽可能的避免数据丢失的问题：
min-slaves-to-write：与主节点通信的从节点数量必须大于等于该值主节点，否则主节点拒绝写入。
min-slaves-max-lag：主节点与从节点通信的 ACK 消息延迟必须小于该值，否则主节点拒绝写入。
这两个配置项必须同时满足，不然主节点拒绝写入。
在假故障期间满足 min-slaves-to-write 和 min-slaves-max-lag 的要求，那么主节点就会被禁止写入，脑裂造成的数据丢失情况自然也就解决了。
12.redis的分片集群有什么作用? ###
分片集群主要解决的是，海量数据存储的问题，集群中有多个master，每个master保存不同数据，并且还可以给每个master设置多个slave节点，就可以继续增大集群的高并发能力。同时每个master之间通过ping监测彼此健康状态，就类似于哨兵模式了。当客户端请求可以访问集群任意节点，最终都会被转发到正确节点.

13. Redis分片集群中数据是怎么存储和读取的？###
redis 集群引入了哈希槽的概念，有 16384 个哈希槽，集群中每个主节点绑定了一定范围的哈希槽范围， key通过 CRC16 校验后对 16384 取模来决定放置哪个槽，通过槽找到对应的节点进行存储。
取值的逻辑是一样的
14. Redis是单线程的，但是为什么还那么快？###
1、完全基于内存的，C语言编写
2、采用单线程，避免不必要的上下文切换可竞争条件
3、使用多路I/O复用模型，非阻塞IO
bgsave 和 bgrewriteaof 都是在后台执行操作，不影响主线程的正常使用，不会产生阻塞

#24-07-25
Redis 使用规范。
业务层面主要面向的业务开发人员：
1、key 的长度尽量短，节省内存空间
2、避免 bigkey，防止阻塞主线程
3、4.0+版本建议开启 lazy-free
4、把 Redis 当作缓存使用，设置过期时间
5、不使用复杂度过高的命令，例如SORT、SINTER、SINTERSTORE、ZUNIONSTORE、ZINTERSTORE
6、查询数据尽量不一次性查询全量，写入大量数据建议分多批写入
7、批量操作建议 MGET/MSET 替代 GET/SET，HMGET/HMSET 替代 HGET/HSET
8、禁止使用 KEYS/FLUSHALL/FLUSHDB 命令
9、避免集中过期 key
10、根据业务场景选择合适的淘汰策略
11、使用连接池操作 Redis，并设置合理的参数，避免短连接
12、只使用 db0，减少 SELECT 命令的消耗
13、读请求量很大时，建议读写分离，写请求量很大，建议使用切片集群