package com.kimchau.rpc.server;

import com.kimchau.rpc.common.bean.RpcRequest;
import com.kimchau.rpc.common.bean.RpcResponse;
import com.kimchau.rpc.common.codec.RpcDecoder;
import com.kimchau.rpc.common.codec.RpcEncoder;
import com.kimchau.rpc.common.util.StringUtil;
import com.kimchau.rpc.registry.ServiceRegistry;
import com.kimchau.rpc.server.annotation.RpcService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * RPC 服务器（用于发布 RPC 服务）
 *
 * @author kimchau
 * @since 1.0.0
 */
public class RpcServer implements ApplicationContextAware, InitializingBean {

    public static final Logger log = LoggerFactory.getLogger(RpcServer.class);

    /**
     * rpc 服务启动地址：host:port
     */
    private String serviceAddress;

    /**
     * 服务注册与发现地址：host:port
     */
    private ServiceRegistry serviceRegistry;

    /**
     * 存放 服务名 与 服务对象 之间的映射关系
     */
    private Map<String, Object> handlerMap = new HashMap<>();

    public RpcServer(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public RpcServer(String serviceAddress, ServiceRegistry serviceRegistry) {
        this.serviceAddress = serviceAddress;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        // 扫描带有 RpcService 注解的类并初始化 handlerMap 对象
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String serviceVersion = rpcService.version();
                if (StringUtil.isNotEmpty(serviceVersion)) {
                    serviceName += "-" + serviceVersion;
                }
                handlerMap.put(serviceName, serviceBean);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            // 创建并初始化 Netty 服务端 Bootstrap 对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 设置线程队列得到连接的个数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 解码 rpc 请求
                            pipeline.addLast(new RpcDecoder(RpcRequest.class));
                            // 编码 rpc 响应
                            pipeline.addLast(new RpcEncoder(RpcResponse.class));
                            // 处理 rpc 请求
                            pipeline.addLast(new RpcServerHandler(handlerMap));
                        }
                    });

            // 获取 RPC 服务器的 IP 地址与端口号
            String[] addressArray = StringUtil.split(serviceAddress, ":");
            String host = addressArray[0];
            int port = Integer.parseInt(addressArray[1]);
            // 启动 RPC 服务器
            ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();
            // 注册监听事件，当操作完成后会进行调用
            channelFuture.addListener(f -> {
                if(f.isSuccess()) {
                    log.info("服务器已在{}端口成功监听...", port);
                    // 注册 RPC 服务地址
                    if (serviceRegistry != null) {
                        for (String interfaceName : handlerMap.keySet()) {
                            serviceRegistry.register(interfaceName, serviceAddress);
                            log.debug("register service: {} => {}", interfaceName, serviceAddress);
                        }
                    }
                } else {
                    log.error("服务启动失败...");
                }
            });
            // 对【关闭通道】进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
