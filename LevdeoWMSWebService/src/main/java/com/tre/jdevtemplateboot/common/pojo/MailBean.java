package com.tre.jdevtemplateboot.common.pojo;

import java.io.Serializable;

/**
 * @description: 发送邮件实体bean
 * @author: JDev
 * @create: 2018-11-19 10:03
 **/
public class MailBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sender;      //邮件发送人
    private String recipient;   //邮件接收人
    private String[] arrRecipient; //邮件接收人
    private String subject;     //邮件主题
    private String content;     //邮件内容

    public MailBean() {
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }


    public String[] getArrRecipient() {
        return arrRecipient;
    }

    public void setArrRecipient(String[] arrRecipient) {
        this.arrRecipient = arrRecipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
