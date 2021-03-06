package pers.elias.financial_management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pers.elias.financial_management.bean.GlobalAccountInfo;
import pers.elias.financial_management.mapper.AccountBookHeaderMapper;
import pers.elias.financial_management.model.AccountBookHeader;
import pers.elias.financial_management.service.IAccountBookHeaderService;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class AccountBookHeaderService implements IAccountBookHeaderService {
    @Autowired
    private AccountBookHeaderMapper accountBookHeaderMapper;

    @Autowired
    private GlobalAccountInfo globalAccountInfo;

    @Autowired
    private AccountBookService accountBookService;

    @Value("${user-img-absolute-path}")
    private String userImgAbsolutePath;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return accountBookHeaderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AccountBookHeader record) {
        return accountBookHeaderMapper.insert(record);
    }

    @Override
    public int insertSelective(AccountBookHeader record) {
        return accountBookHeaderMapper.insertSelective(record);
    }

    @Override
    public AccountBookHeader selectByPrimaryKey(Integer id) {
        return accountBookHeaderMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AccountBookHeader record) {
        return accountBookHeaderMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AccountBookHeader record) {
        return accountBookHeaderMapper.updateByPrimaryKey(record);
    }

    @Override
    public AccountBookHeader selectByAccountBookHeader(AccountBookHeader accountBookHeader) {
        return accountBookHeaderMapper.selectByAccountBookHeader(accountBookHeader);
    }

    @Override
    public boolean isExists(AccountBookHeader accountBookHeader) {
        AccountBookHeader aBH = accountBookHeaderMapper.selectByAccountBookHeader(accountBookHeader);
        if(aBH != null){
            if(aBH.getUserName() != null && aBH.getAccountBookId() != null){
                return aBH.getUserName().equals(globalAccountInfo.getUserName()) && aBH.getAccountBookId().equals(globalAccountInfo.getAccountBookId());
            }
        }
        return false;
    }

    @Override
    public int updateByAccountBookHeader(AccountBookHeader accountBookHeader) {
        return accountBookHeaderMapper.updateByAccountBookHeader(accountBookHeader);
    }

    @Override
    public int deleteByUserName(String userName) {
        List<String> headerList = selectByUserName(userName);
        if(headerList.size() != 0){
            for(String headerPath: headerList){
                //???????????????????????????
                String dirPath = userImgAbsolutePath;
                //?????????????????????????????????
                int beginIndex = headerPath.lastIndexOf("/") + 1;
                String fileName = headerPath.substring(beginIndex);
                //??????????????????
                File headerFile = new File(dirPath, fileName);
                //????????????????????????????????????
                if(headerFile.exists()){
                    boolean success = headerFile.delete();
                }
            }
        }
        return accountBookHeaderMapper.deleteByUserName(userName);
    }

    @Override
    public int deleteByAccountBookId(Integer accountBookId) {
        AccountBookHeader accountBookHeader = new AccountBookHeader();
        accountBookHeader.setUserName(globalAccountInfo.getUserName());
        accountBookHeader.setAccountBookId(accountBookId);
        if(selectByAccountBookHeader(accountBookHeader) != null){
            //????????????????????????
            String headerPath = selectByAccountBookHeader(accountBookHeader).getHeaderPath();
            //???????????????????????????
            String dirPath = userImgAbsolutePath;
            //??????????????????
            int beginIndex = headerPath.lastIndexOf("/") + 1;
            String fileName = headerPath.substring(beginIndex);
            File headerFile = new File(dirPath, fileName);
            //??????????????????????????????????????????????????????????????????
            if(headerFile.exists()){
                boolean success = headerFile.delete();
            }
            //????????????
        }
        return accountBookHeaderMapper.deleteByAccountBookId(accountBookId);
    }

    @Override
    public List<String> selectByUserName(String userName) {
        return accountBookHeaderMapper.selectByUserName(userName);
    }

}
