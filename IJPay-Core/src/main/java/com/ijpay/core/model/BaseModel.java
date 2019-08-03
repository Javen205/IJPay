/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>Model 公用方法</p>
 *
 * @author Javen
 */
package com.ijpay.core.model;

import cn.hutool.core.util.StrUtil;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.core.enums.SignType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BaseModel {

    /**
     * 将建构的 builder 转为 Map
     *
     * @return 转化后的 Map
     */
    public Map<String, String> toMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        String[] fieldNames = getFiledNames(this);
        for (int i = 0; i < fieldNames.length; i++) {
            String name = fieldNames[i];
            String value = (String) getFieldValueByName(name, this);
            System.out.println("name:" + name + ",value:" + value);
            if (StrUtil.isNotEmpty(value)) {
                map.put(name, value);
            }
        }
        return map;
    }

    /**
     * 构建签名 Map
     *
     * @param partnerKey API KEY
     * @param signType   {@link SignType} 签名类型
     * @return 构建签名后的 Map
     */
    public Map<String, String> creatSign(String partnerKey, SignType signType) {
        return WxPayKit.buildSign(toMap(), partnerKey, signType);
    }

    /**
     * 获取属性名数组
     *
     * @param obj 对象
     * @return 返回对象属性名数组
     */
    public String[] getFiledNames(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName 属性名称
     * @param obj       对象
     * @return 返回对应属性的值
     */
    public Object getFieldValueByName(String fieldName, Object obj) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = new StringBuffer().append("get")
                    .append(firstLetter)
                    .append(fieldName.substring(1))
                    .toString();
            Method method = obj.getClass().getMethod(getter, new Class[]{});
            return method.invoke(obj, new Object[]{});
        } catch (Exception e) {
            return null;
        }
    }

}
