package com.kimchau.rpc.sample.server;

import com.kimchau.rpc.api.HelloWorld;
import com.kimchau.rpc.server.annotation.RpcService;

/**
 * @author kimchau
 * @since 1.0.0
 */
@RpcService(HelloWorld.class)
public class HelloWorldImpl implements HelloWorld {
    @Override
    public String sayHello() {
        return "hello world, version 1";
    }
}
