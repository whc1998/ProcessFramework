package com.create.whc.baselib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.create.whc.baselib.annotion.ClassId;
import com.create.whc.baselib.bean.RequestBean;
import com.create.whc.baselib.bean.RequestParameter;
import com.create.whc.baselib.cache.CacheCenter;
import com.create.whc.baselib.service.ProcessInvocationHandler;
import com.create.whc.baselib.service.ProcessService;
import com.google.gson.Gson;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProcessManager {

    private static final ProcessManager ourInstance = new ProcessManager();
    private Gson gson = new Gson();
    private CacheCenter cacheCenter = CacheCenter.getInstance();
    private ProcessInterface processInterface;

    public static ProcessManager getInstance() {
        return ourInstance;
    }

    private ProcessManager() {
    }

    public void register(Class<?> clazz) {
        cacheCenter.register(clazz);
    }

    /*
          json 数据格式
          "className":"com.create.whc.baselib.UserManager",
          "methodName":"getInstance",
          "requestParameters":[{
              "parameterClassName":"java.lang.String",
              "parameterValue":"10001"
          }],
          "type":1
           */

    /**
     * aidl调用
     * @param clazz 类
     * @param parameters 传递的参数
     * @param <T> 动态代理类
     * @return
     */
    public <T> T getInstance(Class<T> clazz, Object... parameters) {

        sendRequest(ProcessService.GET_INSTANCE, clazz, null, parameters);
        T t = getProxy(clazz);
        return t;
    }

    private <T> T getProxy(Class<T> clazz) {
        ClassLoader classLoader = clazz.getClassLoader();
        T proxy = (T) Proxy.newProxyInstance(classLoader, new Class[]{clazz}, new ProcessInvocationHandler(clazz));
        return proxy;
    }

    public String sendRequest(int type, Class<?> clazz, Method method, Object[] parameters) {
        RequestParameter[] requestParameters = null;
        if (parameters != null && parameters.length > 0) {
            requestParameters = new RequestParameter[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                String parameterClassName = parameter.getClass().getName();
                String parameterValue = gson.toJson(parameter);
                RequestParameter requestParameter = new RequestParameter(parameterClassName, parameterValue);
                requestParameters[i] = requestParameter;
            }
        }
        String className = clazz.getAnnotation(ClassId.class).value();
        String methodName = method == null ? " " : method.getName();
        RequestBean requestBean = new RequestBean(type, className, methodName, requestParameters);

        //请求获取单例 --》对象 ---》请求对象的方法
        String request = gson.toJson(requestBean);
        Log.i("whc", "sendRequest: " + request);
        try {
            return processInterface.send(request);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    //b进程调用
    public void connect(Context context) {
        bind(context, null, ProcessService.class);
    }

    public void connect(Context context, String packageName) {
        bind(context, packageName, ProcessService.class);
    }

    public void bind(Context context, String packageName, Class<? extends ProcessService> service) {
        //开启service 连接服务
        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context, service);
        } else {
            intent = new Intent();
            intent.setPackage(packageName);
            intent.setAction(service.getName());
        }
        context.bindService(intent, new ProcessConnection(), Context.BIND_AUTO_CREATE);
    }

    private class ProcessConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            processInterface = ProcessInterface.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

}
