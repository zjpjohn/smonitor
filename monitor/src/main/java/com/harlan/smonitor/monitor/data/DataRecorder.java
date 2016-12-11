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
     * @param id
     * @param value
     */
    public static void record(String id,String value){
        logger.info("{},{}",id,value);
    }
}
