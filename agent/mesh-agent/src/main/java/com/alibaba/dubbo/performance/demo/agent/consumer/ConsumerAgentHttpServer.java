/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.alibaba.dubbo.performance.demo.agent.consumer;

import com.alibaba.dubbo.performance.demo.agent.agent.consumer.NormalClient;
import com.alibaba.dubbo.performance.demo.agent.transport.Client;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ConsumerAgentHttpServer {

    private Logger logger = LoggerFactory.getLogger(ConsumerAgentHttpServer.class);

    private EventLoopGroup bossGroup = new NioEventLoopGroup(4);
    private EventLoopGroup workerGroup = new NioEventLoopGroup(4);

    private ServerBootstrap bootstrap;

    static final int PORT = Integer.parseInt(System.getProperty("server.port"));//20000

    /**
     * 启动服务器接收来自 consumer 的 http 请求
     */
    public void startServer() {
        try {
            Client client = new NormalClient();
            client.init();

            bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ConsumerAgentHttpServerInitializer(client))
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true);
//            Channel ch = bootstrap.bind(PORT).sync().channel();
//            logger.info("consumer-agent provider is ready to receive request from consumer\n" +
//                    "export at http://127.0.0.1:{}", PORT); //20000
//            ch.closeFuture().sync();
            ChannelFuture future = bootstrap.bind("127.0.0.1", PORT);
            future.syncUninterruptibly();
        } catch (Exception e) {

            logger.error("consumer-agent启动失败", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
//            logger.info("consumer-agent provider was closed");
        }
    }

}
