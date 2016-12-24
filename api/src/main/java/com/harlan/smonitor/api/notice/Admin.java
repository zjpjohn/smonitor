package com.harlan.smonitor.api.notice;

import com.harlan.smonitor.api.impl.FieldDeclare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Admin {
	/***
	 * 根据从map中取出属性，转化成对象
	 * @param adminMap adminMap
	 */
	public void init(Map<String,Object> adminMap) {
		this.id = adminMap.get("id").toString();
		if(id.contains("|")){
			throw new RuntimeException("联系人id不能含有‘|’");
		}
		this.type = adminMap.get("type").toString();
		getAttrs(adminMap);
	}

	/**
	 * 唯一标识，用于定位管理员
	 */
	private String id;
	/**
	 * 该管理员对于的通知模块
	 */
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * bean转化成xml元素时，需要将属性保存
	 * @return
	 */
	public Map<String,Object> createMap(){
		Map<String,Object> admin_map=new HashMap<String,Object>();
		admin_map.put("id",id);
		admin_map.put("type",type);
		admin_map=setAttrs(admin_map);
		return admin_map;
	}

	/**
	 * 不同的admin实现类需要将各自属性set到这个map中
	 * @param adminMap 此map存放各个实现类的个性字段
	 * @return
	 */
	protected abstract Map<String,Object> setAttrs(Map<String,Object> adminMap);

	/**
	 * 实现这个方法，把属性取出来
	 * @param adminMap
	 */
	protected abstract void getAttrs(Map<String,Object> adminMap);


	/**
	 * 每个实现类，允许有自己特殊的字段，但该字段要在后台界面展示出来，并要填写值
	 * 所以要用这个方法，把自定义字段的字段名称，意义展示出来
	 * @return
	 */
	public abstract List<FieldDeclare> getFields();
}
