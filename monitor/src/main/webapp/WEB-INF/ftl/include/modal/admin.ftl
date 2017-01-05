<!-- 模态框 -->
<style>
    .admin-modal-row{
        cursor: pointer;
    }
    .admin-pagination{
        margin-right: 40px;
    }
</style>
<link href="../plugin/bootstrap/iCheck/1.0.1/skins/square/green.css" rel="stylesheet">
<div id="admin_modal" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" data-backdrop="true" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">管理员编辑</h4>
            </div>
            <div class="modal-body">
                <div class="row form-inline margin-bottom-sm">
                    <div class="col-xs-12" id="admin_modal_selected_div">
                        <label>已选管理员：</label>
                    </div>
                </div>
                <div class="row form-inline margin-bottom-sm">
                    <div class="col-xs-12">
                        <label>所有管理员：</label>
                        <input  type="text" class="form-control" placeholder="ID查询"/>
                    </div>
                </div>
                <div class="row form-inline margin-bottom-sm">
                    <div class="col-xs-4">
                    </div>
                    <div class="col-xs-4">
                        <label>ID</label>
                    </div>
                    <div class="col-xs-4">
                        <label>通知类型</label>
                    </div>
                </div>
                <hr style="margin-left: 30px;margin-right: 30px">
                <div id="admin_modal_list_div"></div>
                <div class="row form-inline" >
                    <div id="pagingBtnGroupDiv" class="admin-pagination text-right" data-limit="5"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="admin_modal_save_btn" type="button" class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</div>
<script src="../plugin/bootstrap/iCheck/1.0.1/icheck.min.js"></script>
<script src="../plugin/paging/bootstrap.paging.js"></script>
<script>
    var DIV_ID;
    /**
     * 初始化选择联系人的modal
     * 传入div id，会在这个div中放入选中的admin button，并且会在div中寻找input把admin值放在input中
     * @param divId 存放admin标签div的id
     */
    function adminModalInit(adminDivId) {
        DIV_ID=adminDivId;
        $("#admin_modal").modal("show");
        $("#admin_modal_selected_div").find("button").remove();
        $.each($(DIV_ID).find("button"),function (i,item) {
            $("#admin_modal_selected_div").append('<button onclick="delAdmin(this);" class="btn btn-default">'+$(item).text()+'</button>');
        });
        qryAdmin(0,$("#pagingBtnGroupDiv").data("limit"));
        //保存按钮绑定点击事件
        $("#admin_modal_save_btn").click(function () {
            $(DIV_ID).find("button").remove();
            $(DIV_ID).find("input").remove();
            $.each($("#admin_modal_selected_div").find("button"),function(i,item){
                $(DIV_ID).append('<input type="hidden" name="adminList" value="'+$(item).text()+'">');
                $(DIV_ID).append('<button class="btn btn-default" disabled>'+$(item).text()+'</button>');
            });
            $("#admin_modal").modal("hide");
        });
    }
    /**
     * 分页列表中添加一个联系人
     * @param item 联系人对象
     */
    function addRow(item) {
        var html='<div class="admin-modal-row row form-inline margin-bottom-sm"><div class="col-xs-2"></div><div class="col-xs-2">';
        html+='<input type="checkbox" class="icheck-input">';
        html+='</div><div class="col-xs-4 admin-modal-row-id">';
        html+=item.id;
        html+='</div><div class="col-xs-4">';
        html+=item.type;
        html+='</div></div>';
        $("#admin_modal_list_div").append(html);
    }
    //分页查询方法
    function qryAdmin(startNum,limitNum) {
        $.ajax({
            "url":"qryadmin",
            "type":"post",
            "data":{start:startNum,limit:limitNum},
            "dataType":"json",
            "success":function(data){
                if(data.success==true){
                    //console.log(data.obj);
                    $(".admin-modal-row").remove();
                    $("#pagingBtnGroupDiv").pageBtnFresh(startNum,data.obj.count);
                    $.each(data.obj.list,function(i,item){
                        addRow(item);
                    });
                    //在页面添加了元素后绑定事件
                    $('.icheck-input').iCheck({
                        checkboxClass: 'icheckbox_square-green',
                        radioClass: 'iradio_square-green',
                        increaseArea: '20%'
                    });
                    $(".admin-modal-row").click(function () {
                        $(this).find(".icheck-input").iCheck('toggle');
                    });
                    $('.icheck-input').on('ifChecked', function(){
                        var text=$(this).parents(".admin-modal-row").find(".admin-modal-row-id").text();
                        var hasBtn=false;
                        $.each($("#admin_modal_selected_div").find("button"),function (i,item) {
                            if($(item).text()==text){
                                hasBtn=true;
                            }
                        });
                        if(hasBtn==false){
                            $("#admin_modal_selected_div").append('<button onclick="delAdmin(this);" class="btn btn-default">'+text+'</button>');
                        }
                    });
                    $('.icheck-input').on('ifUnchecked', function(){
                        var text=$(this).parents(".admin-modal-row").find(".admin-modal-row-id").text();
                        $.each($("#admin_modal_selected_div").find("button"),function (i,item) {
                            if($(item).text()==text){
                                $(item).remove();
                            }
                        });
                    });
                }else{
                    console.log("exception..."+data.msg);
                }
            },
            "error":function(xhr,err1,err2){
            }
        });
    }
    function delAdmin(obj) {
        $(obj).remove();
    }
    $(function () {
        $("#pagingBtnGroupDiv").pageBtnCallback(function (start,limit) {
            qryAdmin(start,limit);
        });
    })
</script>