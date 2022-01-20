package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.component.FileCopy;
import pers.elias.financial_management.component.FileUpload;
import pers.elias.financial_management.mapper.CategoryTemplateMapper;
import pers.elias.financial_management.model.*;
import pers.elias.financial_management.service.impl.*;
import pers.elias.financial_management.utils.DateTimeUtil;
import pers.elias.financial_management.utils.KeepTwoDecimals;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/setting")
public class SettingController {
    @Autowired
    private FileUpload fileUpload;

    @Autowired
    private FileCopy fileCopy;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountUserService accountUserService;

    @Autowired
    private AccountBookService accountBookService;

    @Autowired
    private AccountUserHeaderService accountUserHeaderService;

    @Autowired
    private AccountCurrentService accountCurrentService;

    @Autowired
    private TotalAmountCalculateService totalAmountCalculateService;

    @Autowired
    private AccountFinancialService accountFinancialService;

    @Autowired
    private CategoryFirstService categoryFirstService;

    @Autowired
    private CategorySecondService categorySecondService;

    @Autowired
    private CategoryTemplateService categoryTemplateService;

    @Autowired
    private AccountTypeService accountTypeService;

    @Autowired
    private AccountIndexService accountIndexService;

    @Value("${user-img-absolute-path}")
    private String userImgAbsolutePath;

    @Value("${user-img-relative-path}")
    private String userImgRelativePath;
    /**
     * 获取用户基本信息
     */
    @ResponseBody
    @RequestMapping("/getPersonalDetails")
    public String getPersonalDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            //查询用户信息
            AccountUser user = accountUserService.selectByPrimaryKey(globalAccountInfo.getUserName());
            jsonObject.put("success", true);
            jsonObject.put("userName", user.getUserName());
            jsonObject.put("tel", user.getTel());
            jsonObject.put("eMail", user.geteMail());
            jsonObject.put("loginDateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(user.getDateLogin()));
            jsonObject.put("registerDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(user.getDateCreated()));
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            return jsonObject.toString();
        }
    }

