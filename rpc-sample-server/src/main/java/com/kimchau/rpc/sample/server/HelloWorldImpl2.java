package com.kimchau.rpc.sample.server;

import com.kimchau.rpc.api.HelloWorld;
import com.kimchau.rpc.server.annotation.RpcService;

/**
 * @author kimchau
 * @since 1.0.0
 */
@RpcService(value = HelloWorld.class, version = "2")
public class HelloWorldImpl2 implements HelloWorld {
    @Override
    public String sayHello() {
        return "hello world, version 2";
    }
}
