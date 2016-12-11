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
    <form id="qry_form" action="list" method="post">
        <div class="row form-inline">
            <div class="col-xs-4 margin-top-sm"><label>帐号：</label><input name="account"  value="${admin.id}"  type="text" class="form-control"></div>
            <div class="col-xs-4 margin-top-sm"><label>备注：</label><input name="mark" value="${admin.mark}" type="text" class="form-control"></div>
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
                <th>ID</th>
                <th>帐号</th>
                <th>类型</th>
                <th>备注</th>
            </tr>
            </thead>
            <tbody id="list_tbody">
            <#list list as admin>
            <tr>
                <th scope="row">${admin.id}</th>
                <td>${admin.account}</td>
                <td>${admin.type}</td>
                <td>${admin.mark}</td>
            </tr>
            </#list>
            </tbody>
        </table>
        <div id="paging_div" data-start="${query.start}" data-count="${count}" data-show_qry="${query.show_qry}" data-limit="${query.limit}">
        </div>
    </div>
</div>
    <script src="../plugin/paging//qry_page.js"></script>
</body>
</html>