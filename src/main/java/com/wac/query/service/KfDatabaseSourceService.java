package com.wac.query.service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.eventbus.Subscribe;
import com.wac.query.dao.AbstractMapper;
import com.wac.query.enums.DriverTypeEnum;
import com.wac.query.event.ClearJdbcTemplateCacheEvent;
import com.wac.query.factory.DataSourceFactory;
import com.wac.query.models.KfDatabaseSource;
import com.wac.query.utils.PageVo;

/**
 * @author huangjinsheng on 2015/6/23.
 */
@Service
@Transactional(propagation= Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class KfDatabaseSourceService extends AbstractService<KfDatabaseSource,KfDatabaseSource>{

	private static Logger logger = LoggerFactory.getLogger(KfDatabaseSourceService.class);

    @Override
    @Resource(name="kfDatabaseSourceMapper")
    public void setAbstractDAO(AbstractMapper<KfDatabaseSource,KfDatabaseSource> abstractMapper) {
        this.abstractMapper = abstractMapper;
    }

    @Resource
    @Qualifier("defaultDataSourceFactory")
    private DataSourceFactory dataSourceFactory;

    @PostConstruct
    public void init(){
    	eventOf影响联合查询缓存.register(this);
    }

    @Override
    public String tableJson(KfDatabaseSource param, String sEcho, List<KfDatabaseSource> list) {
        int total = numOfTotal(param);
        List<List<String>> aaData = new LinkedList();

        for (KfDatabaseSource b : list) {
            List<String> slist = new LinkedList();
            slist.add(""+b.getId());
            slist.add(b.getDbsName());
            slist.add(DriverTypeEnum.getByCode(b.getDriverType()).name());
            slist.add(b.getUserName());
            slist.add(b.getPwd());
            slist.add(b.getJdbcUrl());
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
    public int update(final KfDatabaseSource kfDatabaseSource) {
        int count = super.update(kfDatabaseSource);
        eventOf影响联合查询缓存.post(new ClearJdbcTemplateCacheEvent(kfDatabaseSource.getId()));
        return count;
    }
    
    /**
     * 如果sql或者sqlParam被修改，需要清空cache
     * @param event
     */
    @Subscribe
    public void handleEventPost(ClearJdbcTemplateCacheEvent event){
    	try{
    		dataSourceFactory.delTemplate(event.getDatasourceId());
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    	}
    }
}
