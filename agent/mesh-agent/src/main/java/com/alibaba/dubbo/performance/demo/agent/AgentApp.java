package com.alibaba.dubbo.performance.demo.agent;

import com.alibaba.dubbo.performance.demo.agent.agent.provider.ProviderAgentServer;
import com.alibaba.dubbo.performance.demo.agent.consumer.ConsumerAgentHttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AgentApp {
    public static void main(String[] args) {
        SpringApplication.run(AgentApp.class, args);

        String type = System.getProperty("type");   // 获取type参数
        if ("provider".equals(type)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new ProviderAgentServer().startServer();
                }
            }).start();
        }
        if ("consumer".equals(type)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new ConsumerAgentHttpServer().startServer();
                }
            }).start();
        }
    }
}
