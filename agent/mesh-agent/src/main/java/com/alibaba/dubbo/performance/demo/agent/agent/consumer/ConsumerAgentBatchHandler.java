package com.alibaba.dubbo.performance.demo.agent.agent.consumer;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.DubboRpcResponse;
import com.alibaba.dubbo.performance.demo.agent.myrpc.RpcCallbackFuture;
import com.alibaba.dubbo.performance.demo.agent.myrpc.RpcResponseHolder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AsciiString;

import java.util.List;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ConsumerAgentBatchHandler extends SimpleChannelInboundHandler<Object> {
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final AsciiString CONNECTION = AsciiString.cached("Connection");
    private static final AsciiString KEEP_ALIVE = AsciiString.cached("keep-alive");

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if(msg instanceof DubboRpcResponse){
            DubboRpcResponse dubboRpcResponse = (DubboRpcResponse) msg;
            RpcCallbackFuture rpcCallbackFuture = RpcResponseHolder.get(dubboRpcResponse.getRequestId());
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(dubboRpcResponse.getBytes()));
            response.headers().set(CONTENT_TYPE, "text/plain");
            response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(CONNECTION, KEEP_ALIVE);
            rpcCallbackFuture.getChannel().writeAndFlush(response);
        }else if(msg instanceof List){
            for (Object item : (List)msg) {
                DubboRpcResponse dubboRpcResponse = (DubboRpcResponse) item;
                RpcCallbackFuture rpcCallbackFuture = RpcResponseHolder.get(dubboRpcResponse.getRequestId());
                FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(dubboRpcResponse.getBytes()));
                response.headers().set(CONTENT_TYPE, "text/plain");
                response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
                response.headers().set(CONNECTION, KEEP_ALIVE);
                rpcCallbackFuture.getChannel().writeAndFlush(response);
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
    }

}
