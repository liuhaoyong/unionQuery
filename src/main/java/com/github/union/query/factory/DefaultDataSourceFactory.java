package com.github.union.query.factory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.union.query.models.KfDatabaseSource;
import com.github.union.query.service.KfDatabaseSourceService;

/**
 * 数据源工厂默认实现
 * @author liuhaoyong
 * @version 2015年7月21日
 */
@Service
public class DefaultDataSourceFactory implements DataSourceFactory {
	private static Logger logger = LoggerFactory.getLogger(DefaultDataSourceFactory.class);
    
    @Value("${d.v}")
    private String env;

    @Resource
    private KfDatabaseSourceService kfDatabaseSourceService;

    /** 模板MAP */
    private final static Map<Integer, JdbcTemplate> templateMap = new ConcurrentHashMap<>();

    /*
     * (non-Javadoc)
     * @see com.sdo.basis.biz.query.factory.DataSourceFactory#loadTemplate(int)
     */
    @Override
    public Optional<JdbcTemplate> loadTemplate(KfDatabaseSource dataSource) {
        JdbcTemplate template = templateMap.get(dataSource.getId());
        if (template == null) {
            makeTemplate(dataSource);
            template = templateMap.get(dataSource.getId());
        }
        return Optional.ofNullable(template);
    }

    /**
     * 正式环境用淘宝的数据源，否则用户c3p0数据源
     * @param sourceId
     */
    @Override
    public void makeTemplate(int sourceId) {
        try{
            KfDatabaseSource kfds = kfDatabaseSourceService.selectByPk(sourceId);
            this.makeTemplate(kfds);
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    @Override
    public void makeTemplate(KfDatabaseSource kfds) {
        try{
            if(kfds == null){
                logger.error("KfDatabaseSource not found!");
                return ;
            }

            logger.info("=====================================================");
            logger.info(String.format("env:[%s]", env));
            logger.info("=====================================================");
            
            if(StringUtils.equalsIgnoreCase(env,"www")){//正式环境
            	DruidDataSource ds = new DruidDataSource();
            	ds.setUrl(kfds.getJdbcUrl());
            	ds.setUsername(kfds.getUserName());
            	ds.setPassword(kfds.getPwd());
            	ds.setConnectionProperties("config.decrypt=true");
            	ds.setInitialSize(1);
            	ds.setMinIdle(10);
            	ds.setMaxActive(50);
            	ds.setMaxWait(60000);
            	ds.setTimeBetweenEvictionRunsMillis(60000);
            	ds.setMinEvictableIdleTimeMillis(300000);
            	ds.setValidationQuery("SELECT 'x'");
            	ds.setTestWhileIdle(true);
            	ds.setTestOnBorrow(false);
            	ds.setTestOnReturn(false);
            	ds.setPoolPreparedStatements(true);
            	ds.setMaxPoolPreparedStatementPerConnectionSize(20);
            	ds.setFilters("wall,config");
            	ds.init();

                JdbcTemplate template = new JdbcTemplate(ds);
                templateMap.put(kfds.getId(), template);
            }else{
            	DruidDataSource ds = new DruidDataSource();
            	ds.setUrl(kfds.getJdbcUrl());
            	ds.setUsername(kfds.getUserName());
            	ds.setPassword(kfds.getPwd());
            	ds.setConnectionProperties("config.decrypt=false");
            	ds.setInitialSize(1);
            	ds.setMinIdle(10);
            	ds.setMaxActive(50);
            	ds.setMaxWait(60000);
            	ds.setTimeBetweenEvictionRunsMillis(60000);
            	ds.setMinEvictableIdleTimeMillis(300000);
            	ds.setValidationQuery("SELECT 'x'");
            	ds.setTestWhileIdle(true);
            	ds.setTestOnBorrow(false);
            	ds.setTestOnReturn(false);
            	ds.setPoolPreparedStatements(true);
            	ds.setMaxPoolPreparedStatementPerConnectionSize(20);
            	ds.setFilters("wall,config");
                JdbcTemplate template = new JdbcTemplate(ds);
                templateMap.put(kfds.getId(), template);
            }
        }catch(Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    @Override
    public void delTemplate(int sourceId){
        templateMap.remove(sourceId);
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }
}
