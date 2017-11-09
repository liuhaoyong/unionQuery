package com.wac.query.factory;

import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;

import com.wac.query.models.KfDatabaseSource;

/**
 * 数据源工程
 * @author huiangjisheng
 */
public interface DataSourceFactory {

    /**
     * 根据数据源定义ID获取JDBC模板
     * @param sourceId
     * @return
     */
    public Optional<JdbcTemplate> loadTemplate(KfDatabaseSource dataSource);

    /**
     * 创建数据源模板
     * @param ds
     */
    public void makeTemplate(int sourceId);


    /**
     *
     * @param kfds
     */
    public void makeTemplate(KfDatabaseSource kfds);

    /**
     *
     * @param sourceId
     */
    public void delTemplate(int sourceId);
    
}
