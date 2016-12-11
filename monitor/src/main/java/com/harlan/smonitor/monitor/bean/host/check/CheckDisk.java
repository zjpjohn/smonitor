package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckDiskServiceImpl;
import org.dom4j.Element;
import org.quartz.Job;


public class CheckDisk extends CheckItem {

	private Integer exceed;
	private String path;
	private Integer inodeExceed;
	public CheckDisk(Element checkElement) {
		super(checkElement);
	}

	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckDiskServiceImpl.class;
	}

	@Override
	public void getAttrs(Element element) {
		this.path = element.attributeValue("path");
		this.exceed = Integer.valueOf(element.attributeValue("exceed"));
		this.inodeExceed=Integer.valueOf(element.attributeValue("inodeExceed"));
	}

	@Override
	public Element setAttrs(Element element) {
		element.addAttribute("path",path);
		element.addAttribute("exceed",exceed.toString());
		element.addAttribute("inodeExceed",inodeExceed.toString());
		return element;
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
