package com.wac.query.service;

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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.wac.query.models.ColumnDetail;
import com.wac.query.models.KfSql;
import com.wac.query.models.KfSqlParam;
import com.wac.query.models.QueryRelatedInfo;
import com.wac.query.models.SimpleResult;
import com.wac.query.utils.JsonTool;


/**
 * @author huangjinsheng on 2015/6/23.
 */
@Service
@Transactional(propagation= Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class KfQueryService extends QueryHelper{
    private static Logger logger = LoggerFactory.getLogger(KfQueryService.class);
    /**
     * 获取业务相关查询信息
      * @param bussniessId
     * @return
     * @throws ExecutionException
     */
    public QueryRelatedInfo getQueryRelatedInfo(int bussniessId) throws ExecutionException {
        if(bussniessId <= 0){
            throw new IllegalArgumentException("bussniessId["+bussniessId+"] <= 0");
        }
        return cache.get(makeKey(bussniessId), new Callable<QueryRelatedInfo>() {
            @Override
            public QueryRelatedInfo call() throws Exception {
                return doAggregateQueryRelatedInfo(bussniessId);
            }
        });
    }


    /**
     * 具体查询逻辑的实现
     * @param busniessId 业务id
     * @param paramId 参数id
     * @param paramValue 参数值
     * @return 返回table html
     * @throws Exception 
     */
    public String query(int busniessId,int paramId,String paramValue){
        try{
            QueryRelatedInfo info = this.getQueryRelatedInfo(busniessId);

            /**
             * 所有与paramId参数匹配的sql
             */
            Collection<KfSql> sqls = info.getSqlMap().get(paramId);
            if(sqls.isEmpty()){
                return "没有找到SQL";
            }
            /**
             * 存放使用过的sql
             */
            Map<Integer,KfSql> usedSql = new HashMap<>();
            List<SimpleResult> resultList = new LinkedList<>();

            /**
             * sql处理
             */
            sqls.stream().forEach(sql->{
                buildResult(sql,paramId,paramValue,info,usedSql,resultList);
            });

            if(logger.isDebugEnabled()){
            	logger.debug(String.format("resultList:[%s]", JsonTool.writeValueAsString(resultList)));
            }
            /**
             * 生成表格，排序结果
             */
            return makeTable(resultList);

        }catch(Exception e){
            logger.error(e.getMessage(),e);
            return "error," + printErrorTrace(e);
        }
    }
    
    /**
     * 生成表格,排序记录
     * @param sqlShowMap
     * @param resultList
     */
    private String makeTable(List<SimpleResult> resultList){
    	//根据优先级排序显示顺序
    	if (!CollectionUtils.isEmpty(resultList)) {
            Collections.sort(resultList, new ClearingProcessorComparator());
        }
    	//生成table html
    	StringBuilder tables = new StringBuilder();
    	List<String> list = resultList.stream()
    			.filter(result -> result != null && result.getSql() != null) //如果没有找到相关的sql就不需要再进行处理了
    			.map(result->{
    		StringBuilder sb = new StringBuilder("<br><font color='blue'><h3>"+result.getSql().getSqlName()+"</h3></font><br>");
    		sb.append("<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"1\" class=\"display\" id=\""+result.getSql().getId()+"_table\">");
    		sb.append("<thead><tr>");
    		result.getHeads().stream()
    		.filter(head -> head.isShow())
    		.forEach(head -> {
    			sb.append("<th style=\"min-width: 60px;\">"+head.getLabelName()+"</th>");
    		});
    		sb.append("</tr></thead>");
    		sb.append("<tbody>");
    		int count = 1;
    		for (List<String> valueList : result.getValuesList()) {
    			sb.append("<tr class=\""+(count%2==0?"even":"odd")+"\">");
    			valueList.stream().forEach(val -> {
    				sb.append("<td>").append(val).append("</td>");//
    			});
    			sb.append("</tr>");
    			count ++;
			}
    		return sb.append("</tbody></table><br>").toString();
    	}).collect(Collectors.toList());
    	
    	list.stream().forEach(table ->{
    		tables.append(table);
    	});
    	
    	return StringUtils.defaultIfBlank(tables.toString(),"nothing");
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
    


    /**
     * 
     * @param sql
     * @param paramId
     * @param paramValue
     * @param info
     * @param usedSql
     * @param resultList
     */
    private void buildResult(KfSql sql,int paramId,String paramValue,QueryRelatedInfo info,Map<Integer,KfSql> usedSql ,List<SimpleResult> resultList){
    	
    	if(usedSql.containsKey(sql.getId())){
    		return ;
    	}
    	
    	SimpleResult result = new SimpleResult();
        /**
         * sql数据源
         */
        Optional<JdbcTemplate> template = dataSourceFactory.loadTemplate(sql.getDataSourceId());
        if(!template.isPresent()){
            throw new NullPointerException("can not found datasouce : sourceId:["+sql.getDataSourceId()+"]");
        }

        /**
         * 拼装sql
         */
        Map<String,KfSqlParam> sqlParamMap = new HashMap<String,KfSqlParam>();
        for (KfSqlParam p : sql.getParams()) {
        	sqlParamMap.put(StringUtils.trim(p.getSqlField()), p);
		}
        
        Optional<String> field = sql.getParams().stream()
        		.filter(sqlParam -> sqlParam.getParamId().intValue() == paramId)
                .map(sqlParam->{return sqlParam.getSqlField();})
                .findFirst();
        logger.info(String.format("sqlParamMap:[%s]", JsonTool.writeValueAsString(sqlParamMap)));
        Map<String,String> fieldMap = new HashMap<String,String>();//所有字段的map
        Map<String,String> paramFieldMap = new LinkedHashMap<String,String>();//作为参数字段的map
        Map<String,String> needToShowParamFieldMap = new HashMap<String,String>();//需要显示的参数字段的map
        
        sql.setSqlStatement(StringUtils.replaceEach(sql.getSqlStatement(), new String[]{"\n","\t","/n","/t"}, new String[]{"","","",""}));
        String currentFieldStr = StringUtils.substringBetween(sql.getSqlStatement(),"select","from");
        String[] currentFields = currentFieldStr.split(",");
        for(KfSqlParam sqlParam : sql.getParams()){
            boolean include = false;
            for (String currentField : currentFields) {
                String[] arr = StringUtils.split(currentField," ");
                String s = StringUtils.split(currentField," ")[0];
                fieldMap.put(StringUtils.trim(arr.length>1?StringUtils.isBlank(arr[1])?s:arr[1]:s),StringUtils.trim(s));
                if(StringUtils.equalsIgnoreCase(sqlParam.getSqlField().trim(),s.trim())){
                	paramFieldMap.put(StringUtils.trim(arr.length>1?StringUtils.isBlank(arr[1])?s:arr[1]:s),StringUtils.trim(s));
                	needToShowParamFieldMap.put(StringUtils.trim(arr.length>1?StringUtils.isBlank(arr[1])?s:arr[1]:s),StringUtils.trim(s));
                    include = true;
                    break;
                }
            }
            if(!include){
            	String sqlField = sqlParam.getSqlField();
            	String al = StringUtils.defaultString(info.getAggregatedParams().get(sqlParam.getParamId()).getParamName(),"");
                currentFieldStr = currentFieldStr + "," + sqlField + " " + al;
                fieldMap.put(StringUtils.trim(al),StringUtils.trim(sqlField));
                paramFieldMap.put(StringUtils.trim(al),StringUtils.trim(sqlField));
            }
        }

        logger.info(String.format("fieldMap:[%s]", JsonTool.writeValueAsString(fieldMap)));
        
        StringBuilder sqlStat = new StringBuilder("select "+currentFieldStr+" from " + StringUtils.substringAfter(sql.getSqlStatement(),"from"));
        if(!StringUtils.containsIgnoreCase(sql.getSqlStatement(),"where")){
            sqlStat.append(" where 1=1 ");
        }
        sqlStat.append(" and ").append(field.get()).append("=").append("\""+paramValue+"\"");

        logger.info(String.format("sql:[%s]", sqlStat.toString()));
        
        /**
         * 执行sql
         */
        SqlRowSet rowSet = template.get().queryForRowSet(sqlStat.toString());
        
        /**
         * 生成SimpleResult
         */
        SqlRowSetMetaData metaData = rowSet.getMetaData();
        //封装表头
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
        	String returnfield = fieldMap.get(metaData.getColumnName(i));
        	String returnShow = metaData.getColumnLabel(i);
            logger.info("metaData.getColumnName(i)="+returnfield+"  metaData.getColumnLabel(i)="+returnShow);
            
            boolean show = true;
            boolean isParamColumn = false;
            if(paramFieldMap.get(metaData.getColumnName(i)) != null){
            	isParamColumn = true;
            	if(needToShowParamFieldMap.get(metaData.getColumnName(i)) == null){
            		show = false;
            	}
            }
            result.getHeads().add(new ColumnDetail(returnfield,returnShow,show,isParamColumn));
        }
        
        //封装值
        Map<Integer,String> newLoopParamPair = new LinkedHashMap<>();
        int rowNum = 0;
        while (rowSet.next()) {
        	List<String> rowList = new ArrayList<String>();
            for (int columnNum = 1; columnNum <= result.getHeads().size(); columnNum++) {
                Object value = rowSet.getObject(columnNum);
                String valueStr = "";
                if (value instanceof Timestamp) {
                	valueStr = getDate(value);
                } else {
                	valueStr = value == null?"":value.toString();
                }
                
                ColumnDetail cd = result.getHeads().get(columnNum-1);
                
                /**
                 * 将sql 参数的值转化成 Map<paramId,paramValue>
                 */
                if(cd.isParamColumn() && StringUtils.isNotBlank(valueStr)){
                	KfSqlParam pp = sqlParamMap.get(cd.getColumnName());
                	newLoopParamPair.put(pp.getParamId(), valueStr);
                }
                if(cd.isShow()){
                	rowList.add(valueStr);
                }
            }

            result.getValuesList().add(rowList);
            result.setSql(sql);
            rowNum++;
        }
        result.setTotalRow(rowNum);
        result.setPriorityLevel(sql.getPriority());
        resultList.add(result);
        
        /**
         * 排除已经查询过的sql
         */
        usedSql.put(sql.getId(), sql);
        
        /**
         * 循环 Map<paramId,paramValue> 找到相应的sql 然后递归buildResult
         */
        if(newLoopParamPair.isEmpty()){
        	return ;
        }
        newLoopParamPair.entrySet().stream().forEach(map->{
        	Collection<KfSql> sqls = info.getSqlMap().get(map.getKey());
        	if(sqls.isEmpty()){
        		return ;
        	}
        	
        	sqls.stream().forEach(s->{
        		if(!usedSql.containsKey(s.getId())){
        			buildResult(s,map.getKey(),map.getValue(),info,usedSql ,resultList);
        		}
        	});
        	
        });
        
    }
}
