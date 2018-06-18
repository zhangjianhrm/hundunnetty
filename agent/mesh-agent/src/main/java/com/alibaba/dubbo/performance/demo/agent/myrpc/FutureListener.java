package com.alibaba.dubbo.performance.demo.agent.myrpc;


public interface FutureListener<T> {
    void operationComplete(RpcCallbackFuture<T> future);

}
