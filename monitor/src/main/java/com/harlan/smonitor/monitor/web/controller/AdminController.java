package com.harlan.smonitor.monitor.web.controller;

import com.alibaba.fastjson.JSON;
import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.monitor.common.Constants;
import com.harlan.smonitor.monitor.core.init.ImplRegister;
import com.harlan.smonitor.monitor.data.dao.AdminDao;
import com.harlan.smonitor.monitor.web.cache.PageCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
        String limit="4";
        logger.debug("param：{}",param);
        ModelAndView mv = new ModelAndView("/admin/list");
        mv.addObject("list", AdminDao.getAdminList(param.get("paging_start"),limit));
        mv.addObject("paging_count", AdminDao.getAdminList(null,null).size());
        mv.addObject("paging_limit",limit);
        mv.addObject("notice_types", PageCache.NOTICE_TYPES);
        mv.addAllObjects(param);
        return mv;
    }

    @RequestMapping(value="/add")
    public ModelAndView add() throws Exception {
        ModelAndView mv=new ModelAndView("/admin/add");
        mv.addObject("notice_types", PageCache.NOTICE_TYPES);
        return mv;
    }
    @RequestMapping(value="/toadd")
    public ModelAndView toadd(@RequestParam Map<String,Object> param) throws Exception {
        logger.info("参数：{}",param.toString());
        Admin admin=ImplRegister.getNoticeServiceImpl(param.get("type").toString()).getAdminFrom(param);
        AdminDao.addAdmin(admin);
        AdminDao.saveAdmin();
        return new ModelAndView("/ok");
    }

    @RequestMapping(value="/fields" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody String noticeFields(@RequestParam String type) throws Exception {
        logger.info("type：{}",type);
        List<FieldDeclare> fields= ImplRegister.getNoticeServiceImpl(type).getAdminFields();
        Result res=new Result();
        res.setObj(fields);
        return JSON.toJSONString(res);
    }

}
