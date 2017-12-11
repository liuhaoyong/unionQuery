package com.github.union.query.event;

/**
 * 
 * @author liuhaoyong on 2015年7月30日
 *
 */
public class ClearUnionQueryCacheBySqlEvent implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8746186204934664235L;
	private int sqlId;
	
    public ClearUnionQueryCacheBySqlEvent(int sqlId){
    	this.sqlId = sqlId;
    }

	/**
	 * @return the sqlId
	 */
	public int getSqlId() {
		return sqlId;
	}

	/**
	 * @param sqlId the sqlId to set
	 */
	public void setSqlId(int sqlId) {
		this.sqlId = sqlId;
	}
    
    
	
}
