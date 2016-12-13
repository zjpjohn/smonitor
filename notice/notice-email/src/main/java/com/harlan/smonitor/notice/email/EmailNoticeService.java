package com.harlan.smonitor.notice.email;

import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.impl.TypeValueDeclare;
import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.api.notice.INoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发送邮件实现类
 * Created by harlan on 2016/11/22.
 */
public class EmailNoticeService implements INoticeService {
    private final static Logger logger = LoggerFactory.getLogger(EmailNoticeService.class);

    public Admin getAdminFrom(Map<String,Object> adminMap) {
        return new EmailAdmin(adminMap);
    }

    public Result sendMessage(Admin admin, String title, String content) {
        EmailAdmin emailAdmin= (EmailAdmin) admin;
        logger.info("发送邮件，邮箱为：{},标题为：{},内容为：{}",emailAdmin.getEmailAddress(),title,content);
        return new Result();
    }

    @Override
    public List<FieldDeclare> getAdminFields() {
        List<FieldDeclare> filedList =new ArrayList<FieldDeclare>();
        FieldDeclare emailAddressField =new FieldDeclare();
        emailAddressField.setFieldNmae("emailAddress");
        emailAddressField.setName("邮箱号");
        emailAddressField.setDesc("请填写邮箱号");
        filedList.add(emailAddressField);
        return filedList;
    }

    public TypeValueDeclare getTypeDeclare() {
        TypeValueDeclare type=new TypeValueDeclare();
        type.setTypeValue("email");
        type.setName("发送邮件");
        type.setDesc("可以发送邮件");
        return type;
    }
}
