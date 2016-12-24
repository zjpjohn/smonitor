package com.harlan.smonitor.api.notice;

import com.harlan.smonitor.api.impl.Implementor;
import com.harlan.smonitor.api.Result;

public interface INoticeService extends Implementor {
	/**
	 * 通知管理员
	 * @param admin 待通知的管理员信息
	 * @param title 标题，例如：邮件标题
	 * @param content 通知内容
	 */
	Result sendMessage(Admin admin, String title, String content);

}
