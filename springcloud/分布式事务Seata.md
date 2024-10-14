### 分布式事务（seate）

##### CAP定理

```
一致性（Consistency）：在分布式系统完成某写操作后任何读操作，都应该获取到该写操作写入的那个最新的值。这需要各个节点之间保持数据的一致性，也就是说，在进行写操作时，需要将数据同步到各个节点上，保证数据的一致性
可用性（Availability）：系统能够一直正常地提供读写操作，即客户端可以一直正常地访问并得到系统的正常响应，不会出现系统操作失败或者访问超时等问题
分区容错性（Partition tolerance）：对于横向扩展的服务，部分应用故障时其他服务仍然能够正常提供服务
C、A、P三者不可兼得，通常情况下分区容错性必须保证，然后就是在一致性和可用性上做一些取舍。
```

##### BASE理论：解决CAP问题

```
基本可用（Basically Available）：出现故障时允许损失部分可用性
软状态（Soft State）：在一定时间内可以出现临时不一致状态
最终一致性（Eventually Consistent）：虽然无法保证强一致性，但是在软状态后最终达到数据一致
```

##### 基于BASE和CAP，可以得出以下解决方案

```
最终一致（AP）：所有子事务分别执行和提交，允许结果不一致，然后采用弥补措施恢复数据，实现最终一致
强一致（CP）：所有事务执行后等待，然后同时提交或同时回滚，实现强一致性。但事务等待过程中处于弱可用状态
```



##### Seata事务管理中的三个重要角色

```
事务协调者（TC）：维护全局、分支事务状态，协调全局事务提交或回滚，是一个独立的微服务，需要注册到系统中
事务管理器（TM）：定义全局事务的范围，开始全局事务、提交或回滚全局事务
资源管理器（RM）：管理分支事务处理的资源，注册和报告分支事务的状态，并驱动分支事务提交或回滚
```

##### Seate的四种模式

**XA模式**

```
XA模式：属于强一致性，基于数据库的本地事务，所以依赖于关系型数据库
事务执行过程
第一阶段
TC通知各分支事务的RM执行事务
RM通知TC准备执行完成等待提交
第二阶段
TC通知RM开始提交或回滚
RM通知TC已提交或回滚
由于各分支事务在等待提交时需要等待所有分支事务执行完成，此时会占用者数据库的锁，从而降低了性能，但保证了数据的强一致性
代码示例
发起全局事务的入口方法添加@GlobalTransactional注解
添加配置如下

seata:
    data-source-proxy-mode: XA
```

###### **AT模式**：属于最终一致性（*seate默认模式）*

```
AT模式：属于最终一致性（seate默认模式）
同样是两阶段提交，但区别于XA模式的分支事务的等待阶段，AT模式会直接提交分支事务
RM会记录更新前后的快照，如果需要则根据快照回滚
TC通知提交或回滚完成后删除快照
AT模式引入全局锁解决了以下问题
由于没有对数据库加锁，所以存在隔离性问题，高并发下存在脏写，丢失更新
加全局锁之后虽然也锁定了资源，但是全局锁的锁定力度比起数据库锁要小，性能更高
与非seata管理的事务产生冲突，也可能会出现脏写的问题，这时由于保存了快照，经过对比后发现数据不一致，此时会抛出异常，需要人工介入
代码示例
发起全局事务的入口方法添加@GlobalTransactional注解
添加配置如下

seata:
    data-source-proxy-mode: AT
```

###### **TCC模式**：属于最终一致性，比起XA和AT有代码侵入，需要手动编写提交、回滚等逻辑

