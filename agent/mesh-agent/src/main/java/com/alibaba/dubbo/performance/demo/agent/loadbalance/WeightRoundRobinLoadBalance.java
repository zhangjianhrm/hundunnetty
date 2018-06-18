package com.alibaba.dubbo.performance.demo.agent.loadbalance;

import com.alibaba.dubbo.performance.demo.agent.myrpc.Endpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class WeightRoundRobinLoadBalance implements LoadBalance {


    private volatile EndpointHolder endpointHolder;

    public WeightRoundRobinLoadBalance() {
    }

    class EndpointHolder {

        private int randomKeySize;
        private List<Endpoint> randomKeyList = new ArrayList<>();
        AtomicInteger cursor = new AtomicInteger(0);

        EndpointHolder(List<Endpoint> endpoints) {
            List<Integer> weightsArr = endpoints.stream().map(Endpoint::getWeight).collect(Collectors.toList());

            int weightGcd = findGcd(weightsArr.toArray(new Integer[]{}));
            if (weightGcd != 1) {
                for (Endpoint endpoint : endpoints) {
                    endpoint.setWeight(endpoint.getWeight() / weightGcd);
                }
            }
            for (Endpoint endpoint : endpoints) {
                for (int i = 0; i < endpoint.getWeight(); i++) {
                    randomKeyList.add(endpoint);
                }
            }
            Collections.shuffle(randomKeyList);
            randomKeySize = randomKeyList.size();
        }

        Endpoint next() {
            Endpoint endpoint = randomKeyList.get(Math.abs(cursor.getAndAdd(1)) % randomKeySize);
            return endpoint;
        }

        private int findGcd(int n, int m) {
            return (n == 0 || m == 0) ? n + m : findGcd(m, n % m);
        }


        private int findGcd(Integer[] arr) {
            if (arr.length == 1) return arr[0];
            int i = 0;
            for (; i < arr.length - 1; i++) {
                arr[i + 1] = findGcd(arr[i], arr[i + 1]);
            }
            return findGcd(arr[i], arr[i - 1]);
        }
    }

    @Override
    public Endpoint select() {
        return endpointHolder.next();
    }

    @Override
    public void onRefresh(List<Endpoint> endpoints) {
        this.endpointHolder = new EndpointHolder(endpoints);
    }

}
