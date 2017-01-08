package com.harlan.smonitor.monitor.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.monitor.common.Constants;
import org.quartz.Job;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 检查项
 * 每个检查项是一个quertz 的job
 * 不同的检查项需要实现此父类
 */
public abstract class CheckItem  implements Cloneable{
    private final static Logger logger = LoggerFactory.getLogger(MonitorItem.class);
    public CheckItem() {
        state= Constants.CHECK_PAUSE;//默认暂停状态
    }

    @SuppressWarnings("unchecked")
    public void init(Map<String,Object> checkMap) {
        if(checkMap.get("id")!=null){
            id=Integer.valueOf(checkMap.get("id").toString());
        }
        cronList= (List<String>) checkMap.get("cronList");
        name=checkMap.get("name").toString();
        if(checkMap.get("state")!=null){
            state=Integer.valueOf(checkMap.get("state").toString());
        }
        type=checkMap.get("type").toString();
        if(checkMap.get("alarmTime")!=null){
            alarmTime=Integer.valueOf(checkMap.get("alarmTime").toString());
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
        check_map.put("id",id);
        check_map.put("name",name);
        check_map.put("type",type);
        check_map.put("state",state);
        check_map.put("alarmTime",alarmTime.toString());
        check_map.put("cronList",cronList);
        check_map= setAttrs(check_map);
        return check_map;
    }
    protected Integer id;
//    protected List<TriggerKey> triggerKeys;
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
     * checkItem的状态：执行/暂停，0是执行，1是暂停
     */
    protected Integer state;
    /**
     * 不符合预期状态达到多少次之后报警
     */
    protected Integer alarmTime;
    
    public abstract Class<? extends Job> getJobServiceImpl();

    public Integer getState() {
        return state;
    }

    public Integer getId() {
        return id;
    }

    public List<String> getCronList() {
        return cronList;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

    public void setJobKey(JobKey jobKey) {
        this.jobKey = jobKey;
    }

    public JobKey getJobKey() {
        return jobKey;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * bean转化成xml元素时
     * 需要各个实现类，需要将个性字段创建成xml元素
     */
    public abstract Map<String,Object> setAttrs(Map<String,Object> attrMap);

    public abstract void getAttrs(Map<String,Object> attrMap);

    public void setCronList(List<String> cronList) {
        this.cronList = cronList;
    }

    public Integer getAlarmTime() {
        return alarmTime;
    }

    /**
     * 每个实现类，允许有自己特殊的字段，但该字段要在后台界面展示出来，并要填写值
     * 所以要用这个方法，把自定义字段的字段名称，意义展示出来
     * 这里的定义的字段，会展示在界面
     * 并且可以在 getAttrs 中取出，但目前只支持String
     * @return
     */
    public abstract List<FieldDeclare> getFields();

    @Override
    public CheckItem clone(){
        CheckItem copyCheck=null;
        try {
            copyCheck= (CheckItem) super.clone();
            List<String> copyCornList=new ArrayList<String>(copyCheck.getCronList().size());
            copyCornList.addAll(copyCheck.getCronList());
            copyCheck.setCronList(copyCornList);
        } catch (Exception e) {
            logger.error("colne exception:",e);
        }
        return copyCheck;
    }
}
