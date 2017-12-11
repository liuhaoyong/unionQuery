package com.github.union.query.event;

/**
 * 
 * @author liuhaoyong on 2015年7月30日
 *
 */
public class ClearUnionQueryCacheByBusniessEvent implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4889178274783065290L;
	private int busniessId;
	
    public ClearUnionQueryCacheByBusniessEvent(int busniessId){
    	this.busniessId = busniessId;
    }

	/**
	 * @return the busniessId
	 */
	public int getBusniessId() {
		return busniessId;
	}

	/**
	 * @param busniessId the busniessId to set
	 */
	public void setBusniessId(int busniessId) {
		this.busniessId = busniessId;
	}

	
    
	
}
