package com.alibaba.dubbo.performance.demo.agent.myrpc;


public enum FutureState {

    DOING(0),

    DONE(1),

    CANCELLED(2);

    private final int value;

    FutureState(int value) {
        this.value = value;
    }


    public boolean isDoingState() {
        return this == DOING;
    }
}
