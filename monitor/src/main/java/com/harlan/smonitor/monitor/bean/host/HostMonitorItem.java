package com.harlan.smonitor.monitor.bean.host;


import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.password.IPasswdService;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckCPU;
import com.harlan.smonitor.monitor.bean.host.check.CheckDisk;
import com.harlan.smonitor.monitor.bean.host.check.CheckFile;
import com.harlan.smonitor.monitor.bean.host.check.CheckMem;
import com.harlan.smonitor.monitor.core.init.ModuleRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            IPasswdService passwdService= ModuleRegister.getPasswdServiceImpl(this.passwdType);
            this.passwd =passwdService.getPassword(user);

        }
        this.port = Integer.valueOf(itemMap.get("port").toString());
    }

    @Override
    public List<FieldDeclare> getFields() {
        List<FieldDeclare> fieldList=new ArrayList<FieldDeclare>();
        fieldList.add(new FieldDeclare("ip","IP地址","IP地址"));
        fieldList.add(new FieldDeclare("user","用户名","用户名"));
        fieldList.add(new FieldDeclare("passwd","密码","保存后会加密"));
        fieldList.add(new FieldDeclare("passwdType","密码类型","制定使用哪个密码加密模块"));
        fieldList.add(new FieldDeclare("port","ssh端口","ssh的端口"));
        return fieldList;
    }

    @Override
    protected Map<String, Class<?>> getCheckClassMap() {
        Map<String, Class<?>> CHECK_MAP=new HashMap<String, Class<?>>();
        CHECK_MAP.put("file",CheckFile.class);
        CHECK_MAP.put("cpu",CheckCPU.class);
        CHECK_MAP.put("mem",CheckMem.class);
        CHECK_MAP.put("disk",CheckDisk.class);
        return CHECK_MAP;
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
