package com.ijpay;


import com.ijpay.core.utils.DateTimeZoneUtil;
import org.junit.Test;

import java.util.Date;

public class PayTest {

    @Test
    public void dateTime() throws Exception {
        String str = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 6);
        String str2 = DateTimeZoneUtil.dateToTimeZone(new Date());
        String s = DateTimeZoneUtil.timeZoneDateToStr(str);
        System.out.println(str);
        System.out.println(str2);
        System.out.println(s);
    }
}
