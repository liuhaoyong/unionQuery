package com.github.union.query.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.github.union.query.enums.DriverTypeEnum;
import com.github.union.query.models.ColumnDetail;
import com.github.union.query.models.KfSingleQuery;
import com.github.union.query.models.KfSql;
import com.github.union.query.models.KfSqlParam;
import com.github.union.query.models.QueryRelatedInfo;
import com.github.union.query.models.SimpleResult;
import com.github.union.query.utils.PageVo;

/**
 * @author liuhaoyong on 2015/6/23.
 */
@Service
public class KfQueryService extends QueryHelper {
    private static Logger logger = LoggerFactory.getLogger(KfQueryService.class);

    /**
     * 获取业务相关查询信息
     * 
     * @param bussniessId
     * @return
     * @throws ExecutionException
     */
    public QueryRelatedInfo getQueryRelatedInfo(int bussniessId) throws ExecutionException {
        if (bussniessId <= 0) {
            throw new IllegalArgumentException("bussniessId[" + bussniessId + "] <= 0");
        }
        return cache.get(makeKey(bussniessId), new Callable<QueryRelatedInfo>() {
            @Override
            public QueryRelatedInfo call() throws Exception {
                return doAggregateQueryRelatedInfo(bussniessId);
            }
        });
    }

    /**
     * @param kfSingleQuery
     * @return
     */
    public String singleQuery(KfSingleQuery query) {
        try {
            QueryRelatedInfo info = this.getQueryRelatedInfo(query.getBid());
            KfSql sql = info.getSqls().get(0);
            sql.setSqlStatement(StringUtils.replaceEach(sql.getSqlStatement(),
                    new String[] { "\n", "\t", "/n", "/t", " as ", " AS ", "  ", ";", "SELECT", "FROM", "GROUP BY", "ORDER BY","LIMIT"},
                    new String[] { " ", " ", " ", " ", " ", " ", " ", "", "select", "from", "group by", "order by","limit" }));

            /**
             * sql数据源
             */
            Optional<JdbcTemplate> template = dataSourceFactory.loadTemplate(sql.getDataSource());
            if (!template.isPresent()) {
                throw new NullPointerException("can not found datasouce : sourceId:[" + sql.getDataSourceId() + "]");
            }
            
            int total = template.get().queryForObject("select count(*) from (" +  buildSingleQuerySql(query, info, sql,true) + " ) a ", Integer.class);
            
            List<List<String>> aaData = new LinkedList<>();
            ResultSetWrappingSqlRowSet rowSet = (ResultSetWrappingSqlRowSet)template.get().queryForRowSet(buildSingleQuerySql(query, info, sql,false));
            
            int titleSize =rowSet.getMetaData().getColumnCount();
            while (rowSet.next()) {
                List<String> slist = new LinkedList<>();
                for (int i = 1; i <= titleSize; i++) {
                    Object value = rowSet.getObject(i);
                    String valueStr = "";
                    if (value instanceof Timestamp) {
                        valueStr = getDate(value);
                    } else {
                        valueStr = value == null ? "" : value.toString();
                    }

                    slist.add(valueStr);
                }

                aaData.add(slist);
            }

            PageVo vo = new PageVo(query.getsEcho(), total + "", total + "", aaData);
            return vo.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return "error," + printErrorTrace(e);
        }
    }

