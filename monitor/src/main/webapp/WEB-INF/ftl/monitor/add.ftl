<!DOCTYPE html>
<html>
<head>
<#include "/include/head.ftl">
</head>
<body>
<div class="container">
    <div class="row margin-top-md">
        <a type="button" class="btn btn-success" href="list">列表</a>
        <button type="button" class="btn btn-primary" disabled="disabled">新增</button>
    </div>
    <hr>

    <#--添加form-->
    <form id="add_form" action="toadd" method="post">
        <div class="row">
            <h3>监控项:</h3>
        </div>
        <div class="row form-inline margin-top-sm">
            <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>类型：</label>
                <select class="form-control" name="type">
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
            <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>名称：</label><input name="name"  type="text" class="form-control"/></div>
            <div class="append-div"></div>
        </div>
        <hr>
        <div class="row">
            <div class="col-xs-6 col-md-6 margin-bottom-sm">
                <h4>检查项:</h4>
            </div>
        </div>
        <div class="row">
            <select class="form-control" name="check.type">
                <option value="">-请选择-</option>
            </select>
        </div>
        <div class="row">
            <div class="col-xs-6 col-md-6 margin-bottom-sm text-center">
                <button type="button" class="btn btn-default"><span class="glyphicon glyphicon-plus"></span></button>
            </div>
        </div>
        <hr>
        <div class="row text-center margin-top-md">
            <button type="submit" class="btn btn-danger">增加</button>
            <button type="reset" class="btn btn-default">重置</button>
        </div>
    </form>
</div>
<script>
    $(function(){
        $("select[name='type']").change(function () {
            var type_val=$(this).val();
            if(type_val!=""){
                $(".append-div").html("");
                console.log("去查询");
                $.ajax({
                    "url":"fields",
                    "type":"post",
                    "data":{"type":type_val},
                    "dataType":"json",
                    "success":function(data,desc1){
                        if(data.success==true){
                            console.log(data.obj);
                            var append_html="";
                            $.each(data.obj,function(i,item){
                                append_html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input name='"+item.fieldNmae+"'  type='text' class='form-control'/></div>";
                            });
                            //console.log(append_html);
                            $(".append-div").append(append_html);
                        }else{
                            console.log("exception..."+data.msg);
                        }
                    },
                    "error":function(xhr,err1,err2){

                    }
                });
            }
        });
    });
    
</script>
</body>
</html>