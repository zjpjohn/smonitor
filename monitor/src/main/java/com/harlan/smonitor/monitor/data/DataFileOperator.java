package com.harlan.smonitor.monitor.data;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.monitor.bean.Group;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.common.FileUtil;
import com.harlan.smonitor.monitor.common.Util;
import com.harlan.smonitor.monitor.core.init.ImplRegister;
import com.harlan.smonitor.monitor.data.dao.AdminDao;
import com.harlan.smonitor.monitor.data.dao.GroupDao;
import com.harlan.smonitor.monitor.data.dao.MonitorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 数据接口
 * Created by harlan on 2016/12/1.
 */
public class DataFileOperator {
    private final static Logger logger = LoggerFactory.getLogger(DataFileOperator.class);
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
                data_dir = DataFileOperator.class.getClassLoader().getResource(".").getPath();
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

            GROUP_FILE=data_dir+ "/group.json";
            ADMIN_FILE=data_dir+ "/admin.json";
            ITEM_FILE=data_dir+ "/monitor.json";
        } catch (Exception e) {
            logger.error("配置文件路径初始化出现异常",e);
        }
    }


    @SuppressWarnings("unchecked")
    public static void readMonitorItem() throws Exception{
        File itemfile=new File(ITEM_FILE);
        if(!itemfile.exists()){
            saveMonitor(new LinkedList<MonitorItem>());
        }
        String data_json=FileUtil.read(ITEM_FILE);

        List<Map> jsonArrayData=JSON.parseArray(data_json,Map.class);
        for (Map map:jsonArrayData) {
            MonitorItem monitor=MonitorItem.createMonitor(map);
            MonitorDao.addMonitor(monitor);
        }
        logger.info("monitor 监控项加载完毕，加载{}个监控项",MonitorDao.count());
    }

    public static synchronized void saveMonitor(List<MonitorItem> item_list) throws IOException {
        JSONArray item_json_array=new JSONArray();
        for (MonitorItem item : item_list) {
            Map<String,Object> admin_map= item.createMap();
            item_json_array.add(admin_map);
        }
        String json_string= JSON.toJSONString(item_json_array);
        logger.debug("保存item数据：{}",json_string);
        FileUtil.rewrite(ITEM_FILE,json_string);
    }
    @SuppressWarnings("unchecked")
    public static void readAdmin() throws Exception {
        File adminfile=new File(ADMIN_FILE);
        List<Admin> admin_list;
        if(!adminfile.exists()){
            admin_list=new LinkedList<Admin>();
            saveAdmin(admin_list);
        }
        String data_json=FileUtil.read(ADMIN_FILE);
        List<Map> jsonArrayData=JSON.parseArray(data_json,Map.class);
        for (Map map:jsonArrayData) {
            String  type=map.get("type").toString();
            AdminDao.addAdmin(ImplRegister.getNoticeServiceImpl(type).getAdminFrom(map));
        }
        logger.info("monitor 管理员加载完毕，加载{}个管理员",AdminDao.count());
    }

    public static synchronized void saveAdmin(List<Admin> admin_list) throws IOException {
        JSONArray admin_json_array=new JSONArray();
        for (Admin admin : admin_list) {
            Map<String,Object> admin_map= admin.createMap();
            admin_json_array.add(admin_map);
        }
        String json_string= JSON.toJSONString(admin_json_array);
        logger.debug("保存admin数据：{}",json_string);
        FileUtil.rewrite(ADMIN_FILE,json_string);
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
        String data_json=FileUtil.read(GROUP_FILE);
        List<Group> jsonArrayData=JSON.parseArray(data_json,Group.class);
        for (Group group:jsonArrayData) {
            GroupDao.addGroup(group);
        }
        logger.info("monitor 分类数据加载完毕，加载{}个分类",GroupDao.count());
    }

    public static synchronized void saveGroup(List<Group> groupList) throws IOException {
        String json_string= JSON.toJSONString(groupList);
        logger.debug("保存group数据：{}",json_string);
        FileUtil.rewrite(GROUP_FILE,json_string);
    }

}
