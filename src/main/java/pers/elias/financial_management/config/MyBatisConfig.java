package pers.elias.financial_management.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 配置 mapper 接口扫描目录
 */
@Configuration
@MapperScan("pers.elias.financial_management.mapper")
public class MyBatisConfig {

}
