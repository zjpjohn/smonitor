<!DOCTYPE html>
<html lang="zh-CN">
<head>
<#include "/include/head.ftl">
</head>
<body>
<div class="container">
    <div class="row margin-top-md">
        <button type="button" class="btn btn-success" disabled="disabled">列表</button>
        <a type="button" href="add" class="btn btn-primary">新增</a>
    </div>
    <hr class="margin-bottom-xs">

<#--查询form-->
    <#--
    <form id="paging_query_form" action="list" method="post">
        <input type="hidden" name="paging_start"/>
        <input type="hidden" name="paging_limit"/>
        <div class="row form-inline">
            <div class="col-xs-4 margin-top-sm"><label>id：</label><input name="id" value="${id}" type="text" class="form-control"></div>
            <div class="col-xs-6 col-md-4 margin-top-sm"><label>通知方式：</label>
                <select class="form-control" name="type">
                    <option value="">-请选择-</option>
                <#list notice_types as noticeType>
                    <option value="${noticeType.typeValue}">${noticeType.name}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="row text-center margin-top-md">
            <button id="qry_submit_btn"  class="btn btn-success">查询</button>
            <button type="reset" class="btn btn-default">重置</button>
        </div>
        <hr>
    </form>
    -->
<#--列表div-->
    <div  id="list_table" >
        <table class="table table-hover">
            <thead>
            <tr>
                <th>ID</th>
                <th>通知方式</th>
            </tr>
            </thead>
            <tbody id="list_tbody">
            <#list list as admin>
            <tr>
                <th scope="row">${admin.id}</th>
                <td>${admin.type}</td>
            </tr>
            </#list>
            </tbody>
        </table>
        <div id="paging_btn_div" class="text-right" data-limit="5">
        </div>
    </div>
</div>
    <script src="../plugin/paging/bootstrap.paging.js"></script>
<script>
    $(function(){
        $("#paging_btn_div").pageBtnCallback(function (start,limit) {
            $("#paging_query_form").find("input[name='paging_start']").val(start);
            $("#paging_query_form").find("input[name='paging_limit']").val(limit);
            $("#paging_query_form").submit();
        });
        $("#paging_btn_div").pageBtnFresh(${paging_start},${paging_count});
        $("#list_tbody tr").click(function(){
            var id=$(this).children("th").html();
            window.location="detail?id="+id;
        });
    });
</script>
</body>
</html>