package com.harlan.smonitor.monitor.core.init;

import com.harlan.smonitor.api.impl.Implementor;
import com.harlan.smonitor.api.notice.INoticeService;
import com.harlan.smonitor.api.password.IPasswdService;
import com.harlan.smonitor.monitor.common.FileUtil;
import com.harlan.smonitor.monitor.web.cache.PageCache;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 模块注册中心
 * 自定义的模块需要在 moudules.xml中注册并在 pom.xml中引用
 * Created by harlan on 2016/11/12.
 */
public class ModuleRegister {
    private final static Logger logger = LoggerFactory.getLogger(ModuleRegister.class);

    private static List<String> MODULE_CLASS=new ArrayList<String>();
    /**
     * 配置各个模块实现类的路径，如果不使用，在pom.xml中注释即可，不需要注释掉此处
     * */
    static{
        try {
            String moduleRegFile=System.getProperty("SMONITOR_WEB_INF")+ File.separator+"modules.xml";
            logger.debug("模块注册文件:{}",moduleRegFile);
            String data_xml= FileUtil.read(moduleRegFile);
            Document document = DocumentHelper.parseText(data_xml);
            Element root=document.getRootElement();
            Iterator element_it= root.elementIterator("module");
            while(element_it.hasNext()){
                Element module_element=(Element) element_it.next();
                MODULE_CLASS.add(module_element.getTextTrim());
            }
            logger.info("模块注册文件共有模块{}个",MODULE_CLASS.size());
        } catch (Exception e) {
            logger.error("模块加载异常",e);
        }
    }

    private static HashMap<String,INoticeService> noticeImplMap;
    private static HashMap<String,IPasswdService> passwdImplMap;

    public static void initRegister() {
        noticeImplMap=new HashMap<String, INoticeService>();
        passwdImplMap=new HashMap<String, IPasswdService>();
        for (String className:MODULE_CLASS) {
            try {
                try {
                    newInstanceClass(className);
                }  catch (IllegalAccessException e) {
                    logger.error("实例化异常："+className);
                    break;
                } catch (InstantiationException e)  {
                    logger.error("实例化异常："+className);
                    break;
                }
            } catch (ClassNotFoundException e) {
                logger.debug("跳过实例化模块：{}",className);
                continue;
            }
        }
    }

    /**
     * 如果实现类在类加载路径中，则初始化并放入map
     * @param className 类的路径
     */
    private static void newInstanceClass(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> implClass= Class.forName(className);
        Implementor obj= (Implementor) implClass.newInstance();
        logger.info("通知模块：实现类 {} ,类型为 {}",implClass,obj.getTypeDeclare().getTypeValue());
        if(obj instanceof INoticeService){
            noticeImplMap.put(obj.getTypeDeclare().getTypeValue(),(INoticeService)obj);
            PageCache.NOTICE_TYPES.add(obj.getTypeDeclare());
        }else if(obj instanceof IPasswdService){
            passwdImplMap.put(obj.getTypeDeclare().getTypeValue(),(IPasswdService) obj);
            PageCache.PASSWD_TYPES.add(obj.getTypeDeclare());
        }
    }

    public static INoticeService getNoticeServiceImpl(String type){
        if(!noticeImplMap.containsKey(type)){
            throw new RuntimeException("未发现"+type+"类型的通知模块，请检查admin配置文件与加载的通知模块是否对应");
        }
        return noticeImplMap.get(type);
    }

    public static IPasswdService getPasswdServiceImpl(String type){
        if(!passwdImplMap.containsKey(type)){
            throw new RuntimeException("未发现"+type+"类型的密码加密模块，请检查item配置文件中passwd-type与加载的通知模块是否对应");
        }
        return passwdImplMap.get(type);
    }
}
