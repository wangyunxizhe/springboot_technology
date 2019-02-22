package com.yuan.entity;

/**
 * User对象中的子属性
 */
public class UserSon {

    private String tel;
    private String addr;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "UserSon{" +
                "tel='" + tel + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }
}
