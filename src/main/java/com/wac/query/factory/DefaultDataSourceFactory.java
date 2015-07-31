package com.wac.query.factory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wac.common.web.helper.SecurityDataSource;
import com.wac.query.enums.DriverTypeEnum;
import com.wac.query.models.KfDatabaseSource;
import com.wac.query.service.KfDatabaseSourceService;

/**
 * 数据源工厂默认实现
 * @author huangjinsheng
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
    public Optional<JdbcTemplate> loadTemplate(int sourceId) {
        JdbcTemplate template = templateMap.get(sourceId);
        if (template == null) {
            makeTemplate(sourceId);
            template = templateMap.get(sourceId);
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
                SecurityDataSource ds = new SecurityDataSource();
                ds.setDriverClass(DriverTypeEnum.getByCode(kfds.getDriverType()).driverStr());
                ds.setJdbcUrl(kfds.getJdbcUrl());
                ds.setUsername(kfds.getUserName());
                ds.setPassword(kfds.getPwd());
                ds.setIdleConnectionTestPeriod(120, TimeUnit.SECONDS);
                ds.setIdleMaxAge(60, TimeUnit.SECONDS);
                ds.setPartitionCount(1);
                ds.setAcquireIncrement(1);
                ds.setStatementsCacheSize(10);
                ds.setReleaseHelperThreads(1);
                ds.setConnectionTestStatement("SELECT 1");

                JdbcTemplate template = new JdbcTemplate(ds);
                templateMap.put(kfds.getId(), template);
            }else{
                ComboPooledDataSource ds =  new ComboPooledDataSource();
                ds.setDriverClass(DriverTypeEnum.getByCode(kfds.getDriverType()).driverStr());
                ds.setJdbcUrl(kfds.getJdbcUrl());
                ds.setUser(kfds.getUserName());
                ds.setPassword(kfds.getPwd());
                ds.setInitialPoolSize(1);
                ds.setMinPoolSize(1);
                ds.setMaxPoolSize(20);
                ds.setAcquireIncrement(1);
                ds.setMaxIdleTime(20);
                ds.setCheckoutTimeout(3000);
                ds.setTestConnectionOnCheckin(true);
                ds.setIdleConnectionTestPeriod(120);

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
