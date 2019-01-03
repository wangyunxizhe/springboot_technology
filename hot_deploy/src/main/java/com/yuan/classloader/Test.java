package com.yuan.classloader;

/**
 * 测试Java类的热加载
 *
 * 从测试结果可以看出，在项目启动的情况下，随意改动MyManager类中logic()方法中的输出文字，可以实时变动，不用重启项目
 */
public class Test {

    public static void main(String[] args) {
        new Thread(new TestHandler()).start();
    }

}
