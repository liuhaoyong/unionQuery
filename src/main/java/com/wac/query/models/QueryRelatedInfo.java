package com.wac.query.models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

/**
 * @author huangjinsheng on 2015/7/29.
 */
public class QueryRelatedInfo implements java.io.Serializable{

    private static final long serialVersionUID = 9155573439520860428L;

    /**
     * 业务信息
     */
    private KfBusniess busniess;
    /**
     * 每个全局参数都有包含哪些sql
     * key: 全局参数Param的主键  value：KfSql 列表
     */
    private Multimap<Integer,KfSql> sqlMap = LinkedListMultimap.create();

    /**
     * 按优先级排序的sql
     */
    private List<KfSql> sqls = new LinkedList<>();

    /**
     * 该业务包含的所有全局参数
     * key：全局参数id，value：全局参数对象
     */
    private Map<Integer,KfParam> aggregatedParams = new HashMap<>();


    public KfBusniess getBusniess() {
        return busniess;
    }

    public void setBusniess(KfBusniess busniess) {
        this.busniess = busniess;
    }

    public Multimap<Integer, KfSql> getSqlMap() {
        return sqlMap;
    }

    public void setSqlMap(Multimap<Integer, KfSql> sqlMap) {
        this.sqlMap = sqlMap;
    }

    public List<KfSql> getSqls() {
        return sqls;
    }

    public void setSqls(List<KfSql> sqls) {
        this.sqls = sqls;
    }

    public Map<Integer, KfParam> getAggregatedParams() {
        return aggregatedParams;
    }

    public void setAggregatedParams(Map<Integer, KfParam> aggregatedParams) {
        this.aggregatedParams = aggregatedParams;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
