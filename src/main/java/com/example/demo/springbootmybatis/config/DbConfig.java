package com.example.demo.springbootmybatis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author linjingliang
 * @version v1.0
 * @date 2020/8/25
 */
@Data
@Configuration
@ConfigurationProperties(prefix="spring.datasource")
public class DbConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    /**
     * 最小空闲连接:连接池中容许保持空闲状态的最小连接数量
     */
    private int minIdle;
    /**
     * 初始化连接数量
     */
    private int initialSize;
    private int maxTotal;
    private int maxWaitMillis;
}
