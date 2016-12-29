<form id="check_add_form" style="display: none">
    <input type="hidden" name="monitor.type" id="check_hidden_monitor_type">
    <input type="hidden" name="check.serial" class="check_serial_hidden">
    <input type="hidden" name="runtimes" id="check_runtime">
    <div class="row">
        <div class="col-xs-6 col-md-6 margin-bottom-sm">
            <h4>检查项:</h4>
        </div>
    </div>
    <div class="row form-inline">
        <div class="col-xs-6 col-md-4 margin-bottom-sm">
            <label>check类型：</label>
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
    <div class="row">
        <div class="col-xs-12 col-md-12 margin-bottom-sm text-center">
            <button id="check_add_btn" type="button" class="btn btn-warning">添加项</button>
        </div>
    </div>
</form>
<script>
    var CHECK_LIST_DIV_ID;
    var CHECK_TYPE_MAP;
    function resetAddCheckRow(check_list_div_id) {
        CHECK_LIST_DIV_ID=check_list_div_id;
        //初始化添加一行（没有删除按钮）
        addRuntimeRow(false);
        $("#check_add_form").show();
    }
    function setMonitorType(mType) {
        console.log("234");
        //将monitor的type存在input中，随表单提交
        $("#check_hidden_monitor_type").val(mType);
        //查询这个类型的监控，有哪些类型的检查项
        $.ajax({
            "url":"checktype",
            "type":"post",
            "data":{"mtype":mType},
            "dataType":"json",
            "success":function(data,desc1){
                if(data.success==true){
                    //console.log(data.obj);
                    CHECK_TYPE_MAP=new Object();
                    $.each(data.obj,function(i,item){
                        $("#check_type").append("<option value='"+item.typeValue+"'>"+item.name+"</option>");
                        CHECK_TYPE_MAP[item.typeValue]=item.name;
                    });
                    console.log(CHECK_TYPE_MAP);
                }else{
                    console.log("exception..."+data.msg);
                }
            },
            "error":function(xhr,err1,err2){
            }
        });
    }
    function addRuntimeRow(couldDelete) {
        var html='<div class="row form-inline runtime-row-div">';
        html+='<div class="col-xs-2 margin-bottom-sm"><label>执行时间：</label></div>';
        html+='<div class="col-xs-3 margin-bottom-sm">';
        html+='<input type="text" class="form-control small-input" placeholder="秒"/><span>秒</span>';
        html+='<input type="text" class="form-control small-input" placeholder="分"/><span>分</span>';
        html+='<input type="text" class="form-control small-input" placeholder="时"/><span>时</span>';
        html+='</div>';
        html+='<div class="col-xs-3 margin-bottom-sm">';
        html+='<input type="text" class="form-control small-input" placeholder="日"/><span>日</span>';
        html+='<input type="text" class="form-control small-input" placeholder="月"/><span>月</span>';
        html+='<input type="text" class="form-control small-input" placeholder="周"/><span>周</span>';
        html+='</div>';
        html+='<div class="col-xs-4 margin-bottom-sm">';
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
    function saveCheckRuntime() {
        var check=true;
        var runtimeInputVal="";
        $.each($(".runtime-row-div"),function(i,item){
            for(var i=0;i<6;i++){
                var value=getValNotNull($(item).find("input").eq(i));
                //console.log(i);
                if(check==true && value!=""){
                    runtimeInputVal+=value;
                    runtimeInputVal+=" ";
                }else{
                    check=false;
                    return ;
                }
            }
            var yy=$(item).find("input").eq(6).val();
            if(yy!=""){
                runtimeInputVal+=yy;
                runtimeInputVal+=" ";
            }
            runtimeInputVal=runtimeInputVal.substr(0,runtimeInputVal.length-1);
            console.log("runtimeInputVal:"+runtimeInputVal);
            runtimeInputVal+="@";
        });
        runtimeInputVal=runtimeInputVal.substr(0,runtimeInputVal.length-1);
        if(runtimeInputVal.length>0){
            $("#check_runtime").val(runtimeInputVal);
        }else{
            check=false;
        }
        return check;

    }
    function checkItemInputs(obj) {
        var html='<div class="row"><div class="col-xs-6 col-md-6 margin-bottom-sm"><h4>检查项:</h4></div></div>';
        html+='<div class="row form-inline">';
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>ID：</label><input type='text' class='form-control' value='"+obj.id+"' readonly/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>类型：</label><input type='text' class='form-control' value='"+CHECK_TYPE_MAP[obj.type]+"' readonly/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>名称：</label><input type='text' class='form-control' value='"+obj.name+"'/></div>";
        html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>报警阀值：</label><input type='text' class='form-control' value='"+obj.alarmTimes+"'/></div>";
        html+=getFieldsHtml(obj.fields,obj);
        html+='</div>';
        $.each(obj.cronList,function(i,item){
            html+='<div class="row form-inline">';
            html+='<div class="col-xs-2 margin-bottom-sm"><label>执行时间：</label></div>';
            html+='<div class="col-xs-3 margin-bottom-sm">';
            var runtimeArray=item.split(" ");
            html+='<input value="'+runtimeArray[0]+'" type="text" class="form-control small-input" placeholder="秒"/><span>秒&nbsp;</span>';
            html+='<input value="'+runtimeArray[1]+'" type="text" class="form-control small-input" placeholder="分"/><span>分&nbsp;</span>';
            html+='<input value="'+runtimeArray[2]+'" type="text" class="form-control small-input" placeholder="时"/><span>时&nbsp;</span>';
            html+='</div>';
            html+='<div class="col-xs-3 margin-bottom-sm">';
            html+='<input value="'+runtimeArray[3]+'" type="text" class="form-control small-input" placeholder="秒"/><span>日&nbsp;</span>';
            html+='<input value="'+runtimeArray[4]+'" type="text" class="form-control small-input" placeholder="分"/><span>月&nbsp;</span>';
            html+='<input value="'+runtimeArray[5]+'" type="text" class="form-control small-input" placeholder="时"/><span>周&nbsp;</span>';
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
        return html;
    }
    function getFieldsHtml(fields, obj) {
        var fhtml="";
        $.each(fields,function(i,item){
            var val=obj[item.fieldName];
            if (typeof(val) == "undefined") {
                val="";
            }
            fhtml+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input value='"+val+"' type='text' class='form-control' name='item.fieldName'/></div>";
        });
        return fhtml;
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
                    "data":{"mtype":$("#check_hidden_monitor_type").val(),"ctype":ctype_val},
                    "dataType":"json",
                    "success":function(data,desc1){
                        if(data.success==true){
                            //console.log(data.obj);
                            var append_html="";
                            $.each(data.obj,function(i,item){
                                append_html+="<div class='col-xs-6 col-md-4 margin-bottom-sm'><label>"+item.name+"：</label><input name='"+item.fieldName+"'  type='text' class='form-control'/></div>";
                            });
                            //console.log(append_html);
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
        //添加检查项
        $("#check_add_btn").click(function () {
            //先保存执行时间
            var checked=saveCheckRuntime();
            if(checked==false){
                return;
            }
            //提交session预保存
            $.ajax({
                "url":"form2json",
                "type":"post",
                "data":$("#check_add_form").serialize(),
                "dataType":"json",
                "success":function(data,desc1){
                    if(data.success==true){
                        //console.log(data.obj);
                        var  append_html=checkItemInputs(data.obj);
                        $(CHECK_LIST_DIV_ID).append(append_html);
                    }else{
                        console.log("exception..."+data.msg);
                    }
                },
                "error":function(xhr,err1,err2){
                }
            });
        });
    });

</script>