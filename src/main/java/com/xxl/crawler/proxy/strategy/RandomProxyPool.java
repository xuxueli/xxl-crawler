package com.xxl.crawler.proxy.strategy;

import com.xxl.crawler.proxy.ProxyPool;

import java.net.Proxy;
import java.util.Random;

/**
 * random proxy pool
 *
 * @author xuxueli 2017-11-07 20:06:54
 */
public class RandomProxyPool extends ProxyPool {

    private final Random random = new Random();

    @Override
    public Proxy getProxy() {
        if (super.proxyList==null || super.proxyList.isEmpty()) {
            return null;
        }

        if (super.proxyList.size() == 1) {
            super.proxyList.get(0);
        }

        return super.proxyList.get(random.nextInt(super.proxyList.size()));
    }

}
