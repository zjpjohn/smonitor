package com.harlan.smonitor.notice.email;

import com.harlan.smonitor.api.Implementor;
import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.api.notice.INoticeService;
import com.harlan.smonitor.monitor.common.Util;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送邮件实现类
 * Created by harlan on 2016/11/22.
 */
public class EmailNoticeService implements INoticeService {
    private final static Logger logger = LoggerFactory.getLogger(EmailNoticeService.class);
//    public Admin getAdminFromXml(Element adminElement) {
//        Admin adminBean=new Admin();
//        String id=adminElement.attributeValue("id");
//        String type=adminElement.attributeValue("type");
//        String email=adminElement.attributeValue("email");
//        if(Util.isNull(id)){
//            throw new RuntimeException("admin配置中，id不能为空");
//        }
//        if(Util.isNull(type)){
//            throw new RuntimeException("admin配置中，type不能为空");
//        }
//        if(Util.isNull(email)){
//            throw new RuntimeException("admin配置中，type="+TYPE+"时，email不能为空");
//        }
//        if(!email.contains("@")){
//            throw new RuntimeException("admin配置中，type="+TYPE+"时，email必须填写邮箱");
//        }
//        adminBean.setId(Integer.valueOf(id));
//        adminBean.setType(type);
//        adminBean.setAccount(email);
//        return adminBean;
//    }

    public Result sendMessage(Admin admin, String title, String content) {
        logger.info("发送邮件，邮箱为：{},标题为：{},内容为：{}",admin.getAccount(),title,content);
        return new Result();
    }

    public String getName() {
        return "邮件发送";
    }

    public String getId() {
        return "email";
    }
}
