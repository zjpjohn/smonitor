package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckFileServiceImpl;
import org.quartz.Job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.harlan.smonitor.monitor.common.Util.notNull;

/**
 * CheckFile
 * Created by harlan on 2016/9/21.
 */
public class CheckFile extends CheckItem {
    /**
     * 行数增长检查，记录上次行数
     */
    private static Map<Integer, Long> idRowsMap;
    static{
        idRowsMap=new HashMap<Integer, Long>();
    }

    /**
     * 需要监控的文件绝对路径
     */
    private String path;

    /**
     * 在多少分钟之内修改了
     */
    private Integer modifyIn;

    /**
     * 在多少分钟结束后还没有修改
     */
    private Integer notModifyIn;

    /**
     * 指定文件在两次检查之间内容增长的行数
     */
    private Integer rowsIncrease;

    public void getAttrs(Map<String,Object> checkMap) {
        this.path=checkMap.get("path").toString();

        if(notNull(checkMap.get("modifyIn"))){
            this.modifyIn=Integer.valueOf(checkMap.get("modifyIn").toString());
        }
        if(notNull(checkMap.get("rowsIncrease"))){
            this.rowsIncrease=Integer.valueOf(checkMap.get("rowsIncrease").toString());
        }
        if(notNull(checkMap.get("notModifyIn"))){
            this.notModifyIn=Integer.valueOf(checkMap.get("notModifyIn").toString());
        }
    }

    @Override
    public List<FieldDeclare> getFields() {
        List<FieldDeclare> fieldList=new ArrayList<FieldDeclare>();
        fieldList.add(new FieldDeclare("path","文件路径","不可空，绝对路径"));
        fieldList.add(new FieldDeclare("modifyIn","几分钟内修改","可空"));
        fieldList.add(new FieldDeclare("notModifyIn","几分钟内未修改","可空"));
        fieldList.add(new FieldDeclare("rowsIncrease","新增行数","可空"));
        return fieldList;
    }

    @Override
    public Map<String,Object> setAttrs(Map<String,Object> checkMap) {
        checkMap.put("path",path);
        checkMap.put("modifyIn",modifyIn);
        checkMap.put("notModifyIn",notModifyIn);
        checkMap.put("rowsIncrease",rowsIncrease);
        return checkMap;
    }

    public void setRows(Long rowsNum){
        synchronized (this){
            idRowsMap.put(id,rowsNum);
        }
    }
    public Long getRows(){
        return idRowsMap.get(id);
    }
    public String getPath() {
		return path;
	}
	public Integer getModifyIn() {
		return modifyIn;
	}
	public Integer getNotModifyIn() {
		return notModifyIn;
	}
	public Integer getRowsIncrease() {
		return rowsIncrease;
	}
	@Override
    public Class<? extends Job> getJobServiceImpl() {
        return CheckFileServiceImpl.class;
    }


}