    /**
     * @param query
     * @param info
     * @param sql
     * @param count
     * @return
     */
    private String buildSingleQuerySql(KfSingleQuery query, QueryRelatedInfo info, KfSql sql,boolean isCount) {
        StringBuilder sqlStat = new StringBuilder();
        String oldSql = sql.getSqlStatement();

        //单表分页查询，需要去除原有sql的limit语句
        if (StringUtils.containsIgnoreCase(oldSql, " limit ")) {
            oldSql = StringUtils.substringBefore(oldSql, " limit ");
        }
        sqlStat.append(oldSql);

        StringBuilder whereStr = new StringBuilder();
        if (!StringUtils.containsIgnoreCase(sql.getSqlStatement(), " where ")) {
            whereStr.append(" where 1=1 ");
        }

        for (int i = 0; i < query.getParamIds().length; i++) {
            Integer paramId = Integer.valueOf(query.getParamIds()[i]);
            String paramValue = query.getParamValues()[i];
            if (StringUtils.equalsIgnoreCase(paramValue, "defaultXXX")) {
                continue;
            }

            Optional<String> field = sql.getParamList().stream()
                    .filter(sqlParam -> sqlParam.getParamId().intValue() == paramId).map(sqlParam -> {
                        return sqlParam.getSqlField();
                    }).findFirst();
            //过滤万能参数
            if ("1".equals(StringUtils.trim(field.get()))) {
                continue;
            }
            whereStr.append(" and ").append(field.get()).append("=").append("\'" + paramValue + "\'");
        }

        if (whereStr.length() > 0) {
            if (StringUtils.containsIgnoreCase(sqlStat.toString(), " group by ")) {
                String afterGroupBy = StringUtils.substringAfter(sqlStat.toString(), "group by");
                String beforeGroupBy = StringUtils.substringBefore(sqlStat.toString(), "group by");
                sqlStat = new StringBuilder();
                sqlStat.append(beforeGroupBy).append(whereStr).append(" group by ").append(afterGroupBy);
            } else if (StringUtils.containsIgnoreCase(sqlStat.toString(), " order by ")) {
                String afterOrderBy = StringUtils.substringAfter(sqlStat.toString(), "order by");
                String beforeOrderBy = StringUtils.substringBefore(sqlStat.toString(), "order by");
                sqlStat = new StringBuilder();
                sqlStat.append(beforeOrderBy).append(whereStr).append(" order by ").append(afterOrderBy);
            } else {
                sqlStat.append(whereStr);
            }
        }

        //如果为统计总行数的, 不执行后续limit相关逻辑, 直接返回
        if(isCount)
        {
            return sqlStat.toString();
        }
        
        query.cal();
        if (sql.getDataSource().getDriverType() == DriverTypeEnum.Mysql.toInt()) {
            sqlStat.append(" limit ").append(query.getStartIndex()).append(",").append(query.getEndIndex());
        } else if (sql.getDataSource().getDriverType() == DriverTypeEnum.SQL_SERVER.toInt()) {
            //sql server数据库暂简单些, 直接返回前100条数据
            String resultStr = sqlStat.toString();
            sqlStat = new StringBuilder();
            sqlStat.append(" select top (100) ").append(StringUtils.substringAfter(resultStr, "select"));
        }

        logger.info(String.format("singlqQuerySql:[%s]", sqlStat.toString()));

        return sqlStat.toString();
    }

    /**
     * @param info
     * @return
     */
    public String getSingleParamTableTitle(QueryRelatedInfo info) {
        StringBuilder table = new StringBuilder("<br><font color='blue'><h3>" + info.getSqls().get(0).getSqlName()
                + "  (<span id='spanTime'></span>ms)</h3></font><br>");
        table.append(
                "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"1\" class=\"display\" id=\"query_table\">");
        table.append("<thead><tr>");
        List<String> titleList = getSingleParamTitle(info);
        for (String currentField : titleList ) {
            String[] arr = StringUtils.split(currentField, " ");
            String rawField = arr[0];//原sql字段
            String alias = arr.length > 1 ? arr[arr.length - 1] : null;//如果有别名的话则放别名

            table.append("<th style=\"min-width: 60px;\">");
            table.append(StringUtils.isBlank(alias) ? rawField : alias);
            table.append("</th>");

        }
        table.append("</tr></thead>");
        table.append("<tbody>");
        table.append("</tbody>");
        table.append("</table><br>");

        return table.toString();
    }

