package com.harlan.smonitor.monitor.bean.http.check;

import java.util.LinkedList;
import java.util.List;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.http.CheckBodyServiceImpl;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.Job;

public class CheckBody extends CheckItem {

	private List<String> contains;
	
	private List<String> exclude;
	
	public CheckBody(Element checkElement) {
		super(checkElement);
	}

	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckBodyServiceImpl.class;
	}

	@Override
	public void getAttrs(Element element) {
		Element containsElementList = element.element("contains");
		contains = new LinkedList<String>();
		for (Object keywordObject : containsElementList.elements()) {
			Element keywordElement = (Element) keywordObject;
			String keyword = keywordElement.getTextTrim();
			contains.add(keyword);
		}
		Element excludeElementList =  element.element("exclude");
		exclude = new LinkedList<String>();
		for (Object keywordObject : excludeElementList.elements()) {
			Element keywordElement = (Element) keywordObject;
			String keyword =keywordElement.getTextTrim();
			exclude.add(keyword);
		}
	}

	@Override
	public Element setAttrs(Element element) {
		Element contains_element= DocumentHelper.createElement("contains");
		for (String keyword:contains) {
			Element keyword_element= DocumentHelper.createElement("keyword");
			keyword_element.setText(keyword);
			contains_element.add(keyword_element);
		}
		element.add(contains_element);
		Element exclude_element= DocumentHelper.createElement("exclude");
		for (String keyword:exclude) {
			Element keyword_element= DocumentHelper.createElement("keyword");
			keyword_element.setText(keyword);
			exclude_element.add(keyword_element);
		}
		element.add(exclude_element);
		return element;
	}

	public List<String> getContains() {
		return contains;
	}
	public List<String> getExclude() {
		return exclude;
	}
}
