package com.yuan.classloader;

/**
 * 实现这个接口的子类需要动态更新，也就是说实现这个接口的子类要具有热加载功能
 */
public interface BaseManager {

    void logic();

}
