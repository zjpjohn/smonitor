package com.harlan.smonitor.api.notice;

import java.util.HashMap;
import java.util.Map;

public abstract class Admin {
	/***
	 * 根据从map中取出属性，转化成对象
	 * @param adminMap adminMap
	 */
	public Admin(Map<String,Object> adminMap) {
		if(adminMap.get("id")!=null){
			this.id = Integer.valueOf(adminMap.get("id").toString());
		}
		if(adminMap.get("mark")!=null){
			this.mark = adminMap.get("mark").toString();
		}
		this.type = adminMap.get("type").toString();
	}

	/**
	 * 唯一标识，用于定位管理员
	 */
	private Integer id;
	/**
	 * 该管理员对于的通知模块
	 */
	private String type;

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

	/**
	 * bean转化成xml元素时，需要将属性保存
	 * @return
	 */
	public Map<String,Object> createMap(){
		Map<String,Object> admin_map=new HashMap<String,Object>();
		admin_map.put("id",id.toString());
		admin_map.put("type",type);
		admin_map.put("mark",mark);
		admin_map=setAttrs(admin_map);
		return admin_map;
	}

	/**
	 * 不同的admin实现类需要将各自属性set到这个map中
	 * @param adminMap 此map存放各个实现类的个性字段
	 * @return
	 */
	protected abstract Map<String,Object> setAttrs(Map<String,Object> adminMap);

}
