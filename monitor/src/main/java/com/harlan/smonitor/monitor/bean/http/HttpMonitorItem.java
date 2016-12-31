package com.harlan.smonitor.monitor.bean.http;


import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.impl.TypeDeclare;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckFile;
import com.harlan.smonitor.monitor.bean.http.check.CheckBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http服务监控
 * Created by harlan on 2016/9/13.
 */
public class HttpMonitorItem extends MonitorItem {

	private String method;
	
	private String url;
	
	private String data;

	@Override
	public List<FieldDeclare> getFields() {
		List<FieldDeclare> fieldList=new ArrayList<FieldDeclare>();
		fieldList.add(new FieldDeclare("method","方法","get还是post"));
		fieldList.add(new FieldDeclare("url","服务的url","http服务的url，带http"));
		fieldList.add(new FieldDeclare("data","请求参数","请求参数"));
		return fieldList;
	}

	@Override
	public Map<String, TypeDeclare> getCheckTypeMap() {
		Map<String,TypeDeclare> CHECK_MAP=new HashMap<String,TypeDeclare>();
		TypeDeclare body=new TypeDeclare();
		body.setTypeValue("body");
		body.setName("返回报文");
		body.setDesc("可监控http服务返回报文");
		body.setBeanClass(CheckBody.class);
		CHECK_MAP.put(body.getTypeValue(),body);
		return CHECK_MAP;
	}

	@Override
	protected Map<String,Object> setProps(Map<String,Object> itemMap) {
		return null;
	}

	@Override
	protected void getProps(Map<String, Object> itemMap) {
		this.method = itemMap.get("method").toString();
		this.url = itemMap.get("url").toString();
		this.data = itemMap.get("data").toString();
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getData() {
		return data;
	}
}
