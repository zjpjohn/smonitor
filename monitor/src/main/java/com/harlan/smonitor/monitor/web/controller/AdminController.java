package com.harlan.smonitor.monitor.web.controller;

import com.alibaba.fastjson.JSON;
import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.monitor.common.Constants;
import com.harlan.smonitor.monitor.core.init.ModuleRegister;
import com.harlan.smonitor.monitor.data.dao.AdminDao;
import com.harlan.smonitor.monitor.web.cache.PageCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * AdminController
 * Created by harlan on 2016/12/6.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @RequestMapping(value="/list")
    public ModelAndView list(@RequestParam Map<String,Object> param) throws Exception {
        logger.debug("param：{}",param);
        Integer start=null,limit=null;
        if(param.get("paging_start")!=null){
            start=Integer.valueOf(param.get("paging_start").toString());
        }
        if(param.get("paging_limit")!=null){
            limit=Integer.valueOf(param.get("paging_limit").toString());
        }
        ModelAndView mv = new ModelAndView("/admin/list");
        mv.addObject("list", AdminDao.getAdminList(start,limit));
        mv.addObject("paging_count", AdminDao.getAdminList(null,null).size());
        mv.addObject("notice_types", PageCache.NOTICE_TYPES);
        mv.addObject("paging_start", start==null?0:start);
        mv.addAllObjects(param);
        return mv;
    }

    @RequestMapping(value="/add")
    public ModelAndView add() throws Exception {
        ModelAndView mv=new ModelAndView("/admin/add");
        mv.addObject("notice_types", PageCache.NOTICE_TYPES);
        return mv;
    }
    @RequestMapping(value="/detail")
    public ModelAndView detail(String id) throws Exception {
        ModelAndView mv=new ModelAndView("/admin/detail");
        Admin admin=AdminDao.getAdmin(id);
        mv.addObject("admin",JSON.toJSONString(admin.createMap()));
        mv.addObject("fields",JSON.toJSONString(admin.getFields()));
        mv.addObject("notice_types", PageCache.NOTICE_TYPES);
        return mv;
    }
    @RequestMapping(value="/delete",produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody   String delete(@RequestParam String admin) throws Exception {
        logger.debug("delete -- admin：{}",admin);
        AdminDao.removeAdmin(admin);
        AdminDao.saveAdmin();
        Result res=new Result();
        return JSON.toJSONString(res);
    }
    @RequestMapping(value="/save",produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody String save(@RequestBody String body) throws Exception {
        Result res=new Result();
        String req= URLDecoder.decode(body, Constants.CHARSET);
        logger.debug("saveadmin -- req：{}",req);
        Map<String,Object> reqMap=JSON.parseObject(req);
        Admin admin= (Admin) ModuleRegister.getNoticeServiceImpl(reqMap.get("type").toString()).getTypeDeclare().getBeanClass().newInstance();
        admin.init(reqMap);
        AdminDao.addAdmin(admin);
        AdminDao.saveAdmin();
        return JSON.toJSONString(res);
    }
    @RequestMapping(value="/toadd")
    public ModelAndView toadd(@RequestParam Map<String,Object> param) throws Exception {
        logger.info("参数：{}",param.toString());
        Admin admin= (Admin) ModuleRegister.getNoticeServiceImpl(param.get("type").toString()).getTypeDeclare().getBeanClass().newInstance();
        admin.init(param);
        AdminDao.addAdmin(admin);
        AdminDao.saveAdmin();
        return new ModelAndView("/ok");
    }

    @RequestMapping(value="/fields" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody String noticeFields(@RequestParam String type) throws Exception {
        logger.info("type：{}",type);
        Admin admin= (Admin) ModuleRegister.getNoticeServiceImpl(type).getTypeDeclare().getBeanClass().newInstance();
        List<FieldDeclare> fields=admin.getFields();
        Result res=new Result();
        res.setObj(fields);
        return JSON.toJSONString(res);
    }

}
