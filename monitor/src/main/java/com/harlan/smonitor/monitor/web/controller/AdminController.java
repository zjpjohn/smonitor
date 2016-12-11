package com.harlan.smonitor.monitor.web.controller;

import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.monitor.data.AdminDao;
import com.harlan.smonitor.monitor.web.bean.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by harlan on 2016/12/6.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @RequestMapping(value="/list")
    public ModelAndView list(Admin admin, PageQuery query) throws Exception {
        ModelAndView mv = new ModelAndView("/admin/list");
        mv.addObject("list", AdminDao.getAdminList());
        mv.addObject("count", AdminDao.getAdminList().size());
        mv.addObject("query", query);//分页查询条件
        mv.addObject("product", admin);
//        mv.addObject("select_sold",ConsoleUtil.getSelectHtml(0,product.getSold()));//是否
        return mv;
    }

    @RequestMapping(value="/add")
    public ModelAndView add() throws Exception {
        return new ModelAndView("/admin/add");
    }
    @RequestMapping(value="/toadd")
    public ModelAndView toadd(Admin admin) throws Exception {
        logger.info("添加管理员：{}",admin.toString());
        AdminDao.addAdmin(admin);
        ModelAndView mv = new ModelAndView("/ok");
        return mv;
    }
}
