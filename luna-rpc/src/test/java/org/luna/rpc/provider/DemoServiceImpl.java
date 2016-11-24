package org.luna.rpc.provider;

import org.luna.rpc.api.DemoService;
import org.luna.rpc.api.Member;

/**
 * Created by kaiba on 2016/5/24.
 */
public class DemoServiceImpl implements DemoService {
    @Override
    public void hello() {
        System.out.println("hello");
    }

    public String hello(String id) {
        System.out.println("hello"+id);
        return "hello"+id;
    }

    @Override
    public Member getById(int id) {
        Member m = new Member();
        m.setId(id);
        m.setName("name");
        return m;
    }
}
