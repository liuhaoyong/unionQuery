package com.github.union.query.models;

import com.github.union.query.utils.JsonTool;

/**
 * @author liuhaoyong
 */
public class KfBusniess extends BaseBean{

    private static final long serialVersionUID = 7508623487147522020L;

    private String busniessName;

    public String getBusniessName() {
        return busniessName;
    }

    public void setBusniessName(String busniessName) {
        this.busniessName = busniessName;
    }

    @Override
    public String toString() {
        return JsonTool.writeValueAsString(this);
    }
}
