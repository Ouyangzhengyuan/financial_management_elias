package pers.elias.financial_management.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("pers.elias.financial_management.mapper")
public class MyBatisConfig {

}
