package com.wac.query.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author huangjinsheng
 */
public class KfSql extends BaseBean{

    private static final long serialVersionUID = 7508623487147522020L;

    private String sqlName;
    private Integer busniessId;
    private Integer priority = 0;
    private Integer dataSourceId;
    private Integer sqlStatus;
    private String sqlStatement;
    private String sqlDesc;


    private List<KfSqlParam> params = new LinkedList();
    
    private Map<String,KfSqlParam> sqlParamMap = null;
    
    

    /**
	 * @return the sqlParamMap
	 */
	public Map<String, KfSqlParam> getSqlParamMap() {
		if(sqlParamMap == null){
			sqlParamMap = new HashMap<>();
			for (KfSqlParam kfSqlParam : params) {
				sqlParamMap.put(kfSqlParam.getSqlField(), kfSqlParam);
			}
		}
		
		return sqlParamMap;
	}

	/**
	 * @param sqlParamMap the sqlParamMap to set
	 */
	public void setSqlParamMap(Map<String, KfSqlParam> sqlParamMap) {
		this.sqlParamMap = sqlParamMap;
	}

	public List<KfSqlParam> getParams() {
        return params;
    }

    public void setParams(List<KfSqlParam> params) {
        this.params = params;
    }
    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public Integer getBusniessId() {
        return busniessId;
    }

    public void setBusniessId(Integer busniessId) {
        this.busniessId = busniessId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public Integer getSqlStatus() {
        return sqlStatus;
    }

    public void setSqlStatus(Integer sqlStatus) {
        this.sqlStatus = sqlStatus;
    }

    public String getSqlStatement() {
        return sqlStatement;
    }

    public void setSqlStatement(String sqlStatement) {
        this.sqlStatement = sqlStatement;
    }

    public String getSqlDesc() {
        return sqlDesc;
    }

    public void setSqlDesc(String sqlDesc) {
        this.sqlDesc = sqlDesc;
    }

    @Override
    public String toString() {
        return "KfSql{" +
                "sqlName='" + sqlName + '\'' +
                ", busniessId=" + busniessId +
                ", priority=" + priority +
                ", dataSourceId=" + dataSourceId +
                ", sqlStatus=" + sqlStatus +
                ", sqlStatement='" + sqlStatement + '\'' +
                ", sqlDesc='" + sqlDesc + '\'' +
                "} " + super.toString();
    }
}
