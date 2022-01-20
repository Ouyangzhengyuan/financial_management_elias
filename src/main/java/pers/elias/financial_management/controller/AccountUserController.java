package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.bean.AjaxResult;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.common.api.CommonResult;
import pers.elias.financial_management.model.AccountIndex;
import pers.elias.financial_management.model.AccountUser;
import pers.elias.financial_management.service.impl.AccountIndexService;
import pers.elias.financial_management.service.impl.AccountUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Api(tags = "账户管理接口：AccountUserController", description = "账户管理")
public class AccountUserController {
    @Autowired
    private AjaxResult ajaxResult;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountIndexService accountIndexService;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 注册新用户
     */
    @ResponseBody
    @ApiOperation(value = "登录以后返回token")
    @RequestMapping("/register.do")
    public AjaxResult register(AccountUser user, HttpServletRequest request) {
        try {
            //判断用户名是否被占用
            if(!accountUserService.isExists(user.getUserName())){
                user.setUserPass(passwordEncoder.encode(user.getUserPass()));//编译账户密码
                user.setSecondPass(passwordEncoder.encode(user.getSecondPass()));//编译二级密码
                user.setRoles("ROLE_USER");//设置身份
                user.setDateCreated(new Date());//设置注册时间
                //创建用户
                accountUserService.addAccountUser(user);
                //账户索引实体
                AccountIndex accountIndex = new AccountIndex();
                accountIndex.setUserName(user.getUserName());
                //创建账户索引数据
                accountIndexService.insert(accountIndex);
                //实现自动登录
                UserDetails userDetails = accountUserService.loadUserByUsername(user.getUserName());
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
                //设置登录时间
                user.setDateLogin(new Date());
                //当前登陆时间持久化
                accountUserService.updateByPrimaryKeySelective(user);
                //设置当前用户全局变量
                globalAccountInfo.setUserName(securityContext.getAuthentication().getName());
                //设置当前用户全局登录时间
                globalAccountInfo.setLoginDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(accountUserService.selectByPrimaryKey(globalAccountInfo.getUserName()).getDateLogin()));
                ajaxResult.setSuccess(true);
                ajaxResult.setMessage("注册成功");
                return ajaxResult;
            }
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("您输入的用户名已被占用");
            return ajaxResult;
        } catch (Exception e) {
            e.printStackTrace();
            ajaxResult.setSuccess(false);
            ajaxResult.setMessage("注册异常！");
            return ajaxResult;
        }
    }

    /**
     * 用户登录
     * 自定义使用 jwt token 登录
     */
    @ResponseBody
    @ApiOperation(value = "登录以后返回token")
    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public Object login(String userName, String userPass) {
        String token = accountUserService.login(userName, userPass);
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    /**
     * 获取注册天数
     */
    @ResponseBody
    @RequestMapping("/getUserCreationDays")
    public String getUserCreationDays(){
        JSONObject jsonObject = new JSONObject();
        try{
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            Integer userCreationDays = accountUserService.getUserCreationDays(globalAccountInfo.getUserName());
            jsonObject.put("success", true);
            jsonObject.put("data", userCreationDays);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "获取注册天数异常");
            return jsonObject.toString();
        }
    }

    /**
     * 注销账户
     */
    @ResponseBody
    @RequestMapping("/closeAccount")
    public String users(String userName){
        JSONObject jsonObject = new JSONObject();
        try{
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            //注销当前用户
            accountUserService.deleteByPrimaryKey(globalAccountInfo.getUserName());
            jsonObject.put("success", true);
            jsonObject.put("msg", "注销成功");
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "注销失败");
            return jsonObject.toString();
        }
    }

    /**
     * 验证二级密码
     */
    @ResponseBody
    @RequestMapping("/verifySecondPass")
    public String verifySecondPass(String secondPass) {
        JSONObject jsonObject = new JSONObject();
        //设置当前登录的用户
        globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
        //验证密码
        if (secondPass.isEmpty()) {
            jsonObject.put("success", false);
            jsonObject.put("msg", "请输入二级密码");
        } else if (passwordEncoder.matches(secondPass, accountUserService.selectSecondPass(globalAccountInfo.getUserName()))) {
            jsonObject.put("success", true);
            jsonObject.put("msg", "验证成功");
        } else {
            jsonObject.put("success", false);
            jsonObject.put("msg", "密码不正确，请重新输入");
        }
        return jsonObject.toString();
    }
}
