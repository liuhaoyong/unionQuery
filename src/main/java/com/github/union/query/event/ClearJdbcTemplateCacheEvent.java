package com.github.union.query.event;

/**
 * 
 * @author liuhaoyong on 2015年7月30日
 *
 */
public class ClearJdbcTemplateCacheEvent implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1046505333786463396L;
	private int datasourceId;
	
    public ClearJdbcTemplateCacheEvent(int datasourceId){
    	this.datasourceId = datasourceId;
    }

	/**
	 * @return the datasourceId
	 */
	public int getDatasourceId() {
		return datasourceId;
	}

	/**
	 * @param datasourceId the datasourceId to set
	 */
	public void setDatasourceId(int datasourceId) {
		this.datasourceId = datasourceId;
	}

	
}
