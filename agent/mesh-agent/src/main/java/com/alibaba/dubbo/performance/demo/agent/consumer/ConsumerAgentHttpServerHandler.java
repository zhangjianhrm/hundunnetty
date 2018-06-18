package com.alibaba.dubbo.performance.demo.agent.consumer;

import com.alibaba.dubbo.performance.demo.agent.dubbo.common.JsonUtils;
import com.alibaba.dubbo.performance.demo.agent.dubbo.common.RequestParser;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.DubboRpcRequest;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.DubboRpcResponse;
import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcInvocation;
import com.alibaba.dubbo.performance.demo.agent.myrpc.DefaultRequest;
import com.alibaba.dubbo.performance.demo.agent.myrpc.Request;
import com.alibaba.dubbo.performance.demo.agent.myrpc.RpcCallbackFuture;
import com.alibaba.dubbo.performance.demo.agent.myrpc.RpcResponseHolder;
import com.alibaba.dubbo.performance.demo.agent.mytransport.Client;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

public class ConsumerAgentHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private Client client;

    public ConsumerAgentHttpServerHandler(Client client){
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        processRequest(ctx,req);
    }

    private void processRequest(ChannelHandlerContext ctx,FullHttpRequest req) {
        Map<String, String> requestParams;
        requestParams = RequestParser.parse(req);

        DefaultRequest defaultRequest = new DefaultRequest();
        defaultRequest.setInterfaceName(requestParams.get("interface"));
        defaultRequest.setMethod(requestParams.get("method"));
        defaultRequest.setParameterTypesString(requestParams.get("parameterTypesString"));
        defaultRequest.setParameter(requestParams.get("parameter"));

        this.call(ctx,defaultRequest);

    }

    public void call(ChannelHandlerContext ctx,Request request) {
        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethodName(request.getMethod());
        invocation.setAttachment("path", request.getInterfaceName());
        invocation.setParameterTypes(request.getParameterTypesString());    // Dubbo内部用"Ljava/lang/String"来表示参数类型是String

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out));
        try {
            JsonUtils.writeObject(request.getParameter(), writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        invocation.setArguments(out.toByteArray());
        DubboRpcRequest dubboRpcRequest = new DubboRpcRequest();
        dubboRpcRequest.setVersion("2.0.0");
        dubboRpcRequest.setTwoWay(true);
        dubboRpcRequest.setData(invocation);
        RpcCallbackFuture<DubboRpcResponse> rpcCallbackFuture = new RpcCallbackFuture<>();
        rpcCallbackFuture.setChannel(ctx.channel());
        RpcResponseHolder.put(dubboRpcRequest.getId(), rpcCallbackFuture);
        client.getChannel().writeAndFlush(dubboRpcRequest);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
    }



}