```
TCC模式：属于最终一致性，比起XA和AT有代码侵入，需要手动编写提交、回滚等逻辑
必要的数据库表：增加一个事务表用于记录事务信息和状态
事务id
锁定的资源信息
事务状态（try、confirm、cancel）
其他必要数据...
第一阶段（Try）：资源的检测和预留
事务表中记录冻结资源和事务状态
业务表中扣减冻结的资源
第二阶段（Confirm）：完成资源操作业务
事务表中删除冻结记录
第二阶段（Cancel）：预留资源释放（try的反向操作）
修改事务表中冻结的资源为0，修改事务状态为cancel
返还业务表中的资源
判断是否需要空回滚，如果事务表中没有记录则说明try没有执行，需要空回滚，空回滚的时候也需要像正常回滚一样在事务表中记录一条数据
避免业务悬挂，try时先判断事务表中是否有记录，如果有则说明已经执行过已经执行过cancel，此时不继续执行try
区别于AT模式，需要手动回滚（释放锁定的资源），由于没有全局锁，所以性能高于AT
不依赖数据库事务，可用于非事务型数据库
有代码侵入，需要人为编写try、confirm、cancel
需要考虑confirm和cancel的失败情况，做好幂等处理
空回滚&业务悬挂问题：某一个分支事务没有执行成功try，然后却要执行cancel，这时cancel不能做回滚但也要返回正常，这就是空回滚。由于已经执行了空回滚，此时try业务又继续执行了，就永远不可能在继续往下执行，这就是业务悬挂，要避免这个问题，通过维护事务状态解决该问题
代码示例
添加配置
seata:
    data-source-proxy-mode: TCC
```

编写try、confirm、cancel代码

```java
@LocalTCC
public interface TCCService {
    /**
     * Try逻辑，@TwoPhaseBusinessAction中的name属性要与当前方法名一致，用于指定Try逻辑对应的方法
     */
     @TwoPhaseBusinessAction(name = "prepare", commitMethod = "confirm", rollbackMethod = "cancel")
     void prepare(@BusinessActionContextParameter(paramName = "param") String param);
     
     /**
      * 二阶段confirm确认方法、可以另命名，但要保证与commitMethod一致
      *
      * @param context 上下文,可以传递try方法的参数
      * @return boolean 执行是否成功
      */
      boolean confirm (BusinessActionContext context);
      
     /**
      * 二阶段回滚方法，要保证与rollbackMethod一致
      */
      boolean cancel (BusinessActionContext context);
}
```

TCC Demo

1.订单控制器

```
import cn.itcast.order.entity.Order;
import cn.itcast.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
 
@RestController
@RequestMapping("order")
public class OrderController {
 
    private final OrderService orderService;
 
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
 
    @PostMapping
    public ResponseEntity<Long> createOrder(Order order){
        Long orderId = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }
}
```

