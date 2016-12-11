package com.harlan.smonitor.monitor.bean.inspection;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.inspection.check.CheckSelf;
import org.dom4j.Element;


public class InspectionMonitorItem extends MonitorItem {

	public InspectionMonitorItem(Element itemElement) {
		super(itemElement);
	}

	@Override
	protected CheckItem createCheck(Element checkElement) {
		String type=checkElement.attributeValue("type");
		if("self".equals(type)){
			return new CheckSelf(checkElement);
		}else{
			throw new RuntimeException("InspectionMonitorItem中未配置该类型:"+type);
		}
	}

	@Override
	protected void getProps(Element propElement) {

	}

	@Override
	protected Element setProps(Element propElement) {
		return propElement;
	}


}
