package com.ijpay;


import cn.hutool.core.date.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import java.util.Date;
import java.util.TimeZone;

public class PayTest {

    @Test
    public void dateTime() {
        /**
         * 字母	日期或时间元素	示例
         * y	年	2015
         * M	年中的月份	12
         * w	年中的周数	50
         * W	月份中的周数	02
         * D	年中的天数	344
         * d	月份中的天数	10
         * F	月份中的星期	02
         * E	星期中的天数	星期四、Thu
         * a	AM/PM标记	下午、PM
         * H	一天中的小时数(0~23)	21
         * k	一天中的小时数(1~24)	21
         * K	am/pm中的小时数(0~11)	09
         * h	am/pm中的小时数(1~12)	09
         * m	小时中的分钟数	31
         * s	分钟中的秒数	08
         * S	毫秒数	716
         */

        DateTime dateTime = new DateTime(new Date(), DateTimeZone.forTimeZone(TimeZone.getDefault()));
        cn.hutool.core.date.DateTime huDateTime = new cn.hutool.core.date.DateTime(new Date(), TimeZone.getDefault());
        System.out.println(dateTime.toString());
        System.out.println(huDateTime.toString());

        DateTime dateTimePlus = new DateTime(new Date(System.currentTimeMillis() + 1000 * 60 * 3), DateTimeZone.forTimeZone(TimeZone.getDefault()));
        String dateStr = dateTimePlus.toString();
        System.out.println(dateStr);
        System.out.println(dateStr.substring(0, dateStr.indexOf(".")).concat(dateStr.substring(dateStr.indexOf(".") + 4)));

        DateTime dt = new DateTime("2020-06-14T14:15:15+08:00");
        DateTime dt2 = new DateTime("2020-06-14T14:15:15.150+08:00");

        System.out.println(dt);
        System.out.println(dt2);
        System.out.println(DateUtil.format(new Date(dt.getMillis()), "YYYY-MM-dd HH:mm:ss 今天是 yyyy-MM-dd E HH:mm:ss，是 yyyy 年的第 DD 天，在该月是第 dd 天"));
    }


}
