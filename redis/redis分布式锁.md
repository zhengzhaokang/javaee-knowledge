redis分布式锁

```
注意: key的设置，如果是订单场景，可以order_id + 节点id, 形成唯一标识, 主要侧重于唯一标志，基于业务需求，尽可能减小锁的力度.
```

1.定义注解

```java
package com.wc.single.sky.common.redis.annotation;

import com.wc.single.sky.common.redis.common.LockType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Redisson锁(Redis分布式锁)注解
 *
 *
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//@Inherited
public @interface DistributedLock {

	/**
	 * * 拼接锁的key  属性 (多个属性用,隔开)
	 * @return
	 */
	String lockForAttribute() default "";

	/**
	 * * 解析方法参数第几个
	 * @return
	 */
	int argIndex() default 0;

	/**
	 * * 是否可以重复提交  true 可以 false 不可以
	 * * (可以重复提交是指的是同Key下 必须上一次请求执行完了之后可以重复提交)
	 * * 只能限制锁的时间范围内不能重复提交 如想长期判断不能重复提交仍需业务实现
	 * @return
	 */
	boolean submitAgain() default true;

	//指定锁的key
	String lockForKey() default "'default'";

	String lockType() default LockType.REENTRANT_LOCK;

	/** 获取锁等待时间，默认10秒*/
	long waitTime() default 10000L;

	/** 锁自动释放时间，默认10秒*/
	long leaseTime() default 10000L;

	/** 时间单位（获取锁等待时间和持锁时间都用此单位）*/
	TimeUnit unit() default TimeUnit.MILLISECONDS;

	/**
	 * * 延迟过期
	 * @return
	 */
	boolean isDelay() default false;
}

```



2、切面

```java
package com.wc.single.sky.common.redis.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wc.single.sky.common.redis.annotation.DistributedLock;
import com.wc.single.sky.common.redis.common.LockType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;


/**
 * DistributedLock注解切面类
 *
 **/
@Aspect
@Component
@Slf4j
public class RedissonDistributedLockAspect {

	@Autowired
	private RedissonClient redissonClient;



	@Pointcut("@annotation(com.wc.single.sky.common.redis.annotation.DistributedLock)")
	public void lockPoint() throws UnsupportedOperationException {
		//切面
	}

	@Around("lockPoint()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable{
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();
		DistributedLock lockAction = method.getAnnotation(DistributedLock.class);
		String key = getKey(method, pjp.getArgs(),lockAction);
		RLock lock = getLock(key, lockAction);
		boolean isLocked = lock.isLocked();
		//指定时间内重复提交失败
		if (isLocked && !lockAction.submitAgain()) {
			log.debug("get lock failed [{}]", key);
			log.info(String.format("get lock failed [%s]", LocalDateTime.now()));
			//todo  此处不应该直接返回null 后续应改为返回ResultDTO
			return null;
		}
		lock.tryLock(lockAction.waitTime(), lockAction.leaseTime(), lockAction.unit());
		//得到锁,执行方法，释放锁
		log.debug("get lock success [{}]", key);
		try {
			log.info(String.format("获取锁%s执行时间为:%s", key, LocalDateTime.now()));
			return pjp.proceed();
		} catch (Exception e) {
			log.error("execute locked method occured an exception", e.getMessage());
			if (lock.isLocked() && lock.isHeldByCurrentThread()) {
					lock.unlock();
					log.debug("release lock [{}]", key);
			}
			throw new RuntimeException(e);
		} finally {
			if (lock.isLocked() && lock.isHeldByCurrentThread() && !lockAction.isDelay()) {
				lock.unlock();
				log.debug("release lock [{}]", key);
			}
		}
	}

	private String getKey(Method method, Object[] args, DistributedLock lock) {
		String methodName = method.getName();
		int index = lock.argIndex();
		String attribute = lock.lockForAttribute();
		if (StringUtils.isEmpty(attribute)) {
			return lock.lockForKey();
		}
		Object obj = args[index];
		if (null == obj) {
			throw new RuntimeException("DistributedLock  Args is  Null ArgIndex Is " + index);
		}
		JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
		String[] attributes = attribute.split(",");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DistributedLock:");
		stringBuilder.append(methodName);
		stringBuilder.append(":");
		for (String attr : attributes) {
			String value = jsonObject.getString(attr);
			if (StringUtils.isEmpty(value)) {
				log.error("锁的key 为空");
				throw new RuntimeException("DistributedLock  Key Is  Null Attribute Is " + attr);
			}
			stringBuilder.append(value);
			stringBuilder.append(":");
		}
		return stringBuilder.toString();
	}


	private RLock getLock(String key,DistributedLock lockAction) {
		switch (lockAction.lockType()) {
			case LockType.REENTRANT_LOCK:
				return redissonClient.getLock(key);

			case LockType.FAIR_LOCK:
				return redissonClient.getFairLock(key);

			case LockType.READ_LOCK:
				return redissonClient.getReadWriteLock(key).readLock();

			case LockType.WRITE_LOCK:
				return redissonClient.getReadWriteLock(key).writeLock();

			default:
				throw new RuntimeException("do not support lock type:" + lockAction.lockType());
		}
	}
}


```



3.使用

```java
@Scheduled(cron = "0 0 1,9,23 * * ?")//每天凌晨1点、早晨9点和晚上11点各执行一次
@DistributedLock(lockForKey = "OTFP_AUTO_JOB:INVOICE_EXTRACTION_STATISTICS", submitAgain = false)
public void autoInvoiceExtractionStatisticsOfJob() {
}
```



4、非注解形式使用

```
@Autowired
private RedissonClient redissonClient;

// key的话，如果是订单场景，可以order_id + 节点id, 形成唯一标识, 主要侧重于唯一标志，基于业务需求，尽可能减小锁的力度.
RLock lock = redissonClient.getLock("sky::email::basicTipTask"); 
try {
    if (!lock.tryLock(5, 30, TimeUnit.SECONDS)) {
        log.error("### DisclosuresBasicTipSchedule basicTipTask lock.tryLock failure");
        return;
    }
    // 业务逻辑
} catch (Exception e) {
    log.error("### DisclosuresBasicTipSchedule sendEmail error2", e);
} finally {
    if (lock.isHeldByCurrentThread()){
        lock.unlock();
    }
}    

```

