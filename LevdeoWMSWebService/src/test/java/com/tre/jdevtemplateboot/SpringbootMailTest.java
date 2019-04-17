package com.tre.jdevtemplateboot;

import com.tre.jdevtemplateboot.common.pojo.MailBean;
import com.tre.jdevtemplateboot.common.util.LMailServiceUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.io.*;

/**
 * @description: 测试邮件发送
 * @author: JDev
 * @create: 2018-11-19 08:09
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMailTest {

    @Autowired
    private LMailServiceUtils lMailServiceUtils;

    @Test
    public void sendSimpleMailTest(){

        MailBean mailBean = new MailBean();
        mailBean.setSender("10004299yuan_mingyin@cn.tre-inc.com");
        mailBean.setRecipient("surersen@163.com");
        mailBean.setSubject("Springboot mail test2");
        mailBean.setContent( "Springboot 测试邮件");
        lMailServiceUtils.sendSimpleMail(mailBean);
    }


    @Test
    public void sendHTMLMailTest() throws MessagingException {

        MailBean mailBean = new MailBean();
        mailBean.setSender("10004299yuan_mingyin@cn.tre-inc.com");
        mailBean.setRecipient("surersen@163.com");
        mailBean.setSubject("Springboot mail test2");
        mailBean.setContent( "Springboot 测试邮件");

        StringBuilder sb = new StringBuilder();
        sb.append("<h1>SpirngBoot测试邮件HTML</h1>")
                .append("<p style='color:#F00'>你是真的太棒了！</p>")
                .append("<p style='text-align:right'>右对齐</p>");

        lMailServiceUtils.sendHTMLMail(mailBean,sb);
    }

    @Test
    public void sendHTMLMultiRecipientMailTest() throws MessagingException {

        String[] arrRecipient ={"surersen@163.com","10004299yuan_mingyin@cn.tre-inc.com"};

        MailBean mailBean = new MailBean();
        mailBean.setSender("10004299yuan_mingyin@cn.tre-inc.com");
        mailBean.setArrRecipient(arrRecipient);
        mailBean.setSubject("Springboot mail test3");
        mailBean.setContent( "Springboot 测试邮件");

        StringBuilder sb = new StringBuilder();
        sb.append("<h1>SpirngBoot测试邮件HTML</h1>")
                .append("<p style='color:#F00'>你是真的太棒了！</p>")
                .append("<p style='text-align:right'>右对齐</p>");

        lMailServiceUtils.sendHTMLMultiRecipientMail(mailBean,sb);
    }

    @Test
    public  void sendHTMLMultiRecipientAttachmentMailTest() throws MessagingException, FileNotFoundException {

        String[] arrRecipient ={"surersen@163.com","10004299yuan_mingyin@cn.tre-inc.com"};

        MailBean mailBean = new MailBean();
        mailBean.setSender("10004299yuan_mingyin@cn.tre-inc.com");
        mailBean.setArrRecipient(arrRecipient);
        mailBean.setSubject("Springboot mail test3");
        mailBean.setContent( "Springboot 测试邮件");

        StringBuilder sb = new StringBuilder();
        sb.append("<h1>SpirngBoot测试邮件HTML</h1>")
                .append("<p style='color:#F00'>aaaaaaaaaaaaaaaa！</p>")
                .append("<p style='text-align:right'>右对齐</p>");

        File fs = new File("D:\\Project\\common.js");
        lMailServiceUtils.sendHTMLMultiRecipientAttachmentMail(mailBean,sb,fs);

    }

}
