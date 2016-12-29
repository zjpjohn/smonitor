package com.harlan.smonitor.monitor.web.controller;

import com.alibaba.fastjson.JSON;
import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.notice.Admin;
import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.bean.MonitorItem;
import com.harlan.smonitor.monitor.common.Constants;
import com.harlan.smonitor.monitor.core.init.ModuleRegister;
import com.harlan.smonitor.monitor.data.dao.AdminDao;
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
        mv.addObject("monitorType",MonitorItem.getType(monitor.getType()));
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
        logger.info("toadd 参数：{}",param.toString());
        String serialId=param.get("check.srtial").toString();
        param.remove("check.srtial");
        List<CheckItem> checkItems= (List<CheckItem>) session.getAttribute(serialId);
        if(checkItems==null){
            throw new RuntimeException("没有监控项");
        }
        String adminString=param.get("admin_list").toString();
        String[] adminArray=adminString.split("\\|");
        List<String> adminList=new ArrayList<String>(adminArray.length);
        for (String admin:adminArray) {
            adminList.add(admin);
        }
        param.put("adminList",adminList);
        MonitorItem item=MonitorItem.monitorInstance(param.get("type").toString());
        item.init(param);
        item.setCheckList(checkItems);
        MonitorDao.addMonitor(item);
        MonitorDao.saveMonitorItem();
        return new ModelAndView("/ok");
    }
    @RequestMapping(value="/qryadmin" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody String qryAdmin(@RequestParam("start") String startStr){
        Integer limit=6;
        Integer start=0;
        if(notNull(startStr)){
            start=Integer.valueOf(startStr);
        }
        logger.debug("qryadmin start：{}，limit:{}",start,limit);
        Map<String,Object> resultMap=new HashMap<String,Object>();
        List<Admin> adminList=AdminDao.getAdminList(start,limit);
        //修改admin中type字段类型，展示名称
        for (Admin admin:adminList) {
            logger.debug("admin:{}",admin);
            admin.setType(ModuleRegister.getNoticeServiceImpl(admin.getType()).getTypeDeclare().getName());
        }
        resultMap.put("list", adminList);
        resultMap.put("count", AdminDao.count());
        resultMap.put("limit",limit);
        Result res=new Result();
        res.setObj(resultMap);
        return JSON.toJSONString(res);
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
        String runtimeStr=param.get("runtimes").toString();
        String[] cronArray=runtimeStr.split("@");
        List<String> cronList=new ArrayList<String>(cronArray.length);
        for (String corn:cronArray) {
            cronList.add(corn);
        }
        param.put("cronList",cronList);
        MonitorItem item=MonitorItem.monitorInstance(mtype);
        CheckItem check=item.checkInstance(param.get("type").toString());
        check.init(param);
        //初始化了以后存到session中去
        String serialId;
        if(notNull(param.get("check.serial"))){
            serialId=param.get("check.serial").toString();
            param.remove("check.serial");
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

    /**
     * 将form值转化成json，再在页面中，将json展示出来，这样的好处是公用一套显示逻辑
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/form2json" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String form2json(@RequestParam Map<String,Object> param) throws Exception {
        logger.info("添加checkItem param：{}",param);
        String mtype=param.get("monitor.type").toString();
        param.remove("monitor.type");
        String runtimeStr=param.get("runtimes").toString();
        String[] cronArray=runtimeStr.split("@");
        List<String> cronList=new ArrayList<String>(cronArray.length);
        for (String corn:cronArray) {
            cronList.add(corn);
        }
        param.put("cronList",cronList);
        MonitorItem item=MonitorItem.monitorInstance(mtype);
        CheckItem check=item.checkInstance(param.get("type").toString());
        check.init(param);
        Result res=new Result();
//        Map<String,Object> checkMap=check.createMap();
        res.setObj(check);
        return JSON.toJSONString(res);
    }
}
