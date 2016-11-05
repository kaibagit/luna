//package org.luna.rpc;
//
//import org.luna.rpc.customer.Customer;
//
///**
// * Created by kaiba on 2016/5/24.
// */
//public class ClientTest {
//
//    public static void main(String[] args){
//        Customer customer = new Customer("http://localhost",8080);
//        customer.start();
//
//        DemoService userService = customer.getClient(DemoService.class);
//        String result = userService.hello("1L");
//
//        System.out.println(result);
//    }
//}
