package com.jackinjava.qywx.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCompare {
    @ExcelIgnore
    private User saasUser;

    @ExcelProperty("SAAS用户id")
    private String saasUserid;

    @ExcelProperty("SAAS用户名")
    private String saasName;

    @ExcelIgnore
    private boolean saas;//标记>>>>> false:没有成功匹配   true: 成功匹配

    @ExcelIgnore
    private User localUser;

    @ExcelProperty("私有版用户id")
    private String localUserid;

    @ExcelProperty("私有版用户名")
    private String localName;

    @ExcelIgnore
    private boolean local;//标记>>>>> false:没有成功匹配   true: 成功匹配

    @ExcelProperty("比对状态")
    private String mark;

    public UserCompare(User saasUser, User localUser, String mark) {
        this.saasUser = saasUser;
        this.localUser = localUser;
        this.mark = mark;
        this.saas = saasUser != null;
        this.local = localUser != null;
        if (this.saas) {
            this.saasUserid = saasUser.getUserid();
            this.saasName = saasUser.getName();
        }
        if (this.local) {
            this.localUserid = localUser.getUserid();
            this.localName = localUser.getName();
        }
    }
}
