package com.ijpay.unionpay;

import com.ijpay.core.kit.HttpKit;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>银联支付相关接口</p>
 *
 * @author Javen
 */
public class UnionPayApi {
    /**
     * PC网关支付、WAP支付
     *
     * @param resp    HttpServletResponse
     * @param reqData 请求参数
     * @throws IOException 异常
     */
    @Deprecated
    public static void frontConsume(HttpServletResponse resp, Map<String, String> reqData) throws IOException {
        String html = AcpService.createAutoFormHtml(SDKConfig.getConfig().getFrontRequestUrl(), reqData, "UTF-8");
        resp.getWriter().write(html);
    }

    /**
     * 前端请求
     *
     * @param resp    HttpServletResponse
     * @param reqData 请求参数
     * @throws IOException 异常
     */
    public static void frontRequest(HttpServletResponse resp, Map<String, String> reqData) throws IOException {
        String html = AcpService.createAutoFormHtml(SDKConfig.getConfig().getFrontRequestUrl(), reqData, "UTF-8");
        resp.getWriter().write(html);
    }

    /**
     * 退货交易、撤销交易
     *
     * @param reqData 请求参数
     * @return {String}
     */
    @Deprecated
    public static String refund(Map<String, String> reqData) {
        return doPost(SDKConfig.getConfig().getBackRequestUrl(), reqData);
    }

    /**
     * 退货交、撤销交易
     *
     * @param reqData 请求参数
     * @return 转化后的 Map
     */
    @Deprecated
    public static Map<String, String> refundByMap(Map<String, String> reqData) {
        return SDKUtil.convertResultStringToMap(refund(reqData));
    }


    /**
     * 后台请求返回String
     *
     * @param reqData 请求参数
     * @return {String}
     */
    public static String backRequest(Map<String, String> reqData) {
        return doPost(SDKConfig.getConfig().getBackRequestUrl(), reqData);
    }

    /**
     * 后台请求返回Map
     *
     * @param reqData 请求参数
     * @return 转化后的 Map
     */
    public static Map<String, String> backRequestByMap(Map<String, String> reqData) {
        return SDKUtil.convertResultStringToMap(backRequest(reqData));
    }


    /**
     * 单订单查询返回String
     *
     * @param reqData 请求参数
     * @return {String}
     */
    public static String singleQuery(Map<String, String> reqData) {
        return doPost(SDKConfig.getConfig().getSingleQueryUrl(), reqData);
    }

    /**
     * 单订单查询返回Map
     *
     * @param reqData 请求参数
     * @return 转化后的 Map
     */
    public static Map<String, String> singleQueryByMap(Map<String, String> reqData) {
        return SDKUtil.convertResultStringToMap(singleQuery(reqData));
    }

    /**
     * 文件传输类接口
     *
     * @param reqData 请求参数
     * @return {String}
     */
    public static String fileTransfer(Map<String, String> reqData) {
        return doPost(SDKConfig.getConfig().getFileTransUrl(), reqData);
    }

    /**
     * 文件传输类接口
     *
     * @param reqData 请求参数
     * @return 转化后的 Map
     */
    public static Map<String, String> fileTransferByMap(Map<String, String> reqData) {
        return SDKUtil.convertResultStringToMap(fileTransfer(reqData));
    }

    /**
     * APP控件支付
     *
     * @param reqData 请求参数
     * @return {String}
     */
    public static String AppConsume(Map<String, String> reqData) {
        return doPost(SDKConfig.getConfig().getAppRequestUrl(), reqData);
    }

    /**
     * APP控件支付
     *
     * @param reqData 请求参数
     * @return 转化后的 Map
     */
    public static Map<String, String> AppConsumeByMap(Map<String, String> reqData) {
        return SDKUtil.convertResultStringToMap(AppConsume(reqData));
    }

    /**
     * 网关缴费
     *
     * @param resp    HttpServletResponse
     * @param reqData 请求参数
     * @throws IOException 异常
     */
    public static void jfFrontConsume(HttpServletResponse resp, Map<String, String> reqData) throws IOException {
        String html = AcpService.createAutoFormHtml(SDKConfig.getConfig().getJfFrontRequestUrl(), reqData, "UTF-8");
        resp.getWriter().write(html);
    }

    /**
     * APP缴费
     *
     * @param reqData 请求参数
     * @return {String}
     */
    public static String jfAppTrans(Map<String, String> reqData) {
        return doPost(SDKConfig.getConfig().getAppRequestUrl(), reqData);
    }

    /**
     * APP缴费
     *
     * @param reqData 请求参数
     * @return 转化后的 Map
     */
    public static Map<String, String> jfAppTransByMap(Map<String, String> reqData) {
        return SDKUtil.convertResultStringToMap(jfAppTrans(reqData));
    }

    /**
     * 获取地区列表
     *
     * @return {String}
     */
    public static String getAllAreas() {
        return doGet("https://gateway.95516.com/jiaofei/config/s/areas");
    }

    /**
     * 获取业务目录
     *
     * @return {String}
     */
    public static String getAllCategories() {
        return doGet("https://gateway.95516.com/jiaofei/config/s/categories/00");
    }

    public static String doGet(String url) {
        return HttpKit.getDelegate().get(url);
    }

    public static String doPost(String url, Map<String, String> params) {
        Map<String, Object> temp = new HashMap<String, Object>(params.size());
        temp.putAll(params);
        return HttpKit.getDelegate().post(url, temp);
    }
}