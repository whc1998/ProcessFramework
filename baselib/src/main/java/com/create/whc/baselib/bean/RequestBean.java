package com.create.whc.baselib.bean;

public class RequestBean {

    //类型
    private int type;
    //线程B请求对象的class对象
    private String className;
    private String methodName;
    //参数
    private RequestParameter[] requestParameters;

    public RequestBean(int type, String className,String methodName , RequestParameter[] requestParameters) {
        this.type = type;
        this.methodName = methodName;
        this.className = className;
        this.requestParameters = requestParameters;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public RequestParameter[] getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(RequestParameter[] requestParameters) {
        this.requestParameters = requestParameters;
    }

}
