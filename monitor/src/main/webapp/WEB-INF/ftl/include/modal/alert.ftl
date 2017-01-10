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
                <button class="btn btn-primary" id="confirm_modal_btn" type="button">确认</button>
            </div>
        </div>
    </div>
</div>
<script>
    /**
     * 弹出对话框，提示信息 msg
     * 点击确认后，回调函数 fn
     * @param msg 需要提示的信息
     * @param fn 回调的操作函数
     */
    function confirmAndRun(msg,fn) {
        $("#confirmModal").find(".modal-body").html(msg);
        $("#confirmModal").modal("show");
        $("#confirm_modal_btn").unbind("click");
        $("#confirm_modal_btn").click(fn);
    }
    /**
     * 类似alert，提示信息
     * @param msg 提示信息
     */
    function showMsg(msg) {
        $("#showMsgModal").find(".modal-body").html(msg);
        $("#showMsgModal").modal("show");
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
</script>