<!DOCTYPE html>
<html>
<head>
    <title>添加监控</title>

<#include "/include/head.ftl">
    <link href="../plugin/bootstrap/iCheck/1.0.1/skins/square/green.css" rel="stylesheet">
    <style>
    </style>
</head>
<body>
<div class="container">
    <div class="row margin-top-md">
        <a type="button" class="btn btn-success" href="list">列表</a>
        <button type="button" class="btn btn-primary" disabled="disabled">新增</button>
    </div>
    <hr>

    <#--添加 monitor form-->
    <form id="add_form" action="toadd" method="post">
        <input type="hidden" name="check.srtial" class="check_srtial_hidden">
        <div class="row">
            <h3>监控项:</h3>
        </div>
        <div class="row form-inline margin-top-sm">
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>类型：</label>
                <select id="monitor_type" class="form-control" name="type">
                    <option value="">-请选择-</option>
                    <#list types as type>
                        <option value="${type}">${type}</option>
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
                <input type="hidden" name="AAAAAAAAAAAAA">
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
    <form id="check_add_form">
        <input type="hidden" name="monitor.type" id="monitor_type_hidden">
        <input type="hidden" name="check.srtial" class="check_srtial_hidden">
        <input type="hidden" name="admins" value="1">
        <input type="hidden" name="admins" value="2">
        <div class="row">
            <div class="col-xs-6 col-md-6 margin-bottom-sm">
                <h4>检查项:</h4>
            </div>
        </div>
        <div class="row form-inline">
            <div class="col-xs-6 col-md-4 margin-bottom-sm">
                <label>check类型：</label>
                <select class="form-control" id="check_type" name="type">
                    <option value="">-请选择-</option>
                </select>
            </div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>名称：</label><input name="param.name"  type="text" class="form-control"/></div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>报警阀值：</label><input name="param.alarmTimes"  type="text" class="form-control middle-input"/></div>
            <#--<div class="col-xs-12 col-md-6 margin-bottom-sm">
                <label>执行时间：</label>
                <input name="cronExpression.ss"  type="text" class="form-control small-input" placeholder="秒"/>
                <input name="cronExpression.mm"  type="text" class="form-control small-input" placeholder="分"/>
                <input name="cronExpression.hh"  type="text" class="form-control small-input" placeholder="时"/>
                <input name="cronExpression.day"  type="text" class="form-control small-input" placeholder="日"/>
                <input name="cronExpression.month"  type="text" class="form-control small-input" placeholder="月"/>
                <input name="cronExpression.week"  type="text" class="form-control small-input" placeholder="周"/>
                <input name="cronExpression.year"  type="text" class="form-control middle-input" placeholder="年(可选)"/>
            </div>-->
            <div id="check_append_div"></div>
        </div>
        <div class="row form-inline">
            <button id="add_time_btn" type="button" class="btn btn-default">添加时间</button>
        </div>

        <div class="row">
            <div class="col-xs-12 col-md-12 margin-bottom-sm text-center">
                <button id="add_check_btn" type="button" class="btn btn-default">添加项</button>
            </div>
        </div>
    </form>
    <hr>
    <div class="btn-div row text-center margin-top-md">
        <button type="button" class="btn btn-danger">增加</button>
        <button type="button" class="btn btn-default">重置</button>
    </div>
</div>
<#--选择管理员，使用这个模块-->
<#include "/include/modal/admin.ftl">
<script src="../plugin/bootstrap/iCheck/1.0.1/icheck.min.js"></script>
<script>
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
    function resetAll() {
        $("#check_type").html("<option value=''>-请选择-</option>");
        $("#monitor_append_div").html("");
        $("#check_append_div").html("");
        $(".check_srtial_hidden").val("");
        $("#monitor_type_hidden").val("");
    }
    $(function(){
        $("#monitor_type").change(function () {
            var type_val=$(this).val();
            resetAll();
            if(type_val!=""){
                //将monitor的type存在input中，随表单提交
                $("#monitor_type_hidden").val(type_val);
                //查询监控项的字段
                $.ajax({
                    "url":"fields",
                    "type":"post",
                    "data":{"type":type_val},
                    "dataType":"json",
                    "success":function(data,desc1){
                        if(data.success==true){
                            //console.log(data.obj);
                            var append_html="";
                            $.each(data.obj,function(i,item){
                                append_html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input name='"+item.fieldName+"'  type='text' class='form-control'/></div>";
                            });
                            //console.log(append_html);
                            $("#monitor_append_div").append(append_html);
                        }else{
                            console.log("exception..."+data.msg);
                        }
                    },
                    "error":function(xhr,err1,err2){
                    }
                });
                //查询这个类型的监控，有哪些类型的检查项
                $.ajax({
                    "url":"checktype",
                    "type":"post",
                    "data":{"mtype":type_val},
                    "dataType":"json",
                    "success":function(data,desc1){
                        if(data.success==true){
                            //console.log(data.obj);
                            $.each(data.obj,function(i,item){
                                $("#check_type").append("<option value='"+item+"'>"+item+"</option>");
                            });
                        }else{
                            console.log("exception..."+data.msg);
                        }
                    },
                    "error":function(xhr,err1,err2){
                    }
                });
            }
        });
        //查询这个检查项的字段有哪些
        $("#check_type").change(function () {
            var mtype_val=$("#monitor_type_hidden").val();
            var ctype_val=$(this).val();
            $("#check_append_div").html("");
            if(ctype_val!=""){
                $.ajax({
                    "url":"checkfields",
                    "type":"post",
                    "data":{"mtype":mtype_val,"ctype":ctype_val},
                    "dataType":"json",
                    "success":function(data,desc1){
                        if(data.success==true){
                            //console.log(data.obj);
                            checkFields=data.obj;
                            var append_html="";
                            $.each(checkFields,function(i,item){
                                append_html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input name='"+item.fieldName+"'  type='text' class='form-control'/></div>";
                            });
                            //console.log(append_html);
                            $("#check_append_div").append(append_html);
                        }else{
                            console.log("exception..."+data.msg);
                        }
                    },
                    "error":function(xhr,err1,err2){
                    }
                });
            }
        });
        $(".btn-div .btn-danger").click(function () {
            if($(".check_srtial_hidden").val()==""){
                alert("该监控项没有检查项，请添加检查项");
            }else{
                $("#add_form").submit();
            }
        });
        //全部重置
        $(".btn-div .btn-default").click(function () {
            resetAll();
            $("#add_form")[0].reset();
            $("#check_add_form")[0].reset();
        });
        //添加监控项
        $("#add_check_btn").click(function () {
            $.ajax({
                "url":"addcheck",
                "type":"post",
                "data":$("#check_add_form").serialize(),
                "dataType":"json",
                "success":function(data,desc1){
                    if(data.success==true){
                        console.log(data.obj);
                        $(".check_srtial_hidden").val(data.obj.serial);
                        var  append_html=checkItemInputs(data.obj);
                        $("#check_list_div").append(append_html);
                    }else{
                        console.log("exception..."+data.msg);
                    }
                },
                "error":function(xhr,err1,err2){
                }
            });
        });
        $("#add_admin_btn").click(function () {
            adminModalInit("#admin-btns-div");
        });
    });

</script>
</body>
</html>