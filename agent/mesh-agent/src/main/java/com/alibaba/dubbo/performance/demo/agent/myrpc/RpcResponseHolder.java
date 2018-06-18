package com.alibaba.dubbo.performance.demo.agent.myrpc;

import java.util.concurrent.ConcurrentHashMap;

public class RpcResponseHolder {

    private static ConcurrentHashMap<Long, RpcCallbackFuture> processingRpc = new ConcurrentHashMap<>();

    public static void put(Long requestId, RpcCallbackFuture rpcFuture) {
        processingRpc.put(requestId, rpcFuture);
    }

    public static RpcCallbackFuture get(Long requestId) {
        return processingRpc.get(requestId);
    }

}
