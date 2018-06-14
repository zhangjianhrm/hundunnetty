package com.alibaba.dubbo.performance.demo.agent.transport;

import com.alibaba.dubbo.performance.demo.agent.rpc.Caller;
import io.netty.channel.Channel;

/**

 * 点对点的通信
 */
public interface Client{

    void init();

    Channel getChannel();

}
