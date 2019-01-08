package com.yuan.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailServiceTest {

    @Resource
    private MailService mailService;

    @Resource
    private TemplateEngine templateEngine;

    @Test
    public void sendSimpleMail() throws Exception {
        String to = "543456229@qq.com";
        String to1 = "wangyuan0814@yeah.net";
        mailService.sendSimpleMail(to1, "java发送第一封邮件", "测试");
    }

    @Test
    public void sendHtmlMail() throws Exception {
        String to = "543456229@qq.com";
        String to1 = "wangyuan0814@yeah.net";
        String content = "<html>\n" + "<body>\n" + "<h3>这是一封html邮件</h3>\n" + "</body>\n" + "</html>";
        mailService.sendHtmlMail(to, "java发送的HTML邮件", content);
    }

    @Test
    public void sendAttachmentsMail() throws Exception {
        String to = "543456229@qq.com";
        String to1 = "wangyuan0814@yeah.net";
        String filePath = "C:\\Users\\wangy\\Desktop\\记录.txt";
        mailService.sendAttachmentsMail(to, "java发送的带附件的邮件", "带附件测试", filePath);
    }

    @Test
    public void sendContentWithImgMail() throws Exception {
        String to = "543456229@qq.com";
        String to1 = "wangyuan0814@yeah.net";
        String srcPath = "F:\\图片\\锁屏.jpg";
        String srcId = "001";
        //多个图片的话就继续加img标签，注意图片id别重复
        String content = "<html><body>这是有图片的邮件：<img src=\'cid:" + srcId + "\'></img></body></html>";
        mailService.sendContentWithImgMail(to, "java发送的正文带图片的邮件", content, srcPath, srcId);
    }

    /**
     * 模板邮件测试
     */
    @Test
    public void sendTemplateMail() throws Exception {
        Context context = new Context();
        context.setVariable("id", "006");//name为html文件中定义的${id}的“id”，006就是“id”的值
        //第一个参数为html文件的文件名
        String emailContext = templateEngine.process("emailTemplates", context);
        String to = "543456229@qq.com";
        String to1 = "wangyuan0814@yeah.net";
        mailService.sendHtmlMail(to, "java发送的模板邮件", emailContext);
    }

}