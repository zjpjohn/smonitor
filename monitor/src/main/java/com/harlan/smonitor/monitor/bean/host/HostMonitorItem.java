package com.harlan.smonitor.monitor.bean.host;


import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.impl.TypeDeclare;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckCPU;
import com.harlan.smonitor.monitor.bean.host.check.CheckDisk;
import com.harlan.smonitor.monitor.bean.host.check.CheckFile;
import com.harlan.smonitor.monitor.bean.host.check.CheckMem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.harlan.smonitor.monitor.common.Util.isNull;

/**
 * HostMonitorItem
 * Created by harlan on 2016/8/3.
 */
public class HostMonitorItem extends MonitorItem {
    private String ip;
    private String user;
    private String passwd;
//    private String encryptPwd;
//    private String passwdType;
    //端口默认是22
    private final static int DEFAULT_SSH_PORT = 22 ;
    private Integer port;

    @Override
    protected Map<String,Object> setProps(Map<String,Object> itemMap)throws Exception {
        itemMap.put("ip",ip);
        itemMap.put("user",user);
        itemMap.put("port",port);
        //密码不能放在map里，也就不能查看和存在文件中
        itemMap.put("passwd",passwd);
//        itemMap.put("encryptPwd",encryptPwd);
//        itemMap.put("passwdType",passwdType);
        return itemMap;
    }

    @Override
    protected void getProps(Map<String, Object> itemMap) throws Exception {
        this.ip = itemMap.get("ip").toString();
        this.user = itemMap.get("user").toString();
//        this.encryptPwd = isNull(itemMap.get("encryptPwd"))?null:itemMap.get("encryptPwd").toString();
        this.passwd = itemMap.get("passwd").toString();

        this.port =isNull(itemMap.get("port"))?DEFAULT_SSH_PORT:Integer.valueOf(itemMap.get("port").toString());
//        if(isNull(encryptPwd) && notNull(passwd) && !"******".equals(passwd)){
//            //新增时，页面传过来的map，初始化host对象
//            this.encryptPwd= AESUtil.encrypt(passwd,KEY);
//        }
//        if(notNull(encryptPwd) && "******".equals(passwd)){
//            //文件启动初始化时，密码需要解密
//            this.passwd=AESUtil.decrypt(encryptPwd,KEY);
//        }
//        if(itemMap.get("passwd-type")!=null){
//            this.passwdType=itemMap.get("passwd-type").toString();
//            IPasswdService passwdService= ModuleRegister.getPasswdServiceImpl(this.passwdType);
//            this.passwd =passwdService.getPassword(user);
//        }
    }

    @Override
    public List<FieldDeclare> getFields() {
        List<FieldDeclare> fieldList=new ArrayList<FieldDeclare>();
        fieldList.add(new FieldDeclare("ip","IP地址","IP地址"));
        fieldList.add(new FieldDeclare("user","用户名","主机用户名"));
        fieldList.add(new FieldDeclare("passwd","密码","用户名密码"));
//        fieldList.add(new FieldDeclare("passwdType","密码类型","制定使用哪个密码加密模块"));
        fieldList.add(new FieldDeclare("port","ssh端口","可空，默认22"));
        return fieldList;
    }

    @Override
    public Map<String, TypeDeclare> getCheckTypeMap() {
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
