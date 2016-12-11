package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckFileServiceImpl;
import org.dom4j.Element;
import org.quartz.Job;
/**
 * Created by harlan on 2016/9/21.
 */
public class CheckFile extends CheckItem {

    public CheckFile(Element checkElement) {
        super(checkElement);
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

    @Override
    public void getAttrs(Element checkElement) {
        // 补充检查文件 这个检查项的解析逻辑
        this.path = checkElement.elementText("path");
        String mi = checkElement.elementText( "modify-in");
        this.modifyIn = (mi == null ? null : Integer.valueOf(mi));
        String nmi = checkElement.elementText( "not-modify-in");
        this.notModifyIn = (nmi == null ? null : Integer.valueOf(nmi));
        String ri = checkElement.elementText( "rows-increase");
        this.rowsIncrease = (ri == null ? null : Integer.valueOf(ri));
    }

    @Override
    public Element setAttrs(Element element) {
        return null;
    }
}
