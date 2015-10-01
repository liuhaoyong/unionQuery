package com.wac.query.service;


import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.eventbus.Subscribe;
import com.wac.query.dao.AbstractMapper;
import com.wac.query.event.ClearUnionQueryCacheByBusniessEvent;
import com.wac.query.models.KfBusniess;
import com.wac.query.utils.PageVo;

/**
 * @author huangjinsheng on 2015/6/23.
 */
@Service
@Transactional(propagation= Propagation.REQUIRED,readOnly=false,rollbackFor=Exception.class)
public class KfBusniessService extends AbstractService<KfBusniess,KfBusniess>{
	
	private static Logger logger = LoggerFactory.getLogger(KfBusniessService.class);

//    @Resource
//    private KfBusniessMapper kfBusniessMapper;
    @Override
    @Resource(name="kfBusniessMapper")
    public void setAbstractDAO(AbstractMapper<KfBusniess,KfBusniess> abstractMapper) {
        this.abstractMapper = abstractMapper;
    }
    
    @PostConstruct
    public void init(){
    	eventOf影响联合查询缓存.register(this);
    }

    @Override
    public String tableJson(KfBusniess kfBusniess, String sEcho, List<KfBusniess> list) {
        int total = numOfTotal(kfBusniess);
        List<List<String>> aaData = new LinkedList<>();

        for (KfBusniess b : list) {
            List<String> slist = new LinkedList<>();
            slist.add(""+b.getId());
            slist.add(b.getBusniessName());
            slist.add(b.getCreateTime()!=null?DateFormatUtils.ISO_DATETIME_FORMAT.format(b.getCreateTime()):"");
            slist.add(b.getUpdateTime()!=null?DateFormatUtils.ISO_DATETIME_FORMAT.format(b.getUpdateTime()):"");
            slist.add("<a href=\"#\" onclick=\"edit('"+b.getId()+"')\">修改</a>");
            aaData.add(slist);
        }
        PageVo vo = new PageVo(sEcho,total+"",total+"",aaData);
        return vo.toString();
    }
    
    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=false)
    public int update(KfBusniess e){
        int count = abstractMapper.updateByPrimaryKey(e);
        eventOf影响联合查询缓存.post(new ClearUnionQueryCacheByBusniessEvent(e.getId()));
        return count;
    }
    
    /**
     * 如果sql或者sqlParam被修改，需要清空cache
     * @param event
     */
    @Subscribe
    public void handleEventPost(ClearUnionQueryCacheByBusniessEvent event){
    	try{
    		this.clearCache(event.getBusniessId());
    	}catch(Exception e){
    		logger.error(e.getMessage(),e);
    	}
    }
}