    /**
     * @param info
     * @return
     */
    private List<String> getSingleParamTitle(QueryRelatedInfo info) {
        String sql = info.getSqls().get(0).getSqlStatement();
        sql = StringUtils.replaceEach(sql, new String[] { "SELECT", "FROM" }, new String[] { "select", "from" });

        String currentFieldStr = StringUtils.substringBetween(sql, "select", "from");
        String[] currentFields = currentFieldStr.split(",");
        
        List<String> result = new ArrayList<String>(); 
        for(String item : currentFields)
        {
            //过滤select字段中带: xx(xx,xx) 的函数表达式
            if(!(item.contains("(") && !item.contains(")")))
            {
                result.add(item);
            }
        }
        return result; 
    }

    /**
     * 具体查询逻辑的实现
     * 
     * @param busniessId 业务id
     * @param paramId 参数id
     * @param paramValue 参数值
     * @return 返回table html
     * @throws Exception
     */
    public String query(int busniessId, int paramId, String paramValue) {
        try {
            QueryRelatedInfo info = this.getQueryRelatedInfo(busniessId);

            /**
             * 所有与paramId参数匹配的sql
             */
            Collection<KfSql> sqls = info.getSqlMap().get(paramId);
            if (sqls.isEmpty()) {
                return "没有找到SQL";
            }

            /**
             * 存放使用过的sql
             */
            Map<Integer, KfSql> usedSql = new HashMap<>();
            List<SimpleResult> resultList = new LinkedList<>();

            /**
             * sql处理
             */
            sqls.stream().forEach(sql -> {
                doQuery(sql, paramId, paramValue, info, usedSql, resultList);
            });

            /**
             * 生成表格，排序结果
             */
            return makeTable(resultList);

        } catch (Exception e) {
            logger.error("执行查询异常", e);
            return "error," + printErrorTrace(e);
        }
    }

    /**
     * @param sql
     * @param paramId
     * @param paramValue
     * @param queryContextInfo
     * @param usedSql
     * @param resultList
     */
    private void doQuery(KfSql sql, int paramId, String paramValue, QueryRelatedInfo queryContextInfo,
                         Map<Integer, KfSql> usedSql, List<SimpleResult> resultList) {

        //如果该SQL已经执行过, 则跳过
        if (usedSql.containsKey(sql.getId())) {
            return;
        }

        //1. 构建SQL
        String sqlStatement = buildSql(sql, paramId, paramValue);

        //2. 初始化sql执行结果
        SimpleResult result = new SimpleResult();
        result.setPriorityLevel(sql.getPriority());
        result.setSql(sql);

        //3. 执行sql
        SqlRowSet resultSet = executeSql(sql, sqlStatement, result);
        usedSql.put(sql.getId(), sql);

        //4. 构建查询结果表格头
        List<ColumnDetail> tableTile = buildTableTitle(sql, resultSet);
        result.setHeads(tableTile);

        //5. 构建查询表格body, 并获得输出参数Map
        Map<Integer, String> outParamMap = buildTableBody(resultSet, sql, result, paramId);
        
        if(result.getTotalRow()>0)
        {
            //递归查询
            resultList.add(result);
            recursiveQuery(queryContextInfo, usedSql, resultList, outParamMap);
        }
    }

