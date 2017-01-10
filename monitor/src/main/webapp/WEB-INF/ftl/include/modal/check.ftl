<style>
    .check-modal-add-msg p {
        padding: 15px;
    }
    .runtime-row-div span{
        margin-left: 2px;
        margin-right: 8px;
    }
    .del-runtime-row-btn span{
        margin: 0;
    }
    @media (min-width: 992px){
        .modal-lg {
            width: 992px;
        }
    }
</style>
<link href="../plugin/bootstrap/switch/3.3.2/css/bootstrap-switch.min.css" rel="stylesheet">
<div id="check_modal" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" data-backdrop="true" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">添加检查项</h4>
            </div>
            <div class="modal-body">
                <form id="check_add_form">
                    <div class="row check-modal-add-msg sr-only">
                        <div class="col-xs-12  margin-bottom-sm">
                            <p class="bg-warning">请确认选择监控项的类型</p>
                        </div>
                    </div>
                    <div class="row form-inline">
                        <div class="col-xs-6 col-md-4 margin-bottom-sm">
                            <label>类型：</label>
                            <select class="form-control" id="check_type" name="type">
                                <option value="">-请选择-</option>
                            </select>
                        </div>
                        <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>名称：</label><input name="name" type="text" class="form-control"/></div>
                        <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>通知阀值：</label><input name="alarmTime"  type="text" class="form-control middle-input"/></div>
                        <div id="check_append_div"></div>
                    </div>
                    <div id="check_runtime_div">
                    </div>
                    <div class="row form-inline">
                        <div class="col-xs-3 margin-bottom-sm">
                            <button id="check_add_runtime_btn" type="button" class="btn btn-default">添加执行时间</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button id="check_add_btn" type="button" class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</div>

<div id="showMsgModal" class="modal fade" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="mySmallModalLabel">提示：</h4>
            </div>
            <div class="modal-body">
            </div>
        </div>
    </div>
</div>

<div id="confirmModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title">请确认：</h4>
            </div>
            <div class="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">放弃</button>
                <button id="confirm_modal_btn" type="button" class="btn btn-primary">确认</button>
            </div>
        </div>
    </div>
