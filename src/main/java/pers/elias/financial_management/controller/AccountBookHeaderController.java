package pers.elias.financial_management.controller;

import cn.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pers.elias.financial_management.component.FileCopy;
import pers.elias.financial_management.component.FileUpload;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.model.AccountBookHeader;
import pers.elias.financial_management.service.impl.AccountBookHeaderService;
import pers.elias.financial_management.service.impl.AccountBookService;
import pers.elias.financial_management.service.impl.AccountIndexService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 账本头像控制器
 */
@Controller
@RequestMapping("/accountBookHeader")
public class AccountBookHeaderController {
    @Autowired
    private FileCopy fileCopy;//文件复制

    @Autowired
    private FileUpload fileUpload;//文件上传

    @Autowired
    private GlobalAccountInfo globalAccountInfo;//账户信息

    @Autowired
    private AccountBookHeaderService accountBookHeaderService;//账本头像服务

    @Autowired
    private AccountBookService accountBookService;

    @Autowired
    private AccountIndexService accountIndexService;

    @Value("${user-img-absolute-path}")
    private String userImgAbsolutePath;

    @Value("${user-img-relative-path}")
    private String userImgRelativePath;

    /**
     * 读取用户账本头像
     */
    @RequestMapping("/getHeaderPath")
    @ResponseBody
    public String getHeaderPath(){
        JSONObject jsonObject = new JSONObject();
        try{
            //获取当前登录的用户
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置当前登录的用户
            globalAccountInfo.setUserName(userName);
            //设置当前用户账本
            globalAccountInfo.setAccountBookName(accountIndexService.selectByPrimaryKey(userName).getAccountBookIndex());
            //反向查询账本 id 保存到容器内的全局变量
            Integer accountBookId = accountBookService.selectIdByUserNameAndBook(globalAccountInfo);
            globalAccountInfo.setAccountBookId(accountBookId);
            //实体对象
            AccountBookHeader accountBookHeader = new AccountBookHeader();
            accountBookHeader.setUserName(globalAccountInfo.getUserName());
            accountBookHeader.setAccountBookId(accountBookId);
            //查询用户账本头像路径
            AccountBookHeader aBH = accountBookHeaderService.selectByAccountBookHeader(accountBookHeader);
            //判断账本是否上传头像
            if(aBH != null){
                jsonObject.put("success", true);
                jsonObject.put("msg", "账本头像读取成功！");
                jsonObject.put("data", aBH.getHeaderPath());
                return jsonObject.toString();
            }
            jsonObject.put("success", false);
            jsonObject.put("msg", "该账本还没有上传头像！");
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "账本头像读取失败！");
            return jsonObject.toString();
        }
    }

    /**
     * 账本头像上传
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String uploadAccountBookHeader(MultipartFile file, HttpServletRequest request) {
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
            //设置保存路径
            String dirPath = userImgAbsolutePath;
            String tempPath = request.getServletContext().getRealPath("/images/");
            //执行上传文件到服务器临时目录
            file.transferTo(fileUpload.getDestFile(tempPath, file));
            //获取服务器临时目录文件
            File tempFile = new File(tempPath, fileUpload.getFilename());
            //将临时目录的文件复制到本地当前项目文件夹 /images/
            if(tempFile.exists()){
                fileCopy.copyFile(dirPath, tempFile);
            }
            //设置相对路径
            String relativePath = userImgRelativePath + fileUpload.getFilename();
            //账本头像实体类
            AccountBookHeader accountBookHeader = new AccountBookHeader();
            accountBookHeader.setUserName(globalAccountInfo.getUserName());
            accountBookHeader.setAccountBookId(accountBookId);
            accountBookHeader.setHeaderPath(relativePath);
            //判断用户路径是否存在数据库，如果存在只需要更新路径即可
            if(accountBookHeaderService.isExists(accountBookHeader)){
                AccountBookHeader aBH = new AccountBookHeader();
                aBH.setUserName(globalAccountInfo.getUserName());
                aBH.setAccountBookId(accountBookId);
                //查询现有路径  获取路径后缀文件名称
                String headerPath = accountBookHeaderService.selectByAccountBookHeader(aBH).getHeaderPath();
                int beginIndex = headerPath.lastIndexOf("/") + 1;
                String fileName = headerPath.substring(beginIndex);
                //创建文件对象
                File headerFile = new File(dirPath, fileName);
                //如果 /images/ 目录里有相应的文件存在，则删除该文件
                if(headerFile.exists()){
                    boolean success = headerFile.delete();
                }
                //更新账本头像路径
                accountBookHeaderService.updateByAccountBookHeader(accountBookHeader);
                jsonObject.put("code", 200);
                jsonObject.put("msg", "更新成功");
                jsonObject.put("data", relativePath);
                return jsonObject.toString();
            }
            //上传成功后将路径添加到数据库
            accountBookHeaderService.insert(accountBookHeader);
            //响应头
            jsonObject.put("code", 200);
            jsonObject.put("msg", "上传成功");
            jsonObject.put("data", relativePath);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("msg", "账本头像上传失败");
            return jsonObject.toString();
        }
    }

}
