package com.github.union.query.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuhaoyong
 */
public enum SqlStatusEnum implements BaseEnum {
    正常("1") {
    },
    停用("2") {
    };

    private final String code;

    private SqlStatusEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode(){
        return code;
    }

    public int toInt(){
        return Integer.parseInt(code);
    }
    
    public Map<Integer,String> toMap(){
        Map<Integer,String> map = new HashMap();
        for (SqlStatusEnum s : SqlStatusEnum.values()) {
            map.put(s.toInt(),s.name());
        }
        return map;
    }

    public SqlStatusEnum getByCode(int code){
        for (SqlStatusEnum s : SqlStatusEnum.values()) {
            s.getCode().equalsIgnoreCase(""+code);
            return s;
        }
        return null;
    }
}
