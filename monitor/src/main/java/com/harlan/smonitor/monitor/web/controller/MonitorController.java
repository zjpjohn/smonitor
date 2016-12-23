package com.harlan.smonitor.monitor.web.controller;

import com.alibaba.fastjson.JSON;
import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.common.Constants;
import com.harlan.smonitor.monitor.data.dao.GroupDao;
import com.harlan.smonitor.monitor.data.dao.MonitorDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.*;

import static com.harlan.smonitor.monitor.common.Util.notNull;

/**
 * MonitorController
 * Created by harlan on 2016/12/13.
 */
@Controller
@RequestMapping("/monitor")
public class MonitorController {
    private final static Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @RequestMapping(value="/list")
    public ModelAndView list(@RequestParam Map<String,Object> param) throws Exception {
        String limit="4";
        logger.debug("监控列表 param：{}",param);
        ModelAndView mv = new ModelAndView("/monitor/list");
        mv.addObject("list", MonitorDao.getMonitorItemList(param.get("paging_start"),limit));
        mv.addObject("paging_count", MonitorDao.getMonitorItemList(null,null).size());
        mv.addObject("paging_limit",limit);
        mv.addObject("types", MonitorItem.getTypes());
        mv.addAllObjects(param);
        return mv;
    }
    @RequestMapping(value="/detail")
    public ModelAndView detail(Integer id){
        logger.debug("监控详情 id：{}",id);
        ModelAndView mv=new ModelAndView("/monitor/detail");
        MonitorItem monitor=MonitorDao.getMonitor(id);
        mv.addObject("monitor",JSON.toJSONString(monitor));

        mv.addObject("groups", GroupDao.getGroupList(null,null));
        return mv;
    }
    @RequestMapping(value="/add")
    public ModelAndView add(){
        ModelAndView mv=new ModelAndView("/monitor/add");
        mv.addObject("types", MonitorItem.getTypes());
        mv.addObject("groups", GroupDao.getGroupList(null,null));
        return mv;
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/toadd")
    public ModelAndView toadd(@RequestParam Map<String,Object> param,HttpSession session) throws Exception {
        logger.info("参数：{}",param.toString());
        String serialId=param.get("check.srtial").toString();
        List<CheckItem> checkItems= (List<CheckItem>) session.getAttribute(serialId);
        MonitorItem item=MonitorItem.monitorInstance(param.get("type").toString());
        item.init(param);
        item.setCheckList(checkItems);
        MonitorDao.addMonitor(item);
        MonitorDao.saveMonitorItem();
        return new ModelAndView("/ok");
    }
    @RequestMapping(value="/fields" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String noticeFields(@RequestParam String type) throws Exception {
        logger.info("type：{}",type);
        MonitorItem item=MonitorItem.monitorInstance(type);
        List<FieldDeclare> fields= item.getFields();
        Result res=new Result();
        res.setObj(fields);
        return JSON.toJSONString(res);
    }
    @RequestMapping(value="/checktype" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String checkType(@RequestParam("mtype") String mtype){
        logger.info("mtype：{}",mtype);
        try {
            MonitorItem item=MonitorItem.monitorInstance(mtype);
            Result res=new Result();
            res.setObj(item.getCheckTypes());
            return JSON.toJSONString(res);
        } catch (Exception e) {
            logger.error("查询checkType异常",e);
            return JSON.toJSONString(new Result(e.toString()));
        }
    }
    @RequestMapping(value="/checkfields" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String checkFields(@RequestParam("mtype") String mtype,@RequestParam("ctype") String ctype) throws Exception {
        logger.info("查询check字段：monitor type：{} , check type:{}",mtype,ctype);
        MonitorItem item=MonitorItem.monitorInstance(mtype);
        CheckItem check=item.checkInstance(ctype);
        Result res=new Result();
        res.setObj(check.getFields());
        return JSON.toJSONString(res);
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value="/addcheck" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String addCheck(@RequestParam Map<String,Object> param,HttpSession session) throws Exception {
        logger.info("添加checkItem param：{}",param);
        String mtype=param.get("monitor.type").toString();
        param.remove("monitor.type");
        setCronExpression(param);
        MonitorItem item=MonitorItem.monitorInstance(mtype);
        CheckItem check=item.checkInstance(param.get("type").toString());
        check.init(param);
        //初始化了以后存到session中去
        String serialId;
        if(notNull(param.get("check.srtial"))){
            serialId=param.get("check.srtial").toString();
            param.remove("check.srtial");
        }else{
            serialId=UUID.randomUUID().toString();
        }
        List<CheckItem> checkItems= (List<CheckItem>) session.getAttribute(serialId);
        if(checkItems==null){
            checkItems=new LinkedList<CheckItem>();
            session.setAttribute(serialId,checkItems);
        }
        checkItems.add(check);
        logger.info("添加checkItem的个数：{}",checkItems.size());
        Result res=new Result();
        Map<String,Object> checkMap=check.createMap();
        checkMap.put("serial",serialId);
        res.setObj(checkMap);
        return JSON.toJSONString(res);
    }

    private void setCronExpression(Map<String, Object> param) {
        String ss=param.get("cronExpression.ss").toString().trim();
        param.remove("cronExpression.ss");
        String mm=param.get("cronExpression.mm").toString().trim();
        param.remove("cronExpression.mm");
        String hh=param.get("cronExpression.hh").toString().trim();
        param.remove("cronExpression.hh");
        String day=param.get("cronExpression.day").toString().trim();
        param.remove("cronExpression.day");
        String month=param.get("cronExpression.month").toString().trim();
        param.remove("cronExpression.month");
        String week=param.get("cronExpression.week").toString().trim();
        param.remove("cronExpression.week");
        String year=param.get("cronExpression.year")==null?null:param.get("cronExpression.year").toString().trim();
        param.remove("cronExpression.year");
        StringBuilder cron=new StringBuilder(ss);
        cron.append(" ").append(mm);
        cron.append(" ").append(hh);
        cron.append(" ").append(day);
        cron.append(" ").append(month);
        cron.append(" ").append(week);
        if(notNull(year)){
            cron.append(" ").append(year);
        }
        logger.debug("执行时间为：{}",cron.toString());
        param.put("cronExpression",cron.toString());
    }
}
