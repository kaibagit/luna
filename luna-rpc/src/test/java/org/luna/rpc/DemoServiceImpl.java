package org.luna.rpc;

/**
 * Created by kaiba on 2016/5/24.
 */
public class DemoServiceImpl implements DemoService {
    public String hello(String id) {
        return "hello"+id;
    }
}