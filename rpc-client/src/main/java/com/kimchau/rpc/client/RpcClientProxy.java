package com.kimchau.rpc.client;

import com.kimchau.rpc.common.bean.RpcRequest;
import com.kimchau.rpc.common.bean.RpcResponse;
import com.kimchau.rpc.common.errorcode.RpcCode;
import com.kimchau.rpc.common.util.StringUtil;
import com.kimchau.rpc.registry.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 *
 * RPC 客户端服务代理
 *
 * @author kimchau
 * @since 1.0.0
 */
public class RpcClientProxy {

    public static final Logger log = LoggerFactory.getLogger(RpcClientProxy.class);

    /**
     * 服务注册与发现的地址 host:port
     */
    private String serviceAddress;
    private ServiceDiscovery serviceDiscovery;

    public RpcClientProxy(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public RpcClientProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass) {
        return create(interfaceClass, "");
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceClass, final String serviceVersion) {
        // 创建动态代理对象
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                (proxy, method, args) -> {
                    // 创建 RPC 请求对象并设置请求属性
                    RpcRequest request = new RpcRequest();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setInterfaceName(method.getDeclaringClass().getName());
                    request.setServiceVersion(serviceVersion);
                    request.setMethodName(method.getName());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(args);
                    // 获取 RPC 服务地址
                    if (serviceDiscovery != null) {
                        String serviceName = interfaceClass.getName();
                        if (StringUtil.isNotEmpty(serviceVersion)) {
                            serviceName += "-" + serviceVersion;
                        }
                        serviceAddress = serviceDiscovery.discover(serviceName);
                        log.debug("discover service: {} => {}", serviceName, serviceAddress);
                    }
                    if (StringUtil.isEmpty(serviceAddress)) {
                        throw new RuntimeException("server address is empty");
                    }
                    // 从 RPC 服务地址中解析主机名与端口号
                    String[] array = StringUtil.split(serviceAddress, ":");
                    String host = array[0];
                    int port = Integer.parseInt(array[1]);
                    // 创建 RPC 客户端对象并发送 RPC 请求
                    RpcClient client = new RpcClient(host, port);
                    long time = System.currentTimeMillis();
                    RpcResponse response = client.send(request);
                    log.debug("time: {}ms", System.currentTimeMillis() - time);
                    if (response == null) {
                        throw new RuntimeException("response is null");
                    }
                    // 返回 RPC 响应结果
                    if (response.getCode() != RpcCode.OK.getCode()) {
                        throw new RuntimeException(response.getMessage());
                    } else {
                        return response.getData();
                    }
                }
        );
    }
}
