package com.yuan.classloader;

/**
 * 封装加载类的信息
 */
public class LoadInfo {

    private MyClassLoader myLoader;//自定义的类加载器

    private long loadTime;//记录要加载的类的时间戳-->记录加载的时间

    private BaseManager manager;

    public LoadInfo(MyClassLoader myLoader, long loadTime) {
        this.myLoader = myLoader;
        this.loadTime = loadTime;
    }

    public MyClassLoader getMyLoader() {
        return myLoader;
    }

    public void setMyLoader(MyClassLoader myLoader) {
        this.myLoader = myLoader;
    }

    public long getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(long loadTime) {
        this.loadTime = loadTime;
    }

    public BaseManager getManager() {
        return manager;
    }

    public void setManager(BaseManager manager) {
        this.manager = manager;
    }
}
