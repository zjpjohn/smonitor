package com.harlan.smonitor.monitor.web.controller;

import com.harlan.smonitor.monitor.bean.Group;
import com.harlan.smonitor.monitor.data.dao.GroupDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * GroupController
 * Created by harlan on 2016/12/13.
 */
@Controller
@RequestMapping("/group")
public class GroupController {
    private final static Logger logger = LoggerFactory.getLogger(GroupController.class);

    @RequestMapping(value="/list")
    public ModelAndView list(@RequestParam Map<String,Object> param) throws Exception {
        String limit="4";
        logger.debug("param：{}",param);
        ModelAndView mv = new ModelAndView("/group/list");
        mv.addObject("list", GroupDao.getGroupList(param.get("paging_start"),limit));
        mv.addObject("paging_count", GroupDao.getGroupList(null,null).size());
        mv.addObject("paging_limit",limit);
        mv.addAllObjects(param);
        return mv;
    }

    @RequestMapping(value="/add")
    public ModelAndView add() throws Exception {
        return new ModelAndView("/group/add");
    }
    @RequestMapping(value="/toadd")
    public ModelAndView toadd(@RequestParam Map<String,Object> param) throws Exception {
        logger.info("参数：{}",param.toString());
        Group group=new Group();
        group.setName(param.get("name").toString());
        GroupDao.addGroup(group);
        GroupDao.saveGroup();
        return new ModelAndView("/ok");
    }
}
