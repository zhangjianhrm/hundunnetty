package com.alibaba.dubbo.performance.demo.agent.myrpc;

import java.util.Map;

public interface Response {


    Map<String, String> getAttachments();

    void setAttachment(String key, String value);

}
