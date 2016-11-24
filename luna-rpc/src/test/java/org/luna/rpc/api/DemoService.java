package org.luna.rpc.api;

/**
 * Created by kaiba on 2016/5/6.
 */
public interface DemoService {

    public void hello();

    public String hello(String id);

    public Member getById(int id);
}
