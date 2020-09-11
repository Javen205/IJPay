/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p> JFinal 配置入口 </p>
 *
 * @author Javen
 */
package com.ijpay.demo;

import com.ijpay.demo.controller.IndexController;
import com.ijpay.demo.controller.alipay.AliPayController;
import com.ijpay.demo.controller.wxpay.WxPayController;
import com.ijpay.demo.controller.wxpay.WxPayV3Controller;
import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

public class AppConfig extends JFinalConfig {
    static Prop p;

    /**
     * PropKit.useFirstFound(...) 使用参数中从左到右最先被找到的配置文件
     * 从左到右依次去找配置，找到则立即加载并立即返回，后续配置将被忽略
     */
    static void loadConfig() {
        if (p == null) {
            p = PropKit.useFirstFound("demo-config-pro.txt", "demo-config-dev.txt");
        }
    }

    /**
     * 配置常量
     */
    @Override
    public void configConstant(Constants me) {
        loadConfig();

        me.setDevMode(p.getBoolean("devMode", false));
        me.setEncoding("utf-8");
        me.setError401View("/WEB-INF/error/401.html");
        me.setError404View("/WEB-INF/error/404.html");
        me.setError500View("/WEB-INF/error/500.html");

        /**
         * 支持 Controller、Interceptor、Validator 之中使用 @Inject 注入业务层，并且自动实现 AOP
         * 注入动作支持任意深度并自动处理循环注入
         */
        me.setInjectDependency(true);

        // 配置对超类中的属性进行注入
        me.setInjectSuperClass(true);
    }

    /**
     * 配置路由
     */
    @Override
    public void configRoute(Routes me) {
        me.setBaseViewPath("/WEB-INF/_views");
        me.add("/", IndexController.class);
        me.add("/aliPay", AliPayController.class);
        me.add("/wxPay", WxPayController.class);
        me.add("/v3", WxPayV3Controller.class);
    }

    @Override
    public void configEngine(Engine me) {

    }

    /**
     * 配置插件
     */
    @Override
    public void configPlugin(Plugins plugins) {

    }

    /**
     * 配置全局拦截器
     */
    @Override
    public void configInterceptor(Interceptors interceptors) {

    }

    /**
     * 配置处理器
     */
    @Override
    public void configHandler(Handlers me) {
        me.add(new ContextPathHandler("ctxPath"));
    }

    /**
     * 启动入口，运行此 main 方法可以启动项目，此 main 方法可以放置在任意的 Class 类定义中，不一定要放于此
     */
    public static void main(String[] args) {
        UndertowServer.start(AppConfig.class);
    }
}
