package com.harlan.smonitor.monitor.bean.host;


import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.impl.TypeDeclare;
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
    protected Map<String, TypeDeclare> getCheckTypeMap() {
        Map<String,TypeDeclare> CHECK_MAP=new HashMap<String,TypeDeclare>();
        TypeDeclare file=new TypeDeclare();
        file.setTypeValue("file");
        file.setName("文件");
        file.setDesc("可监控主机文件修改时间、总行数两个属性");
        file.setBeanClass(CheckFile.class);
        CHECK_MAP.put(file.getTypeValue(),file);

        TypeDeclare cpu=new TypeDeclare();
        cpu.setTypeValue("cpu");
        cpu.setName("cpu使用率");
        cpu.setDesc("可监控主机cpu使用率");
        cpu.setBeanClass(CheckCPU.class);
        CHECK_MAP.put(cpu.getTypeValue(),cpu);

        TypeDeclare mem=new TypeDeclare();
        mem.setTypeValue("mem");
        mem.setName("内存使用率");
        mem.setDesc("可监控主机内存使用率");
        mem.setBeanClass(CheckMem.class);
        CHECK_MAP.put(mem.getTypeValue(),mem);

        TypeDeclare disk=new TypeDeclare();
        disk.setTypeValue("disk");
        disk.setName("磁盘使用率");
        disk.setDesc("可监控主机磁盘使用率、inode使用率");
        disk.setBeanClass(CheckDisk.class);
        CHECK_MAP.put(disk.getTypeValue(),disk);
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
