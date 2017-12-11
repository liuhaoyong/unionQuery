package com.github.union.query.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import com.github.union.query.enums.SqlStatusEnum;
import com.github.union.query.factory.DataSourceFactory;
import com.github.union.query.models.BaseBean;
import com.github.union.query.models.KfBusniess;
import com.github.union.query.models.KfDatabaseSource;
import com.github.union.query.models.KfParam;
import com.github.union.query.models.KfSql;
import com.github.union.query.models.KfSqlParam;
import com.github.union.query.models.ParamBean;
import com.github.union.query.models.QueryRelatedInfo;

/**
 * @author liuhaoyong on 2015/7/29.
 */
public class QueryHelper extends AbstractService<ParamBean,BaseBean>{
	private static Logger logger = LoggerFactory.getLogger(QueryHelper.class);
    @Resource
    protected KfBusniessService kfBusniessService;
    @Resource
    protected KfDatabaseSourceService kfDatabaseSourceService;
    @Resource
    protected KfSqlService kfSqlService;
    @Resource
    protected KfParamService kfParamService;
    @Resource
    @Qualifier("defaultDataSourceFactory")
    protected DataSourceFactory dataSourceFactory;



    /**
     * 聚合某个业务相关的查询的所有信息到缓存中去
     * 1. 聚合的参数信息
     * 2. sql语句
     * 3. 业务
     * 4. sql相关的参数
     * @param bussniessId 业务id
     * @return
     */
    protected QueryRelatedInfo doAggregateQueryRelatedInfo(int bussniessId){
        //获取业务
        KfBusniess busniess = kfBusniessService.selectByPk(bussniessId);
        Objects.requireNonNull(busniess);

        QueryRelatedInfo info = new QueryRelatedInfo();
        info.setBusniess(busniess);

        //获取所有状态正常的sql
        List<KfSql> sqls = kfSqlService.getSqlByBusniess(bussniessId)
                .stream()
                .filter(sql -> sql.getSqlStatus().intValue() == SqlStatusEnum.正常.toInt())
                .collect(Collectors.toList());
        sqls.forEach(sql ->{
            KfDatabaseSource dataSource = kfDatabaseSourceService.selectByPk(sql.getDataSourceId());
            sql.setDataSource(dataSource);
        });
        info.setSqls(sqls);
        
        Map<Integer,KfSql> tempSqlMap = sqls.stream()
                .collect(Collectors.toMap(KfSql::getId, (v) -> v));

        //获取所有sql中的参数
        List<KfSqlParam> sqlParams = sqls.stream()
                .map(sql -> {
                    List<KfSqlParam> sqlParamList = kfSqlService.getParams(sql.getId());
                    sql.getParamList().addAll(sqlParamList);
                    return sqlParamList;
                })
                .flatMap(params -> params.stream())
                .collect(Collectors.toList());
        //info.getSqls().addAll(sqls);

        /**
         * 获取所有的全局参数
         */
        Map<Integer,KfParam> map = sqlParams.stream()
                .map(sqlParam ->{return kfParamService.selectByPk(sqlParam.getParamId());})
                .collect(Collectors.toMap(KfParam::getId,(v)->v,(v1, v2) -> v2));
        info.getAggregatedParams().putAll(map);

        //sql有哪些全局参数进行分类
        sqlParams.stream()
                .filter(sqlParam -> map.get(sqlParam.getParamId()) != null && tempSqlMap.get(sqlParam.getSqlId()) != null)
                .forEach(sqlParam -> {
                    info.getSqlMap().put(sqlParam.getParamId(), tempSqlMap.get(sqlParam.getSqlId()));
                });


        return info;
    }
    
    
    /**
     * 获取日期字符串
     * @param value
     * @return
     */
    protected static String getDate(Object value) {
        Timestamp timestamp = null;
        try {
            timestamp = (Timestamp) value;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        if (timestamp != null) {
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(timestamp);
        } else {
            return null;
        }
    }
    
    public static void main(String[] args)
    {
        Timestamp  timestamp = new Timestamp(2017, 5,26,20,27,13,356);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp));
    }


}
