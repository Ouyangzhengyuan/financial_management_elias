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
     * ????????????????????????
     */
    @ResponseBody
    @RequestMapping("/getPersonalDetails")
    public String getPersonalDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            //???????????????????????????
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            //??????????????????
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
     * ????????????????????????
     */
    @ResponseBody
    @RequestMapping("/editPersonalDetails")
    public String editPersonalDetails(AccountUser user) {
        JSONObject jsonObject = new JSONObject();
        try {
            //???????????????????????????
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            user.setUserName(globalAccountInfo.getUserName());
            accountUserService.updateByPrimaryKeySelective(user);
            jsonObject.put("success", true);
            jsonObject.put("msg", "????????????");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "????????????");
            return jsonObject.toString();
        }
    }

    /**
     * ??????????????????
     */
    @RequestMapping("/getUserHeaderPath")
    @ResponseBody
    public String getHeaderPath() {
        JSONObject jsonObject = new JSONObject();
        try {
            //???????????????????????????
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            AccountUserHeader accountUserHeader = accountUserHeaderService.selectByPrimaryKey(globalAccountInfo.getUserName());
            if (accountUserHeader != null) {
                //??????????????????????????????
                String headerPath = accountUserHeaderService.selectByPrimaryKey(globalAccountInfo.getUserName()).getHeaderPath();
                jsonObject.put("success", true);
                jsonObject.put("data", headerPath);
                jsonObject.put("msg", "????????????");
                return jsonObject.toString();
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "????????????");
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "????????????");
            return jsonObject.toString();
        }
    }


    /**
     * ??????????????????
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String uploadAccountUserHeader(MultipartFile file, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            //???????????????????????????
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            //??????????????????
            String dirPath = userImgAbsolutePath;
            String tempPath = request.getServletContext().getRealPath("/images/");
            //??????????????????????????????????????????
            file.transferTo(fileUpload.getDestFile(tempPath, file));
            //?????????????????????????????????
            File tempFile = new File(tempPath, fileUpload.getFilename());
            //???????????????????????????????????????????????????????????? /images/
            if (tempFile.exists()) {
                fileCopy.copyFile(dirPath, tempFile);
            }
            //??????????????????
            String relativePath = userImgRelativePath + fileUpload.getFilename();
            //?????????????????????
            AccountUserHeader accountUserHeader = new AccountUserHeader();
            accountUserHeader.setUserName(globalAccountInfo.getUserName());
            accountUserHeader.setHeaderPath(relativePath);
            //??????????????????????????????????????????????????????????????????????????????
            if (accountUserHeaderService.selectByPrimaryKey(globalAccountInfo.getUserName()) != null) {
                //??????????????????  ??????????????????????????????
                String headerPath = accountUserHeaderService.selectByPrimaryKey(globalAccountInfo.getUserName()).getHeaderPath();
                int beginIndex = headerPath.lastIndexOf("/") + 1;
                String fileName = headerPath.substring(beginIndex);
                //??????????????????
                File headerFile = new File(dirPath, fileName);
                //?????? /images/ ??????????????????????????????????????????????????????
                if (headerFile.exists()) {
                    boolean success = headerFile.delete();
                }
                //????????????????????????
                accountUserHeaderService.updateByPrimaryKey(accountUserHeader);
                jsonObject.put("code", 200);
                jsonObject.put("msg", "???????????????????????????");
                jsonObject.put("data", relativePath);
                return jsonObject.toString();
            }
            //??????????????????????????????????????????
            accountUserHeaderService.insert(accountUserHeader);
            //?????????
            jsonObject.put("code", 200);
            jsonObject.put("msg", "????????????????????????");
            jsonObject.put("data", relativePath);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "?????????????????????");
            return jsonObject.toString();
        }
    }

    /**
     * ??????????????????
     */
    @ResponseBody
    @RequestMapping("/editAccountPass")
    public JSONObject editAccountPass(String nowPass, String newPass) {
        JSONObject jsonObject = new JSONObject();
        try {
            //???????????????????????????
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            String userPass = accountUserService.selectByPrimaryKey(globalAccountInfo.getUserName()).getUserPass();
            if (passwordEncoder.matches(nowPass, userPass)) {
                AccountUser user = new AccountUser();
                user.setUserName(globalAccountInfo.getUserName());
                user.setUserPass(passwordEncoder.encode(newPass));
                accountUserService.updateByPrimaryKeySelective(user);
                jsonObject.put("success", true);
                jsonObject.put("msg", "????????????");
                return jsonObject;
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "???????????????????????????????????????");
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "????????????");
            return jsonObject;
        }
    }

    /**
     * ??????????????????
     */
    @ResponseBody
    @RequestMapping("/editSecondPass")
    public JSONObject editSecondPass(String nowSecondPass, String newSecondPass) {
        JSONObject jsonObject = new JSONObject();
        try {
            //???????????????????????????
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            //????????????????????????
            String oldPass = accountUserService.selectByPrimaryKey(globalAccountInfo.getUserName()).getSecondPass();
            if (passwordEncoder.matches(nowSecondPass, oldPass)) {//??????????????????
                AccountUser user = new AccountUser();
                user.setUserName(globalAccountInfo.getUserName());
                user.setSecondPass(passwordEncoder.encode(newSecondPass));
                accountUserService.updateByPrimaryKeySelective(user);//????????????
                jsonObject.put("success", true);
                jsonObject.put("msg", "????????????????????????");
                return jsonObject;
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "???????????????????????????");
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "????????????");
            return jsonObject;
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    @ResponseBody
    @RequestMapping("/accountBookTable")
    public String accountBookTable() {
        JSONObject jsonObject = new JSONObject();
        try {
            //???????????????????????????
            globalAccountInfo.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());
            List<AccountBookManage> accountBookManageList = new ArrayList<>();
            for (String accountBookName : accountBookService.selectAllByUserName(globalAccountInfo.getUserName())) {
                //????????????id
                globalAccountInfo.setAccountBookName(accountBookName);
                Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
                //??????????????????
                Map<String, Object> map = new HashMap<>();
                map.put("userName", globalAccountInfo.getUserName());
                map.put("accountBookId", accountBookId);
                //????????????????????????
                Integer count = accountCurrentService.selectCount(map);
                //??????????????? ???????????????????????????????????????
                AccountFinancial accountFinancial = new AccountFinancial();
                accountFinancial.setUserName(globalAccountInfo.getUserName());
                accountFinancial.setAccountBookId(accountBookId);
                accountFinancial.setFinancialAccountName("????????????");
                Integer creditAccountId = accountFinancialService.selectId(accountFinancial);
                accountFinancial.setFinancialAccountName("????????????");
                Integer liabilityAccountId = accountFinancialService.selectId(accountFinancial);
                List<Integer> limitAccountFinancialIdList = new ArrayList<>();
                limitAccountFinancialIdList.add(creditAccountId);
                limitAccountFinancialIdList.add(liabilityAccountId);
                //???????????????
                Map<String, Object> allInMap = new HashMap<>();
                allInMap.put("userName", globalAccountInfo.getUserName());
                allInMap.put("accountBookId", accountBookId);
                allInMap.put("inExStatus", "???");
                allInMap.put("accountFinancialId", limitAccountFinancialIdList);
                //?????????????????????
                Double allIn = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculateService.calculateAllInEx(allInMap));
                //???????????????
                allInMap.put("inExStatus", "???");
                Double allEx = KeepTwoDecimals.calculateKeepTwoDeci(totalAmountCalculateService.calculateAllInEx(allInMap));
                //??????????????????
                List<AccountCurrent> accountCurrentList = new ArrayList<>();
                AccountCurrent creditAccount = new AccountCurrent();
                creditAccount.setUserName(globalAccountInfo.getUserName());
                creditAccount.setAccountBookId(accountBookId);
                creditAccount.setAccountFinancialId(creditAccountId);//????????????id
                accountCurrentList.add(creditAccount);
                //??????????????????
                AccountCurrent liabilityAccount = new AccountCurrent();
                liabilityAccount.setUserName(globalAccountInfo.getUserName());
                liabilityAccount.setAccountBookId(accountBookId);
                liabilityAccount.setAccountFinancialId(liabilityAccountId);//????????????id
                accountCurrentList.add(liabilityAccount);
                //???????????????
                Double allDebts = totalAmountCalculateService.calculateAllDebts(accountCurrentList);
                //??????????????????
                AccountBookManage accountBookManage = new AccountBookManage();
                accountBookManage.setAccountBookName(accountBookName);//????????????
                accountBookManage.setCount(count);
                accountBookManage.setAllIn(allIn);
                accountBookManage.setAllEx(allEx);
                accountBookManage.setAllDebts(allDebts);
                accountBookManage.setNetAssets(KeepTwoDecimals.calculateKeepTwoDeci(allIn - allDebts));
                //??????????????????????????????
                accountBookManageList.add(accountBookManage);
            }
            jsonObject.put("code", 0);
            jsonObject.put("msg", "????????????????????????");
            jsonObject.put("count", accountBookManageList.size());
            jsonObject.put("data", accountBookManageList);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "????????????????????????");
            return jsonObject.toString();
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????
     */
    @ResponseBody
    @RequestMapping("/categoryList")
    public String categoryList(String accountBookName, String inExStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            //???????????????????????????
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //???????????????????????????
            globalAccountInfo.setUserName(userName);
            //????????????????????????
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //??????id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            List<Object> categoryManageList = new ArrayList<>();
            //??????????????????
            CategoryFirst categoryFirst = new CategoryFirst();
            categoryFirst.setUserName(globalAccountInfo.getUserName());
            categoryFirst.setAccountBookId(accountBookId);
            categoryFirst.setInExStatus(inExStatus);
            //??????????????????
            List<String> categoryFirstList = categoryFirstService.selectAllByCategoryFirst(categoryFirst);
            for (String categoryFirstName : categoryFirstList) {
                //??????????????????id
                categoryFirst.setFirstCategoryName(categoryFirstName);
                Integer firstCategoryId = categoryFirstService.selectByCategoryFirst(categoryFirst).getId();
                //??????????????????
                CategorySecond categorySecond = new CategorySecond();
                categorySecond.setUserName(globalAccountInfo.getUserName());
                categorySecond.setAccountBookId(accountBookId);
                categorySecond.setFirstCategoryId(firstCategoryId);
                categorySecond.setInExStatus(inExStatus);
                //??????????????????
                List<String> categorySecondList = categorySecondService.selectAllByCategorySecond(categorySecond);
                if (categorySecondList.size() == 0) {//???????????????????????????????????????
                    Map<String, Object> m = new HashMap<>();
                    m.put("firstCategoryName", categoryFirstName);
                    categoryManageList.add(m);
                } else {
                    for (int i = 0; i < categorySecondList.size(); i++) {
                        //?????????????????? id
                        categorySecond.setSecondCategoryName(categorySecondList.get(i));
                        Integer secondCategoryId = categorySecondService.selectIdByCategorySecond(categorySecond);
                        //???????????????
                        Map<String, Object> map = new HashMap<>();
                        map.put("userName", globalAccountInfo.getUserName());
                        map.put("accountBookId", accountBookId);
                        map.put("inExStatus", inExStatus);
                        map.put("secondCategoryId", secondCategoryId);
                        //????????????????????????????????????
                        Double allCount = totalAmountCalculateService.calculateSecondCategoryAllInEx(map);
                        //??????????????????
                        Map<String, Object> fMap = new HashMap<>();
                        //???????????????????????????????????????????????????
                        if (i == 0) {
                            fMap.put("firstCategoryName", categoryFirstName);
                            fMap.put("secondCategoryName", null);
                            fMap.put("allAmount", null);
                            categoryManageList.add(fMap);
                        }
                        //???????????????????????????????????????????????????????????????
                        Map<String, Object> m = new HashMap<>();
                        m.put("secondCategoryName", categorySecondList.get(i));
                        m.put("allAmount", KeepTwoDecimals.keepTwoDeci(allCount));
                        categoryManageList.add(m);
                    }
                }
            }
            jsonObject.put("code", 0);
            jsonObject.put("msg", "????????????????????????");
            jsonObject.put("count", categoryManageList.size());
            jsonObject.put("data", categoryManageList);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "????????????????????????");
            return jsonObject.toString();
        }
    }

    /**
     * ????????????????????????
     */
    @ResponseBody
    @RequestMapping("/templateManageList")
    public String templateManageList(String inExStatus) {
        JSONObject jsonObject = new JSONObject();
        try {
            //???????????????????????????
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //???????????????????????????
            globalAccountInfo.setUserName(userName);
            //????????????????????????
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //???????????? id
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            //????????????
            CategoryTemplate categoryTemplate = new CategoryTemplate();
            categoryTemplate.setUserName(globalAccountInfo.getUserName());
            categoryTemplate.setAccountBookId(accountBookId);
            categoryTemplate.setInExStatus(inExStatus);
            //????????????
            List<CategoryTemplate> categoryTemplateList = categoryTemplateService.selectAllTemplate(categoryTemplate);
            List<Object> categoryTemplateObject = new ArrayList<>();//??????????????????
            for (CategoryTemplate template : categoryTemplateList) {
                Map<String, Object> map = new HashMap<>();
                //????????????????????????
                String firstCategoryName = categoryFirstService.selectByPrimaryKey(template.getCategoryFirstId()).getFirstCategoryName();
                map.put("firstCategoryName", firstCategoryName);
                //????????????????????????
                String secondCategoryName = categorySecondService.selectByPrimaryKey(template.getCategorySecondId()).getSecondCategoryName();
                map.put("secondCategoryName", secondCategoryName);
                //????????????????????????
                String accountTypeName = accountTypeService.selectByPrimaryKey(template.getAccountTypeId()).getAccountTypeName();
                map.put("accountTypeName", accountTypeName);
                //?????? id
                map.put("id", template.getId());
                //????????????
                map.put("templateName", template.getTemplateName());
                //????????????
                categoryTemplateObject.add(map);
            }
            jsonObject.put("code", 0);
            jsonObject.put("msg", "??????????????????????????????");
            jsonObject.put("count", categoryTemplateObject.size());
            jsonObject.put("data", categoryTemplateObject);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", 0);
            jsonObject.put("msg", "??????????????????????????????");
            return jsonObject.toString();
        }
    }
}
