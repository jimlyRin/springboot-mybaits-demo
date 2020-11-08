package com.example.demo.springbootmybatis.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author linjingliang
 * @version v1.0
 * @date 2020/8/25
 */
@Configuration
@MapperScan(basePackages="com.example.demo.springbootmybatis.dao", sqlSessionFactoryRef="baseSqlSessionFactory")
public class DataSourceConfig {

    @Autowired
    private DbConfig dbConfig;

    @Bean("baseDataSource")
    @Primary
    public DataSource baseDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(dbConfig.getUrl());
        dataSource.setUsername(dbConfig.getUsername());
        dataSource.setPassword(dbConfig.getPassword());
        dataSource.setDriverClassName(dbConfig.getDriverClassName());
        dataSource.setMinIdle(dbConfig.getMinIdle());
        dataSource.setInitialSize(dbConfig.getInitialSize());
        dataSource.setMaxTotal(dbConfig.getMaxTotal());
        dataSource.setMaxWaitMillis(dbConfig.getMaxWaitMillis());
        return dataSource;
    }

    @Bean("baseSqlSessionFactory")
    @Primary
    public SqlSessionFactory baseSqlSessionFactory(@Qualifier("baseDataSource") DataSource baseDataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(baseDataSource);
        try {
            bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapping/*Mapper.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = null;
        try {
            sqlSessionFactory = bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }

    @Bean("baseDataSourceTransactionManager")
    @Primary
    public DataSourceTransactionManager baseDataSourceTransactionManager(@Qualifier("baseDataSource") DataSource baseDataSource) {
        return new DataSourceTransactionManager(baseDataSource);
    }

    @Bean("baseSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate baseSqlSessionTemplate(@Qualifier("baseSqlSessionFactory") SqlSessionFactory baseSqlSessionFactory) {
        return new SqlSessionTemplate(baseSqlSessionFactory);
    }

}
