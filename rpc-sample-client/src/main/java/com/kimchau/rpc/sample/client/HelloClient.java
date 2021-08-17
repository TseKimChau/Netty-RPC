package com.kimchau.rpc.sample.client;

import com.kimchau.rpc.api.HelloWorld;
import com.kimchau.rpc.client.RpcClientProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author kimchau
 * @since 1.0.0
 */
public class HelloClient {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        RpcClientProxy rpcClientProxy = context.getBean(RpcClientProxy.class);

        HelloWorld helloWorld = rpcClientProxy.create(HelloWorld.class);
        String res = helloWorld.sayHello();
        System.out.println("hello world: " + res);

        HelloWorld helloWorld2 = rpcClientProxy.create(HelloWorld.class, "2");
        String res2 = helloWorld2.sayHello();
        System.out.println("hello world: " + res2);
    }

}
