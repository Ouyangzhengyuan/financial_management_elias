package pers.elias.financial_management.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.elias.financial_management.common.utils.JwtTokenUtil;
import pers.elias.financial_management.component.UserCreationDays;
import pers.elias.financial_management.mapper.AccountUserMapper;
import pers.elias.financial_management.model.AccountUser;
import pers.elias.financial_management.service.IAccountUserService;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AccountUserService implements IAccountUserService, UserDetailsService {
    @Autowired
    private AccountUserMapper accountUserMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CategoryFirstService categoryFirstService;

    @Autowired
    private CategorySecondService categorySecondService;

    @Autowired
    private AccountFinancialService accountFinancialService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private CategoryTemplateService categoryTemplateService;

    @Autowired
    private AccountCurrentService accountCurrentService;

    @Autowired
    private AccountBookHeaderService accountBookHeaderService;

    @Autowired
    private AccountUserHeaderService accountUserHeaderService;

    @Autowired
    private AccountBookService accountBookService;

    @Autowired
    private AccountIndexService accountIndexService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountUserService.class);

    /**
     * 注销账户、删除账户有关的所有数据
     */
    @Override
    @Transactional
    public int deleteByPrimaryKey(String userName) {
        //删除该账户下的所有一级分类
        categoryFirstService.deleteByUserName(userName);
        //删除该账户下的所有二级分类
        categorySecondService.deleteByUserName(userName);
        //删除该账户下的所有分类模板
        categoryTemplateService.deleteByUserName(userName);
        //删除该账户下的所有一级金融账户
        accountFinancialService.deleteByUserName(userName);
        //删除该账户下的所有二级金融账户
        accountTypeService.deleteByUserName(userName);
        //删除该账户下的所有流水记录
        accountCurrentService.deleteByUserName(userName);
        //删除该账户下的所有账本头像
        accountBookHeaderService.deleteByUserName(userName);
        //删除该账户的头像
        accountUserHeaderService.deleteByUserName(userName);
        //删除该账户的所有账本
        accountBookService.deleteByUserName(userName);
        //删除账户索引
        accountIndexService.deleteByPrimaryKey(userName);
        return accountUserMapper.deleteByPrimaryKey(userName);
    }

    @Override
    public int insert(AccountUser record) {
        return accountUserMapper.insert(record);
    }

    @Override
    public int insertSelective(AccountUser record) {
        return accountUserMapper.insertSelective(record);
    }

    @Override
    public AccountUser selectByPrimaryKey(String userName) {
        return accountUserMapper.selectByPrimaryKey(userName);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountUser record) {
        return accountUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountUser record) {
        return accountUserMapper.updateByPrimaryKey(record);
    }

    @Override
    public String selectSecondPass(String userName) {
        return accountUserMapper.selectSecondPass(userName);
    }

    @Override
    public boolean isSettingSecondPass(String userName) {
        return accountUserMapper.selectSecondPass(userName) != null;
    }

    @Override
    public boolean checkSecondPass(String userName, String secondPass) {
        return !secondPass.equals(accountUserMapper.selectSecondPass(userName));
    }

    @Override
    public boolean verifySecondPass(String userName, String secondPass) {
        return secondPass.equals(accountUserMapper.selectSecondPass(userName));
    }

    @Override
    public AccountUser addAccountUser(AccountUser user) {
        accountUserMapper.insert(user);
        return user;
    }

    @Override
    public boolean isExists(String userName) {
        return accountUserMapper.selectByPrimaryKey(userName) != null;
    }

    @Override
    public int getUserCreationDays(String userName) {
        String nowDate = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
        String createdDate = new SimpleDateFormat("YYYY-MM-dd").format(accountUserMapper.selectDateCreated(userName));
        return UserCreationDays.getUserCreationDays(nowDate, createdDate);
    }

    @Override
    public String login(String userName, String userPass) {
        String token = null;
        try {
            UserDetails userDetails = loadUserByUsername(userName);
            if (!passwordEncoder.matches(userPass, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常:{}", e.getMessage());
        }
        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountUser user = accountUserMapper.selectByPrimaryKey(username);
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        return new User(username, user.getUserPass(), AuthorityUtils.commaSeparatedStringToAuthorityList("user"));
    }
}
