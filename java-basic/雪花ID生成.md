雪花ID生成

```java
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.Inet4Address;
import java.net.UnknownHostException;

@Component
public class SnowFlakeUtil {
    private static Snowflake snowflake;


    /**
     * workId使用IP生成
     * @return workId
     */
    private static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = org.apache.commons.lang3.StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums = sums + b;
            }
            return (long) (sums % 32);
        } catch (UnknownHostException e) {
            // 失败就随机
            return RandomUtils.nextLong(0, 31);
        }
    }


    /**
     * dataCenterId使用hostName生成
     * @return dataCenterId
     */
    private static Long getDataCenterId() {
        try {
            String hostName = SystemUtils.getHostName();
            int[] ints = StringUtils.toCodePoints(hostName);
            int sums = 0;
            for (int i : ints) {
                sums = sums + i;
            }
            return (long) (sums % 32);
        } catch (Exception e) {
            // 失败就随机
            return RandomUtils.nextLong(0, 31);
        }
    }

    @PostConstruct
    public void init() {
        snowflake = IdUtil.createSnowflake(getWorkId(), getDataCenterId());
    }

    public static long nextId() {
        return snowflake.nextId();
    }
}

```

