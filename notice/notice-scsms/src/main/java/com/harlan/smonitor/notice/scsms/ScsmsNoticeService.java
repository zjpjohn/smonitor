package com.harlan.smonitor.notice.scsms;

import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.impl.TypeValueDeclare;
import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.api.notice.INoticeService;
import com.harlan.smonitor.notice.scsms.webserviceclient.SendInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScsmsNoticeService implements INoticeService {
	
	private final static Logger logger = LoggerFactory.getLogger(ScsmsNoticeService.class);

	@Override
	public Admin getAdminFrom(Map<String, Object> adminMap) {
		return new ScsmsAdmin(adminMap);
	}

	public Result sendMessage(Admin admin, String title, String content) {
		ScsmsAdmin scAdmin=(ScsmsAdmin)admin;
		logger.info("(通知管理员;)"+scAdmin.getId());
		logger.info("(通知内容)"+content);
		try {
			SendInfoUtil.Resp resp = SendInfoUtil.sendMobileMsg(scAdmin.getPhoneNo(), content);
			if("0".equals(resp.getCode())){
				logger.info("短信发送成功");
				return new Result();
			}else{
				logger.info("短信发送失败:{}",resp.getMsg());
				return new Result(resp.getMsg());
			}
		} catch (Exception e) {
			logger.error("(插入数据库发送短信失败)",e);
			return new Result("发生异常");
		}
	}

	@Override
	public List<FieldDeclare> getAdminFields() {
		List<FieldDeclare> filedList =new ArrayList<FieldDeclare>();
		filedList.add(new FieldDeclare("phoneNo","手机号","仅限于四川移动手机号"));
		return filedList;
	}

	public TypeValueDeclare getTypeDeclare() {
		TypeValueDeclare type=new TypeValueDeclare();
		type.setTypeValue("scsms");
		type.setName("四川移动发送短信");
		type.setDesc("只能发送四川移动手机用户的手机号");
		return type;
	}
}
