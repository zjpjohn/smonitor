package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckFileServiceImpl;
import org.quartz.Job;

import java.util.Map;

/**
 * Created by harlan on 2016/9/21.
 */
public class CheckFile extends CheckItem {

    public CheckFile(Map<String,Object> checkMap) {
        super(checkMap);
        // 补充检查文件 这个检查项的解析逻辑
        if(checkMap.get("path")!=null){
            this.path=checkMap.get("path").toString();
        }
        if(checkMap.get("modifyIn")!=null){
            this.modifyIn=Integer.valueOf(checkMap.get("modifyIn").toString());
        }
        if(checkMap.get("rowsIncrease")!=null){
            this.rowsIncrease=Integer.valueOf(checkMap.get("rowsIncrease").toString());
        }
        if(checkMap.get("notModifyIn")!=null){
            this.notModifyIn=Integer.valueOf(checkMap.get("notModifyIn").toString());
        }
    }
    @Override
    public Map<String,Object> setAttrs(Map<String,Object> checkMap) {
        checkMap.put("path",path);
        checkMap.put("modifyIn",modifyIn);
        checkMap.put("notModifyIn",notModifyIn);
        checkMap.put("rowsIncrease",rowsIncrease);
        return checkMap;
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