</div>
<script src="../plugin/bootstrap/switch/3.3.2/js/bootstrap-switch.min.js"></script>
<script>
    //switch全局变量
    $.fn.bootstrapSwitch.defaults.size = 'small';
    var CHECK_LIST_DIV_ID;
    var MONITOR_TYPE="";
    var CHECK_FIELDS_MAP;
    var CHECK_TYPE_NAME_MAP;
    function showMsg(msg) {
        $("#showMsgModal").find(".modal-body").html(msg);
        $("#showMsgModal").modal("show");
    }
    function confirmAndRun(msg,fn) {
        $("#confirmModal").find(".modal-body").html(msg);
        $("#confirmModal").modal("show");
        $("#confirm_modal_btn").unbind("click");
        $("#confirm_modal_btn").click(fn);
    }

    function resetAddCheck() {
        //初始化
        $("#check_runtime_div").html("");
        $("#check_append_div").html("");
        addRuntimeRow(false);
        $("#check_add_form")[0].reset();
    }
    /**
     * 显示添加check的modal
     */
    function showCheckModal() {
        if(MONITOR_TYPE==""){
            $(".check-modal-add-msg").removeClass("sr-only");
        }else{
            $(".check-modal-add-msg").addClass("sr-only");
        }
        for(var typeVal in CHECK_TYPE_NAME_MAP){//遍历CHECK_TYPE_NAME_MAP对象中的属性
            $("#check_type").append("<option value='"+typeVal+"'>"+CHECK_TYPE_NAME_MAP[typeVal]+"</option>");
        }
        resetAddCheck();
        $("#check_modal").modal("show");
    }
    /**
     * 初始化 check modal
     * @param check_list_div html中存放check的div
     * @param monitor_type monitor的类型
     * @param check_type_name_map map数据
     * @param check_fields_map map数据
     */
    function addCheckModalInit(check_list_div,monitor_type,check_type_name_map,check_fields_map) {
        CHECK_LIST_DIV_ID=check_list_div;
        MONITOR_TYPE=monitor_type;
        CHECK_TYPE_NAME_MAP=check_type_name_map;
        CHECK_FIELDS_MAP=check_fields_map;
        $("#check_type").html("<option value=''>-请选择-</option>");
        resetAddCheck();
        if(typeof(CHECK_FIELDS_MAP)=="undefined"){
            CHECK_FIELDS_MAP={};
        }
        $(CHECK_LIST_DIV_ID).html("");
        if(typeof(CHECK_TYPE_NAME_MAP)=="undefined"){
            CHECK_TYPE_NAME_MAP={};
            if(MONITOR_TYPE!=""){
                $.ajax({
                    "url":"checktype",
                    "type":"post",
                    "data":{"mtype":MONITOR_TYPE},
                    "dataType":"json",
                    "success":function(data){
                        if(data.success==true){
                            $.each(data.obj,function(i,item){
                                CHECK_TYPE_NAME_MAP[item.typeValue]=item.name;
                            });
                        }else{
                            console.log("exception..."+data.msg);
                        }
                    },
                    "error":function(xhr,err1,err2){
                    }
                });
            }
        }


    }
    function getValNotNull(obj){
        if(obj.val()==""){
            obj.parent("div").addClass("has-error");
            showMsg("此处内容为必填");
            return "";
        }else{
            obj.parent("div").removeClass("has-error");
            return obj.val();
        }
    }
    function addRuntimeRow(couldDelete) {
        var html='<div class="row form-inline runtime-row-div">';
        html+='<div class="col-xs-2 margin-bottom-sm"><label>执行时间：</label></div>';
        html+='<div class="col-xs-3 margin-bottom-sm">';
        html+='<input type="text" class="form-control small-input" placeholder="秒"/><span>秒</span>';
        html+='<input type="text" class="form-control small-input" placeholder="分"/><span>分</span>';
        html+='<input type="text" class="form-control small-input" placeholder="时"/><span>时</span>';
        html+='</div>';
        html+='<div class="col-xs-4 margin-bottom-sm">';
        html+='<input type="text" class="form-control small-input" placeholder="日"/><span>日</span>';
        html+='<input type="text" class="form-control small-input" placeholder="月"/><span>月</span>';
        html+='<input type="text" class="form-control small-input" placeholder="周"/><span>周</span>';
        html+='</div>';
        html+='<div class="col-xs-3 margin-bottom-sm">';
        html+='<input type="text" class="form-control middle-input" placeholder="年(可选)"/><span>年</span>';
        if(couldDelete){
            html+='<button type="button" class="btn btn-default del-runtime-row-btn"><span class="glyphicon glyphicon-remove"></span></button>';
        }
        html+='</div>';
        html+='</div>';
        $("#check_runtime_div").append(html);
        if(couldDelete){
            $(".del-runtime-row-btn").click(function () {
                $(this).parents(".runtime-row-div").remove();
            });
        }
    }
    /**
     * 将执行时间保存成input
     * @param fromid 可以是添加form，也可以是修改check 的form
     */
    function saveCheckRuntime(fromid) {
        $.each($(fromid).find(".runtime-row-div"),function(i,item){
            var runtimeInputVal="";
            for(var i=0;i<6;i++){
                var value=getValNotNull($(item).find("input").eq(i));
                if(value!=""){
                    runtimeInputVal+=value;
                    runtimeInputVal+=" ";
                }else{
                    return ;
                }
            }
            var yy=$(item).find("input").eq(6).val();
            if(yy!=""){
                runtimeInputVal+=yy;
                runtimeInputVal+=" ";
            }
            runtimeInputVal=runtimeInputVal.substr(0,runtimeInputVal.length-1);
            $(fromid).append("<input type='hidden' name='cronList' value='"+runtimeInputVal+"'/>");
        });

    }
    /**
     * 删除本行check
     * */
    function delCheck(obj) {
        $(obj).parents('.check_row_form').remove();
    }
    /**
     * 讲一个 check json 添加转化成html添加至 div
     * */
    function addCheck2Html(obj,hasSwitchBtn) {
        var html="<form class='check_row_form'>";
        html+="<input type='hidden' name='type' value='"+obj.type+"'/>";
        html+='<div class="row">';
        html+='<div class="col-xs-12 col-md-6 margin-bottom-sm"><h4>检查项:</h4></div>';
        html+='<div class="col-xs-10 col-md-5 margin-bottom-sm text-right">';
        if(hasSwitchBtn==true){
            html+='<label>运行状态：</label>';
            html+='<input type="checkbox" class="check-job-switch"';
            if(obj.state==0){
                html+=' checked ';
            }
            html+=">";
        }
        html+='</div>';
        html+='<div class="col-xs-2 col-md-1 margin-bottom-sm text-right"><button type="button" onclick="delCheck(this);" class="btn btn-default"><span class="glyphicon glyphicon-remove"></span></button></div>';
        html+='</div>';
        html+='<div class="row form-inline">';

        if(typeof(obj.id) != "undefined"){
            html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>ID：</label><input type='text' name='id' class='form-control' value='"+obj.id+"' readonly/></div>";
        }
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>类型：</label><input type='text' class='form-control' value='"+CHECK_TYPE_NAME_MAP[obj.type]+"' readonly/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>名称：</label><input type='text' name='name' class='form-control' value='"+obj.name+"'/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>通知阀值：</label><input type='text' name='alarmTime' class='form-control' value='"+obj.alarmTime+"'/></div>";
        html+=getFieldsHtml(CHECK_FIELDS_MAP[obj.type],obj);
        html+='</div>';
        $.each(obj.cronList,function(i,item){
            html+='<div class="row form-inline runtime-row-div">';
            html+='<div class="col-xs-2 margin-bottom-sm"><label>执行时间：</label></div>';
            html+='<div class="col-xs-3 margin-bottom-sm">';
            var runtimeArray=item.split(" ");
            html+='<input value="'+runtimeArray[0]+'" type="text" class="form-control small-input" placeholder="秒"/><span>秒</span>';
            html+='<input value="'+runtimeArray[1]+'" type="text" class="form-control small-input" placeholder="分"/><span>分</span>';
            html+='<input value="'+runtimeArray[2]+'" type="text" class="form-control small-input" placeholder="时"/><span>时</span>';
            html+='</div>';
            html+='<div class="col-xs-3 margin-bottom-sm">';
            html+='<input value="'+runtimeArray[3]+'" type="text" class="form-control small-input" placeholder="秒"/><span>日</span>';
            html+='<input value="'+runtimeArray[4]+'" type="text" class="form-control small-input" placeholder="分"/><span>月</span>';
            html+='<input value="'+runtimeArray[5]+'" type="text" class="form-control small-input" placeholder="时"/><span>周</span>';
            html+='</div>';
            var yy="";
            if(runtimeArray.length>6){
                yy=runtimeArray[6];
            }
            html+='<div class="col-xs-4 margin-bottom-sm">';
            html+='<input value="'+yy+'" type="text" class="form-control middle-input" placeholder="年(可选)"/><span>年</span>';
            html+='</div>';
            html+='</div>';
        });
        html+='<hr>';
        html+="</form>";
        $(CHECK_LIST_DIV_ID).append(html);
    }

    /**
     * 预保存所有的已添加的check到session
     * 返回serialId，用于确认session
     */
    function getMonitorObj(monitorForm) {
        if($(".check_row_form").length==0){
            return "没有检查项";
        }
        var monitorJson=$(monitorForm).serializeObject();
        if(typeof(monitorJson.adminList)=="undefined"){
            return "没有联系人";
        }
        if(typeof(monitorJson.adminList)=="string"){
            monitorJson.adminList=new Array(monitorJson.adminList);;
        }
        var checkList=new Array();
        $.each($(".check_row_form"),function(i,item){
            saveCheckRuntime(item);
            var checkJsonObj=getCheckJsonObj(item);
            $(item).find("input[name='cronList']").remove();
            checkList.push(checkJsonObj);
        });
        monitorJson.checkList=checkList;
        return monitorJson;

    }
    function getCheckJsonObj(formid) {
        var checkJsonObj=$(formid).serializeObject();
        //如果只有一个执行时间，要转化成数组
        if(typeof(checkJsonObj.cronList)=="string"){
            checkJsonObj.cronList=new Array(checkJsonObj.cronList);
        }
        return checkJsonObj;
    }
    function addChecks2html(checkList) {
        $.each(checkList,function(i,check){
            addCheck2Html(check,true);
        });
        $(".check-job-switch").bootstrapSwitch();
        $('.check-job-switch').on('switchChange.bootstrapSwitch', function(event, state) {
            var checkId=$(this).parents(".check_row_form").find("input[name='id']").val();
            var obj=this;
            var url="";
            if(state){
                //打开
                url="startcheck";
            }else{
                //关闭
                url="pausecheck";
            }
            $.ajax({
                "url":url,
                "type":"post",
                "data":{"mtype":MONITOR_TYPE,"cid":checkId},
                "dataType":"json",
                "success":function(data){
                    if(data.success==true){
                    }else{
                        $(obj).bootstrapSwitch('toggleState');
                        showMsg(data.msg);
                    }
                },
                "error":function(xhr,err1,err2){
                }
            });

        });
    }
    $(function(){
        //查询这个检查项的字段有哪些
        $("#check_type").change(function () {
            var ctype_val=$(this).val();
            $("#check_append_div").html("");
            if(ctype_val!=""){
                $.ajax({
                    "url":"checkfields",
                    "type":"post",
                    "data":{"mtype":MONITOR_TYPE,"ctype":ctype_val},
                    "dataType":"json",
                    "success":function(data,desc1){
                        if(data.success==true){
                            var append_html="";
                            CHECK_FIELDS_MAP[ctype_val]=data.obj;
                            $.each(data.obj,function(i,item){
                                append_html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input name='"+item.fieldName+"'  type='text' class='form-control'/></div>";
                            });
                            $("#check_append_div").append(append_html);
                        }else{
                            console.log("exception..."+data.msg);
                        }
                    },
                    "error":function(xhr,err1,err2){
                    }
                });
            }
        });
        //添加执行时间按钮
        $("#check_add_runtime_btn").click(function () {
            addRuntimeRow(true);
        });
        //保存 按钮 添加检查项
        $("#check_add_btn").click(function () {
            //先保存执行时间
            saveCheckRuntime("#check_add_form");
            var checkJsonObj=getCheckJsonObj("#check_add_form");
            addCheck2Html(checkJsonObj);
            $("#check_modal").modal("hide");
            $("#check_add_form").find("input[name='cronList']").remove();
        });
    });

</script>