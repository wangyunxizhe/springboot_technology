package com.yuan.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService {

    private final Logger logger = LoggerFactory.getLogger(MailService.class);

    @Value("${spring.mail.username}")
    private String from;//发件人

    @Autowired
    private JavaMailSender mailSender;//邮件发送对象

    /**
     * 发送普通邮件
     * 这里用的qq邮箱，必须先在qq邮箱的设置中，开启对smtp协议的支持，并得到授权码
     *
     * @param to      收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(from);
        //发送邮件
        mailSender.send(message);
    }

    /**
     * 发送HTML格式邮件
     *
     * @param to      收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    public void sendHtmlMail(String to, String subject, String content) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setFrom(from);
        //发送邮件
        mailSender.send(message);
    }

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人
     * @param subject  邮件主题
     * @param content  邮件内容
     * @param filePath 邮件中附件的地址
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        //创建发送附件对象
        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = file.getFilename();
        helper.addAttachment(fileName, file);//如果是多个附件，就继续addAttachment()方法
        helper.setFrom(from);
        //发送邮件
        mailSender.send(message);
    }

    /**
     * 发送正文中带图片的邮件
     * 注：发送邮件的异常处理以该方法为例
     *
     * @param to      收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param srcPath 图片地址
     * @param srcId
     */
    public void sendContentWithImgMail(String to, String subject, String content,
                                       String srcPath, String srcId) {
        logger.info("发送静态图片邮件开始：{},{},{},{},{}", to, subject, content, srcPath, srcId);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            //创建发送图片对象
            FileSystemResource img = new FileSystemResource(new File(srcPath));
            helper.addInline(srcId, img);//如果正文包含多个图片，就继续addInline()方法
            //发送邮件
            mailSender.send(message);
            logger.info("发送静态图片邮件成功！");
        } catch (MessagingException e) {
            logger.error("发送静态图片邮件异常：", e);
        }
    }

}
