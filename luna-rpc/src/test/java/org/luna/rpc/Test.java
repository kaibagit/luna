package org.luna.rpc;

import org.luna.rpc.api.DemoService;
import org.luna.rpc.provider.DemoServiceImpl;

/**
 * Created by luliru on 2016/11/23.
 */
public class Test {

    public static void main(String[] args) throws NoSuchMethodException {
        Class clazz = DemoService.class;
        System.out.println(clazz.getMethod("getById",new Class[]{Integer.TYPE}));
    }
}
