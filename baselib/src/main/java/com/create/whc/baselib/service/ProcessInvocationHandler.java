package com.create.whc.baselib.service;

import android.text.TextUtils;
import android.util.Log;

import com.create.whc.baselib.ProcessManager;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProcessInvocationHandler implements InvocationHandler {

    private Class<?> clazz;
    private Gson gson=new Gson();

    public <T> ProcessInvocationHandler(Class<T> mclazz) {
        this.clazz=mclazz;
        Log.i("whc", "ProcessInvocationHandler: "+clazz);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.i("whc", "ProcessInvocationHandler: "+clazz+"<<<<<<"+method+"<<<<"+args);
        //发请求
        String respones = ProcessManager.getInstance().sendRequest(ProcessService.GET_METHOD, clazz, method, args);
        if (!TextUtils.isEmpty(respones)&&!respones.equals("null")){
            Class<?> userClass = method.getReturnType();
            Object o = gson.fromJson(respones, userClass);
            return o;
        }
        return null;
    }
}
