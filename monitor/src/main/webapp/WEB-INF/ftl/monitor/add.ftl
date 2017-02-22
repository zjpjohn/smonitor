<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>添加监控</title>
<#include "/include/head.ftl">
</head>
<body>
<div class="container">
    <div class="row margin-top-md">
        <a type="button" class="btn btn-success" href="list">列表</a>
        <button type="button" class="btn btn-primary" disabled="disabled">新增</button>
        <button id="monitor_show_add_check_btn" type="button" class="btn btn-default">添加检查项</button>
    </div>
    <hr>

    <#--添加 monitor form-->
    <form id="add_form" >
        <div class="row">
            <h3>监控项:</h3>
        </div>
        <div class="row form-inline margin-top-sm">
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>类型：</label>
                <select id="monitor_type" class="form-control" name="type">
                    <option value="">-请选择-</option>
                    <#list types as type>
                        <option value="${type.typeValue}">${type.name}</option>
                    </#list>
                </select>
            </div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>分组：</label>
                <select class="form-control" name="groupId">
                    <option value="">-请选择-</option>
                <#list groups as group>
                    <option value="${group.id}">${group.name}</option>
                </#list>
                </select>
            </div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>名称：</label>
                <input name="name"  type="text" class="form-control"/>
            </div>
            <div id="monitor_append_div"></div>
        </div>
        <div class="row form-inline">
            <div id="admin-btns-div" class="col-xs-9 margin-bottom-sm">
                <label>联系人：</label>
            </div>
            <div class="col-xs-3 margin-bottom-sm">
                <button id="add_admin_btn" type="button" class="btn btn-default">编辑联系人</button>
            </div>
        </div>
    </form>

    <hr>
    <#--所有检查项-->
    <div id="check_list_div">
    </div>

    <div class="row text-center margin-top-md margin-bottom-lg">
        <button id="add_monitor_btn" type="button" class="btn btn-danger">增加</button>
        <button id="add_reset_monitor_btn" type="button" class="btn btn-default">重置</button>
    </div>
</div>
<#-- 提示框modal -->
<#include "/include/modal/alert.ftl">
<#-- 添加 检查项 form -->
<#include "/include/modal/check.ftl">
<#--选择管理员，使用这个模块-->
<#include "/include/modal/admin.ftl">
<script>
    function resetAll() {
        $("#monitor_append_div").html("");
        $("#check_append_div").html("");
        $(".check_srtial_hidden").val("");
    }

    $(function(){
        //选择monitor类型时
        $("#monitor_type").change(function () {
            var type_val=$(this).val();
            resetAll();
            addCheckModalInit("#check_list_div",type_val);
            if(type_val!=""){
                //查询监控项的字段
                $.ajax({
                    "url":"monitorfields",
                    "type":"post",
                    "data":{"type":type_val},
                    "dataType":"json",
                    "success":function(data){
                        if(data.success==true){
                            var fields=data.obj;
                            //console.log(fields);
                            var append_html=getFieldsHtml(fields);
                            $("#monitor_append_div").append(append_html);
                        }else{
                            console.log("exception..."+data.msg);
                        }
                    },
                    "error":function(xhr,err1,err2){
                    }
                });
            }
        });

        //添加monitor按钮
        $("#add_monitor_btn").click(function () {
            var monitorObj= getMonitorObj("#add_form");
            //错误信息
            if(typeof(monitorObj)=="string"){
                showMsg(monitorObj);
                return ;
            }
            $.ajax({
                "url":"addmonitor",
                "type":"post",
                "data":JSON.stringify(monitorObj),
                "dataType":"json",
                "contentType":"application/json",
                "success":function(data,desc1){
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
        //全部重置
        $("#add_reset_monitor_btn").click(function () {
            resetAll();
            $("#add_form")[0].reset();

        });
        $("#add_admin_btn").click(function () {
            adminModalInit("#admin-btns-div");
        });
        $("#monitor_show_add_check_btn").click(function () {
            showCheckModal();
        });
        $("#monitor_type").focus();
        
    });

</script>
</body>
</html>