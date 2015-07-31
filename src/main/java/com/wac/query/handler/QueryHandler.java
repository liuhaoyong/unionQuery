package com.wac.query.handler;

import org.springframework.stereotype.Service;

/**
 * @author huangjinsheng on 2015/7/29.
 */
public interface QueryHandler {

    /**
     * 具体查询逻辑的是此案
     * @param busniessId 业务id
     * @param paramId 参数id
     * @param paramValue 参数值
     * @return 返回table html
     */
    public String query(int busniessId,int paramId,String paramValue);
}
