package com.alibaba.dubbo.performance.demo.agent.mytransport;

import io.netty.channel.Channel;

public interface Client{

    void init();
    Channel getChannel();

}
