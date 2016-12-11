//包含分页逻辑、查询模块的显示与隐藏
//其中 id=qry_form 的form是查询form
//需要加上这个div  <div id="paging_div" data-start="${query.start}" data-count="${count}" data-show_qry="${query.show_qry}" data-limit="${query.limit}">
var count=$("#paging_div").attr("data-count");
var start=$("#paging_div").attr("data-start");
var limit=$("#paging_div").attr("data-limit");
var show_qry_flag=$("#paging_div").attr("data-show_qry");

function qry_page(start){
	$("#qry_form").append("<input type='hidden' name='start'value='"+start+"'/>");
	$("#qry_form").append("<input type='hidden' name='limit'value='"+limit+"'/>");
	if(show_qry_flag=="1"){
		$("#qry_form").append("<input type='hidden' name='show_qry' value=1 />");
	}
	$("#qry_form").submit();
	
}
function paging(){
	if (typeof(limit) == "undefined" ||limit=="") {
		  console.log("默认的每页15个数据");
		  limit=10;
	}
	if (typeof(count) == "undefined" ||count=="") { 
		 alert("需要返回分页查询的总数");
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
	var paging_html="<nav class='text-right'><ul class='pagination'>";
	if(now_page==1){
		paging_html+="<li class='disabled'><a href='javascript:;' aria-label='第一页'><span aria-hidden='true'>&laquo;</span></a></li>";
		paging_html+="<li class='disabled'><a href='javascript:;' aria-label='上一页'><span aria-hidden='true'>&lsaquo;</span></a></li>";
	}else{
		paging_html+="<li><a href='javascript:qry_page(0);' aria-label='第一页'><span aria-hidden='true'>&laquo;</span></a></li>";
		paging_html+="<li><a href='javascript:qry_page("+last_page+");' aria-label='上一页'><span aria-hidden='true'>&lsaquo;</span></a></li>";
	}
	paging_html+="<li><span aria-hidden='true'>第"+now_page+"页</span></li>";
	paging_html+="<li><span aria-hidden='true'>共"+total_page+"页</span></li>";
	paging_html+="<li><span aria-hidden='true'>共"+count+"条</span></li>";
	if(total_page==now_page || total_page==0){
		paging_html+="<li class='disabled'><a href='javascript:;' aria-label='下一页'><span aria-hidden='true'>&rsaquo;</span></a></li>";
		paging_html+="<li class='disabled'><a href='javascript:;' aria-label='最后页'><span aria-hidden='true'>&raquo;</span></a></li>";
	}else{
		paging_html+="<li><a href='javascript:qry_page("+next_page+");' aria-label='下一页'><span aria-hidden='true'>&rsaquo;</span></a></li>";
		paging_html+="<li><a href='javascript:qry_page("+((total_page-1)*limit)+");' aria-label='最后页'><span aria-hidden='true'>&raquo;</span></a></li>";
	}
	paging_html+="</ul></nav>";
	$("#paging_div").html(paging_html);
}

$(function(){
		//console.log("show_qry:"+show_qry);
		if(show_qry_flag!="1"){
			$("#qry_form").addClass("sr-only");
		}
		$("#add_form").addClass("sr-only");
		$("#qry_btn").click(function(){
			$("#add_form").addClass("sr-only");
			$("#list_table").removeClass("sr-only");
			$("#add_btn").removeClass("active");
			$("#qry_btn").toggleClass("active");
			$("#qry_form").toggleClass("sr-only");
		});
		$("#add_btn").click(function(){
			$("#add_btn").toggleClass("active");
			$("#add_form").toggleClass("sr-only");
			$("#qry_form").addClass("sr-only");
			$("#list_table").addClass("sr-only");
			$("#qry_btn").removeClass("active");
		});
		$("#qry_submit_btn").click(function(){
			$("#qry_form").append("<input type='hidden' name='show_qry' value=1 />");
			qry_page(0);
		});
		paging();
});