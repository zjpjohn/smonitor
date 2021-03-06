package com.harlan.smonitor.notice.email;

import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.impl.TypeDeclare;
import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.api.notice.INoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送邮件实现类
 * Created by harlan on 2016/11/22.
 */
public class EmailNoticeService implements INoticeService {
    private final static Logger logger = LoggerFactory.getLogger(EmailNoticeService.class);

    public Result sendMessage(Admin admin, String title, String content) {
        EmailAdmin emailAdmin= (EmailAdmin) admin;
        logger.info("发送邮件，邮箱为：{},标题为：{},内容为：{}",emailAdmin.getEmailAddress(),title,content);
        boolean success=new SendEmail(emailAdmin.getEmailAddress()).sendMail(title,content);
        if(success){
            return new Result();
        }else{
            return new Result("发送失败");
        }
    }

    public TypeDeclare getTypeDeclare() {
        TypeDeclare type=new TypeDeclare();
        type.setTypeValue("email");
        type.setName("发送邮件");
        type.setDesc("可以发送邮件");
        type.setBeanClass(EmailAdmin.class);
        return type;
    }
}
