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
        cronList= (List<String>) checkMap.get("cronList");
        name=checkMap.get("name").toString();
        if(checkMap.get("state")!=null){
            state=Integer.valueOf(checkMap.get("state").toString());
        }
        type=checkMap.get("type").toString();
        if(checkMap.get("alarmTimes")!=null){
            alarmTimes=Integer.valueOf(checkMap.get("alarmTimes").toString());
        }
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
        check_map.put("cronList",cronList);
        check_map= setAttrs(check_map);
        return check_map;
    }
    protected Integer id;
    protected List<TriggerKey> triggerKeys;
    protected JobKey jobKey;
    /**
     * 检查间隔，单位：分
     */
    protected List<String> cronList;
    
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
    
    public abstract Class<? extends Job> getJobServiceImpl();


    public Integer getId() {
        return id;
    }

    public List<String> getCronList() {
        return cronList;
    }

    public void setCronList(List<String> cronList) {
        this.cronList = cronList;
    }

    public List<TriggerKey> getTriggerKeys() {
        return triggerKeys;
    }

    public void setTriggerKeys(List<TriggerKey> triggerKeys) {
        this.triggerKeys = triggerKeys;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    /**
     * bean转化成xml元素时
     * 需要各个实现类，需要将个性字段创建成xml元素
     */
    public abstract Map<String,Object> setAttrs(Map<String,Object> attrMap);

    public abstract void getAttrs(Map<String,Object> attrMap);

    /**
     * 每个实现类，允许有自己特殊的字段，但该字段要在后台界面展示出来，并要填写值
     * 所以要用这个方法，把自定义字段的字段名称，意义展示出来
     * @return
     */
    public abstract List<FieldDeclare> getFields();
}
