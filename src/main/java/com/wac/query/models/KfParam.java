package com.wac.query.models;

/**
 * 
 * @author huangjinsheng on 2015年7月28日
 *
 */
public class KfParam extends BaseBean{

    private static final long serialVersionUID = 7508623487147522020L;

    private String paramName;
    private String fieldName;
    private String memo;
	/**
	 * @return the paramName
	 */
	public String getParamName() {
		return paramName;
	}
	/**
	 * @param paramName the paramName to set
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "KfParam [paramName=" + paramName + ", fieldName=" + fieldName
				+ ", memo=" + memo + "]";
	}

    
}
