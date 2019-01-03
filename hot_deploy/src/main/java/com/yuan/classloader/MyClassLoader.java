package com.yuan.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 演示Java类的热加载核心类
 */
public class MyClassLoader extends ClassLoader {

    private String classpath;//要加载的Java类的classpath路径

    public MyClassLoader(String classpath) {
        super(ClassLoader.getSystemClassLoader());
        this.classpath = classpath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        return this.defineClass(name, data, 0, data.length);
    }

    /**
     * 加载class文件中的内容
     */
    private byte[] loadClassData(String name) {
        try {
            name = name.replace(".", "//");//把Java中的路径“.”换成Windows里的路径“//”
            FileInputStream is = new FileInputStream(new File(classpath + name + ".class"));
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int b = 0;
            while ((b = is.read()) != -1) {
                os.write(b);
            }
            is.close();
            return os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
