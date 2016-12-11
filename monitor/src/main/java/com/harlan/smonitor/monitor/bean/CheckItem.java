package com.harlan.smonitor.monitor.bean;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.TriggerKey;


/**
 * 检查项
 * 每个检查项是一个quertz 的job
 * 不同的检查项需要实现此父类
 */
public abstract class CheckItem {

    public CheckItem(Element checkElement) {
        cronExpression=checkElement.attributeValue("cronExpression");
        name=checkElement.attributeValue("name");
        type=checkElement.attributeValue("type");
        alarmTimes=Integer.valueOf(checkElement.attributeValue("alarmTimes"));

        Element adminElementList = checkElement.element("admin");
        adminList = new LinkedList<String>();
        for (Object adminIdObject : adminElementList.elements()) {
            Element adminidElement = (Element) adminIdObject;
            adminList.add(adminidElement.getTextTrim());
        }
        Element attrsElement = checkElement.element("attrs");
        getAttrs(attrsElement);
    }
    protected Integer id;
    protected TriggerKey triggerKey;
    protected JobKey jobKey;
    /**
     * 检查间隔，单位：分
     */
    protected String cronExpression;
    
    /**
     * 检查项的描述 
     */
    protected String name;
    
    /**
     * 检查项的类型 
     */
    protected String type;
    
    /**
     * 不符合预期状态达到多少次之后报警
     */
    protected Integer alarmTimes;
    
    /**
     * 通知的管理员
     */
    protected List<String> adminList;
    
    public abstract Class<? extends Job> getJobServiceImpl();


    public Integer getId() {
        return id;
    }

    public TriggerKey getTriggerKey() {
        return triggerKey;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTriggerKey(TriggerKey triggerKey) {
        this.triggerKey = triggerKey;
    }

    public String getCronExpression() {
        return cronExpression;
    }
    public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getName() {
		return name;
	}

	public Integer getAlarmTimes() {
		return alarmTimes;
	}
    
	public String getType() {
		return type;
	}
	public List<String> getAdminList() {
		return adminList;
	}

    public JobKey getJobKey() {
        return jobKey;
    }

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    /**
     * bean转化成xml对象时
     * 需要将check转化成 xml元素
     * @return
     */
    public Element createElement() {
        Element check= DocumentHelper.createElement("check");
        check.addAttribute("name",name);
        check.addAttribute("type",type);
        check.addAttribute("alarmTimes",alarmTimes.toString());
        check.addAttribute("cronExpression",cronExpression);
        Element admin_element= DocumentHelper.createElement("admin");
        for (String adminId: adminList) {
            Element id=  DocumentHelper.createElement("id");
            id.setText(adminId);
            admin_element.add(id);
        }
        check.add(admin_element);
        Element attrElement= DocumentHelper.createElement("attrs");
        attrElement= setAttrs(attrElement);
        check.add(attrElement);
        return check;
    }

    /**
     * bean转化成xml元素时，各个check实现类
     * @return
     */
    public abstract void getAttrs(Element element);

    /**
     * bean转化成xml元素时
     * 需要各个实现类，需要将个性字段创建成xml元素
     */
    public abstract Element setAttrs(Element element);
}
