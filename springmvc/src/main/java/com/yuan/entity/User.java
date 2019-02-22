package com.yuan.entity;

/**
 * 用于测试mvc数据绑定
 */
public class User {

    private String name;
    private Integer age;
    private UserSon son;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public UserSon getSon() {
        return son;
    }

    public void setSon(UserSon son) {
        this.son = son;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", son=" + son +
                '}';
    }
}
