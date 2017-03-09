package org.luna.rpc.cluster.loadbalance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.luna.rpc.common.constant.URLParamType;
import org.luna.rpc.core.Client;
import org.luna.rpc.core.Invocation;
import org.luna.rpc.core.Result;
import org.luna.rpc.core.URL;

/**
 * Created by luliru on 2017/3/9.
 */
public class WeightLoadBalanceTest {

    @Test
    public void testSelectOnDiffrentWeight(){
        WeightLoadBalance loadBalance = null;
        Map<String,Integer> weightSelectCount = new HashMap<>();

        int[] weightArr = {100,300,600};
        List<Client> clientList = new ArrayList<>();
        for(int weight : weightArr){
            URL url = new URL("luna","localhost",0,String.valueOf(weight),"service","1.0");
            url.addParameter(URLParamType.weight.getName(),weight);
            Client client = new Client() {
                @Override
                public Result call(Invocation invocation) {
                    return null;
                }

                @Override
                public URL getUrl() {
                    return url;
                }

                @Override
                public void start() {

                }

                @Override
                public void destory() {

                }
            };
            clientList.add(client);
            weightSelectCount.put(url.getGroup(),0);
        }

        loadBalance = new WeightLoadBalance(clientList);

        int selectTimes = 10000;
        for(int i=0;i<selectTimes;i++){
            Client client = loadBalance.select(null);
            String group = client.getUrl().getGroup();
            weightSelectCount.put(group,weightSelectCount.get(group)+1);
        }
        for(Map.Entry<String,Integer> entry : weightSelectCount.entrySet()){
            String group = entry.getKey();
            int weight = Integer.valueOf(group);
            int count = entry.getValue();
            int rate = count * 100/selectTimes;
            int expectRate = weight * 100/1000;
            Assert.assertTrue(Math.abs(rate - expectRate) < 2); // 权重误差不超过阈值。
            System.out.println(group+":rate=>"+rate);
        }
    }

    @Test
    public void testSelectOnSameWeight(){
        WeightLoadBalance loadBalance = null;
        Map<String,Integer> weightSelectCount = new HashMap<>();

        List<Client> clientList = new ArrayList<>();
        for(int i=1;i<=5;i++){
            URL url = new URL("luna","localhost",0,String.valueOf(i),"service","1.0");
            Client client = new Client() {
                @Override
                public Result call(Invocation invocation) {
                    return null;
                }

                @Override
                public URL getUrl() {
                    return url;
                }

                @Override
                public void start() {

                }

                @Override
                public void destory() {

                }
            };
            clientList.add(client);
            weightSelectCount.put(url.getGroup(),0);
        }

        loadBalance = new WeightLoadBalance(clientList);

        int selectTimes = 10000;
        for(int i=0;i<selectTimes;i++){
            Client client = loadBalance.select(null);
            String group = client.getUrl().getGroup();
            weightSelectCount.put(group,weightSelectCount.get(group)+1);
        }
        for(Map.Entry<String,Integer> entry : weightSelectCount.entrySet()){
            String group = entry.getKey();
            int count = entry.getValue();
            int rate = count * 100/selectTimes;
            int expectRate = 1 * 100/clientList.size();
            Assert.assertTrue(Math.abs(rate - expectRate) < 2); // 权重误差不超过阈值。
            System.out.println(group+":rate=>"+rate);
        }
    }


}
