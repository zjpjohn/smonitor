package com.harlan.smonitor.monitor.bean.inspection;

import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.impl.TypeDeclare;
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
	public Map<String, TypeDeclare> getCheckTypeMap() {
		Map<String,TypeDeclare> CHECK_MAP=new HashMap<String,TypeDeclare>();
		TypeDeclare self=new TypeDeclare();
		self.setTypeValue("self");
		self.setName("定时通知信息");
		self.setDesc("可定时发送信息，确认通知模块正常");
		self.setBeanClass(CheckSelf.class);
		CHECK_MAP.put(self.getTypeValue(),self);
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
