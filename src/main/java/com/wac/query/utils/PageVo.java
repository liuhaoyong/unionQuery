package com.wac.query.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author huangjinsheng
 *
 */
public class PageVo {
	private String sEcho;
	private String iTotalRecords;
	private String iTotalDisplayRecords;
	private List<List<String>> aaData = new ArrayList<List<String>>();
	
	
	
	public PageVo() {
	}
	
	
	public PageVo(String sEcho, String iTotalRecords,
			String iTotalDisplayRecords, List<List<String>> aaData) {
		super();
		this.sEcho = sEcho;
		this.iTotalRecords = iTotalRecords;
		this.iTotalDisplayRecords = iTotalDisplayRecords;
		this.aaData = aaData;
	}


	public String getsEcho() {
		return sEcho;
	}
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
	public String getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(String iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public String getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(String iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public List<List<String>> getAaData() {
		return aaData;
	}
	public void setAaData(List<List<String>> aaData) {
		this.aaData = aaData;
	}
	
	@Override
	public String toString() {
		return JsonTool.writeValueAsString(this);
	}
}
