package com.harlan.smonitor.monitor.web.bean;

public class PageQuery {
	private Integer start;
	private Integer limit;
	private Integer show_qry;
	public Integer getStart() {
		if(start==null){
			start=0;
		}
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLimit() {
		if(limit==null){
			limit=7;
		}
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getShow_qry() {
		return show_qry;
	}
	public void setShow_qry(Integer show_qry) {
		this.show_qry = show_qry;
	}
	@Override
	public String toString() {
		return "PageQuery [start=" + start + ", limit=" + limit + ", show_qry=" + show_qry + "]";
	}
	
}
