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
                        <div class="col-xs-6 col-md-4 margin-bottom-sm"><label>报警阀值：</label><input name="alarmTimes"  type="text" class="form-control middle-input"/></div>
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
<script>
    var CHECK_LIST_DIV_ID;
    var MONITOR_TYPE;
    var CHECK_FIELDS_MAP;
    var CHECK_TYPE_NAME_MAP;
    $.fn.serializeObject = function()
    {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function() {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return o;
    };
    /**
     * 初始化添加检查项的div，并获得焦点
     * @param check_list_div_id
     */
    function showAddCheck(check_list_div_id) {
        CHECK_LIST_DIV_ID=check_list_div_id;
        //初始化添加一行（没有删除按钮）
        resetAddCheck();
        if(MONITOR_TYPE==""){
            $(".check-modal-add-msg").removeClass("sr-only");
        }else{
            $(".check-modal-add-msg").addClass("sr-only");
        }
        $("#check_modal").modal("show");
    }
    function resetAddCheck() {
        //初始化
        $("#check_runtime_div").html("");
        $("#check_append_div").html("");
        addRuntimeRow(false);
        $("#check_add_form")[0].reset();
    }
    function setMaps(checkFieldsMap,checkTypeNameMap) {
        CHECK_FIELDS_MAP=checkFieldsMap;
        CHECK_TYPE_NAME_MAP=checkTypeNameMap;
    }
    /**
     * 根据监控项type初始化检查项类型
     * 初始化添加检查项div
     * @param mType
     */
    function setMonitorType(mType) {
        resetAddCheck();
        $("#check_type").html("<option value=''>-请选择-</option>");
        //将monitor的type存在变量中，常用
        MONITOR_TYPE=mType;
        CHECK_TYPE_NAME_MAP={};
        CHECK_FIELDS_MAP={};
        //修改 monitor 类型，之前填写的 check要清空
        $(CHECK_LIST_DIV_ID).html("");
        //查询这个类型的监控，有哪些类型的检查项
        if(mType!=""){
            $.ajax({
                "url":"checktype",
                "type":"post",
                "data":{"mtype":mType},
                "dataType":"json",
                "success":function(data,desc1){
                    if(data.success==true){
                        $.each(data.obj,function(i,item){
                            CHECK_TYPE_NAME_MAP[item.typeValue]=item.name;
                            $("#check_type").append("<option value='"+item.typeValue+"'>"+item.name+"</option>");
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
    function getValNotNull(obj){
        if(obj.val()==""){
            obj.parent("div").addClass("has-error");
            alert("此处内容为必填");
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
    function checkRowHtml(obj) {
        var html="<form class='check_row_form'>";
        html+="<input type='hidden' name='type' value='"+obj.type+"'/>";
        html+='<div class="row"><div class="col-xs-6 col-md-6 margin-bottom-sm"><h4>检查项:</h4></div></div>';
        html+='<div class="row form-inline">';

        if(typeof(obj.id) != "undefined"){
            html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>ID：</label><input type='text' name='id' class='form-control' value='"+obj.id+"' readonly/></div>";
        }
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>类型：</label><input type='text' class='form-control' value='"+CHECK_TYPE_NAME_MAP[obj.type]+"' readonly/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>名称：</label><input type='text' name='name' class='form-control' value='"+obj.name+"'/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>报警阀值：</label><input type='text' name='alarmTimes' class='form-control' value='"+obj.alarmTimes+"'/></div>";
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
        return html;
    }
    function getFieldsHtml(fields, obj) {
        var fhtml="";
        $.each(fields,function(i,item){
            var val=obj[item.fieldName];
            if (typeof(val) == "undefined") {
                val="";
            }
            fhtml+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input value='"+val+"' type='text' class='form-control' name='"+item.fieldName+"'/></div>";
        });
        return fhtml;
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
        var checkList=new Array();
        $.each($(".check_row_form"),function(i,item){
            saveCheckRuntime(item);
            var checkJsonObj=getCheckJsonObj(item);
            $(item).find("input[name='cronList']").remove();
            checkList[i]=checkJsonObj;
        });
        monitorJson.checkList=checkList;
        return monitorJson;

    }
    function getCheckJsonObj(formid) {
        var checkJsonObj=$(formid).serializeObject();
        //如果只有一个执行时间，要转化成数组
        if(typeof(checkJsonObj.cronList)=="string"){
            var item = new Array(1);
            item[0] = checkJsonObj.cronList;
            checkJsonObj.cronList=item;
        }
        return checkJsonObj;
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
            var  append_html=checkRowHtml(checkJsonObj);
            $(CHECK_LIST_DIV_ID).append(append_html);
            $("#check_modal").modal("hide");
            $("#check_add_form").find("input[name='cronList']").remove();
        });
    });

</script>