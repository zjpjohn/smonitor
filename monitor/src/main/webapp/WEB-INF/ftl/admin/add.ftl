<!DOCTYPE html>
<html lang="zh-CN">
<head>
<#include "/include/head.ftl">
</head>
<body>
<div class="container">
    <div class="row margin-top-md">
        <a type="button" class="btn btn-success" href="list">列表</a>
        <button type="button" class="btn btn-primary" disabled="disabled">新增</button>
    </div>
    <hr class="margin-bottom-xs">

    <#--添加form-->
    <form id="add_form" action="toadd" method="post">
        <div class="row form-inline margin-top-sm">
            <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>id：</label><input name="id"  type="text" class="form-control"/></div>
            <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>通知方式：</label>
                <select class="form-control" name="type">
                    <option value="">-请选择-</option>
                    <#list notice_types as noticeType>
                        <option value="${noticeType.typeValue}">${noticeType.name}</option>
                    </#list>
                </select>
            </div>
            <div class="append-div"></div>
        </div>
        <div class="row text-center margin-top-md">
            <button class="btn btn-danger" type="submit">增加</button>
            <button class="btn btn-default" type="reset">重置</button>
        </div>
        <hr>
    </form>
</div>
<script>
    $(function(){
        $("select[name='type']").change(function () {
            var type_val=$(this).val();
            if(type_val!=""){
                $(".append-div").html("");
                $.ajax({
                    "url":"fields",
                    "type":"post",
                    "data":{"type":type_val},
                    "dataType":"json",
                    "success":function(data,desc1){
                        if(data.success==true){
                            var append_html="";
                            $.each(data.obj,function(i,item){
                                append_html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input name='"+item.fieldName+"'  type='text' class='form-control'/></div>";
                            });
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