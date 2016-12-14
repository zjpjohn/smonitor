package com.harlan.smonitor.monitor.bean.inspection;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.inspection.check.CheckSelf;

import java.util.Map;


public class InspectionMonitorItem extends MonitorItem {

	@Override
	protected CheckItem createCheck(Map<String,Object> checkMap) {
		String type=checkMap.get("type").toString();
		if("self".equals(type)){
			return new CheckSelf(checkMap);
		}else{
			throw new RuntimeException("InspectionMonitorItem中未配置该类型:"+type);
		}
	}

	@Override
	protected Map<String,Object> setProps(Map<String,Object> checkMap) {
		return checkMap;
	}

	@Override
	protected void getProps(Map<String, Object> itemMap) {

	}


}
