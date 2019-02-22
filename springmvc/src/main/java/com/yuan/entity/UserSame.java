package com.yuan.entity;

/**
 * 与User对象的有相同属性
 */
public class UserSame {

    private String name;
    private Integer age;

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

    @Override
    public String toString() {
        return "UserSame{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
