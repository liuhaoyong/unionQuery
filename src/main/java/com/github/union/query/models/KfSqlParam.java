package com.github.union.query.models;

import com.github.union.query.utils.JsonTool;

/**
 * @author liuhaoyong
 */
public class KfSqlParam extends BaseBean{

    private static final long serialVersionUID = 7508623487147522020L;

    //关联的sqlID
    private Integer sqlId;
    
    //参数ID
    private Integer paramId;
    
    //参数字段名
    private String sqlField;
    
    //参数描述
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
