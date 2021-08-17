package com.kimchau.rpc.sample.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * RPC 服务启动类
 *
 * @author kimchau
 * @since 1.0.0
 */
public class ServerBootstrap {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring.xml");
    }

}
