<!DOCTYPE html>
<html lang="zh-CN">
<head>
<#include "/include/head.ftl">
</head>
<body>
<div class="container">
    <div class="row margin-top-md">
        <a type="button" class="btn btn-success" href="list">列表</a>
        <button class="btn btn-primary" id="del_admin_btn" type="button"  >删除</button>
    </div>
    <hr class="margin-bottom-xs">

<#--修改form-->
    <form id="modify_form" action="modify" method="post">
        <div class="row form-inline margin-top-sm">
            <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>id：</label><input name="id" value="${id}" type="text" class="form-control" readonly/></div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>通知方式：</label>
                <select class="form-control" name="type" >
                    <option value="">-请选择-</option>
                <#list notice_types as noticeType>
                    <option value="${noticeType.typeValue}">${noticeType.name}</option>
                </#list>
                </select>
            </div>
            <div id="append-div"></div>
        </div>
        <div class="row text-center margin-top-md">
            <button id="mdf_admin_btn" type="button" class="btn btn-danger">修改</button>
            <button type="reset" class="btn btn-default">重置</button>
        </div>
        <hr>
    </form>
</div>
<#-- 提示框modal -->
<#include "/include/modal/alert.ftl">
<script>
    var fields=${fields};
    var adminObj=${admin};
    function del() {
        console.log(adminObj.id);
    }
    $(function(){
        $("input[name='id']").val(adminObj.id);
        $("select[name='type']").val(adminObj.type);
        var append_html=getFieldsHtml(fields,adminObj);
        $("#append-div").html(append_html);
        $("#mdf_admin_btn").click(function () {
            var adminJson=$("#modify_form").serializeObject();
            $.ajax({
                "url":"save",
                "type":"post",
                "data":JSON.stringify(adminJson),
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
        $("#del_admin_btn").click(function () {
            confirmAndRun("确认删除此管理员？",del);
        });
    });
</script>
</body>
</html>