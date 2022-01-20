package pers.elias.financial_management.config;

import oracle.jrockit.jfr.events.RequestableEventEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.mapper.AccountUserMapper;
import pers.elias.financial_management.model.AccountUser;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountFinancialService;
import pers.elias.financial_management.service.impl.AccountUserService;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 登录成功处理器
 */
@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private AccountUserMapper accountUserMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //设置用户属性
        setUserInfo(authentication);
        //页面跳转处理
        response.sendRedirect("/keepAccounts");
    }

    public void setUserInfo(Authentication authentication){
        //当前用户
        String userName = authentication.getName();
        //登录时间持久化
        AccountUser accountUser = new AccountUser();
        accountUser.setUserName(userName);
        accountUser.setDateLogin(new Date());
        accountUserService.updateByPrimaryKeySelective(accountUser);
    }
}
