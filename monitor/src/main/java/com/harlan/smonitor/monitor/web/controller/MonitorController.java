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
import com.harlan.smonitor.monitor.data.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
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
        logger.debug("list map：{}",param);
        Integer start=null,limit=null;
        if(param.get("paging_start")!=null){
            start=Integer.valueOf(param.get("paging_start").toString());
        }
        if(param.get("paging_limit")!=null){
            limit=Integer.valueOf(param.get("paging_limit").toString());
        }
        ModelAndView mv = new ModelAndView("/monitor/list");
        mv.addObject("list", MonitorDao.getMonitorItemList(start,limit));
        mv.addObject("paging_count", MonitorDao.getMonitorItemList(null,null).size());
        mv.addObject("paging_start", start==null?0:start);
        mv.addObject("types", MonitorItem.getTypes());
        mv.addAllObjects(param);
        return mv;
    }
    @RequestMapping(value="/detail")
    public ModelAndView detail(Integer id)throws Exception{
        logger.debug("detail id：{}",id);
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
        logger.debug("monitor:{}",JSON.toJSONString(monitor));
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
    public @ResponseBody String toadd(@RequestBody String body) throws Exception {
        Result res=new Result();
        String req= URLDecoder.decode(body, Constants.CHARSET);
        logger.debug("addmonitor -- req：{}",req);
        Map<String,Object> reqMap=JSON.parseObject(req);
        MonitorItem item=MonitorItem.monitorInstance(reqMap.get("type").toString());
        item.init(reqMap);
        MonitorDao.addMonitor(item);
        MonitorDao.saveMonitorFile();
        return JSON.toJSONString(res);
    }

    @RequestMapping(value="/savemonitor" ,produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody String saveMonitor(@RequestBody String body)throws Exception{
        Result res=new Result();
        String req= URLDecoder.decode(body, Constants.CHARSET);
        logger.debug("savemonitor -- req：{}",req);
        Map<String,Object> reqMap=JSON.parseObject(req);
        MonitorItem item=MonitorItem.monitorInstance(reqMap.get("type").toString());
        item.init(reqMap);
        MonitorDao.saveMonitor(item);
        MonitorDao.saveMonitorFile();
        return JSON.toJSONString(res);
    }
    @RequestMapping(value="/qryadmin" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody String qryAdmin(@RequestParam("start") String startStr,@RequestParam("limit") String limitStr){
        Integer limit=4;
        Integer start=0;
        if(notNull(startStr)){
            start=Integer.valueOf(startStr);
        }
        if(notNull(limitStr)){
            limit=Integer.valueOf(limitStr);
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
        MonitorItem monitor=MonitorDao.getMonitor(id);
        for (CheckItem check:monitor.getCheckList()) {
            if(check.getJobKey()!=null){
                JobDao.removeCheck(check.getJobKey());
            }
        }
        MonitorDao.deleteMonitor(id);
        MonitorDao.saveMonitorFile();
        Result res=new Result();
        return JSON.toJSONString(res);
    }
    @RequestMapping(value="/startcheck" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String startCheck(@RequestParam("cid") String cid) throws Exception {
        logger.debug("startCheck -- cid:{}",cid);
        Result res=new Result();
        List<MonitorItem> monitors=MonitorDao.getMonitorItemList(null,null);
        for (MonitorItem monitor:monitors) {
            for (CheckItem check:monitor.getCheckList()) {
                if(check.getId().equals(Integer.valueOf(cid))){
                    if(check.getJobKey()!=null){
                        logger.debug("restartCheck -- checkId：{}",check.getId());
                        JobDao.restartCheck(check.getJobKey());
                    }else{
                        logger.debug("Jobkey 不存在，addCheck -- checkId：{}",check.getId());
                        res=JobDao.addCheck(check,monitor);
                    }
                    if(res.isSuccess()){
                        check.setState(Constants.CHECK_RUN);
                        MonitorDao.saveMonitor(monitor);
                        MonitorDao.saveMonitorFile();
                    }

                    break;
                }
            }
        }
        return JSON.toJSONString(res);
    }
    @RequestMapping(value="/pausecheck" , produces= Constants.JSON_PRODUCES, method= RequestMethod.POST)
    public @ResponseBody
    String pauseCheck(@RequestParam("cid") String cid) throws Exception {
        logger.debug("pausecheck -- cid:{}",cid);
        Result res=new Result();
        List<MonitorItem> monitors=MonitorDao.getMonitorItemList(null,null);
        for (MonitorItem monitor:monitors) {
            for (CheckItem check:monitor.getCheckList()) {
                if(check.getId().equals(Integer.valueOf(cid))){
                    res=JobDao.pauseCheck(check.getJobKey());
                    if(res.isSuccess()){
                        check.setState(Constants.CHECK_PAUSE);
                        MonitorDao.saveMonitor(monitor);
                        MonitorDao.saveMonitorFile();
                    }
                    break;
                }
            }
        }

        return JSON.toJSONString(res);
    }
}
