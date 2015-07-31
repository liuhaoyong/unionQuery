package com.wac.query.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangjinsheng
 */
public enum DriverTypeEnum implements BaseEnum {
    Mysql("1") {
        @Override
        public String driverStr(){
            return "com.mysql.jdbc.Driver";
        }
    };

    private final String code;

    private DriverTypeEnum(String code) {
        this.code = code;
    }

    @Override
    public String getCode(){
        return code;
    }

    public int toInt(){
        return Integer.parseInt(code);
    }
    
    public Map<String,Integer> toMap(){
        Map<String ,Integer> map = new HashMap();
        for (DriverTypeEnum s : DriverTypeEnum.values()) {
            map.put(s.name(),s.toInt());
        }
        return map;
    }

    public static DriverTypeEnum getByCode(int code){
        for (DriverTypeEnum s : DriverTypeEnum.values()) {
            s.getCode().equalsIgnoreCase("" + code);
            return s;
        }
        return null;
    }

    public String driverStr(){
        return null;
    }
}
