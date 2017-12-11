package com.github.union.query.models;

import java.util.LinkedList;
import java.util.List;

import com.github.union.query.utils.JsonTool;

/**
 * @author liuhaoyong
 */
public class KfMultiQuery extends ParamBean{

    private static final long serialVersionUID = 7508623487147522020L;

    private int paramId;
    private int bid;
    private String paramValue;

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Override
    public String toString() {
        return JsonTool.writeValueAsString(this);
    }
}