    /**
     * 构建待执行的SQL字符串
     * 
     * @param sql
     * @param paramId
     * @param paramValue
     * @param queryContextInfo
     * @return
     */
    public String buildSql(KfSql sql, int paramId, String paramValue) {

        //替换sql中的特殊字符
        sql.setSqlStatement(StringUtils.replaceEach(sql.getSqlStatement(),
                new String[] { "\n", "\t", "/n", "/t", " as ", " AS ", "  ", ";", "SELECT", "FROM",  "GROUP BY", "ORDER BY" },
                new String[] { " ", " ", " ", " ", " ", " ", " ", "", "select", "from",  "group by", "order by"}));

        //获得sql中的select到form部分的内容
        String selectFieldStr = StringUtils.substringBetween(sql.getSqlStatement(), "select", "from");

        //获得where条件对应的字段
        Optional<String> wherefield = sql.getParamList().stream()
                .filter(sqlParam1 -> sqlParam1.getParamId().intValue() == paramId).map(sqlParam2 -> {
                    return sqlParam2.getSqlField();
                }).findFirst();

        //拼装sql, 不包含where条件后的部分
        StringBuilder result = new StringBuilder(
                "select " + selectFieldStr + " from " + StringUtils.substringAfter(sql.getSqlStatement(), " from "));

        //拼装where 条件部分
        StringBuilder whereStr = new StringBuilder();
        if (!StringUtils.containsIgnoreCase(sql.getSqlStatement(), " where ")) {
            whereStr.append(" where 1=1 ");
        }
        
        if(!"1".equals(wherefield.get().trim()))
        {
            whereStr.append(" and ").append(wherefield.get()).append("=").append("\'" + paramValue + "\'");
        }

        //拼装group by, order部分
        if (StringUtils.containsIgnoreCase(result.toString(), " group by ")) {
            String afterGroupBy = StringUtils.substringAfter(result.toString(), "group by");
            String beforeGroupBy = StringUtils.substringBefore(result.toString(), "group by");
            result = new StringBuilder();
            result.append(beforeGroupBy).append(whereStr).append(" group by ").append(afterGroupBy);
        } else if (StringUtils.containsIgnoreCase(result.toString(), " order by ")) {
            String afterOrderBy = StringUtils.substringAfter(result.toString(), "order by");
            String beforeOrderBy = StringUtils.substringBefore(result.toString(), "order by");
            result = new StringBuilder();
            result.append(beforeOrderBy).append(whereStr).append(" order by ").append(afterOrderBy);
        } else {
            result.append(whereStr);
        }

        //处理limit部分, 防止内容太多默认情况下只查100条记录
        if (DriverTypeEnum.Mysql.toInt() == sql.getDataSource().getDriverType().intValue()) {
            if (!StringUtils.containsIgnoreCase(result.toString(), " limit ")) {
                result.append(" limit 0,100");
            }
        } else if (DriverTypeEnum.SQL_SERVER.toInt() == sql.getDataSource().getDriverType().intValue()) {
            String resultStr = result.toString();
            result = new StringBuilder();
            result.append(" select top (100) ").append(StringUtils.substringAfter(resultStr, "select"));
        }
        return result.toString();
    }

    /**
     * 执行sql
     * 
     * @param sql
     * @param sqlStatement
     * @param result
     * @return
     */
    private SqlRowSet executeSql(KfSql sql, String sqlStatement, SimpleResult result) {
        Optional<JdbcTemplate> template = dataSourceFactory.loadTemplate(sql.getDataSource());
        if (!template.isPresent()) {
            throw new NullPointerException("can not found datasouce : sourceId:[" + sql.getDataSourceId() + "]");
        }

        logger.info("待执行的sql:[{}]", sqlStatement);
        long time = System.currentTimeMillis();
        SqlRowSet resultSet = template.get().queryForRowSet(sqlStatement);
        result.setTime(System.currentTimeMillis() - time);
        return resultSet;
    }

