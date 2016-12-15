<!DOCTYPE html>
<html>
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
    <form id="paging_query_form" action="list" method="post">
        <div class="row form-inline">
            <div class="col-xs-4 margin-top-sm"><label>名称：</label><input name="mark" value="${admin.mark}" type="text" class="form-control"></div>
            <div class="col-xs-6 col-md-4 margin-top-sm"><label>类型：</label>
                <select class="form-control" name="type">
                    <option value="">-请选择-</option>
                <#list types as type>
                    <option value="${types}">${types}</option>
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

<#--列表div-->
    <div  id="list_table" >
        <table class="table table-hover">
            <thead>
            <tr>
                <th>名称</th>
                <th>分组</th>
                <th>类型</th>
            </tr>
            </thead>
            <tbody id="list_tbody">
            <#list list as monitor>
            <tr>
                <th scope="row">${monitor.name}</th>
                <td>${monitor.groupId}</td>
                <td>${monitor.type}</td>
            </tr>
            </#list>
            </tbody>
        </table>
        <div id="paging_btn_div" data-start="${paging_start}" data-count="${paging_count}" data-limit="${paging_limit}">
        </div>
    </div>
</div>
    <script src="../plugin/paging/bootstrap.paging.js"></script>
</body>
</html>