2.[订单服务类](https://so.csdn.net/so/search?q=订单服务类&spm=1001.2101.3001.7020)

主事物开启，先开单在用户[支付](https://so.csdn.net/so/search?q=支付&spm=1001.2101.3001.7020)，发货扣减库存

```
@Override
@GlobalTransactional
public Long create(Order order) {
    // 创建订单
    orderMapper.insert(order);
    try {
        // 扣用户余额
        accountTCCService.deduct(order.getUserId(), order.getMoney());
        // 扣库存
        storageClient.deduct(order.getCommodityCode(), order.getCount());

    } catch (FeignException e) {
        log.error("下单失败，原因:{}", e.contentUTF8(), e);
        throw new RuntimeException(e.contentUTF8(), e);
    }
    return order.getId();
}
```

3.余额子事务

```
AccountTCCService 类
 
/**
 * Tcc业务处理类
 */
@LocalTCC
public interface AccountTCCService {
    /**
     * 从用户账户中扣款
     * Try接口
     *   name: 指定try接口
     *   commitMethod： confirm接口
     *   rollbackMethod：cancel接口
     */
    @TwoPhaseBusinessAction(name = "deduct",commitMethod = "confirm",rollbackMethod = "cancel")
    void deduct(@BusinessActionContextParameter(paramName = "userId") String userId,@BusinessActionContextParameter(paramName = "money") int money);
 
    /**
     * Confirm接口
     */
    public boolean confirm(BusinessActionContext cxt);
 
    /**
     * Cancel接口
     */
    public boolean cancel(BusinessActionContext cxt);
```

业务具体实现

1.避免业务悬挂的问题，查询冻结金额表，是否存在记录，存在记录，且状态CANCEL，则证明执行过cancel，不执行try，没有问题记录冻结金额表以及事务状态
2.cancel 判断该事务是否已经执行过try方法 ，没有执行过try，执行空回滚，新增冻结金额数据，金额为0，状态为CANCEL

```
@Service
public class AccountTCCServiceImpl implements AccountTCCService {
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountFreezeMapper freezeMapper;
 
    @Override
    @Transactional
    public void deduct(String userId, int money) {
        //获取seata内部产生的全局事务ID
        String xid = RootContext.getXID();
 
        //避免业务悬挂的问题
        //查询冻结金额表，是否存在记录，存在记录，且状态CANCEL，则证明执行过cancel，不执行try
        AccountFreeze oldFreeze = freezeMapper.selectById(xid);
        if(oldFreeze!=null && oldFreeze.getState().equals(AccountFreeze.State.CANCEL)){
            return;
        }
 
        //扣减account表可用余额
        accountMapper.deduct(userId,money);
 
        //记录冻结金额表以及事务状态
        AccountFreeze freeze = new AccountFreeze();
        freeze.setXid(xid);
        freeze.setUserId(userId);
        freeze.setFreezeMoney(money);
        freeze.setState(AccountFreeze.State.TRY);
        freezeMapper.insert(freeze);
    }
 
    @Override
    public boolean confirm(BusinessActionContext cxt) {
        int count = freezeMapper.deleteById(cxt.getXid());
        return count==1;
    }
 
    @Override
    public boolean cancel(BusinessActionContext cxt) {
        //先查询account_freeze表记录
        AccountFreeze freeze = freezeMapper.selectById(cxt.getXid());
 
        //从BusinessActionContext取出之前存入的参数
        String userId = (String)cxt.getActionContext("userId");
        Integer money = (Integer)cxt.getActionContext("money");
 
        //判断该事务是否已经执行过try方法
        if(freeze==null){
            //没有执行过try，执行空回滚
            //新增冻结金额数据，金额为0，状态为CANCEL
            freeze = new AccountFreeze();
            freeze.setXid(cxt.getXid());
            freeze.setUserId(userId);
            freeze.setFreezeMoney(0);
            freeze.setState(AccountFreeze.State.CANCEL);
            freezeMapper.insert(freeze);
            return true;
        }
 
        //修改account表，恢复可用金额
        accountMapper.refund(userId,money);
 
        //修改account_freeze表，冻结金额设置为0，stata为CANCEL
        freeze.setFreezeMoney(0);
        freeze.setState(AccountFreeze.State.CANCEL);
        int count = freezeMapper.updateById(freeze);
        return count==1;
    }
```



###### **SAGA模式**：属于最终一致性，该模式为长事务解决方案

```
第一阶段：通过串行的方式逐个执行分支事务，每个分支事务直接提交本地事务
第二阶段 - 成功：则什么都不做
第二阶段 - 失败：则按照分支事务的执行顺序逐个回滚，回滚操作通过编写代码来补偿业务
该模式下每个分支事务直接提交，由于没有像XA模式一样对数据库加锁，也没有像AT模式一样加全局锁，还没有像TCC模式一样锁定资源，导致该模式会存在隔离性问题，可能出现更新丢失
事务参与者可以基于事件驱动实现异步调用，而且直接提交事务，无锁，所以性能高
有代码侵入，需要编写状态机、补偿业务
```



### Seate**四种模式对比：**

|          | XA                             | AT(默认)                                     | TCC                                                  | SAGA                                                         |
| -------- | ------------------------------ | -------------------------------------------- | ---------------------------------------------------- | ------------------------------------------------------------ |
| 一致性   | 强一致                         | 最终一致                                     | 最终一致                                             | 最终一致                                                     |
| 隔离性   | 完全隔离                       | 基于全局锁隔离                               | 基于资源预留隔离                                     | 无隔离                                                       |
| 代码侵入 | 无                             | 无                                           | 有，要编写三个接口                                   | 有，要编写状态机和补偿业务                                   |
| 性能     | 差                             | 好                                           | 非常好                                               | 非常好                                                       |
| 场景     | 对一致性、隔离性有高要求的业务 | 基于关系型数据库的大多数分布式事务场景都可以 | 对性能要求较高的事务。有非关系型数据库要参与的事务。 | 业务流程长、业务流程多参与者包含其它公司或遗留系统服务，无法提供 TCC 模式要求的三个接口 |