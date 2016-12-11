package com.harlan.smonitor.api.notice;

import com.harlan.smonitor.api.Implementor;
import com.harlan.smonitor.api.Result;
import org.dom4j.Element;

public interface INoticeService extends Implementor {
//	/**
//	 * 将admin配置文件中的每个admin配置转化成 admin对象
//	 * 如果需要增加字段，新建bean extends Admin对象即可
//	 * @param adminElement xml对象元素
//	 * @return Admin 返回admin对象
//	 */
//	Admin getAdminFromXml(Element adminElement);
	/**
	 * 通知管理员
	 * @param admin 待通知的管理员信息
	 * @param title 标题，例如：邮件标题
	 * @param content 通知内容
	 */
	Result sendMessage(Admin admin, String title, String content);
	
}
