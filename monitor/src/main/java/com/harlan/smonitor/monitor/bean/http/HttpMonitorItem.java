package com.harlan.smonitor.monitor.bean.http;


import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.HostMonitorItem;
import com.harlan.smonitor.monitor.bean.http.check.CheckBody;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by harlan on 2016/9/13.
 */
public class HttpMonitorItem extends MonitorItem {
	private final static Logger logger = LoggerFactory.getLogger(HttpMonitorItem.class);
	
	private String method;
	
	private String url;
	
	private String data;
	
    public HttpMonitorItem(Element itemElement) {
        super(itemElement);
        this.method = itemElement.attributeValue("method");
        this.url = itemElement.attributeValue("url");
        this.data = itemElement.attributeValue("data");
    }

    @Override
    protected CheckItem createCheck(Element checkElement) {
    	String type=checkElement.attributeValue("type");
    	if("code".equals(type)){
    		throw new RuntimeException("not create this type");
    	}else if("body".equals(type)){
    		return new CheckBody(checkElement);
    	}
        return null;
    }

	@Override
	protected void getProps(Element propElement) {

	}

	@Override
	protected Element setProps(Element propElement) {
		return null;
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
