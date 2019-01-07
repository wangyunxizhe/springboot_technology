package com.yuan.dao;

import com.yuan.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserDao {

    //采用内存型的存储方式-->Map
    private final ConcurrentMap<Integer, User> repo = new ConcurrentHashMap<>();

    //做自增id用
    private final static AtomicInteger idGenerator = new AtomicInteger();

    /**
     * 标准javadoc写法：保存用户对象
     *
     * @param user {@link User}    用户对象
     * @return 如果保存成功，返回<code>true</code>；否则，返回<code>false</code>
     */
    public boolean save(User user) {
        //id从1开始
        Integer id = idGenerator.incrementAndGet();
        user.setId(id);
        return repo.put(id, user) == null;
    }

    /**
     * 返回所有用户列表
     *
     * @return 所有用户列表
     */
    public Collection<User> findAll() {
        return repo.values();
    }
}
