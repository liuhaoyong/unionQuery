/**
 *
 */
package com.wac.query.service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.eventbus.AsyncEventBus;
import com.wac.query.dao.AbstractMapper;
import com.wac.query.models.BaseBean;
import com.wac.query.models.ParamBean;
import com.wac.query.models.QueryRelatedInfo;

/**
 * 
 * @author huangjinsheng on 2015年7月30日
 *
 * @param <T>
 * @param <E>
 */
public abstract class AbstractService<T extends ParamBean /** 查询条件*/, E extends BaseBean /** 返回对象*/> {
	private static Logger logger = LoggerFactory.getLogger(AbstractService.class);
    protected AbstractMapper abstractMapper;

    protected static final ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);
    protected static final  AsyncEventBus eventOf影响联合查询缓存 = new AsyncEventBus(pool);
    
    
    /**
     * 查询相关的本地缓存
     * key ： 商务id  value 其查询相关的所有信息
     */
    protected static final Cache<String, QueryRelatedInfo> cache = CacheBuilder.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .removalListener(new RemovalListener<String, QueryRelatedInfo>() {
                @Override
                public void onRemoval(RemovalNotification<String, QueryRelatedInfo> removalNotification) {
                    logger.info(String.format("Cache is removed! key = [%s] ", removalNotification.getKey()));
                }
            })
            .build();
    
    public void setAbstractDAO(AbstractMapper<T,E> abstractMapper) {
        this.abstractMapper = abstractMapper;
    }

    /**
     *
     * @param t
     * @param sEcho
     * @param list
     * @return
     */
    public String tableJson(T t, String sEcho, List<E> list) {return null;}

    /**
     *
     * @param t
     * @return
     */
    public List<E> search(T t) {
        t.cal();
        return abstractMapper.queryByCondition(t);
    }

    /**
     *
     * @param t
     * @return
     */
    public List<T> all(T t) {
        t.setStartIndex(0);
        t.setEndIndex(Integer.MAX_VALUE);
        return abstractMapper.queryByCondition(t);
    }

    /**
     *
     * @param t
     * @return
     */
    public int numOfTotal(T t) {
        Integer it = abstractMapper.countByCondition(t);
        if (it == null)
            return 0;
        return it.intValue();
    }

    /**
     *
     * @param pk
     * @return
     */
    public E selectByPk(Integer pk) {
       return (E)abstractMapper.queryByPrimaryKey(pk);
    }

    /**
     *
     * @param e
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,readOnly=false)
    public int insert(E e){
        e.setCreateTime(new Date(System.currentTimeMillis()));
        return abstractMapper.insert(e);
    }

    /**
     *
     * @param e
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,readOnly=false)
    public int update(E e){
        return abstractMapper.updateByPrimaryKey(e);
    }
    
    /**
     * 清除缓存
     * @param bussniessId
     */
    public void clearCache(int bussniessId){
        if(bussniessId <= 0){
            return ;
        }
        cache.invalidate(makeKey(bussniessId));
    }
    
    /**
    *
    * @param bussniessId
    * @return
    */
   protected String makeKey(int bussniessId){
       return ""+bussniessId;
   }
   
   /**
    * 
    * @param exception
    * @return
    */
   public static String printErrorTrace(Throwable exception) {
		StringBuffer errorString = new StringBuffer();
		errorString.append(exception.toString()).append("\n");
		StackTraceElement[] trace = exception.getStackTrace();
		for (int i=0; i < trace.length; i++) {
			errorString.append(trace[i]).append("\n");
		}
		return errorString.toString();
	}
}
