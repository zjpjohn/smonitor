package com.harlan.smonitor.monitor.web.controller;

import com.alibaba.fastjson.JSON;
import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.api.impl.FieldDeclare;
import com.harlan.smonitor.api.impl.TypeDeclare;
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

import java.net.URLDecoder;
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
        logger.debug("list map：{}",param);
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
        Map<String, Object> monitorMap = monitor.createMap();
        monitorMap.put("fields",monitor.getFields());
        Map<String,Object> checkTypeNameMap=new HashMap<String,Object>();
        for (TypeDeclare type:monitor.getCheckTypes()) {
            checkTypeNameMap.put(type.getTypeValue(),type.getName());
        }

        Map<String,Object> checkFieldsMap=new HashMap<String,Object>(monitor.getCheckList().size());
        for (CheckItem check:monitor.getCheckList()) {
            checkFieldsMap.put(check.getType(),check.getFields());
        }
        mv.addObject("monitor",JSON.toJSONString(monitorMap));
        mv.addObject("checkTypeNameMap",JSON.toJSONString(checkTypeNameMap));
        mv.addObject("checkFieldsMap",JSON.toJSONString(checkFieldsMap));
        mv.addObject("monitorTypeName",MonitorItem.getType(monitor.getType()).getName());
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
    @RequestMapping(value="/addmonitor" ,produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody String toadd(@RequestBody String body){
        Result res;
        try {
            String req= URLDecoder.decode(body, Constants.CHARSET);
            logger.debug("addmonitor -- req：{}",req);
            Map<String,Object> reqMap=JSON.parseObject(req);
            MonitorItem item=MonitorItem.monitorInstance(reqMap.get("type").toString());
            item.init(reqMap);
            MonitorDao.addMonitor(item);
            MonitorDao.saveMonitorFile();
            res=new Result();
        } catch (Exception e) {
            logger.error("添加异常",e);
            res=new Result(e.toString());
        }
        return JSON.toJSONString(res);
    }

    @RequestMapping(value="/savemonitor" ,produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody String saveMonitor(@RequestBody String body){
        Result res;
        try {
            String req= URLDecoder.decode(body, Constants.CHARSET);
            logger.debug("savemonitor -- req：{}",req);
            Map<String,Object> reqMap=JSON.parseObject(req);
            MonitorItem item=MonitorItem.monitorInstance(reqMap.get("type").toString());
            item.init(reqMap);
            MonitorDao.saveMonitor(item);
            MonitorDao.saveMonitorFile();
            res=new Result();
        } catch (Exception e) {
            logger.error("添加异常",e);
            res=new Result(e.toString());
        }
        return JSON.toJSONString(res);
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
            admin.setType(ModuleRegister.getNoticeServiceImpl(admin.getType()).getTypeDeclare().getName());
        }
        resultMap.put("list", adminList);
        resultMap.put("count", AdminDao.count());
        resultMap.put("limit",limit);
        Result res=new Result();
        res.setObj(resultMap);
        return JSON.toJSONString(res);
    }

    @RequestMapping(value="/monitorfields" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String noticeFields(@RequestParam String type) throws Exception {
        logger.debug("monitorfields -- type：{}",type);
        MonitorItem item=MonitorItem.monitorInstance(type);
        List<FieldDeclare> fields= item.getFields();
        Result res=new Result();
        res.setObj(fields);
        return JSON.toJSONString(res);
    }
    @RequestMapping(value="/checktype" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String checkType(@RequestParam("mtype") String mtype){
        logger.debug("checkType -- monitor type：{}",mtype);
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
    @RequestMapping(value="/checkfields", produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String checkFields(@RequestParam("mtype") String mtype,@RequestParam("ctype") String ctype) throws Exception {
        logger.debug("查询check字段：monitor type：{} , check type:{}",mtype,ctype);
        MonitorItem item=MonitorItem.monitorInstance(mtype);
        CheckItem check=item.checkInstance(ctype);
        Result res=new Result();
        res.setObj(check.getFields());
        return JSON.toJSONString(res);
    }
    @RequestMapping(value="/delmonitor",produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String delMonitor(@RequestParam String monitorid){
        logger.debug("delmonitor -- monitorid：{}",monitorid);
        Integer id=Integer.valueOf(monitorid);
        MonitorDao.deleteMonitor(id);
        MonitorDao.saveMonitorFile();
        Result res=new Result();
        return JSON.toJSONString(res);
    }
}
