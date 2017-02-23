<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>监控详情</title>
<#include "/include/head.ftl">
</head>
<body>
<div class="container">
    <div class="row margin-top-md">
        <a type="button" class="btn btn-success" href="list">列表</a>
        <button id="monitor_detail_del_check_btn" type="button" class="btn btn-primary">删除</button>
        <button id="monitor_show_add_check_btn" type="button" class="btn btn-default">添加检查项</button>
    </div>
    <hr>

    <#--修改form-->
    <form id="modify_form">
        <input type="hidden" name="check.srtial" class="check_srtial_hidden">
        <div class="row">
            <h3>监控项:</h3>
        </div>
        <div class="row form-inline margin-top-sm">
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>ID：</label>
                <input id="monitor_id" type="text" name="id" readonly class="form-control"/>
            </div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>类型：</label>
                <input type="text" readonly class="form-control" value="${monitorTypeName}"/>
                <input id="monitor_type" type="hidden" name="type"/>
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
            </div>
            <div class="col-xs-12 col-md-3 margin-bottom-sm text-right">
                <button id="add_admin_btn" type="button" class="btn btn-default">编辑联系人</button>
            </div>
        </div>
    </form>
    <hr>
    <div id="check_list_div">
    </div>

    <div class="row text-center margin-top-md margin-bottom-lg">
        <button id="modify_btn" type="button" class="btn btn-danger">修改</button>
        <button type="button" class="btn btn-default">重置</button>
    </div>

</div>
<#-- 提示框modal -->
<#include "/include/modal/alert.ftl">
<!-- 添加 检查项 form -->
<#include "/include/modal/check.ftl">
<#include "/include/modal/admin.ftl">
<script>
    var MONITOR=${monitor};
    function del() {
        $.ajax({
            "url":"delmonitor",
            "type":"post",
            "data":{"monitorid":MONITOR.id},
            "dataType":"json",
            "success":function(data){
                if(data.success==true){
                    window.location="list";
                }else{
                    console.log("exception..."+data.msg);
                }
            },
            "error":function(xhr,err1,err2){
            }
        });
    }
    $(function(){
        addCheckModalInit("#check_list_div",MONITOR.type,${checkTypeNameMap},${checkFieldsMap});
        $("#monitor_type").val(MONITOR.type);
        $("#monitor_id").val(MONITOR.id);
        $("#group_select").val(MONITOR.groupId);
        $("#monitor_name").val(MONITOR.name);
        //添加monitor自定义字段及值
        var monitorHtml=getFieldsHtml(MONITOR.fields,MONITOR);
        $("#monitor_append_div").append(monitorHtml);
        //添加联系人
        $.each(MONITOR.adminList,function(i,admin){
            $("#admin-btns-div").append('<button class="btn btn-default" disabled>'+admin+'</button>');
            $("#admin-btns-div").append('<input type="hidden" name="adminList" value="'+admin+'" >');
        });
        $("#add_admin_btn").click(function () {
            adminModalInit("#admin-btns-div");
        });
        //添加check
        addChecks2html(MONITOR.checkList);

        $("#monitor_show_add_check_btn").click(function () {
            showCheckModal();
        });
        $("#modify_btn").click(function () {
            var monitorObj= getMonitorObj("#modify_form");
            //错误信息
            if(typeof(monitorObj)=="string"){
                showMsg(monitorObj);
                return ;
            }
            //console.log(JSON.stringify(monitorObj));
            $.ajax({
                "url":"savemonitor",
                "type":"post",
                "data":JSON.stringify(monitorObj),
                "dataType":"json",
                "contentType":"application/json",
                "success":function(data){
                    if(data.success==true){
                        window.location="list";
                    }else{
                        console.log("exception..."+data.msg);
                    }
                },
                "error":function(xhr,err1,err2){
                }
            });
        });
        $("#monitor_detail_del_check_btn").click(function () {
            confirmAndRun("删除监控项会终止全部检查任务，确认删除此监控项？",del);
        });
    });

</script>
</body>
</html>