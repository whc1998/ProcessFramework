package com.create.whc.baselib.cache;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.HashMap;

public class CacheCenter {

    private static CacheCenter instance=new CacheCenter();
    //name String  类名和类的实体
    private HashMap<String,Class<?>> mClass =new HashMap<>();
    //类对应的方法
    private HashMap<Class<?>,HashMap<String,Method>> mMethods=new HashMap<>();
    //类和对象
    private HashMap<String,Object> mObjects=new HashMap<>();


    private CacheCenter(){

    }

    public static CacheCenter getInstance(){
        return instance;
    }

    public void register(Class<?> clazz){
        mClass.put(clazz.getName(),clazz);
        registerMethod(clazz);
    }

    public Object getObject(String name){
        return mObjects.get(name);
    }

    public void putObject(String name,Object object){
        mObjects.put(name,object);
    }


    @TargetApi(Build.VERSION_CODES.N)
    private void registerMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            mMethods.putIfAbsent(clazz,new HashMap<String, Method>());
            HashMap<String,Method> map=mMethods.get(clazz);
            map.put(method.getName(),method);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    public Method getMethod(String className, String name){
        Class clazz=getClassType(className);
        if (name!=null){
            mMethods.putIfAbsent(clazz,new HashMap<String, Method>());
            HashMap<String, Method> methods = mMethods.get(clazz);
            Method method = methods.get(name);
            if (method!=null){
                return method;
            }
        }
        return null;
    }

    public Class<?> getClassType(String name) {
        if (TextUtils.isEmpty(name)){
            return null;
        }
        Class<?> clazz = mClass.get(name);
        if (clazz==null){
            try {
                clazz=Class.forName(name);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }

}
