package com.harlan.smonitor.monitor.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harlan.smonitor.api.impl.FieldDeclare;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.TriggerKey;


/**
 * 检查项
 * 每个检查项是一个quertz 的job
 * 不同的检查项需要实现此父类
 */
public abstract class CheckItem {
    public CheckItem() {
        state=0;
    }

    @SuppressWarnings("unchecked")
    public void init(Map<String,Object> checkMap) {
        cronExpression=checkMap.get("cronExpression").toString();
        name=checkMap.get("name").toString();
        state=Integer.valueOf(checkMap.get("state").toString());
        type=checkMap.get("type").toString();
        if(checkMap.get("alarmTimes")!=null){
            alarmTimes=Integer.valueOf(checkMap.get("alarmTimes").toString());
        }
        adminList = (List<Integer>) checkMap.get("adminList");
        getAttrs(checkMap);
    }

    /**
     * bean转化成 map 对象
     * 需要将check转化成 map
     * @return
     */
    public Map<String,Object> createMap() {
        Map<String,Object> check_map=new HashMap<String,Object>();
        check_map.put("name",name);
        check_map.put("type",type);
        check_map.put("state",state);
        check_map.put("alarmTimes",alarmTimes.toString());
        check_map.put("cronExpression",cronExpression);
        check_map.put("adminList",adminList);
        check_map= setAttrs(check_map);
        return check_map;
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
     * checkItem的状态：执行/暂停
     */
    protected Integer state;
    /**
     * 不符合预期状态达到多少次之后报警
     */
    protected Integer alarmTimes;
    
    /**
     * 通知的管理员
     */
    protected List<Integer> adminList;
    
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
	public String getName() {
		return name;
	}

	public Integer getAlarmTimes() {
		return alarmTimes;
	}
    
	public String getType() {
		return type;
	}
	public List<Integer> getAdminList() {
		return adminList;
	}

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }


    /**
     * bean转化成xml元素时
     * 需要各个实现类，需要将个性字段创建成xml元素
     */
    public abstract Map<String,Object> setAttrs(Map<String,Object> attrMap);

    public abstract void getAttrs(Map<String,Object> attrMap);

    public abstract List<FieldDeclare> getFields();
}
