package com.yuan.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * springmvc绑定Set对象
 */
public class UserSetForm {

    private Set<User> users;

    public UserSetForm() {
        /**
         * 和组装List的区别：set的size是不能为空的
         */
        users = new HashSet<>();
        users.add(new User());
        users.add(new User());
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "UserSetForm{" +
                "users=" + users +
                '}';
    }
}
