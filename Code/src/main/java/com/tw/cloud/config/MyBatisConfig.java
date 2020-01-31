package com.tw.cloud.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis配置类
 * Created by wei on 2019/4/8.
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.tw.cloud.mapper","com.tw.cloud.dao"})
public class MyBatisConfig {
}
