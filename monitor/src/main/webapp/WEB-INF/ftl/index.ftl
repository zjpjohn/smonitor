<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>Smonitor</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="plugin/bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="plugin/font-awesome/4.5.0/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="plugin/ionicons/2.0.1/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="plugin/AdminLTE/2.3.11/css/AdminLTE.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="plugin/AdminLTE/2.3.11/css/skins/_all-skins.min.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <#include "/include/index_head.ftl">
    <#include "/include/index_sidebar.ftl">

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper" style="background-color:#ffffff">
        <iframe   id="dashboard_content" width="100%"  frameborder="0">

        </iframe>
        <!-- Main content -->

        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <footer class="main-footer">
        <div class="pull-right hidden-xs">
            <b>Version</b> 2.3.8
        </div>
        <strong>Copyright &copy; 2014-2016 <a href="http://almsaeedstudio.com">Almsaeed Studio</a>.</strong> All rights
        reserved.
    </footer>

    <!-- Control Sidebar -->
    <#include "/include/index_setting.ftl">
    <!-- Add the sidebar's background. This div must be placed
         immediately after the control sidebar -->
    <div class="control-sidebar-bg"></div>
</div>
<!-- ./wrapper -->

<!-- jQuery 2.2.3 -->
<script src="plugin/jquery/2.2.3/jquery.min.js"></script>
<!-- jQuery UI 1.11.4
<script src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>-->
<!-- Resolve conflict in jQuery UI tooltip with Bootstrap tooltip -->

<!-- Bootstrap 3.3.6 -->
<script src="plugin/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<!-- AdminLTE App -->
<script src="plugin/AdminLTE/2.3.11/js/app.min.js"></script>
<!-- AdminLTE dashboard demo (This is only for demo purposes)
<script src="dist/js/pages/dashboard.js"></script>-->
<!-- AdminLTE for demo purposes
<script src="dist/js/demo.js"></script>-->
<script>
    var ifm= document.getElementById("dashboard_content");
    ifm.height=document.documentElement.clientHeight;
    $(function () {
        $(".treeview-menu a").click(function () {
            var src=$(this).data("href");
            console.log("src="+src);
            $("#dashboard_content").prop("src",src);
        });
    });
</script>
</body>
</html>
