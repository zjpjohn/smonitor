package com.harlan.smonitor.monitor.bean.host;


import com.harlan.smonitor.api.password.IPasswdService;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckCPU;
import com.harlan.smonitor.monitor.bean.host.check.CheckDisk;
import com.harlan.smonitor.monitor.bean.host.check.CheckFile;
import com.harlan.smonitor.monitor.bean.host.check.CheckMem;
import com.harlan.smonitor.monitor.core.init.ImplRegister;

import java.util.Map;

/**
 * HostMonitorItem
 * Created by harlan on 2016/8/3.
 */
public class HostMonitorItem extends MonitorItem {

    private String ip;
    private String user;
    private String passwd;
    private String passwdType;
    private Integer port;

    @Override
    protected Map<String,Object> setProps(Map<String,Object> itemMap) {
        itemMap.put("ip",ip);
        itemMap.put("user",user);
        itemMap.put("port",port);
        itemMap.put("passwd",passwd);
        itemMap.put("passwdType",passwdType);
        return itemMap;
    }

    @Override
    protected void getProps(Map<String, Object> itemMap) {
        this.ip = itemMap.get("ip").toString();
        this.user = itemMap.get("user").toString();
        if(itemMap.get("passwd-type")==null){
            this.passwd = itemMap.get("passwd").toString();
        }else{
            this.passwdType=itemMap.get("passwd-type").toString();
            IPasswdService passwdService= ImplRegister.getPasswdServiceImpl(this.passwdType);
            this.passwd =passwdService.getPassword(user);

        }
        this.port = Integer.valueOf(itemMap.get("port").toString());
    }

    @Override
    protected CheckItem createCheck(Map<String,Object> checkMap) {
        String type=checkMap.get("type").toString();
        if("file".equalsIgnoreCase(type)){
            return new CheckFile(checkMap);
        }else if("cpu".equalsIgnoreCase(type)){
            return new CheckCPU(checkMap);
        }else if("mem".equalsIgnoreCase(type)){
            return new CheckMem(checkMap);
        }else if("disk".equalsIgnoreCase(type)){
            return new CheckDisk(checkMap);
        }
        return null;
    }



    public String getIp() {
        return ip;
    }

    public String getUser() {
        return user;
    }

    public String getPasswd() {
        return passwd;
    }

    public Integer getPort() {
		return port;
	}
}
