package com.harlan.smonitor.monitor.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by harlan on 2016/9/22.
 * 记录监控过程数据
 */
public class DataRecorder {
    private final static Logger logger = LoggerFactory.getLogger("com.harlan.smonitor.record");

    /**
     * 把每次检查项的关键信息记录到单独的文件中，方便以后对数据进行入库统计和分析
     * @param monitorType 监控项的类型
     * @param checkType 检查项类型
     * @param values 每个业务需要记录的业务点
     */
    public static void record(String monitorType,String checkType,String... values){
        if(logger.isInfoEnabled()){
            StringBuffer valueString=new StringBuffer();
            for (String str:values) {
                valueString.append(str).append(",");
            }
            logger.info("{},{},{}",monitorType,checkType,valueString.substring(0,valueString.length()-1));
        }
    }
}
