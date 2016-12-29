<!DOCTYPE html>
<html>
<head>
    <title>添加监控</title>
<#include "/include/head.ftl">
    <style>
        .runtime-row-div span{
            margin-left: 2px;
            margin-right: 8px;
        }
        .del-runtime-row-btn span{
            margin: 0;
        }
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
                <input type="hidden" name="admin_list">
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
    <!-- 添加 检查项 form -->
<#include "/include/check.ftl">
    <hr>
    <div class="btn-div row text-center margin-top-md">
        <button type="button" class="btn btn-danger">增加</button>
        <button type="button" class="btn btn-default">重置</button>
    </div>
</div>
<#--选择管理员，使用这个模块-->
<#include "/include/modal/admin.ftl">
<script>

    /*function showAddedCheck(obj) {
        var html='<div class="row"><div class="col-xs-6 col-md-6 margin-bottom-sm"><h4>检查项:</h4></div></div>';
        html+='<div class="row form-inline">';
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>类型：</label><input type='text' class='form-control' value='"+obj.type+"' disabled/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>名称：</label><input type='text' class='form-control' value='"+obj.name+"' disabled/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>报警阀值：</label><input type='text' class='form-control' value='"+obj.alarmTimes+"' disabled/></div>";
        $.each(checkFields,function(i,item){
            var val=obj[item.fieldName];
            if (typeof(val) != "undefined") {
                html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input value='"+val+"' type='text' class='form-control' disabled/></div>";
            }
        });
        html+='</div>';
        $.each(obj.cronList,function(i,item){
            html+='<div class="row form-inline">';
            html+='<div class="col-xs-2 margin-bottom-sm"><label>执行时间：</label></div>';
            html+='<div class="col-xs-3 margin-bottom-sm">';
            var runtimeArray=item.split(" ");
            html+='<input disabled value="'+runtimeArray[0]+'" type="text" class="form-control small-input" placeholder="秒"/><span>秒&nbsp;</span>';
            html+='<input disabled value="'+runtimeArray[1]+'" type="text" class="form-control small-input" placeholder="分"/><span>分&nbsp;</span>';
            html+='<input disabled value="'+runtimeArray[2]+'" type="text" class="form-control small-input" placeholder="时"/><span>时&nbsp;</span>';
            html+='</div>';
            html+='<div class="col-xs-3 margin-bottom-sm">';
            html+='<input disabled value="'+runtimeArray[3]+'" type="text" class="form-control small-input" placeholder="秒"/><span>日&nbsp;</span>';
            html+='<input disabled value="'+runtimeArray[4]+'" type="text" class="form-control small-input" placeholder="分"/><span>月&nbsp;</span>';
            html+='<input disabled value="'+runtimeArray[5]+'" type="text" class="form-control small-input" placeholder="时"/><span>周&nbsp;</span>';
            html+='</div>';
            if(runtimeArray.length>6){
                html+='<div class="col-xs-4 margin-bottom-sm">';
                html+='<input disabled value="'+runtimeArray[6]+'" type="text" class="form-control middle-input" placeholder="年(可选)"/><span>年</span>';
                html+='</div>';
            }
            html+='</div>';
        });
        html+='<hr>';
        return html;
    }*/
    function resetAll() {
        $("#check_type").html("<option value=''>-请选择-</option>");
        $("#monitor_append_div").html("");
        $("#check_append_div").html("");
        $(".check_srtial_hidden").val("");
        $("#monitor_type_hidden").val("");
        $(".del-runtime-row-btn").trigger("click");//触发删除按钮click事件，删除新增的执行时间行
    }

    function getValNotNull(obj){
        if(obj.val()==""){
            obj.parent("div").addClass("has-error");
            alert("此处内容为必填");
            return "";
        }else{
            obj.parent("div").removeClass("has-error");
            return obj.val();
        }
    }

    $(function(){
        resetAddCheckRow("#check_list_div");
        //选择monitor类型时
        $("#monitor_type").change(function () {
            var type_val=$(this).val();
            resetAll();
            if(type_val!=""){
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
                setMonitorType(type_val);
            }
        });

        //添加monitor按钮
        $(".btn-div .btn-danger").click(function () {
            if($(".check_srtial_hidden").val()==""){
                alert("该监控项没有检查项，请添加检查项");
            }else if($("input[name='admin_list']").val()==""){
                alert("该监控项没有联系人，请添加联系人");
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


        $("#add_admin_btn").click(function () {
            adminModalInit("#admin-btns-div");
        });
    });

</script>
</body>
</html>