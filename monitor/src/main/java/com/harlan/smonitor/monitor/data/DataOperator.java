package com.harlan.smonitor.monitor.data;

import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.Group;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.common.FileUtil;
import com.harlan.smonitor.monitor.common.Util;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 数据接口
 * Created by harlan on 2016/12/1.
 */
public class DataOperator {
    private final static Logger logger = LoggerFactory.getLogger(DataOperator.class);
    private static String GROUP_FILE;
    private static String ADMIN_FILE;
    private static String ITEM_FILE;

    static {
        initDataFilePath();
    }
    /**
     * 确认数据存储文件夹路径
     * @return
     */
    private static void initDataFilePath() {
        String data_dir;
        try {
            data_dir=System.getProperty("SMONITOR_DATA_DIR");
            if(Util.isNull(data_dir)){
                data_dir = DataOperator.class.getClassLoader().getResource(".").getPath();
            }
            data_dir=data_dir+File.separator+"config";
            logger.info("data_dir={}", data_dir);
            File data_dir_file=new File(data_dir);
            if(!data_dir_file.exists()){
                data_dir_file.mkdir();
            }
            if(!data_dir_file.isDirectory()){
                throw new RuntimeException("配置的data-dir需要是一个目录");
            }

            GROUP_FILE=data_dir+ "/group.xml";
            ADMIN_FILE=data_dir+ "/admin.xml";
            ITEM_FILE=data_dir+ "/monitor.xml";
        } catch (Exception e) {
            logger.error("配置文件路径初始化出现异常",e);
        }
    }



    public static void readMonitorItem() throws Exception{
        File itemfile=new File(ITEM_FILE);
        List<MonitorItem> monitor_list=new LinkedList<MonitorItem>();
        if(!itemfile.exists()){
            saveItem(monitor_list);
        }
        String data_xml=FileUtil.read(ITEM_FILE);
        Document document = DocumentHelper.parseText(data_xml);
        Element root=document.getRootElement();
        Iterator element_it= root.elementIterator("monitor");
        while(element_it.hasNext()){
            Element monitoer_element=(Element)element_it.next();
            MonitorItem monitor=MonitorItem.createMonitor(monitoer_element);
            monitor_list.add(monitor);
            for (CheckItem checkItem:monitor.getCheckList()) {
                if(CachedData.MONITOR_INDEX < checkItem.getId()){
                    CachedData.ADMIN_INDEX=checkItem.getId();
                }

            }
        }
        CachedData.MONITOR_LIST=monitor_list;
        logger.info("monitor 监控项加载完毕，加载{}个监控项",CachedData.MONITOR_LIST.size());
    }

    public static synchronized void saveItem(List<MonitorItem> item_list) throws IOException {
        Element root = DocumentHelper.createElement("monitor_list");
        for (MonitorItem item : item_list) {
            root.add(item.createElement());
        }
        Document document = DocumentHelper.createDocument(root);
        String xml_string=document.asXML();
        logger.debug("保存item数据：{}",xml_string);
        FileUtil.rewrite(ITEM_FILE,xml_string);
    }

    public static void readAdmin() throws Exception {
        File adminfile=new File(ADMIN_FILE);
        List<Admin> admin_list;
        if(!adminfile.exists()){
            admin_list=new LinkedList<Admin>();
            saveAdmin(admin_list);
        }
        Map<Integer, Admin> adminMap=new HashMap<Integer, Admin>();
        String data_xml=FileUtil.read(ADMIN_FILE);
        Document document = DocumentHelper.parseText(data_xml);
        Element root=document.getRootElement();
        Iterator element_it= root.elementIterator("admin");
        while(element_it.hasNext()){
            Admin admin=new Admin((Element) element_it.next());

            adminMap.put(admin.getId(),admin);
            if(CachedData.ADMIN_INDEX < admin.getId()){
                CachedData.ADMIN_INDEX=admin.getId();
            }
        }
        CachedData.ADMIN_MAP=adminMap;
        logger.info("monitor 管理员加载完毕，加载{}个管理员",CachedData.ADMIN_MAP.size());

    }

    public static synchronized void saveAdmin(List<Admin> admin_list) throws IOException {
        Element root = DocumentHelper.createElement("admin_list");
        for (Admin admin : admin_list) {
            root.add(admin.createElement());
        }
        Document document = DocumentHelper.createDocument(root);
        String xml_string=document.asXML();
        logger.debug("保存admin数据：{}",xml_string);
        FileUtil.rewrite(ADMIN_FILE,xml_string);
    }

    public static void readGorup() throws Exception {

        File groupfile=new File(GROUP_FILE);
        if(!groupfile.exists()){
            List<Group> default_group=new ArrayList<Group>(1);
            Group group=new Group();
            group.setId(0);
            group.setName("默认分组");
            default_group.add(group);
            saveGroup(default_group);
        }
        String xml_json=FileUtil.read(GROUP_FILE);
        Document document = DocumentHelper.parseText(xml_json);
        Element root=document.getRootElement();
        Map<Integer,Group> groupMap=new HashMap<Integer, Group>();
        Iterator element_it= root.elementIterator("group");
        while(element_it.hasNext()){
            Group group=new Group((Element) element_it.next());
            groupMap.put(group.getId(),group);
        }
        CachedData.GROUP_MAP=groupMap;
        logger.info("monitor 分类数据加载完毕，加载{}个分类",CachedData.GROUP_MAP.size());

    }

    public static synchronized void saveGroup(List<Group> groupList) throws IOException {
        // 第一种方式：创建文档，并创建根元素
        // 创建文档:使用了一个Helper类

        // 创建根节点并添加进文档
        Element root = DocumentHelper.createElement("group_list");
        for (Group group : groupList) {
            root.add(group.createElement());
        }
        Document document = DocumentHelper.createDocument(root);
        String xml_string=document.asXML();
        logger.debug("保存group数据：{}",xml_string);
        FileUtil.rewrite(GROUP_FILE,xml_string);
    }

    public static int nextMonitorIndex(){

        return ++CachedData.MONITOR_INDEX;
    }
    public static int nextAdminIndex(){
        return ++CachedData.ADMIN_INDEX;
    }


    /**
     * 重置alamcount
     * @param checkItemId
     */
    public static void jobAlarmReset(Integer checkItemId){
        CachedData.JOB_ALARM_COUNT.put(checkItemId,0);
    }

    /**
     * alamCount加一
     * @param checkItemId
     * @return
     */
    public static int jobAlamIncrease(Integer checkItemId){
        Integer count=CachedData.JOB_ALARM_COUNT.get(checkItemId);
        if(count==null){
            count=1;
        }else{
            count++;
        }
        CachedData.JOB_ALARM_COUNT.put(checkItemId,count);
        return count;
    }


}
