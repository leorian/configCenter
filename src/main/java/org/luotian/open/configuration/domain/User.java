package org.luotian.open.configuration.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xiezhonggui on 17-1-10.
 */
public class User implements Serializable{

    private String userId;
    private String userCode;
    private String userName;
    private String sex;
    private String age;
    private transient String password;
    private String email;
    private String cellphone;
    private String createUserId;
    private Date createDateTime;
    private String updateUserId;
    private Date updateDateTIme;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateDateTIme() {
        return updateDateTIme;
    }

    public void setUpdateDateTIme(Date updateDateTIme) {
        this.updateDateTIme = updateDateTIme;
    }
}
