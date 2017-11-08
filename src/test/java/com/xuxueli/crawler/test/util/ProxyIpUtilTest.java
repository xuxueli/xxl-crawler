package com.xuxueli.crawler.test.util;

import com.xuxueli.crawler.util.ProxyIpUtil;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * proxy ip util test
 *
 * @author xuxueli 2017-11-08 13:35:16
 */
public class ProxyIpUtilTest {

    public static void main(String[] args) {
        int code = ProxyIpUtil.checkProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("---", 80)), null);
        System.out.println(code);
    }

}