    /**
     * 修改用户基本信息
     */
    @ResponseBody
    @RequestMapping("/editPersonalDetails")
    public String editPersonalDetails(AccountUser user) {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            user.setUserName(globalAccountInfo.getUserName());
            accountUserService.updateByPrimaryKeySelective(user);
            jsonObject.put("success", true);
            jsonObject.put("msg", "修改成功");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "修改失败");
            return jsonObject.toString();
        }
    }

    /**
     * 读取用户头像
     */
    @RequestMapping("/getUserHeaderPath")
    @ResponseBody
    public String getHeaderPath() {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            AccountUserHeader accountUserHeader = accountUserHeaderService.selectByPrimaryKey(globalAccountInfo.getUserName());
            if (accountUserHeader != null) {
                //查询用户账本头像路径
                String headerPath = accountUserHeaderService.selectByPrimaryKey(globalAccountInfo.getUserName()).getHeaderPath();
                jsonObject.put("success", true);
                jsonObject.put("data", headerPath);
                jsonObject.put("msg", "上传成功");
                return jsonObject.toString();
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "上传失败");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "上传异常");
            return jsonObject.toString();
        }
    }


    /**
     * 账户头像上传
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String uploadAccountUserHeader(MultipartFile file, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            //设置保存路径
            String dirPath = userImgAbsolutePath;
            String tempPath = request.getServletContext().getRealPath("/images/");
            //执行上传文件到服务器临时目录
            file.transferTo(fileUpload.getDestFile(tempPath, file));
            //获取服务器临时目录文件
            File tempFile = new File(tempPath, fileUpload.getFilename());
            //将临时目录的文件复制到本地当前项目文件夹 /images/
            if (tempFile.exists()) {
                fileCopy.copyFile(dirPath, tempFile);
            }
            //设置相对路径
            String relativePath = userImgRelativePath + fileUpload.getFilename();
            //账本头像实体类
            AccountUserHeader accountUserHeader = new AccountUserHeader();
            accountUserHeader.setUserName(globalAccountInfo.getUserName());
            accountUserHeader.setHeaderPath(relativePath);
            //判断数据库是否有头像记录，如果存在只需要更新路径即可
            if (accountUserHeaderService.selectByPrimaryKey(globalAccountInfo.getUserName()) != null) {
                //查询现有路径  获取路径后缀文件名称
                String headerPath = accountUserHeaderService.selectByPrimaryKey(globalAccountInfo.getUserName()).getHeaderPath();
                int beginIndex = headerPath.lastIndexOf("/") + 1;
                String fileName = headerPath.substring(beginIndex);
                //创建文件对象
                File headerFile = new File(dirPath, fileName);
                //如果 /images/ 目录里有相应的文件存在，则删除该文件
                if (headerFile.exists()) {
                    boolean success = headerFile.delete();
                }
                //更新账本头像路径
                accountUserHeaderService.updateByPrimaryKey(accountUserHeader);
                jsonObject.put("code", 200);
                jsonObject.put("msg", "账户头像更新成功！");
                jsonObject.put("data", relativePath);
                return jsonObject.toString();
            }
            //上传成功后将路径添加到数据库
            accountUserHeaderService.insert(accountUserHeader);
            //响应头
            jsonObject.put("code", 200);
            jsonObject.put("msg", "账户头像上传成功");
            jsonObject.put("data", relativePath);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "头像上传失败！");
            return jsonObject.toString();
        }
    }

    /**
     * 修改账户密码
     */
    @ResponseBody
    @RequestMapping("/editAccountPass")
    public JSONObject editAccountPass(String nowPass, String newPass) {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            String userPass = accountUserService.selectByPrimaryKey(globalAccountInfo.getUserName()).getUserPass();
            if (passwordEncoder.matches(nowPass, userPass)) {
                AccountUser user = new AccountUser();
                user.setUserName(globalAccountInfo.getUserName());
                user.setUserPass(passwordEncoder.encode(newPass));
                accountUserService.updateByPrimaryKeySelective(user);
                jsonObject.put("success", true);
                jsonObject.put("msg", "修改成功");
                return jsonObject;
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "当前密码不正确，请重新输入");
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "修改失败");
            return jsonObject;
        }
    }

    /**
     * 修改二级密码
     */
    @ResponseBody
    @RequestMapping("/editSecondPass")
    public JSONObject editSecondPass(String nowSecondPass, String newSecondPass) {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            //获取用户二级密码
            String oldPass = accountUserService.selectByPrimaryKey(globalAccountInfo.getUserName()).getSecondPass();
            if (passwordEncoder.matches(nowSecondPass, oldPass)) {//校验二级密码
                AccountUser user = new AccountUser();
                user.setUserName(globalAccountInfo.getUserName());
                user.setSecondPass(passwordEncoder.encode(newSecondPass));
                accountUserService.updateByPrimaryKeySelective(user);//更新密码
                jsonObject.put("success", true);
                jsonObject.put("msg", "二级密码修改成功");
                return jsonObject;
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "当前二级密码不正确");
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "修改异常");
            return jsonObject;
        }
    }

    /**
     * 获取账本管理数据：账本列表、流水计数、总支出、总收入、总负债、净资产
     */
    @ResponseBody
    @RequestMapping("/accountBookTable")
    public String accountBookTable() {
        JSONObject jsonObject = new JSONObject();
        try {
            //设置当前登录的用户
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            List<AccountBookManage> accountBookManageList = new ArrayList<>();
            for (String accountBookName : accountBookService.selectAllByUserName(globalAccountInfo.getUserName())) {
                //获取账本id
                globalAccountInfo.setAccountBookName(accountBookName);
                Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
                //流水实体映射
                Map<String, Object> map = new HashMap<>();
                map.put("userName", globalAccountInfo.getUserName());
                map.put("accountBookId", accountBookId);
                //查询账本流水计数
                Integer count = accountCurrentService.selectCount(map);
                //查询总支出 过滤所有信用账户和负债账户
                AccountFinancial accountFinancial = new AccountFinancial();
                accountFinancial.setUserName(globalAccountInfo.getUserName());
                accountFinancial.setAccountBookId(accountBookId);
                accountFinancial.setFinancialAccountName("信用账户");
                Integer creditAccountId = accountFinancialService.selectId(accountFinancial);
                accountFinancial.setFinancialAccountName("负债账户");
                Integer liabilityAccountId = accountFinancialService.selectId(accountFinancial);
                List<Integer> limitAccountFinancialIdList = new ArrayList<>();
                limitAccountFinancialIdList.add(creditAccountId);
                limitAccountFinancialIdList.add(liabilityAccountId);
                //实体类字段
                Map<String, Object> allInMap = new HashMap<>();
                allInMap.put("userName", globalAccountInfo.getUserName());
                allInMap.put("accountBookId", accountBookId);
                allInMap.put("inExStatus", "收");
                allInMap.put("accountFinancialId", limitAccountFinancialIdList);
                //执行查询总收入
                Double allIn = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculateService.calculateAllInEx(allInMap));
                //查询总支出
                allInMap.put("inExStatus", "支");
                Double allEx = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculateService.calculateAllInEx(allInMap));
                //信用账户实体
                List<AccountCurrent> accountCurrentList = new ArrayList<>();
                AccountCurrent creditAccount = new AccountCurrent();
                creditAccount.setUserName(globalAccountInfo.getUserName());
                creditAccount.setAccountBookId(accountBookId);
                creditAccount.setAccountFinancialId(creditAccountId);//信用账户id
                accountCurrentList.add(creditAccount);
                //负债账户实体
                AccountCurrent liabilityAccount = new AccountCurrent();
                liabilityAccount.setUserName(globalAccountInfo.getUserName());
                liabilityAccount.setAccountBookId(accountBookId);
                liabilityAccount.setAccountFinancialId(liabilityAccountId);//负债账户id
                accountCurrentList.add(liabilityAccount);
                //查询总负债
                Double allDebts = totalAmountCalculateService.calculateAllDebts(accountCurrentList);
                //封装数据对象
                AccountBookManage accountBookManage = new AccountBookManage();
                accountBookManage.setAccountBookName(accountBookName);//账本名称
                accountBookManage.setCount(count);
                accountBookManage.setAllIn(allIn);
                accountBookManage.setAllEx(allEx);
                accountBookManage.setAllDebts(allDebts);
                accountBookManage.setNetAssets(KeepTwoDecimals.calculateKeepTwoDeci(allIn - allDebts));
                //将数据对象添加到列表
                accountBookManageList.add(accountBookManage);
            }
            jsonObject.put("code", 0);
            jsonObject.put("msg", "查询账本管理成功");
            jsonObject.put("count", accountBookManageList.size());
            jsonObject.put("data", accountBookManageList);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "查询账本管理异常");
            return jsonObject.toString();
        }
    }

    /**
     * 获取分类管理数据：一级收支分类、二级收支分类
     */
    @ResponseBody
    @RequestMapping("/categoryList")
    public String categoryList(String accountBookName, String inExStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //账本id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            List<Object> categoryManageList = new ArrayList<>();
            //一级分类实体
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName());
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus(inExStatus);
            //遍历一级分类
            List<String> categoryFirstList = categoryFirstService.selectAllByCategoryFirst(categoryFirst);
            for (String categoryFirstName : categoryFirstList) {
                //查询一级分类id
                categoryFirst.setFirstCategoryName(categoryFirstName);
                Integer firstCategoryId = categoryFirstService.selectByCategoryFirst(categoryFirst).getId();
                //二级分类实体
                CategorySecond categorySecond = new CategorySecond();
                categorySecond.setUserName(globalAccountInfo.getUserName());
                categorySecond.setAccountBookId(accountBookId);
                categorySecond.setFirstCategoryId(firstCategoryId);
                categorySecond.setInExStatus(inExStatus);
                //遍历二级分类
                List<String> categorySecondList = categorySecondService.selectAllByCategorySecond(categorySecond);
                if (categorySecondList.size() == 0) {//显示没有二级分类的一级分类
                    Map<String, Object> m = new HashMap<>();
                    m.put("firstCategoryName", categoryFirstName);
                    categoryManageList.add(m);
                } else {
                    for (int i = 0; i < categorySecondList.size(); i++) {
                        //查询二级分类 id
                        categorySecond.setSecondCategoryName(categorySecondList.get(i));
                        Integer secondCategoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
                        //实体类映射
                        Map<String, Object> map = new HashMap<>();
                        map.put("userName", globalAccountInfo.getUserName());
                        map.put("accountBookId", accountBookId);
                        map.put("inExStatus", inExStatus);
                        map.put("secondCategoryId", secondCategoryId);
                        //计算每个二级分类收支总额
                        Double allCount = totalAmountCalculateService.calculateSecondCategoryAllInEx(map);
                        //封装数据对象
                        Map<String, Object> fMap = new HashMap<>();
                        //一级分类单独一行显示并且只显示一次
                        if (i == 0) {
                            fMap.put("firstCategoryName", categoryFirstName);
                            fMap.put("secondCategoryName", null);
                            fMap.put("allAmount", null);
                            categoryManageList.add(fMap);
                        }
                        //显示所有一级分类下的二级分类，如果存在的话
                        Map<String, Object> m = new HashMap<>();
                        m.put("secondCategoryName", categorySecondList.get(i));
                        m.put("allAmount", KeepTwoDecimals.keepTwoDeci(allCount));
                        categoryManageList.add(m);
                    }
                }
            }
            jsonObject.put("code", 0);
            jsonObject.put("msg", "查询分类管理成功");
            jsonObject.put("count", categoryManageList.size());
            jsonObject.put("data", categoryManageList);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "查询分类管理失败");
            return jsonObject.toString();
        }
    }

    /**
     * 获取模板管理数据
     */
    @ResponseBody
    @RequestMapping("/templateManageList")
    public String templateManageList(String inExStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //查询账本 id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //模板实体
            CategoryTemplate categoryTemplate = new CategoryTemplate();
            categoryTemplate.setUserName(globalAccountInfo.getUserName());
            categoryTemplate.setAccountBookId(accountBookId);
            categoryTemplate.setInExStatus(inExStatus);
            //执行查询
            List<CategoryTemplate> categoryTemplateList = categoryTemplateService.selectAllTemplate(categoryTemplate);
            List<Object> categoryTemplateObject = new ArrayList<>();//构建模板对象
            for (CategoryTemplate template : categoryTemplateList) {
                Map<String, Object> map = new HashMap<>();
                //执行一级分类查询
                String firstCategoryName = categoryFirstService.selectByPrimaryKey(template.getCategoryFirstId()).getFirstCategoryName();
                map.put("firstCategoryName", firstCategoryName);
                //执行二级分类查询
                String secondCategoryName = categorySecondService.selectByPrimaryKey(template.getCategorySecondId()).getSecondCategoryName();
                map.put("secondCategoryName", secondCategoryName);
                //执行金融账户查询
                String accountTypeName = accountTypeService.selectByPrimaryKey(template.getAccountTypeId()).getAccountTypeName();
                map.put("accountTypeName", accountTypeName);
                //模板 id
                map.put("id", template.getId());
                //模板名称
                map.put("templateName", template.getTemplateName());
                //封装数据
                categoryTemplateObject.add(map);
            }
            jsonObject.put("code", 0);
            jsonObject.put("msg", "查询模板管理数据成功");
            jsonObject.put("count", categoryTemplateObject.size());
            jsonObject.put("data", categoryTemplateObject);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 0);
            jsonObject.put("msg", "查询模板管理数据失败");
            return jsonObject.toString();
        }
    }
}
