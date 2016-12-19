package com.harlan.smonitor.monitor.bean.inspection;

import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.http.check.CheckBody;
import com.harlan.smonitor.monitor.bean.inspection.check.CheckSelf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InspectionMonitorItem extends MonitorItem {

	@Override
	public List<FieldDeclare> getFields() {
		return new ArrayList<FieldDeclare>();
	}

	@Override
	protected Map<String, Class<?>> getCheckClassMap() {
		Map<String, Class<?>> CHECK_MAP=new HashMap<String, Class<?>>();
		CHECK_MAP.put("self",CheckSelf.class);
		return CHECK_MAP;
	}

	@Override
	protected Map<String,Object> setProps(Map<String,Object> checkMap) {
		return checkMap;
	}

	@Override
	protected void getProps(Map<String, Object> itemMap) {

	}


}
