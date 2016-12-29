<!DOCTYPE html>
<html>
<head>
    <title>监控详情</title>
<#include "/include/head.ftl">
</head>
<body>
<div class="container">
    <div class="row margin-top-md">
        <a type="button" class="btn btn-success" href="list">列表</a>
        <button type="button" class="btn btn-primary">删除</button>
    </div>
    <hr>

    <#--添加form-->
    <form id="add_form" action="toadd" method="post">
        <input type="hidden" name="check.srtial" class="check_srtial_hidden">
        <div class="row">
            <h3>监控项:</h3>
        </div>
        <div class="row form-inline margin-top-sm">
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>ID：</label>
                <input id="monitor_id" type="text" readonly class="form-control"/>
            </div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>类型：</label>
                <input type="text" readonly class="form-control" value="${monitorType.name}"/>
                <input type="hidden" name="type" value="${monitorType.typeValue}"/>
            </div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>分组：</label>
                <select id="group_select" class="form-control" name="groupId">
                    <option value="">-请选择-</option>
                <#list groups as group>
                    <option value="${group.id}">${group.name}</option>
                </#list>
                </select>
            </div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>名称：</label>
                <input id="monitor_name" name="name"  type="text" class="form-control"/>
            </div>
            <div id="monitor_append_div"></div>
        </div>
        <div class="row form-inline">
            <div id="admin-btns-div" class="col-xs-9 margin-bottom-sm">
                <label>联系人：</label>
                <input type="hidden" name="admin_list">
            </div>
            <div class="col-xs-3 margin-bottom-sm">
                <button id="add_admin_btn" type="button" class="btn btn-default">编辑联系人</button>
            </div>
        </div>
    </form>
    <hr>
    <div id="check_list_div">
    </div>
    <!-- 添加 检查项 form -->
    <#include "/include/check.ftl">
</div>
<#include "/include/modal/admin.ftl">
<script>
    var MONITOR=${monitor};
    $(function(){
        //resetAddCheckRow("#check_list_div");
        setMonitorType(MONITOR.type);
        $("#monitor_id").val(MONITOR.id);
        $("#group_select").val(MONITOR.groupId);
        $("#monitor_name").val(MONITOR.name);
        //添加自定义字段及值
        var monitorHtml=getFieldsHtml(MONITOR.fields,MONITOR);
        $("#monitor_append_div").append(monitorHtml);
        //添加联系人
        var adminStr="";
        $.each(MONITOR.adminList,function(i,admin){
            adminStr+=admin;
            adminStr+="|";
            $("#admin-btns-div").append('<button class="btn btn-default" disabled>'+admin+'</button>');
        });
        adminStr=adminStr.substr(0,adminStr.length-1);
        $("#admin-btns-div").find("input").val(adminStr);
        $("#add_admin_btn").click(function () {
            adminModalInit("#admin-btns-div");
        });
        //添加check
        $.each(MONITOR.checkList,function(i,check){
            $("#check_list_div").append(checkItemInputs(check));
        });
    });

</script>
</body>
</html>