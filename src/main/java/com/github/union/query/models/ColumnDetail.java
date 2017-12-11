package com.github.union.query.models;

import com.github.union.query.utils.JsonTool;

/**
 * @author liuhaoyong on 2015/7/29.
 */
public class ColumnDetail implements java.io.Serializable{

    private static final long serialVersionUID = 6341514081409964691L;

    /** 字段名 */
    private String columnName;
    /** 别名 */
    private String labelName;
    
    private boolean show;
    
    private boolean isParamColumn;
    
    public ColumnDetail(String columnName,String labelName,boolean show,boolean isParamColumn){
    	this.columnName = columnName;
    	this.show = show;
    	this.labelName = labelName;
    	this.isParamColumn = isParamColumn;
    }
    

    /**
	 * @return the isParamColumn
	 */
	public boolean isParamColumn() {
		return isParamColumn;
	}


	/**
	 * @param isParamColumn the isParamColumn to set
	 */
	public void setParamColumn(boolean isParamColumn) {
		this.isParamColumn = isParamColumn;
	}


	/**
	 * @return the show
	 */
	public boolean isShow() {
		return show;
	}



	/**
	 * @param show the show to set
	 */
	public void setShow(boolean show) {
		this.show = show;
	}



	public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public String toString() {
        return JsonTool.writeValueAsString(this);
    }
}
