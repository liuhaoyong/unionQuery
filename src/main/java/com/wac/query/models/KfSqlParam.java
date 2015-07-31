package com.wac.query.models;

import com.wac.query.utils.JsonTool;

/**
 * @author huangjinsheng
 */
public class KfSqlParam extends BaseBean{

    private static final long serialVersionUID = 7508623487147522020L;

    private Integer sqlId;
    private Integer paramId;
    private String sqlField;
    private String paramDesc;

    public Integer getParamId() {
        return paramId;
    }

    public void setParamId(Integer paramId) {
        this.paramId = paramId;
    }

    public Integer getSqlId() {
        return sqlId;
    }

    public void setSqlId(Integer sqlId) {
        this.sqlId = sqlId;
    }


    public String getSqlField() {
        return sqlField;
    }

    public void setSqlField(String sqlField) {
        this.sqlField = sqlField;
    }

    public String getParamDesc() {
        return paramDesc;
    }

    public void setParamDesc(String paramDesc) {
        this.paramDesc = paramDesc;
    }


    @Override
    public String toString() {
        return JsonTool.writeValueAsString(this);
    }
}
