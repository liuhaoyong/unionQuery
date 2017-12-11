package com.github.union.query.models;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.github.union.query.utils.Page;



/**
 * 
 * 
 * @author huanjinsheng
 * 
 */
public class ParamBean extends Page implements java.io.Serializable{
	public static int RECORDSEARCH_YES=1;
	public static int RECORDADD_YES=2;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5247617315771541396L;

	private String loginUserName;
	private List<MultipartFile> multipartFileList;
	private Integer[] ids;
	private int is4RecordType;
	
	public boolean is4RecordSearch(){
		return is4RecordType==RECORDSEARCH_YES;
	}
	
	public boolean is4RecordAdd(){
		return is4RecordType == RECORDADD_YES;
	}
	
	public boolean isNormal(){
		return (is4RecordType!=RECORDSEARCH_YES && is4RecordType!=RECORDADD_YES);
	}


	public int getIs4RecordType() {
		return is4RecordType;
	}

	public void setIs4RecordType(int is4RecordType) {
		this.is4RecordType = is4RecordType;
	}

	

	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}

	public List<MultipartFile> getMultipartFileList() {
		return multipartFileList;
	}

	public void setMultipartFileList(List<MultipartFile> multipartFileList) {
		this.multipartFileList = multipartFileList;
	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	
	
	
	
}
