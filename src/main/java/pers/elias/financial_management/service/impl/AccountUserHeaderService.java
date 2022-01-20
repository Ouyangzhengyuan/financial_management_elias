package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.mapper.AccountUserHeaderMapper;
import pers.elias.financial_management.model.AccountUserHeader;
import pers.elias.financial_management.service.IAccountUserHeaderService;

import java.io.File;

@Service
public class AccountUserHeaderService implements IAccountUserHeaderService {
    @Value("${user-img-absolute-path}")
    private String userImgAbsolutePath;

    @Autowired
    private AccountUserHeaderMapper accountUserHeaderMapper;

    @Override
    public int deleteByPrimaryKey(String userName) {
        return accountUserHeaderMapper.deleteByPrimaryKey(userName);
    }

    @Override
    public int insert(AccountUserHeader record) {
        return accountUserHeaderMapper.insert(record);
    }

    @Override
    public int insertSelective(AccountUserHeader record) {
        return accountUserHeaderMapper.insertSelective(record);
    }

    @Override
    public AccountUserHeader selectByPrimaryKey(String userName) {
        return accountUserHeaderMapper.selectByPrimaryKey(userName);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountUserHeader record) {
        return accountUserHeaderMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountUserHeader record) {
        return accountUserHeaderMapper.updateByPrimaryKey(record);
    }

    @Override
    public int deleteByUserName(String userName) {
        //查询账本头像记录
        AccountUserHeader accountUserHeader = selectByPrimaryKey(userName);
        if(accountUserHeader != null){
            //账本头像的存放路径
            String dirPath = userImgAbsolutePath;
            //创建文件对象
            int beginIndex = accountUserHeader.getHeaderPath().lastIndexOf("/") + 1;
            String fileName = accountUserHeader.getHeaderPath().substring(beginIndex);
            File headerFile = new File(dirPath, fileName);
            //如果存放目录里有相应的文件存在，则删除该文件
            if(headerFile.exists()){
                boolean success = headerFile.delete();
            }
        }

        return accountUserHeaderMapper.deleteByUserName(userName);
    }
}
