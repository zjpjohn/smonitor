package com.harlan.smonitor.api.notice;

import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.impl.Implementor;
import com.harlan.smonitor.api.Result;
import java.util.List;
import java.util.Map;

public interface INoticeService extends Implementor {
	/**
	 * 将admin配置文件中的每个admin配置转化成 admin对象
	 * 如果需要增加字段，新建bean extends Admin对象即可
	 * @param adminMap xml对象元素
	 * @return Admin 返回admin对象
	 */
	Admin getAdminFrom(Map<String,Object> adminMap);
	/**
	 * 通知管理员
	 * @param admin 待通知的管理员信息
	 * @param title 标题，例如：邮件标题
	 * @param content 通知内容
	 */
	Result sendMessage(Admin admin, String title, String content);

	/**
	 * 将所有个性化字段拼成list，用于页面展示及填写
	 * ps:要定义展示的字段名称和注释
	 * @return
	 */
	List<FieldDeclare> getAdminFields();

}
