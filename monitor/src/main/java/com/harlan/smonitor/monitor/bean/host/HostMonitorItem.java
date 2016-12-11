package com.harlan.smonitor.monitor.bean.host;


import com.harlan.smonitor.api.password.IPasswdService;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.bean.host.check.CheckCPU;
import com.harlan.smonitor.monitor.bean.host.check.CheckDisk;
import com.harlan.smonitor.monitor.bean.host.check.CheckFile;
import com.harlan.smonitor.monitor.bean.host.check.CheckMem;
import com.harlan.smonitor.monitor.common.Util;
import com.harlan.smonitor.monitor.core.init.ImplRegister;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by harlan on 2016/8/3.
 */
public class HostMonitorItem extends MonitorItem {
    private final static Logger logger = LoggerFactory.getLogger(HostMonitorItem.class);

    private String ip;
    private String user;
    private String passwd;
    private Integer port;

    public HostMonitorItem(Element itemElement) {
        super(itemElement);
    }

    @Override
    protected CheckItem createCheck(Element checkElement) {
        String type=checkElement.attributeValue("type");
        if("file".equalsIgnoreCase(type)){
            return new CheckFile(checkElement);
        }else if("cpu".equalsIgnoreCase(type)){
            return new CheckCPU(checkElement);
        }else if("mem".equalsIgnoreCase(type)){
            return new CheckMem(checkElement);
        }else if("disk".equalsIgnoreCase(type)){
            return new CheckDisk(checkElement);
        }
        return null;
    }

    @Override
    protected void getProps(Element propElement) {
        this.ip = propElement.attributeValue("ip");
        this.user = propElement.attributeValue("user");
        String passwd_type=propElement.attributeValue("passwd-type");
        if(Util.isNull(passwd_type)){//如果没有配置 passwd_type ，就直接取字段 passwd
            this.passwd = propElement.attributeValue("passwd");
        }else{
            IPasswdService passwdService= ImplRegister.getPasswdServiceImpl(passwd_type);
            this.passwd =passwdService.getPassword(user);
        }
        this.port = Integer.valueOf(propElement.attributeValue("port"));
    }

    @Override
    protected Element setProps(Element propElement) {
        propElement.addAttribute("ip",ip);
        propElement.addAttribute("user",user);
        propElement.addAttribute("ip",ip);
        return propElement;
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
