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
                <label>类型：</label>
                <input  type="text" disabled value="${monitor.type}" class="form-control"/>
            </div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>分组：</label>
                <select class="form-control" name="groupId">
                    <option value="">-请选择-</option>
                <#list groups as group>
                    <option value="${group.id}">${group.name}</option>
                </#list>
                </select>
            </div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>名称：</label>
                <input value="${monitor.name}" name="name"  type="text" class="form-control"/>
            </div>
            <div id="monitor_append_div"></div>
        </div>
    </form>
    <hr>
    <div id="check_list_div">
    </div>
</div>
<script>
    var monitorFields=${monitor.fields};
    var checkFields;
    function checkItemInputs(obj) {
        var html='<div class="row"><div class="col-xs-6 col-md-6 margin-bottom-sm"><h4>检查项:</h4></div></div>';
        html+='<div class="row form-inline">';
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>执行时间：</label><input type='text' class='form-control' value='"+obj.cronExpression+"' disabled/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>类型：</label><input type='text' class='form-control' value='"+obj.type+"' disabled/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>名称：</label><input type='text' class='form-control' value='"+obj.name+"' disabled/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>报警阀值：</label><input type='text' class='form-control' value='"+obj.alarmTimes+"' disabled/></div>";
        $.each(checkFields,function(i,item){
            var val=obj[item.fieldName];
            if (typeof(val) != "undefined") {
                html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input value='"+val+"' type='text' class='form-control' disabled/></div>";
            }
        });
        html+='</div><hr>';
        return html;
    }
    $(function(){
        console.log("monitorFields："+monitorFields);

    });

</script>
</body>
</html>