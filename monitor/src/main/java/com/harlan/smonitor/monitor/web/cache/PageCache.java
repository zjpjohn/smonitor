package com.harlan.smonitor.monitor.web.cache;

import com.harlan.smonitor.api.impl.TypeDeclare;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于页面展示数据
 * 目前只缓存了 模块的类型
 * Created by harlan on 2016/12/9.
 */
public class PageCache {

    static{
        NOTICE_TYPES=new ArrayList<TypeDeclare>();
        PASSWD_TYPES=new ArrayList<TypeDeclare>();
    }

    public static List<TypeDeclare> NOTICE_TYPES;
    public static List<TypeDeclare> PASSWD_TYPES;

}
