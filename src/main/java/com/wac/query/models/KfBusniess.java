package com.wac.query.models;

import com.wac.query.utils.JsonTool;

/**
 * @author huangjinsheng
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
