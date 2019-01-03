package com.yuan.classloader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * 加载Manager的工厂类
 */
public class ManagerFactory {

    //记录热加载类的加载信息
    private static final Map<String, LoadInfo> loadTimeMap = new HashMap<>();

    //要加载的类的classpath
    public static final String CLASS_PATH = "E:/idea-home-workspace/springboot_technology" +
            "/hot_deploy/target/classes/";

    public static final String MY_MANAGER = "com.yuan.classloader.MyManager";

    public static BaseManager getManager(String className) {
        File loadFile = new File(CLASS_PATH + className.replaceAll("\\.", "/") + ".class");
        long lastModified = loadFile.lastModified();//获取该文件最后的修改时间
        //loadTimeMap没有该className的加载信息。说明该类没有被加载，那么就需要把该类加载到JVM中去
        if (loadTimeMap.get(className) == null) {
            load(className, lastModified);
        }//加载类的时间戳变了。说明该类有过改动了，那么同样要重新把这个类加载到JVM中
        else if (loadTimeMap.get(className).getLoadTime() != lastModified) {
            load(className, lastModified);
        }
        return loadTimeMap.get(className).getManager();
    }

    /**
     * 更新热加载类的加载信息
     *
     * @param className    类全名
     * @param lastModified class文件最后修改时间
     */
    private static void load(String className, long lastModified) {
        MyClassLoader myClassLoader = new MyClassLoader(CLASS_PATH);
        Class<?> loadClass = null;
        try {
            loadClass = myClassLoader.findClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        BaseManager manager = newInstance(loadClass);
        LoadInfo loadInfo = new LoadInfo(myClassLoader, lastModified);
        loadInfo.setManager(manager);
        loadTimeMap.put(className, loadInfo);
    }

    //以反射的方式创建BaseManager的子类对象
    private static BaseManager newInstance(Class<?> loadClass) {
        try {
            return (BaseManager) loadClass.getConstructor(new Class[]{}).newInstance(new Object[]{});
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
