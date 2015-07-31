package com.wac.query.models;

import com.wac.query.utils.JsonTool;

import java.util.LinkedList;
import java.util.List;

/**
 * @author huangjinsheng on 2015/7/29.
 */
public class SimpleResult implements java.io.Serializable {
    private static final long serialVersionUID = 9155573439520860428L;

    private KfSql sql;
    /**
     * 优先级
     */
    private int priorityLevel;
    /**
     * 总行数
     */
    private int totalRow = 0;
    /**
     * 列名
     */
    private List<ColumnDetail> heads = new LinkedList<>();
    /**
     * 列值
     */
    private List<List<String>> valuesList = new LinkedList<>();

    public KfSql getSql() {
        return sql;
    }

    public void setSql(KfSql sql) {
        this.sql = sql;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public List<ColumnDetail> getHeads() {
        return heads;
    }

    public void setHeads(List<ColumnDetail> heads) {
        this.heads = heads;
    }

    public List<List<String>> getValuesList() {
        return valuesList;
    }

    public void setValuesList(List<List<String>> valuesList) {
        this.valuesList = valuesList;
    }

    @Override
    public String toString() {
        return JsonTool.writeValueAsString(this);
    }
}
