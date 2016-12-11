package com.harlan.smonitor.monitor.bean;

import com.harlan.smonitor.monitor.bean.host.HostMonitorItem;
import com.harlan.smonitor.monitor.bean.http.HttpMonitorItem;
import com.harlan.smonitor.monitor.bean.inspection.InspectionMonitorItem;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 监控项
 * 监控项的概念是指能够抽象成一个连接的东西：
 * 如：一个主机的某个帐号可以检查的所有东西，如：文件、内存、cpu等等
 * 如：数据库oracle的一次连接，mongodb的一次连接
 * @author harlan
 */
public abstract class MonitorItem{
    private final static Logger logger = LoggerFactory.getLogger(MonitorItem.class);

    /**
     * 监控项描述
     */
    protected String name;
    /**
     * 类型
     */
    protected String type;

    /**
     * 类型
     */
    protected Integer groupId;

    /**
     * 通知对象
     */
    protected List<CheckItem> checkList;

    /**
     * xml对象转化成bean，需要调用构造方法
     * @param itemElement xml节点
     */
    public MonitorItem(Element itemElement) {
        logger.debug("初始化公共参数...");
        name =itemElement.attributeValue("name");
        type =itemElement.attributeValue("type");
        groupId=Integer.valueOf(itemElement.attributeValue("groupId"));

        Element check_list_element=itemElement.element("check_list");

        Element props_element=itemElement.element("props");

        getProps(props_element);//实现类要把属性设置到字段中

        Iterator check_elements= check_list_element.elementIterator("check");
        checkList=new ArrayList<CheckItem>();
        while(check_elements.hasNext()){
            Element check_element= (Element) check_elements.next();
            CheckItem checkItem=createCheck(check_element);
            checkList.add(checkItem);
        }
        logger.info("name={}的监控项，共配置了{}个检查项",name,checkList.size());
    }

    /**
     * 各个监控项实现类需要实现checkItem的工厂方法
     * @param checkElement check节点
     * @return CheckItem对象
     */
    protected abstract CheckItem createCheck(Element checkElement);

    /**
     * xml对象转化为bean时
     * 各个监控项实现类从element对象中取出个性属性
     * @param propElement 个性字段xml节点
     * @return
     */
    protected abstract void getProps(Element propElement);

    /**
     * bean转化成xml对象时
     * 个监控项实现类从需要将个性字段放入propElement
     * @return
     */
    protected abstract Element setProps(Element propElement);

    public String getName() {
        return name;
    }

    public List<CheckItem> getCheckList() {
        return checkList;
    }

    /**
     * bean转化成xml对象
     * @return
     */
    public Element createElement(){
        Element monitor= DocumentHelper.createElement("monitor");
        monitor.addAttribute("name",name);
        monitor.addAttribute("type",type);
        monitor.addAttribute("groupId",groupId.toString());
        Element propsElement= DocumentHelper.createElement("props");
        setProps(propsElement);
        monitor.add(propsElement);
        Element check_list_element= DocumentHelper.createElement("check_list");
        for (CheckItem checkItem: checkList) {
            check_list_element.add(checkItem.createElement());
        }
        monitor.add(check_list_element);
        return monitor;
    }

    public static MonitorItem createMonitor(Element itemElement){
        String type=itemElement.attributeValue("type");
        logger.debug("需要实例化的监控项 type:{}",type);
        if("host".equalsIgnoreCase(type)){
            return new HostMonitorItem(itemElement);
        }else if("http".equalsIgnoreCase(type)){
            return new HttpMonitorItem(itemElement);
        }else if("inspection".equals(type)){
            return new InspectionMonitorItem(itemElement);
        }{
            throw new RuntimeException("Monitor.createMonitor()方法中，没有配置该类型监控项:"+type);
        }
    }

    public static  boolean checkTime(String default_run_time){
        String[] checkTimeArray = default_run_time.split("\\s+");
        if (checkTimeArray.length != 6 && checkTimeArray.length != 7) {
            return false;
        }
        return true;
    }
}