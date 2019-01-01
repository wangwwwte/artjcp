package com.cobla.concurrent.delay;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author cobla
 * @version 1.0
 * @className DelayEvent
 * @date 2018/12/31 0031 17:34
 * @description 队列元素，必须实现Delayed接口<br/>
 * 1.实现comparTo接口，this和o哪个的到期剩余时间更短，哪个排在前面return -1
 * 2.getDelay(TimeUnit)，传入当前时间的单位，调用convert转换成需要的时间单位下的剩余到期时间
 */
public class DelayEvent implements Delayed {
    private Date deadlineDate;
    public DelayEvent(Date deadlineDate){
        super();
        this.deadlineDate = deadlineDate;
    }

    /**
     * @Author cobla
     * @Date 2018/12/31 0031 17:36
     * @Param [unit]
     * @return long
     * @Description 获取到期剩余时间
     */
    @Override
    public long getDelay(TimeUnit unit) {
        Date now = new Date();
        long diff = deadlineDate.getTime() - now.getTime();
        //转换成毫秒
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long result = this.getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        //当前元素剩余到期时间短，排在前面
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        }
        return 0;
    }

}
