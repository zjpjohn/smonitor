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
    <hr class="margin-bottom-xs">

    <#--添加form-->
    <form id="add_form" action="toadd" method="post">
        <div class="row form-inline margin-top-sm">
            <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>分类名称：</label><input name="name"  type="text" class="form-control"/></div>
            <div class="append-div"></div>
        </div>
        <div class="row text-center margin-top-md">
            <button type="submit" class="btn btn-danger">增加</button>
            <button type="reset" class="btn btn-default">重置</button>
        </div>
        <hr>
    </form>
</div>
</body>
</html>