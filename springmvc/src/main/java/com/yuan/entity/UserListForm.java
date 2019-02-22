package com.yuan.entity;

import java.util.List;

/**
 * springmvc绑定List对象
 * 需要像这样建一个类
 */
public class UserListForm {

    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "UserListForm{" +
                "users=" + users +
                '}';
    }
}
