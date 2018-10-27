package com.create.whc.baselib.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.create.whc.baselib.ProcessInterface;
import com.create.whc.baselib.bean.RequestBean;
import com.create.whc.baselib.bean.RequestParameter;
import com.create.whc.baselib.cache.CacheCenter;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProcessService extends Service {

    public static final int GET_INSTANCE=1;
    public static final int GET_METHOD=2;
    private CacheCenter cacheCenter=CacheCenter.getInstance();
    private Gson gson=new Gson();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessInterface.Stub() {
            @Override
            public String send(String request) throws RemoteException {
                //实例化单例对象
                RequestBean requestBean = gson.fromJson(request, RequestBean.class);
                switch (requestBean.getType()){
                    case GET_INSTANCE:
                        Method method = cacheCenter.getMethod(requestBean.getClassName(), "getInstance");
                        Object[] mParameters = makeParameterObject(requestBean);
                        try {
                            Object userManager = method.invoke(null, mParameters);
                            cacheCenter.putObject(requestBean.getClassName(),userManager);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                    case GET_METHOD:
                        Object userManager = cacheCenter.getObject(requestBean.getClassName());
                        Method getPerson=cacheCenter.getMethod(requestBean.getClassName(),requestBean.getMethodName());
                        Object[] mParameters1=makeParameterObject(requestBean);
                        try {
                            Object person=getPerson.invoke(userManager,mParameters1);
                            String data=gson.toJson(person);
                            return data;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return null;
            }
        };
    }

    private Object[] makeParameterObject(RequestBean requestBean){
        //参数还原
        Object[] mParameters=null;
        RequestParameter[] requestParameters = requestBean.getRequestParameters();
        if (requestParameters!=null&&requestParameters.length>0){
            mParameters=new Object[requestParameters.length];
            for (int i=0;i<requestParameters.length;i++){
                RequestParameter requestParameter=requestParameters[i];
                Class<?> clazz = cacheCenter.getClassType(requestParameter.getParameterClassName());
                mParameters[i]=gson.fromJson(requestParameter.getParameterValue(),clazz);
            }
        }else{
            mParameters=new Object[0];
        }
        return mParameters;
    }

}
