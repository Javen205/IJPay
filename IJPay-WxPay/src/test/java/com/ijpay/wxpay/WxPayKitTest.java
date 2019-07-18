package com.ijpay.wxpay;


import com.ijpay.wxpay.kit.WxPayKit;
import org.junit.Assert;
import org.junit.Test;

public class WxPayKitTest {

    @Test
    public void hmacSHA256(){
        Assert.assertEquals("9d67ee93641f99f0de85756c1debd8a4e315fbf8fd4ed966ed352bc1a658a8df",
                WxPayKit.hmacSHA256("IJPay 让支付触手可及","123"));
    }

    @Test
    public void mad5(){
        Assert.assertEquals("42cc1d91bab89b65ff55b19e28fff4f0",
                WxPayKit.md5("IJPay 让支付触手可及"));
    }

    @Test
    public void generateStr(){
        Assert.assertEquals("42cc1d91bab89b65ff55b19e28fff4f0",WxPayKit.generateStr());
    }


    @Test
    public void encryptData(){
        Assert.assertEquals("",WxPayKit.encryptData("IJPay 让支付触手可及","42cc1d91bab89b65ff55b19e28fff4f0"));
    }
    
    @Test
    public void decryptData(){
        Assert.assertEquals("IJPay 让支付触手可及",WxPayKit.decryptData(WxPayKit.encryptData("IJPay 让支付触手可及","42cc1d91bab89b65ff55b19e28fff4f0"),"42cc1d91bab89b65ff55b19e28fff4f0"));
    }
}
