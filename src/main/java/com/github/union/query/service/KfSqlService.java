package com.github.union.query.service;

import com.github.union.query.dao.AbstractMapper;
import com.github.union.query.dao.KfSqlParamMapper;
import com.github.union.query.enums.SqlStatusEnum;
import com.github.union.query.event.ClearUnionQueryCacheBySqlEvent;
import com.github.union.query.models.KfBusniess;
import com.github.union.query.models.KfDatabaseSource;
import com.github.union.query.models.KfSql;
import com.github.union.query.models.KfSqlParam;
import com.github.union.query.utils.PageVo;
import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liuhaoyong on 2015/6/23.
 */
@Service
@Transactional(propagation= Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class KfSqlService extends AbstractService<KfSql,KfSql>{
	private static Logger logger = LoggerFactory.getLogger(KfSqlService.class);
    @Resource
    private KfBusniessService kfBusniessService;

    @Resource
    private KfDatabaseSourceService kfDatabaseSourceService;

    @Resource
    private KfSqlParamMapper kfSqlParamMapper;
    
    @Resource
    private KfParamService kfParamService;

    @Override
    @Resource(name="kfSqlMapper")
    public void setAbstractDAO(AbstractMapper<KfSql,KfSql> abstractMapper) {
        this.abstractMapper = abstractMapper;
    }
    
    @PostConstruct
    public void init(){
    	eventOf影响联合查询缓存.register(this);
    }

    @Override
    public String tableJson(KfSql param, String sEcho, List<KfSql> list) {
        int total = numOfTotal(param);
        List<List<String>> aaData = new LinkedList<>();

        for (KfSql b : list) {
            List<String> slist = new LinkedList<>();
            slist.add("" + b.getId());
            slist.add(b.getSqlName());
            KfBusniess bus = kfBusniessService.selectByPk(b.getBusniessId());
            slist.add(bus !=null?bus.getBusniessName():"");
            KfDatabaseSource dbs = kfDatabaseSourceService.selectByPk(b.getDataSourceId());
            slist.add(dbs!=null?dbs.getDbsName():"");
            slist.add(""+b.getPriority());
            slist.add(b.getSqlStatement());
            slist.add(b.getSqlDesc());
            slist.add(SqlStatusEnum.停用.getByCode(b.getSqlStatus()).name());
            slist.add(b.getCreateTime()!=null?DateFormatUtils.ISO_DATETIME_FORMAT.format(b.getCreateTime()):"");
            slist.add(b.getUpdateTime()!=null?DateFormatUtils.ISO_DATETIME_FORMAT.format(b.getUpdateTime()):"");
            //slist.add("<a href=\"#\" onclick=\"edit('"+b.getId()+"')\">修改</a>");
            slist.add("<a href='#' onclick='edit("+b.getId()+")'>修改</a>");
            aaData.add(slist);
        }
        PageVo vo = new PageVo(sEcho,total+"",total+"",aaData);
        return vo.toString();
    }

    @Override
    public KfSql selectByPk(Integer pk) {
        KfSql sql = super.selectByPk(pk);
        if(sql != null){
            sql.getParamList().addAll(getParams(sql.getId()));
        }
        return sql;
    }

    /**
     * 查找sql的参数列表
     * @param sqlId
     * @return
     */
    public List<KfSqlParam> getParams(int sqlId){
        if(sqlId <= 0){
            return new LinkedList<KfSqlParam>();
        }
        KfSqlParam param = new KfSqlParam();
        param.setSqlId(sqlId);
        param.setStartIndex(0);
        param.setEndIndex(Integer.MAX_VALUE);
        return kfSqlParamMapper.queryByCondition(param);
    }

    /**
     * 根据业务号获取sql
     * @param bid
     * @return
     */
    public List<KfSql> getSqlByBusniess(int bid){
        if(bid <= 0){
            return new LinkedList<KfSql>();
        }
        KfSql sql = new KfSql();
        sql.setBusniessId(bid);
        return this.all(sql);
    }

    /**
     * 更具业务获取所有参数
     * @param bid
     * @return
     */
    public List<KfSqlParam> getParamsByBusniess(int bid){
        return this.getSqlByBusniess(bid).stream()
                .map(sql -> { return this.getParams(sql.getId());})
                .flatMap(params -> params.stream())
                .filter(param -> param != null)
                .collect(Collectors.toList());
    }


    /**
     * 
     * @param list
     */
    public void saveParams(List<KfSqlParam> list,Integer sqlId){
    	
        if(list == null || list.isEmpty()){
            logger.warn("list is empty..");
            return;
        }
        KfSql sql = selectByPk(sqlId);
        if(sql == null){
            logger.error("KfSql[id="+sqlId+"] is null..");
            return;
        }
        

        //原有的参数id
        List<Integer> oldParamIds = sql.getParamList().stream().map(param -> {
            return param.getId();
        }).collect(Collectors.toList());

        //新参数id
        List<Integer> newParamIds = list.stream()
                .filter(param -> param.getId()!=null && param.getId().intValue() > 0 )
                .map(param -> { return param.getId(); })
                .collect(Collectors.toList());

        //新参数中不包含老参数的id，就是需要删除的id
        List<Integer> delParamIds = oldParamIds.stream()
                .filter(oldId -> !newParamIds.contains(oldId))
                .collect(Collectors.toList());

        list.stream().forEach(param -> {
            if(param.getId().intValue() <= 0){
                kfSqlParamMapper.insert(param);
            }else{
                kfSqlParamMapper.updateByPrimaryKey(param);
            }
        });

        delParamIds.stream().forEach(id -> {
            KfSqlParam p = new KfSqlParam();
            p.setId(id);
            kfSqlParamMapper.deleteByCondition(p);
        });
        
        eventOf影响联合查询缓存.post(new ClearUnionQueryCacheBySqlEvent(sqlId));
    }
    
    
    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=false)
    public int insert(KfSql e){
        e.setCreateTime(new Date(System.currentTimeMillis()));
        
        int sqlId = abstractMapper.insert(e);
        
        this.saveParams(e.getParamList(), sqlId);
        
        return sqlId;
    }

    
    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=false)
    public int update(KfSql e){
        int count = abstractMapper.updateByPrimaryKey(e);
        this.saveParams(e.getParamList(), e.getId());
        //eventOf影响联合查询缓存.post(new ClearUnionQueryCacheBySqlEvent(e.getId()));
        return count;
    }
    
    /**
     * 如果sql或者sqlParam被修改，需要清空cache
     * @param event
     */
    @Subscribe
    public void handleEventPost(ClearUnionQueryCacheBySqlEvent event){
    	try{
    		KfSql sql = this.selectByPk(event.getSqlId());
    		if(sql != null){
    			this.clearCache(sql.getBusniessId());
    		}
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    	}
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args){

    }
}
