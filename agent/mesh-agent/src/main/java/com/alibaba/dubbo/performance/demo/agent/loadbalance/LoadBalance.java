package com.alibaba.dubbo.performance.demo.agent.loadbalance;

import com.alibaba.dubbo.performance.demo.agent.myrpc.Endpoint;

import java.util.List;

public interface LoadBalance {

    Endpoint select();

    void onRefresh(List<Endpoint> endpoints);

}
