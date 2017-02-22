package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckDiskServiceImpl;
import org.quartz.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.harlan.smonitor.monitor.common.Util.notNull;

public class CheckDisk extends CheckItem {

	private Integer exceed;
	private String path;
	private Integer inodeExceed;

	public void getAttrs(Map<String,Object> checkMap) {
		this.path=checkMap.get("path").toString();
		if(notNull(checkMap.get("exceed"))){
			this.exceed=Integer.valueOf(checkMap.get("exceed").toString());
		}
		if(notNull(checkMap.get("inodeExceed"))){
			this.inodeExceed=Integer.valueOf(checkMap.get("inodeExceed").toString());
		}
	}

	@Override
	public List<FieldDeclare> getFields() {
		List<FieldDeclare> fieldList=new ArrayList<FieldDeclare>();
		fieldList.add(new FieldDeclare("exceed","磁盘阀值","不可空，磁盘超过这个值"));
		fieldList.add(new FieldDeclare("inodeExceed","inode阀值","可空"));
		fieldList.add(new FieldDeclare("path","文件系统","不可空，绝对路径"));
		return fieldList;
	}

	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckDiskServiceImpl.class;
	}

	@Override
	public Map<String,Object> setAttrs(Map<String,Object> checkMap) {
		checkMap.put("path",path);
		checkMap.put("exceed",exceed);
		checkMap.put("inodeExceed",inodeExceed);
		return checkMap;
	}

	public Integer getExceed() {
		return exceed;
	}
	public String getPath() {
		return path;
	}
	public Integer getInodeExceed() {
		return inodeExceed;
	}
}
