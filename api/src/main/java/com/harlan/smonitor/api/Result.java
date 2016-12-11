package com.harlan.smonitor.api;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/***
 * 统一返回 dubbo服务的结果
 * success：成功/失败
 * msg：失败时有值，提示
 * obj：用于服务返回有用数据
 */
public class Result implements Serializable {

	private boolean success;
	private String msg;
	private Object obj;
	
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	/***
	 * 成功时使用此构造器
	 * 初始化的对象，msg=null,obj=null
	 */
	public Result() {
		super();
		success=true;
	}
	
	/***
	 * 返回错误时使用这个构造器，自动设置success=false
	 * @param msg 错误提示
	 */
	public Result(String msg) {
		super();
		this.success = false;
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}
	public boolean notSuccess() {
		return !success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
