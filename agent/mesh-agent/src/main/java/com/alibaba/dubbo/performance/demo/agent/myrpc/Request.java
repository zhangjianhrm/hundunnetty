package com.alibaba.dubbo.performance.demo.agent.myrpc;


public interface Request {

    String getInterfaceName();

    String getMethod();


    String getParameterTypesString();

    String getParameter();
}
