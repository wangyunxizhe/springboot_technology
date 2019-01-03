package com.yuan.classloader;

/**
 * 后台启动一条线程不断刷新，重新加载，实现了热加载的类
 */
public class TestHandler implements Runnable {

    @Override
    public void run() {
        while (true) {
            BaseManager manager = ManagerFactory.getManager(ManagerFactory.MY_MANAGER);
            manager.logic();
            try {
                Thread.sleep(1500);//睡1.5秒，便于观察
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
