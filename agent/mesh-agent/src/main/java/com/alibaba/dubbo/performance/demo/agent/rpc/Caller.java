package com.alibaba.dubbo.performance.demo.agent.rpc;

import io.netty.channel.Channel;


public interface Caller {

    void call(Channel channel,Request request);

}
