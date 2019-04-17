package com.tre.jdevtemplateboot.domain.vo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: Login验证
 * @author: JDev
 * @create: 2018-12-03 09:43
 **/
public class LoginVO {

    @NotNull(message = "id 不能为空")
    private Long id;

    @Future(message = "需要一个将来的日期")
    @NotNull
    private Date date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "LoginVO{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}