    /**
     * 构建表头
     * 
     * @param sql
     * @param rowSet
     * @return
     */
    private List<ColumnDetail> buildTableTitle(KfSql sql, SqlRowSet rowSet) {
        List<ColumnDetail> result = new LinkedList<>();
        SqlRowSetMetaData metaData = rowSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            boolean isParamField = sql.getParam(metaData.getColumnName(i)) != null;
            result.add(new ColumnDetail(metaData.getColumnName(i), metaData.getColumnLabel(i), true, isParamField));
        }
        return result;
    }

    /**
     * 构建表格内容
     * 
     * @param rowSet
     * @param sql
     * @param result
     * @return
     */
    private LinkedHashMap<Integer, String> buildTableBody(SqlRowSet rowSet, KfSql sql, SimpleResult result,
                                                          Integer currentParamId) {
        LinkedHashMap<Integer, String> outParamMap = new LinkedHashMap<Integer, String>();
        int rowNum = 0;
        while (rowSet.next()) {
            List<String> cellList = new ArrayList<String>();
            for (int columnNum = 1; columnNum <= result.getHeads().size(); columnNum++) {
                Object value = rowSet.getObject(columnNum);
                String valueStr = "";
                if (value instanceof Timestamp) {
                    valueStr = getDate(value);
                } else {
                    valueStr = value == null ? "" : value.toString();
                }
                /**
                 * 如果是输出参数, 且不为当前查询参数的, 放到输出参数列表里
                 */
                ColumnDetail cd = result.getHeads().get(columnNum - 1);
                if (cd.isParamColumn() && StringUtils.isNotBlank(valueStr)) {
                    KfSqlParam pp = sql.getParam(cd.getColumnName());
                    if (pp != null && !pp.getParamId().equals(currentParamId)) {
                        outParamMap.put(pp.getParamId(), valueStr);
                    }
                }
                //如果需要显示, 添加到值列表里
                if (cd.isShow()) {
                    cellList.add(valueStr);
                }
            }
            result.getValuesList().add(cellList);
            rowNum++;
        }
        result.setTotalRow(rowNum);
        return outParamMap;
    }

    /**
     * 递归查询
     * 
     * @param queryContextInfo
     * @param usedSql
     * @param resultList
     * @param outParamMap
     */
    private void recursiveQuery(QueryRelatedInfo queryContextInfo, Map<Integer, KfSql> usedSql,
                                List<SimpleResult> resultList, Map<Integer, String> outParamMap) {
        /**
         * 循环 Map<paramId,paramValue> 找到相应的sql 然后递归buildResult
         */
        if (outParamMap.isEmpty()) {
            return;
        }

        outParamMap.entrySet().stream().forEach(map -> {
            Collection<KfSql> sqls = queryContextInfo.getSqlMap().get(map.getKey());
            if (sqls.isEmpty()) {
                return;
            }
            sqls.stream().forEach(s -> {
                if (!usedSql.containsKey(s.getId())) {//过滤已经被使用的sql
                    doQuery(s, map.getKey(), map.getValue(), queryContextInfo, usedSql, resultList);
                }
            });
        });
    }

    /**
     * 生成表格,排序记录
     * 
     * @param sqlShowMap
     * @param resultList
     */
    private String makeTable(List<SimpleResult> resultList) {
        //根据优先级排序显示顺序
        if (!CollectionUtils.isEmpty(resultList)) {
            Collections.sort(resultList, new ClearingProcessorComparator());
        }
        //生成table html
        StringBuilder tables = new StringBuilder();
        List<String> list = resultList.stream().filter(result -> result != null && result.getSql() != null) //如果没有找到相关的sql就不需要再进行处理了
                .map(result -> {
                    StringBuilder sb = new StringBuilder("<br><font color='blue'><h3>" + result.getSql().getSqlName()
                            + "  (" + result.getTime() + "ms)</h3></font><br>");
                    sb.append(
                            "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"1\" class=\"display\" id=\""
                                    + result.getSql().getId() + "_table\">");
                    sb.append("<thead><tr>");
                    result.getHeads().stream().filter(head -> head.isShow()).forEach(head -> {
                        sb.append("<th style=\"min-width: 60px;\">" + head.getLabelName() + "</th>");
                    });
                    sb.append("</tr></thead>");
                    sb.append("<tbody>");
                    int count = 1;
                    for (List<String> valueList : result.getValuesList()) {
                        sb.append("<tr class=\"" + (count % 2 == 0 ? "even" : "odd") + "\">");
                        valueList.stream().forEach(val -> {
                            sb.append("<td>").append(val).append("</td>");//
                        });
                        sb.append("</tr>");
                        count++;
                    }
                    return sb.append("</tbody></table><br>").toString();
                }).collect(Collectors.toList());

        list.stream().forEach(table -> {
            tables.append(table);
        });

        return StringUtils.defaultIfBlank(tables.toString(), "nothing");
    }

    /** 查询结果排序器 */
    static class ClearingProcessorComparator implements Comparator<SimpleResult>, Serializable {
        private static final long serialVersionUID = 1252104853656832688L;

        /*
         * (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare(SimpleResult self, SimpleResult other) {
            return self.getPriorityLevel() - other.getPriorityLevel();
        }
    }

}
