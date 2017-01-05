package com.harlan.smonitor.monitor.data.dao;

import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.monitor.bean.Group;
import com.harlan.smonitor.monitor.data.DataFileOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * GroupDao
 * Created by harlan on 2016/12/5.
 */
public class GroupDao {
    private final static Logger logger = LoggerFactory.getLogger(GroupDao.class);


    public static Group getGroup(Integer groupId){
        return  CachedData.getGroup(groupId);
    }

    public static List<Group> getGroupList(Integer start,Integer limit){
        if(start==null){
            start=0;
        }
        if(limit==null){
            limit=CachedData.groupSize();
        }
        List<Group> qryGroupList=new LinkedList<Group>();
        List<Group> allGroupList=CachedData.getAllGroup();
        for (int i = 0; i <allGroupList.size(); i++) {
            if(i>=start && i< (start+limit)){
                qryGroupList.add(allGroupList.get(i));
            }
        }
        return  qryGroupList;
    }
    public static Result addGroup(Group group) throws Exception {
        CachedData.putGroup(group);
        return  new Result();
    }
    public static Result saveGroup() throws Exception {
        DataFileOperator.saveGroup(CachedData.getAllGroup());
        return  new Result();
    }
    public static int count(){
        return CachedData.groupSize();
    }
}
