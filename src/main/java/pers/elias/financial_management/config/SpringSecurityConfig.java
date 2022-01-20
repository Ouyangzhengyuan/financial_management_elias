package pers.elias.financial_management.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import pers.elias.financial_management.component.JwtAuthenticationTokenFilter;
import pers.elias.financial_management.component.RestAuthenticationEntryPoint;
import pers.elias.financial_management.component.RestfulAccessDeniedHandler;
import pers.elias.financial_management.service.impl.AccountUserService;

import javax.sql.DataSource;

@Configuration //注解配置
@EnableWebSecurity //注解开启网络安全
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired //注入数据源
    private DataSource dataSource;

    @Autowired //注入登录成功处理器
    private AuthSuccessHandler authSuccessHandler;

    @Autowired //注入登录失败处理器
    private AuthFailureHandler authFailureHandler;

    @Autowired //注入用户服务
    private AccountUserService accountUserService;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;


    @Bean //自动登录、记住我token数据库
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    //注入密码编译器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //自定义身份验证服务
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountUserService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //授权认证请求
                .authorizeRequests()
                //允许访问指定的页面和资源
                .antMatchers("/login.html", "/login.do", "/register", "/register.do", "/initAccountBook").permitAll()
                .antMatchers("/static/**", "/css/**", "/font/**", "/images/**", "/lay/**", "/layui.js", "/modules/**")
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
                .anyRequest()
//               其他任何请求都要请求认证
                .authenticated()
                .and()
                //使用表单登录 指定登录页面
                .formLogin().loginPage("/login.html")
                //指定表单参数
                .usernameParameter("userName")
                .passwordParameter("userPass")
                //登录成功默认跳转 url
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .and()
                //记住我 自动登录
                .rememberMe()
                //自定义记住我参数
                .rememberMeParameter("remember-me")
                //设置cookies失效时间
                .tokenValiditySeconds(24 * 60 * 60)
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(accountUserService)
                .and()
                //防止报Refused to display in a frame because it set 'X-Frame-Options' to 'DENY'错误
                .headers().frameOptions().disable()
                .and()
                .csrf().disable();
//                .sessionManagement()// 基于token，所以不需要session
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//         禁用缓存
//        http.headers().cacheControl();
//        // 添加JWT filter
//        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.exceptionHandling()
//                .accessDeniedHandler(restfulAccessDeniedHandler)
//                .authenticationEntryPoint(restAuthenticationEntryPoint);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**", "/css/**", "/font/**", "/images/**", "/lay/**", "/layui.js", "/modules/**");
    }
}
