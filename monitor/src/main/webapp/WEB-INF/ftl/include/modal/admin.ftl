<!-- 模态框 -->
<style>
    .admin-modal-row{
        cursor: pointer;
    }
    .admin-pagination{
        margin-right: 40px;

    }
    .admin-pagination .pagination{
        margin: 0 !important;
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
                <div class="row form-inline">
                    <nav class="text-right admin-pagination">
                        <ul class="pagination">
                            <li><a href='javascript:qryAdmin(0);' aria-label='第一页'>&laquo;</a></li>
                            <li><a href='javascript:;' aria-label='上一页'>&lsaquo;</a></li>
                            <li class='disabled'><span aria-hidden='true'>NaN/NaN页</span></li>
                            <li class='disabled'><span aria-hidden='true'>NaN条</span></li>
                            <li><a href='javascript:;' aria-label='下一页'>&rsaquo;</a></li>
                            <li><a href='javascript:;' aria-label='最后页'>&raquo;</a></li>
                        </ul>
                    </nav>
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
<script>
    var div_id;
    /**
     * 使用该方法需要
     * 传入div id，会在这个div中放入选中的admin button，并且会在div中寻找input把admin值放在input中
     * @param divId 存放admin标签div的id
     */
    function adminModalInit(divId) {
        div_id=divId;
        $("#admin_modal").modal("show");
        qryAdmin(0);
        //保存按钮绑定点击事件
        $("#admin_modal_save_btn").click(function () {
            $(div_id).find("button").remove();
            var adminString="";
            $.each($("#admin_modal_selected_div").find("button"),function(i,item){
                //console.log($(item).text());
                adminString+=$(item).text();
                adminString+="|";
                $(div_id).append('<button class="btn btn-default" disabled>'+$(item).text()+'</button>');
            });

            $(div_id).children("input").val(adminString.substr(0,adminString.length-1));
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
    //刷新翻页按钮
    function pageingFresh(start, limit, count) {
        //console.log("start:"+start+" limit:"+limit+" count:"+count);
        count=parseInt(count);
        start=parseInt(start);
        limit=parseInt(limit);
        var now_page=start/limit+1;
        var total_page=Math.ceil(count/limit);
        if(total_page==0){
            total_page=1;
        }
        var next_page_start=start+limit;
        var pre_page_start=start-limit;
        var last_page_start=(total_page-1)*limit;
        if(now_page==1){
            $(".pagination").children("li").eq(0).prop("class","disabled");
            $(".pagination").children("li").eq(1).prop("class","disabled");
        }else{
            $(".pagination").children("li").eq(0).removeProp("class").find("a").prop("href","javascript:qryAdmin(0);");
            $(".pagination").children("li").eq(1).removeProp("class").find("a").prop("href","javascript:qryAdmin("+pre_page_start+");");
        }
        if(next_page_start>count){
            $(".pagination").children("li").eq(4).prop("class","disabled");
            $(".pagination").children("li").eq(5).prop("class","disabled");
        }else{
            $(".pagination").children("li").eq(4).removeProp("class").find("a").prop("href","javascript:qryAdmin("+next_page_start+");");
            $(".pagination").children("li").eq(5).removeProp("class").find("a").prop("href","javascript:qryAdmin("+last_page_start+");");
        }
        $(".pagination").children("li").eq(2).find("span").text(now_page+"/"+total_page+"页");
        $(".pagination").children("li").eq(3).find("span").text(count+"条");

    }
    //分页查询方法
    function qryAdmin(startNum) {
        $.ajax({
            "url":"qryadmin",
            "type":"post",
            "data":{start:startNum},
            "dataType":"json",
            "success":function(data,desc1){
                if(data.success==true){
                    //console.log(data.obj);
                    $(".admin-modal-row").remove();
                    pageingFresh(startNum,data.obj.limit,data.obj.count);
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
                        //console.log("选中的id:"+text);
                        var num=$("#admin_modal_selected_div").find("button:contains('"+text+"')").length;
                        //console.log("已选区域中这个id的admin个数："+num);
                        if(num==0){
                            $("#admin_modal_selected_div").append('<button onclick="delAdmin(this);" class="btn btn-default">'+text+'</button>');
                        }
                    });
                    $('.icheck-input').on('ifUnchecked', function(){
                        var text=$(this).parents(".admin-modal-row").find(".admin-modal-row-id").text();
                        //console.log("选中的id:"+text);
                        var num=$("#admin_modal_selected_div").find("button:contains('"+text+"')").length;
                        //console.log("已选区域中这个id的admin个数："+num);
                        if(num==1){
                            $("#admin_modal_selected_div").find("button:contains('"+text+"')").remove();
                        }
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
</script>