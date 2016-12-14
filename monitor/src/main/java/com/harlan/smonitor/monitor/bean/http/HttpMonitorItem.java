package com.harlan.smonitor.monitor.bean.http;


import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.http.check.CheckBody;

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
    protected CheckItem createCheck(Map<String,Object> checkMap) {
    	String type=checkMap.get("type").toString();
    	if("code".equals(type)){
    		throw new RuntimeException("not create this type");
    	}else if("body".equals(type)){
    		return new CheckBody(checkMap);
    	}
        return null;
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
