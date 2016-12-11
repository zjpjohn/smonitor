package com.harlan.smonitor.api.notice;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Admin {
	public Admin() {
	}

	public Admin(Element element) {
		this.id = Integer.valueOf(element.attributeValue("id"));
		this.type = element.attributeValue("type");
		this.account = element.attributeValue("account");
		this.mark = element.attributeValue("mark");
	}

	/**
	 * 唯一标识，用于定位管理员
	 */
	private Integer id;
	/**
	 * 该管理员对于的通知模块
	 */
	private String type;
	/***
	 * 帐号：可以是手机号、邮箱、微信号等
	 */
	private String account;

	private String mark;

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Element createElement(){
		Element g= DocumentHelper.createElement("admin");
		g.addAttribute("id",id.toString());
		g.addAttribute("account",account);
		g.addAttribute("type",type);
		g.addAttribute("mark",mark);
		return g;
	}


	@Override
	public String toString() {
		return "Admin{" +
				"id=" + id +
				", type='" + type + '\'' +
				", account='" + account + '\'' +
				", mark='" + mark + '\'' +
				'}';
	}
}
