package com.harlan.smonitor.monitor.core.connection;

import com.harlan.smonitor.monitor.common.SshConnecter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SshPool
 * Created by harlan on 2017/1/9.
 */
public class SshPool {
    private final static Logger logger = LoggerFactory.getLogger(SshPool.class);
    private static Map<String,List<SshConnecter>> HOST_SSH_MAP = new HashMap<String,List<SshConnecter>>();
    static{
        HOST_SSH_MAP=new HashMap<String, List<SshConnecter>>();
    }
    /**
     * 取出连接
     * @return
     */
    public static SshConnecter getSsh(String host, int port, String username, String password) throws Exception {
        SshConnecter ssh = new SshConnecter(host,port, username,password);
        return ssh;
    }
}
