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
</div>
<#include "/include/modal/admin.ftl">
<script>
    var monitor=${monitor};
    function checkItemInputs(obj) {
        var html='<div class="row"><div class="col-xs-6 col-md-6 margin-bottom-sm"><h4>检查项:</h4></div></div>';
        html+='<div class="row form-inline">';
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>ID：</label><input type='text' class='form-control' value='"+obj.id+"' readonly/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>类型：</label><input type='text' class='form-control' value='"+monitor.checkTypeMap[obj.type].name+"' readonly/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>名称：</label><input type='text' class='form-control' value='"+obj.name+"'/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>报警阀值：</label><input type='text' class='form-control' value='"+obj.alarmTimes+"'/></div>";
        html+=getFieldsHtml(obj.fields,obj);
        html+='</div>';
        $.each(obj.cronList,function(i,item){
            html+='<div class="row form-inline">';
            html+='<div class="col-xs-2 margin-bottom-sm"><label>执行时间：</label></div>';
            html+='<div class="col-xs-3 margin-bottom-sm">';
            var runtimeArray=item.split(" ");
            html+='<input value="'+runtimeArray[0]+'" type="text" class="form-control small-input" placeholder="秒"/><span>秒&nbsp;</span>';
            html+='<input value="'+runtimeArray[1]+'" type="text" class="form-control small-input" placeholder="分"/><span>分&nbsp;</span>';
            html+='<input value="'+runtimeArray[2]+'" type="text" class="form-control small-input" placeholder="时"/><span>时&nbsp;</span>';
            html+='</div>';
            html+='<div class="col-xs-3 margin-bottom-sm">';
            html+='<input value="'+runtimeArray[3]+'" type="text" class="form-control small-input" placeholder="秒"/><span>日&nbsp;</span>';
            html+='<input value="'+runtimeArray[4]+'" type="text" class="form-control small-input" placeholder="分"/><span>月&nbsp;</span>';
            html+='<input value="'+runtimeArray[5]+'" type="text" class="form-control small-input" placeholder="时"/><span>周&nbsp;</span>';
            html+='</div>';
            var yy="";
            if(runtimeArray.length>6){
                yy=runtimeArray[6];
            }
            html+='<div class="col-xs-4 margin-bottom-sm">';
            html+='<input value="'+yy+'" type="text" class="form-control middle-input" placeholder="年(可选)"/><span>年</span>';
            html+='</div>';
            html+='</div>';
        });
        html+='<hr>';
        return html;
    }
    function getFieldsHtml(fields, obj) {
        var fhtml="";
        $.each(fields,function(i,item){
            var val=obj[item.fieldName];
            if (typeof(val) == "undefined") {
                val="";
            }
            fhtml+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input value='"+val+"' type='text' class='form-control' name='item.fieldName'/></div>";
        });
        return fhtml;
    }
    $(function(){
        console.log(monitor);
        $("#monitor_id").val(monitor.id);
        $("#group_select").val(monitor.groupId);
        $("#monitor_name").val(monitor.name);
        //添加自定义字段及值
        var monitorHtml=getFieldsHtml(monitor.fields,monitor);
        $("#monitor_append_div").append(monitorHtml);
        //添加联系人
        var adminStr="";
        $.each(monitor.adminList,function(i,admin){
            //console.log(admin);
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
        $.each(monitor.checkList,function(i,check){
            //console.log(check);
            $("#check_list_div").append(checkItemInputs(check));
        });
    });

</script>
</body>
</html>