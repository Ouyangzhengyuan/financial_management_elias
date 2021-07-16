package pers.elias.financial_management.config;

import com.sun.org.apache.xpath.internal.operations.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pers.elias.financial_management.service.impl.AccountUserService;

@Configuration //注解配置
@EnableWebSecurity //注解开启网络安全
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired //注入用户服务
    private AccountUserService accountUserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //自定义身份验证服务
        auth.userDetailsService(accountUserService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //授权认证请求
                .authorizeRequests()
                //允许访问指定的页面和资源
//                .antMatchers("/css/**", "/font/**", "/images/**", "/lay/**", "/*.js")
//                .permitAll()


                //其他任何请求都要请求认证
                .and()
                //使用表单登录 指定登录页面
                .formLogin().loginPage("/login")
                //指定表单参数
                .usernameParameter("userName")
                .passwordParameter("userPass")
                //登录成功默认跳转 url
                .defaultSuccessUrl("/keepAccounts/toKeepAccounts")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                //记住我 自动登录
                .rememberMe()
                //自定义记住我参数
                .rememberMeParameter("rememberMe")
                //设置cookies失效时间
                .tokenValiditySeconds(24 * 60 * 60)
                .and()
                // 防止报Refused to display in a frame because it set 'X-Frame-Options' to 'DENY'错误
                .headers().frameOptions().disable()
                .and()
                .csrf().disable();
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/css/**", "/font/**", "/images/**", "/lay/**", "/*.js");
//    }
}
