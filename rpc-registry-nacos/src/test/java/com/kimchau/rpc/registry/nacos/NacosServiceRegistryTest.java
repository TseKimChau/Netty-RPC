package com.kimchau.rpc.registry.nacos;

import com.alibaba.nacos.client.utils.ParamUtil;
import org.junit.Test;

import java.util.Properties;

/**
 * @author kimchau
 * @since 1.0.0
 */
public class NacosServiceRegistryTest {

    @Test
    public void test() {
        Properties properties = new Properties();
        properties.setProperty("namespace","kimchau");
        String namespace = ParamUtil.parseNamespace(properties);
        System.out.println("namespace: " + namespace);
    }

}