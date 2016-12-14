package com.harlan.smonitor.monitor.core.init;

import com.harlan.smonitor.api.impl.Implementor;
import com.harlan.smonitor.api.notice.INoticeService;
import com.harlan.smonitor.api.password.IPasswdService;
import com.harlan.smonitor.monitor.web.cache.PageCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 实现类注册中心，模块化的实现逻辑需要在此注册
 * Created by harlan on 2016/11/12.
 */
public class ImplRegister {
    private final static Logger logger = LoggerFactory.getLogger(ImplRegister.class);

    private static List<String> impl_classes=new ArrayList<String>();
    /**
     * 配置各个模块实现类的路径，如果不使用，在pom.xml中注释即可，不需要注释掉此处
     * */
    static{
        //四川短信下发通知方式
        impl_classes.add("com.harlan.smonitor.notice.scsms.ScsmsNoticeService");

        //邮件发送通知方式
        impl_classes.add("com.harlan.smonitor.notice.email.EmailNoticeService");

        //密码获取模块-四川
        impl_classes.add("com.harlan.smonitor.password.scmobile.ScmobilePasswdService");
    }

    private static HashMap<String,INoticeService> noticeImplMap;
    private static HashMap<String,IPasswdService> passwdImplMap;

    public static void initRegister() {
        noticeImplMap=new HashMap<String, INoticeService>();
        passwdImplMap=new HashMap<String, IPasswdService>();
        for (String className:impl_classes) {
            try {
                try {
                    newInstanceClass(className);
                } catch (NoSuchFieldException e) {
                    logger.error("实现类没有TYPE属性："+className);
                    break;
                } catch (IllegalAccessException e) {
                    logger.error("实现类没有可访问的TYPE属性："+className);
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
    private static void newInstanceClass(String className) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        Class<?> implClass= Class.forName(className);
//        Field field=  implClass.getDeclaredField("TYPE");
//        String impl_type= (String) field.get(null);
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
