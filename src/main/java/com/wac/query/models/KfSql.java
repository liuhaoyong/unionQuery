package com.wac.query.models;

import java.util.*;

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
    private List<Integer> busniessIds;
    
    private String[] pids;
    private String[] paramId;
    private String[] sqlField;
    private String[] paramDesc;


    private List<KfSqlParam> params = new LinkedList();
    
    private Map<String,KfSqlParam> sqlParamMap = null;
    
	/**
	 * @return the pids
	 */
	public String[] getPids() {
		return pids;
	}

	/**
	 * @param pids the pids to set
	 */
	public void setPids(String[] pids) {
		this.pids = pids;
	}

	/**
	 * @return the paramId
	 */
	public String[] getParamId() {
		return paramId;
	}

	/**
	 * @param paramId the paramId to set
	 */
	public void setParamId(String[] paramId) {
		this.paramId = paramId;
	}

	/**
	 * @return the sqlField
	 */
	public String[] getSqlField() {
		return sqlField;
	}

	/**
	 * @param sqlField the sqlField to set
	 */
	public void setSqlField(String[] sqlField) {
		this.sqlField = sqlField;
	}

	/**
	 * @return the paramDesc
	 */
	public String[] getParamDesc() {
		return paramDesc;
	}

	/**
	 * @param paramDesc the paramDesc to set
	 */
	public void setParamDesc(String[] paramDesc) {
		this.paramDesc = paramDesc;
	}

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

    public List<Integer> getBusniessIds() {
        return busniessIds;
    }

    public void setBusniessIds(List<Integer> busniessIds) {
        this.busniessIds = busniessIds;
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
                ", busniessIds=" + busniessIds +
                ", pids=" + Arrays.toString(pids) +
                ", paramId=" + Arrays.toString(paramId) +
                ", sqlField=" + Arrays.toString(sqlField) +
                ", paramDesc=" + Arrays.toString(paramDesc) +
                ", params=" + params +
                ", sqlParamMap=" + sqlParamMap +
                '}';
    }
}
