package com.alibaba.dubbo.performance.demo.agent.rpc;


public interface FutureListener<T> {

    /**
     * 回调时触发
     * @param future
     */
    void operationComplete(RpcCallbackFuture<T> future);

}
