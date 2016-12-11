package com.harlan.smonitor.monitor.bean;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by harlan on 2016/12/2.
 */
public class Group {

    public Group() {
        super();
    }

    public Group(Element element) {
        this.id=Integer.valueOf(element.attributeValue("id"));
        this.name=element.attributeValue("name");
//        this.type=Integer.valueOf(element.attributeValue("type"));
    }
    private Integer id;
    private String name;
    private Integer type;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Element createElement(){
        Element g= DocumentHelper.createElement("group");
        g.addAttribute("id",id.toString());
        g.addAttribute("name",name);
//        g.addAttribute("type",type.toString());
        return g;
    }
}
