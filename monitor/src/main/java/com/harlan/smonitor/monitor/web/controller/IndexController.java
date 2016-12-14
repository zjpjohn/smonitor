package com.harlan.smonitor.monitor.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IndexController
 * Created by harlan on 2016/12/8.
 */
@Controller
public class IndexController {
    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
    @RequestMapping(value="/")
    public String index() throws Exception {
        return "index";
    }
}
