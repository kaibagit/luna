package org.luna.rpc;

/**
 * Created by kaiba on 2016/5/24.
 */
public class UserServiceImpl implements UserService {
    public String hello(String id) {
        return "hello"+id;
    }
}
