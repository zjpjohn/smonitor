package com.harlan.smonitor.monitor.core.init;

import com.harlan.smonitor.monitor.data.DataFileOperator;
import com.harlan.smonitor.monitor.data.dao.JobDao;
import com.harlan.smonitor.monitor.data.dao.MonitorDao;
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
            ModuleRegister.initRegister();

            logger.info("monitor 数据加载 ...");

            DataFileOperator.readAdmin();

            DataFileOperator.readMonitorItem();

            DataFileOperator.readGorup();

            logger.info("monitor 数据加载完毕");

            JobDao.start();
        } catch (Exception e) {
            logger.error("初始化过程中出现异常，启动失败", e);
        }
    }

}
