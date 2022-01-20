package pers.elias.financial_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.elias.financial_management.model.AccountBook;
import pers.elias.financial_management.service.impl.AccountBookService;

@Controller
public class AddTestController {
    @Autowired
    private AccountBookService accountBookService;

    @ResponseBody
    @RequestMapping("/addAccountBookTest")
    public String addAccountBookTest(){
        AccountBook accountBook = new AccountBook();
        accountBook.setUserName("1");
        for(int i=0; i<=200; i++){
            accountBook.setName(i+"");
            accountBookService.insertSelective(accountBook);
        }
        return "添加成功";
    }
}
