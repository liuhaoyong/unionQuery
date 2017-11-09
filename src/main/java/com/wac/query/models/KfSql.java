package com.wac.query.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author huangjinsheng
 */
public class KfSql extends BaseBean {

    private static final long       serialVersionUID = 7508623487147522020L;

    private String                  sqlName;
    private Integer                 busniessId;
    private Integer                 dataSourceId;
    private Integer                 priority         = 0;
    private Integer                 sqlStatus;
    private String                  sqlStatement;
    private String                  sqlDesc;
    private List<Integer>           busniessIds;

    private String[]                pids;
    private String[]                paramId;
    private String[]                sqlField;
    private String[]                paramDesc;

    private List<KfSqlParam>        params           = new LinkedList<KfSqlParam>();

    private Map<String, KfSqlParam> sqlParamMap;
    private KfDatabaseSource dataSource;
    
    
    
    

    public KfDatabaseSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(KfDatabaseSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataSourceId(Integer dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public void setSqlParamMap(Map<String, KfSqlParam> sqlParamMap) {
        this.sqlParamMap = sqlParamMap;
    }

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
        if (sqlParamMap == null) {
            synchronized (this) {
                if (sqlParamMap != null) {
                    return sqlParamMap;
                }
                sqlParamMap = new HashMap<>();
                for (KfSqlParam kfSqlParam : params) {
                    sqlParamMap.put(StringUtils.upperCase(StringUtils.trim(kfSqlParam.getSqlField())), kfSqlParam);
                }
            }
        }
        return sqlParamMap;
    }

    public List<KfSqlParam> getParamList() {
        return params;
    }
    
    /**
     * vm页面里写死了这个值
     * @return
     */
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

    public KfSqlParam getParam(String paramName) {
        return this.getSqlParamMap().get(StringUtils.upperCase(StringUtils.trim(paramName)));
    }


    @Override
    public String toString() {
        return "KfSql{" + "sqlName='" + sqlName + '\'' + ", busniessId=" + busniessId + ", priority=" + priority
                + ", dataSourceId=" + dataSourceId + ", sqlStatus=" + sqlStatus + ", sqlStatement='" + sqlStatement
                + '\'' + ", sqlDesc='" + sqlDesc + '\'' + ", busniessIds=" + busniessIds + ", pids="
                + Arrays.toString(pids) + ", paramId=" + Arrays.toString(paramId) + ", sqlField="
                + Arrays.toString(sqlField) + ", paramDesc=" + Arrays.toString(paramDesc) + ", params=" + params
                + ", sqlParamMap=" + sqlParamMap + '}';
    }
}
