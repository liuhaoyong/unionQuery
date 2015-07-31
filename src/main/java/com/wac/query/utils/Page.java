package com.wac.query.utils;

/**
 * 
 * @author huangjinsheng
 *
 */
public class Page {
	public int iDisplayLength;
	public int iDisplayStart;
	public String sEcho;
	public int startIndex;
	public int endIndex;
	
	public void cal(int iDstart,int iDLen){
		this.iDisplayLength = iDLen;
		this.iDisplayStart = iDstart;
		
		this.startIndex = iDstart;
		this.endIndex = iDLen+iDstart;
		
		this.endIndex = this.endIndex - this.startIndex;
	}
	
	public void cal(){
		cal(this.iDisplayStart,this.iDisplayLength);
	}
	
	
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	public int getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}
	public int getiDisplayLength() {
		return iDisplayLength;
	}
	public void setiDisplayLength(int iDisplayLength) {
		this.iDisplayLength = iDisplayLength;
	}
	public int getiDisplayStart() {
		return iDisplayStart;
	}
	public void setiDisplayStart(int iDisplayStart) {
		this.iDisplayStart = iDisplayStart;
	}
	public String getsEcho() {
		return sEcho;
	}
	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}
	
	
	
}
