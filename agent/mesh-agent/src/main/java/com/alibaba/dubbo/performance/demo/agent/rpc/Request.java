package com.alibaba.dubbo.performance.demo.agent.rpc;


public interface Request {

    String getInterfaceName();

    /**
     * service method name
     *
     * @return
     */
    String getMethod();

    /**
     * service method param desc (sign)
     *
     * @return
     */
    String getParameterTypesString();

    /**
     * service method param
     *
     * @return
     */
//    Object[] getArgs();
    String getParameter();

    /**
     * request id
     *
     * @return
     */
    long getRequestId();
}
