package pers.elias.financial_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.elias.financial_management.bean.AjaxResult;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.bean.GlobalAccountInfoTemp;
import pers.elias.financial_management.bean.TableReload;

@Configuration
public class BeanConfig {
    @Bean
    public AjaxResult ajaxResult(){
        return new AjaxResult();
    }

    @Bean
    public GlobalAccountInfoTemp globalAccountInfoTemp(){
        return new GlobalAccountInfoTemp();
    }

    @Bean
    public TableReload tableReload(){
        return new TableReload();
    }

}
