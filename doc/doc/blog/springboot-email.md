# SpringBoot 2.x 集成QQ邮箱、网易系邮箱、Gmail邮箱发送邮件

[TOC]

>在Spring中提供了非常好用的 JavaMailSender接口实现邮件发送,在SpringBoot的Starter模块中也为此提供了自动化配置。

**几个名词解释**
- 什么是POP3、SMTP和IMAP？

  详细介绍-请移步至[网易帮助文档](http://help.mail.163.com/faqDetail.do?code=d7a5dc8471cd0c0e8b4b8f4f8e49998b374173cfe9171305fa1ce630d7f67ac22dc0e9af8168582a)

- IMAP和POP3有什么区别？

  详细介绍-请移步至[网易帮助文档](http://help.mail.163.com/faqDetail.do?code=d7a5dc8471cd0c0e8b4b8f4f8e49998b374173cfe9171305fa1ce630d7f67ac2f56104105f35a05d)

- 什么是免费邮箱客户端授权码功能？

  详细介绍-请移步至[网易帮助文档](http://help.mail.163.com/faqDetail.do?code=d7a5dc8471cd0c0e8b4b8f4f8e49998b374173cfe9171305fa1ce630d7f67ac2346f3c315579c0a8)


### Spring Boot中发送邮件步骤

Spring Boot中发送邮件具体的使用步骤如下
- 1、添加Starter模块依赖
- 2、添加Spring Boot配置(QQ/网易系/Gmail)
- 3、调用JavaMailSender接口发送邮件

#### 添加Starter模块依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```
#### 添加Spring Boot配置

>在**application.yml**中添加邮件相关的配置，这里分别罗列几个常用邮件的配置比如QQ邮箱、网易系邮箱、Gmail邮箱。

##### QQ邮箱配置
官方配置说明：[参考官方帮助中心](http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=369)

获取客户端授权码：[参考官方帮助中心](http://service.mail.qq.com/cgi-bin/help?subtype=1&&no=1001256&&id=28)

详细的配置如下：

```
spring:
  mail:
    host: smtp.qq.com #发送邮件服务器
    username: xx@qq.com #QQ邮箱
    password: xxxxxxxxxxx #客户端授权码
    protocol: smtp #发送邮件协议
    properties.mail.smtp.auth: true
    properties.mail.smtp.port: 465 #端口号465或587
    properties.mail.display.sendmail: Javen #可以任意
    properties.mail.display.sendname: Spring Boot Guide Email #可以任意
    properties.mail.smtp.starttls.enable: true
    properties.mail.smtp.starttls.required: true
    properties.mail.smtp.ssl.enable: true
    default-encoding: utf-8
    from: xx@qq.com #与上面的username保持一致
```
>说明：开启SSL时使用587端口时无法连接QQ邮件服务器

##### 网易系(126/163/yeah)邮箱配置

网易邮箱客户端授码:[参考官方帮助中心](http://help.mail.163.com/faq.do?m=list&categoryID=197)

客户端端口配置说明:[参考官方帮助中心](http://mail.163.com/html/110127_imap/index.htm#tab=android)

详细的配置如下：
```
spring:
  mail:
    host: smtp.126.com
    username: xx@126.com
    password: xxxxxxxx
    protocol: smtp
    properties.mail.smtp.auth: true
    properties.mail.smtp.port: 994 #465或者994
    properties.mail.display.sendmail: Javen
    properties.mail.display.sendname: Spring Boot Guide Email
    properties.mail.smtp.starttls.enable: true
    properties.mail.smtp.starttls.required: true
    properties.mail.smtp.ssl.enable: true
    default-encoding: utf-8
    from: xx@126.com
```

> 特别说明:
- 126邮箱SMTP服务器地址:smtp.126.com,端口号:465或者994
- 163邮箱SMTP服务器地址:smtp.163.com,端口号:465或者994
- yeah邮箱SMTP服务器地址:smtp.yeah.net,端口号:465或者994

##### Gmail邮箱配置

Gmail 客户端设置说明:[参考官方Gmail帮助](https://support.google.com/mail/answer/78754)

>以上链接需要自行搭梯子，这里截几张图参考下

![](https://oscimg.oschina.net/oscnet/5269cde35e59f75cae162c16a05c6771b00.jpg) 
![](https://oscimg.oschina.net/oscnet/c69257f221941f2ecba71078f8a780e8505.jpg)
![](https://oscimg.oschina.net/oscnet/4b13137391366183ea811ad83875254ea35.jpg)

> 总结:
> Gmail 发送邮件服务器为:smtp.gmail.com,端口号:465。**客户端授权码为Gmail账号的密码**，必须使用使用SSL。

> 还需要开启[允许不够安全的应用](https://accounts.google.com/signin/continue?sarp=1&scc=1&plt=AKgnsbvM) ，不然会出现`Authentication failed`的异常
> 选择登录与安全滑到底部有个`允许不够安全的应用`开启即可

详细的配置如下：

```
spring:
  mail:
    host: smtp.gmail.com
    username:xxx@gmail.com
    password: xxxxx #Gmail账号密码
    protocol: smtp
    properties.mail.smtp.auth: true
    properties.mail.smtp.port: 465
    properties.mail.display.sendmail: Javen
    properties.mail.display.sendname: Spring Boot Guide Email
    properties.mail.smtp.starttls.enable: true
    properties.mail.smtp.starttls.required: true
    properties.mail.smtp.ssl.enable: true
    from: xxx@gmail.com
    default-encoding: utf-8
```

#### 调用JavaMailSender接口发送邮件

> 常用几种邮件形式接口的封装

```
import javax.mail.MessagingException;

public interface IMailService {
    /**
     * 发送文本邮件
     * @param to
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String to, String subject, String content);

    public void sendSimpleMail(String to, String subject, String content, String... cc);

    /**
     * 发送HTML邮件
     * @param to
     * @param subject
     * @param content
     * @throws MessagingException
     */
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException;

    public void sendHtmlMail(String to, String subject, String content, String... cc);

    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param filePath
     * @throws MessagingException
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException;

    public void sendAttachmentsMail(String to, String subject, String content, String filePath, String... cc);

    /**
     * 发送正文中有静态资源的邮件
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     * @param rscId
     * @throws MessagingException
     */
    public void sendResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException;

    public void sendResourceMail(String to, String subject, String content, String rscPath, String rscId, String... cc);

} 
```

> 再写一个组件实现上面的接口并注入JavaMailSender

```
@Component
public class IMailServiceImpl implements IMailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.from}")
    private String from;
    //具体实现请继续向下阅读
}
```

##### 发送文本邮件

```
 /**
     * 发送文本邮件
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendSimpleMail(String to, String subject, String content, String... cc) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setCc(cc);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
```

##### 发送html邮件
```
 /**
     * 发送HTML邮件
     * @param to
     * @param subject
     * @param content
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
```

省略实现带有抄送方法的实现

##### 发送带附件的邮件
```
 /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param filePath
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
        helper.addAttachment(fileName, file);

        mailSender.send(message);
    }
```
省略实现带有抄送方法的实现

##### 发送正文中有静态资源的邮件

```
/**
     * 发送正文中有静态资源的邮件
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     * @param rscId
     */
    public void sendResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        FileSystemResource res = new FileSystemResource(new File(rscPath));
        helper.addInline(rscId, res);

        mailSender.send(message);
    }
```
省略实现带有抄送方法的实现

##### 发送模板邮件

**发送模板邮件使用的方法与发送HTML邮件的方法一致**。只是发送邮件时使用到的模板引擎,这里使用的模板引擎为`Thymeleaf`。

```
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

> 模板HTML代码如下:

```
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>IJPay让支付触手可及</title>
    <style>
        body {
            text-align: center;
            margin-left: auto;
            margin-right: auto;
        }
        #welcome {
            text-align: center;
            position: absolute;
        }
    </style>
</head>
<body>
<div id="welcome">
    <h3>欢迎使用 <span th:text="${project}"></span> -By <span th:text=" ${author}"></span></h3>
    <span th:text="${url}"></span>
    <div style="text-align: center; padding: 10px">
        <a style="text-decoration: none;" href="#" th:href="@{${url}}" target="_bank">
            <strong>IJPay让支付触手可及,欢迎Start支持项目发展:)</strong>
        </a>
    </div>
    <div style="text-align: center; padding: 4px">
        如果对你有帮助,请任意打赏
    </div>
    <img width="180px" height="180px"
         src="https://oscimg.oschina.net/oscnet/8e86fed2ee9571eb133096d5dc1b3cb2fc1.jpg">
</div>
</body>
</html>
```

>如何使用请看测试中实现的代码。

#### 测试

```
package com.javen.controller;

import com.javen.email.impl.IMailServiceImpl;
import com.javen.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RestController
@RequestMapping("email")
public class EmailController {

    @Autowired
    private IMailServiceImpl mailService;//注入发送邮件的各种实现方法
    @Autowired
    private TemplateEngine templateEngine;//注入模板引擎

    @RequestMapping
    public JsonResult index(){
        try {
            mailService.sendSimpleMail("xxx@126.com","SpringBoot Email","这是一封普通的SpringBoot测试邮件");
        }catch (Exception ex){
            ex.printStackTrace();
            return new JsonResult(-1,"邮件发送失败!!");
        }
        return new JsonResult();
    }

    @RequestMapping("/htmlEmail")
    public JsonResult htmlEmail(){
        try {
            mailService.sendHtmlMail(""xxx@126.com","IJPay让支付触手可及","<body style=\"text-align: center;margin-left: auto;margin-right: auto;\">\n"
                    + " <div id=\"welcome\" style=\"text-align: center;position: absolute;\" >\n"
                    +"      <h3>欢迎使用IJPay -By Javen</h3>\n"
                    +"      <span>https://github.com/Javen205/IJPay</span>"
                    + "     <div\n"
                    + "         style=\"text-align: center; padding: 10px\"><a style=\"text-decoration: none;\" href=\"https://github.com/Javen205/IJPay\" target=\"_bank\" ><strong>IJPay 让支付触手可及,欢迎Start支持项目发展:)</strong></a></div>\n"
                    + "     <div\n" + "         style=\"text-align: center; padding: 4px\">如果对你有帮助,请任意打赏</div>\n"
                    + "     <img width=\"180px\" height=\"180px\"\n"
                    + "         src=\"https://javen205.gitbooks.io/ijpay/content/assets/wxpay.png\">\n"
                    + " </div>\n" + "</body>");
        }catch (Exception ex){
            ex.printStackTrace();
            return new JsonResult(-1,"邮件发送失败!!");
        }
        return new JsonResult();
    }

    @RequestMapping("/attachmentsMail")
    public JsonResult attachmentsMail(){
        try {
            String filePath = "/Users/Javen/Desktop/IJPay.png";
            mailService.sendAttachmentsMail("xxx@126.com", "这是一封带附件的邮件", "邮件中有附件，请注意查收！", filePath);
        }catch (Exception ex){
            ex.printStackTrace();
            return new JsonResult(-1,"邮件发送失败!!");
        }
        return new JsonResult();
    }

    @RequestMapping("/resourceMail")
    public JsonResult resourceMail(){
        try {
            String rscId = "IJPay";
            String content = "<html><body>这是有图片的邮件<br/><img src=\'cid:" + rscId + "\' ></body></html>";
            String imgPath = "/Users/Javen/Desktop/IJPay.png";
            mailService.sendResourceMail("xxx@126.com", "这邮件中含有图片", content, imgPath, rscId);

        }catch (Exception ex){
            ex.printStackTrace();
            return new JsonResult(-1,"邮件发送失败!!");
        }
        return new JsonResult();
    }

    @RequestMapping("/templateMail")
    public JsonResult templateMail(){
        try {
            Context context = new Context();
            context.setVariable("project", "IJPay");
            context.setVariable("author", "Javen");
            context.setVariable("url", "https://github.com/Javen205/IJPay");
            String emailContent = templateEngine.process("emailTemp", context);

            mailService.sendHtmlMail("xxx@126.com", "这是模板邮件", emailContent);
        }catch (Exception ex){
            ex.printStackTrace();
            return new JsonResult(-1,"邮件发送失败!!");
        }
        return new JsonResult();
    }
}

```

#### 效果图
[![接收到的所有邮件](https://oscimg.oschina.net/oscnet/34733a9357a7700dc9c2e51c7612d4cc022.jpg "接收到的所有邮件")
![发送普通邮件](https://oscimg.oschina.net/oscnet/6275e9368baf81ae8928d4a661f570d6688.jpg "发送普通邮件")
![发送HTML邮件](https://oscimg.oschina.net/oscnet/addfe46525c34be333587b413eec37f66b9.jpg "发送普通邮件")
![发送带有附件的邮件](https://oscimg.oschina.net/oscnet/789bdd77960a35131c62b6a3d864c4a0273.jpg "发送带有附件的邮件")
![发送含有图片的邮件](https://oscimg.oschina.net/oscnet/ec8162f0df5533342a3e9d73be05728d47a.jpg "发送含有图片的邮件")
![发送模板邮件](https://oscimg.oschina.net/oscnet/67f16adec4c8e16142c959baf4ff2bd4265.jpg "发送模板邮件")

#### 完

使用 Spring Boot 发送邮件到这里就介绍完了。个人能力有限如有错误欢迎指正。你有更好的解决方案或者建议欢迎一起交流讨论，如有疑问欢迎留言。

[参考资料](https://javen205.gitbooks.io/ijpay/study.html)

