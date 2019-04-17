package com.tre.jdevtemplateboot.common.util;

import com.tre.jdevtemplateboot.common.pojo.MailBean;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @description: java mail 发送邮件服务
 * @author: JDev
 * @create: 2018-11-19 10:12
 **/
@Component
public class LMailServiceUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    /** 
    * @Description: 发送一个简单格式的邮件
    * @Param: [mailBean] 
    * @return: void 
    * @Author: JDev
    * @Date: 2018/11/19 
    **/
    public void sendSimpleMail(MailBean mailBean) throws MailException{

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailBean.getSender());
        simpleMailMessage.setTo(mailBean.getRecipient());
        simpleMailMessage.setSubject(mailBean.getSubject());
        simpleMailMessage.setText(mailBean.getContent());

        javaMailSender.send(simpleMailMessage);
    }

    /** 
    * @Description:  发送一个HTML格式的邮件 
    * @Param: [mailBean] 
    * @return: void 
    * @Author: JDev
    * @Date: 2018/11/19 
    **/
    public void sendHTMLMail(MailBean mailBean, StringBuilder sb) throws MessagingException {

        MimeMessage mimeMailMessage = null;
        mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,true);
        mimeMessageHelper.setFrom(mailBean.getSender());
        mimeMessageHelper.setTo(mailBean.getRecipient());
        mimeMessageHelper.setSubject(mailBean.getSubject());
        mimeMessageHelper.setText(sb.toString(),true);

        javaMailSender.send(mimeMailMessage);
    }

    /**
    * @Description:  给多个接收者发送一个HTML格式的邮件
     * @Param: [mailBean, sb]
    * @return: void
    * @Author: JDev
    * @Date: 2018/11/19
    **/
    public void sendHTMLMultiRecipientMail (MailBean mailBean, StringBuilder sb) throws MessagingException {

        MimeMessage mimeMailMessage = null;
        mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,true);
        mimeMessageHelper.setFrom(mailBean.getSender());
        mimeMessageHelper.setTo(mailBean.getArrRecipient());
        mimeMessageHelper.setSubject(mailBean.getSubject());
        mimeMessageHelper.setText(sb.toString(),true);

        javaMailSender.send(mimeMailMessage);
    }

    /** 
    * @Description: 发送带附件格式的简单邮件
    * @Param: [mailBean] 
    * @return: void 
    * @Author: JDev
    * @Date: 2018/11/19 
    **/
    public void sendAttachmentMail(MailBean mailBean, File file) throws  MessagingException{

        MimeMessage mimeMailMessage = null;
        mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,true);
        mimeMessageHelper.setFrom(mailBean.getSender());
        mimeMessageHelper.setTo(mailBean.getArrRecipient());
        mimeMessageHelper.setSubject(mailBean.getSubject());
        mimeMessageHelper.setText(mailBean.getContent());
        FileSystemResource fsr = new FileSystemResource(file);
        mimeMessageHelper.addAttachment(file.getName(),fsr);

        javaMailSender.send(mimeMailMessage);
    }

    /**
    * @Description:  给多个接收者发送一个HTML格式的邮件
    * @Param: [mailBean, sb, file]
    * @return: void
    * @Author: JDev
    * @Date: 2018/11/19
    **/
    public void  sendHTMLMultiRecipientAttachmentMail(MailBean mailBean, StringBuilder sb, File file)
            throws MessagingException{

        MimeMessage mimeMailMessage = null;

        mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage,true);
        mimeMessageHelper.setFrom(mailBean.getSender());
        mimeMessageHelper.setTo(mailBean.getArrRecipient());
        mimeMessageHelper.setSubject(mailBean.getSubject());
        mimeMessageHelper.setText(sb.toString(),true);
        FileSystemResource fs = new FileSystemResource(file);
        mimeMessageHelper.addAttachment(file.getName(),fs);

        javaMailSender.send(mimeMailMessage);
    }


}
