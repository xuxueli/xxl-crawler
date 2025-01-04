package com.xuxueli.crawler.proxy.strategy;

import com.xuxueli.crawler.proxy.ProxyPool;

import java.net.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * round proxy pool
 *
 * @author xuxueli 2017-11-07 20:06:54
 */
public class RoundProxyPool extends ProxyPool {

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public Proxy getProxy() {
        if (super.proxyList==null || super.proxyList.isEmpty()) {
            return null;
        }

        if (super.proxyList.size() == 1) {
            super.proxyList.get(0);
        }

        int countVal = count.incrementAndGet();
        if (countVal > 100000) {
            countVal = 0;
            count.set(countVal);
        }

        return super.proxyList.get(countVal%super.proxyList.size());
    }

}
