package com.github.union.query.models;

import com.github.union.query.utils.JsonTool;

/**
 * @author liuhaoyong
 */
public class KfSingleQuery extends ParamBean{

    private static final long serialVersionUID = 7508623487147522020L;

    private int bid;
    private String[] paramIds;
    private String[] paramValues;

    

    /**
	 * @return the bid
	 */
	public int getBid() {
		return bid;
	}



	/**
	 * @param bid the bid to set
	 */
	public void setBid(int bid) {
		this.bid = bid;
	}



	/**
	 * @return the paramIds
	 */
	public String[] getParamIds() {
		return paramIds;
	}



	/**
	 * @param paramIds the paramIds to set
	 */
	public void setParamIds(String[] paramIds) {
		this.paramIds = paramIds;
	}



	/**
	 * @return the paramValues
	 */
	public String[] getParamValues() {
		return paramValues;
	}



	/**
	 * @param paramValues the paramValues to set
	 */
	public void setParamValues(String[] paramValues) {
		this.paramValues = paramValues;
	}



	@Override
    public String toString() {
        return JsonTool.writeValueAsString(this);
    }
}
