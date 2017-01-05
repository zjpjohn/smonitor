;(function($,window,document,undefined){
    var PAGINATION;
    var CALLBACK_FUNCTION;
    function getInitHtml() {
        var html='<nav>';
        html+='<ul class="pagination" style="margin:0">';
        html+='<li><a aria-label="第一页">&laquo;</a></li>';
        html+='<li><a aria-label="上一页">&lsaquo;</a></li>';
        html+='<li class="disabled"><span aria-hidden="true">NaN/NaN页</span></li>';
        html+='<li class="disabled"><span aria-hidden="true">NaN条</span></li>';
        html+='<li><a aria-label="下一页">&rsaquo;</a></li>';
        html+='<li><a aria-label="最后页">&raquo;</a></li>';
        html+='</ul>';
        html+='</nav>';
        return html;
    }

    //刷新翻页按钮
    function pageingFresh(start,count,limit) {
        var now_page=start/limit+1;
        var total_page=(Math.ceil(count/limit)==0?1:Math.ceil(count/limit));
        var next_page_start=start+limit;
        var pre_page_start=start-limit;
        var last_page_start=(total_page-1)*limit;
        if(now_page==1){
            PAGINATION.children("li").eq(0).prop("class","disabled");
            PAGINATION.children("li").eq(1).prop("class","disabled");
        }else{
            PAGINATION.children("li").eq(0).removeProp("class").find("a").unbind("click").bind("click",function () {
                CALLBACK_FUNCTION(0,limit);
            });
            PAGINATION.children("li").eq(1).removeProp("class").find("a").unbind("click").bind("click",function () {
                CALLBACK_FUNCTION(pre_page_start,limit);
            });
        }
        if(next_page_start<count){
            PAGINATION.children("li").eq(4).removeProp("class").find("a").unbind("click").bind("click",function () {
                CALLBACK_FUNCTION(next_page_start,limit);
            });
            PAGINATION.children("li").eq(5).removeProp("class").find("a").unbind("click").bind("click",function () {
                CALLBACK_FUNCTION(last_page_start,limit);
            });
        }else{
            PAGINATION.children("li").eq(4).prop("class","disabled");
            PAGINATION.children("li").eq(5).prop("class","disabled");
        }
        PAGINATION.children("li").eq(2).find("span").text(now_page+"/"+total_page+"页");
        PAGINATION.children("li").eq(3).find("span").text(count+"条");
    }
    $.fn.pageBtnFresh = function(start,count) {
        if(typeof(PAGINATION)=="undefined"){
            this.html(getInitHtml());
            PAGINATION=this.find(".pagination");
        }
        var limit =this.data("limit");
        if(typeof limit =="undefined" || limit==""){
            alert(this.prop("id")+" need data-limit");
            return ;
        }
        if(typeof count =="undefined"){
            alert("pageBtnFresh need count");
            return;
        }
        if(typeof start =="undefined"){
            start=0;
        }
        start=parseInt(start);
        count=parseInt(count);
        limit=parseInt(limit);
        pageingFresh(start,count,limit);
    }
    $.fn.pageBtnCallback = function(fn) {
        CALLBACK_FUNCTION=fn;
    }
})(jQuery,window,document);