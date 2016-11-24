package org.luna.rpc;

/**
 * Created by luliru on 2016/11/23.
 */
public class Test {

    public static void main(String[] args) throws NoSuchMethodException {
        Class clazz = DemoServiceImpl.class;
        System.out.println(clazz.getMethod("hello",new Class[]{String.class}));
    }
}
