package com.yuan.classloader;

/**
 * BaseManager的子类，此类需要实现Java类的热加载功能
 */
public class MyManager implements BaseManager {

    @Override
    public void logic() {
        System.out.println("如何实现Java类的热加载功能的案例");
    }

}
