package com.harlan.smonitor.monitor.core.init;

import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.ServletContextEvent;
import java.util.ResourceBundle;

/**
 * Log4jInitListener
 * 初始化几个变量,配置在log4j中
 * Created by harlan on 2016/12/7.
 */
public class Log4jInitListener extends Log4jConfigListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        String data_dir;
        String log_dir;
        try {
            data_dir = ResourceBundle.getBundle("config").getString("data-dir");
            System.setProperty("SMONITOR_DATA_DIR",data_dir);
            System.out.println("Log4jInitListener:data-dir="+data_dir);
        } catch (Exception e) {
            System.out.println("Log4jInitListener:未配置data-dir...");
        }
        try {
            log_dir = ResourceBundle.getBundle("config").getString("log-dir");
            System.setProperty("SMONITOR_LOG_DIR",log_dir);
            System.out.println("Log4jInitListener:log-dir="+log_dir);
        } catch (Exception e) {
            System.out.println("Log4jInitListener:未配置log-dir...");
        }
        super.contextInitialized(event);
    }


}
