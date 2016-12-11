package com.harlan.smonitor.monitor.core.init;

import com.harlan.smonitor.monitor.data.DataOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 程序启动加载...
 * 程序初始化是从 init 方法开始的
 */
@Component
public class MonitorInitServlet implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(MonitorInitServlet.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("monitor 启动 ...");
        try {
            logger.info("monitor 模块加载 ...");
            ImplRegister.initRegister();

            logger.info("monitor 数据加载 ...");

            DataOperator.readAdmin();

            DataOperator.readMonitorItem();

            DataOperator.readGorup();

            logger.info("monitor 数据加载完毕");

//            for(String path:admin_xml_file_list){
//                logger.info("管理员 配置文件有:{}",path);
//            }
//            if(item_xml_file_list.size()==0){
//                throw new RuntimeException("item 配置文件数量为0");
//            }
//            if(admin_xml_file_list.size()==0){
//                throw new RuntimeException("admin 配置文件数量为0");
//            }
//
//            //一定要先加载 admin，因为加载 item时回去查询 admin是否存在
//            ConfigReader.initAdmin(admin_xml_file_list);
//
//            List<MonitorItem> monitorItemList =ConfigReader.initMonitorItem(item_xml_file_list);
            //初始化ssh链接池
//            SSHSource.initSSHSource(monitorItemList);

//            int size = SSHSource.getSSHConnSize();
//            if(size < 1){
//            	logger.warn("连接池没有可用链接");
//            }
            //TODO 修改连接池架构

//            TaskService.startTask(monitorItemList);
        } catch (Exception e) {
            logger.error("初始化过程中出现异常，启动失败", e);
        }
    }

}
