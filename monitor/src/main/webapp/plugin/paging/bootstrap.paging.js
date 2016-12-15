/***
 * bootstrap.paging.js  v1.0
 * 该插件用于（基于bootstrap的）动态分页页面（非ajax）
 * 使用该插件，需要页面中：
 * 一个 form id默认为：paging.query.form
 *     其中，form中的查询字段需要返回
 * 一个 div id默认为：paging.btn.div
 *    其中，div有以下几个属性需要从后台返回数据
 *	  data-count：查询条件下，查出数据总条数
 *	  data-start：起始条数
 *	  data-limit：每页展示条数
 */
var query_form_id="paging_query_form";
var paging_div_id="paging_btn_div";
var query_form_start_input_name="paging_start";
var query_form_limit_input_name="paging_limit";

//===================================================
var count=$("#"+paging_div_id).attr("data-count");
var start=$("#"+paging_div_id).attr("data-start");
var limit=$("#"+paging_div_id).attr("data-limit");
function toPage(start){
	$("#"+query_form_id).append("<input type='hidden' name='"+query_form_start_input_name+"'value='"+start+"'/>").append("<input type='hidden' name='"+query_form_limit_input_name+"'value='"+limit+"'/>").submit();
}
function pagingBtnInit(){
	if (typeof(limit) == "undefined" ||limit=="") {
		alert("未发现设置每页显示数量，应在controller中设置并回传至页面");
		return ;
	}
	if (typeof(count) == "undefined" ||count=="") { 
		 alert("未发现查询的总数量，应在controller中设置并回传至页面");
		return ;
	}
	if (typeof(start) == "undefined" ||start=="") {
		start=0;
	}
	count=parseInt(count);
	start=parseInt(start);
	limit=parseInt(limit);
	console.log("count:"+count);
	console.log("start:"+start);
	console.log("limit:"+limit);
	var last_page=start-limit;
	if(last_page<0){
		last_page=0;
	}
	var now_page=start/limit+1;
	var next_page=start+limit;
	var total_page=Math.ceil(count/limit);
	if(total_page==0){
		total_page=1;
	}
	var paging_html="<nav class='text-right'><ul class='pagination'>";
	if(now_page==1){
		paging_html+="<li class='disabled'><a href='javascript:;' aria-label='第一页'><span aria-hidden='true'>&laquo;</span></a></li>";
		paging_html+="<li class='disabled'><a href='javascript:;' aria-label='上一页'><span aria-hidden='true'>&lsaquo;</span></a></li>";
	}else{
		paging_html+="<li><a href='javascript:toPage(0);' aria-label='第一页'><span aria-hidden='true'>&laquo;</span></a></li>";
		paging_html+="<li><a href='javascript:toPage("+last_page+");' aria-label='上一页'><span aria-hidden='true'>&lsaquo;</span></a></li>";
	}
	paging_html+="<li><span aria-hidden='true'>第"+now_page+"页</span></li>";
	paging_html+="<li><span aria-hidden='true'>共"+total_page+"页</span></li>";
	paging_html+="<li><span aria-hidden='true'>"+count+"条</span></li>";
	if(total_page==now_page || total_page==0){
		paging_html+="<li class='disabled'><a href='javascript:;' aria-label='下一页'><span aria-hidden='true'>&rsaquo;</span></a></li>";
		paging_html+="<li class='disabled'><a href='javascript:;' aria-label='最后页'><span aria-hidden='true'>&raquo;</span></a></li>";
	}else{
		paging_html+="<li><a href='javascript:toPage("+next_page+");' aria-label='下一页'><span aria-hidden='true'>&rsaquo;</span></a></li>";
		paging_html+="<li><a href='javascript:toPage("+((total_page-1)*limit)+");' aria-label='最后页'><span aria-hidden='true'>&raquo;</span></a></li>";
	}
	paging_html+="</ul></nav>";
	$("#"+paging_div_id).html(paging_html);
}

$(function(){
	pagingBtnInit();
});