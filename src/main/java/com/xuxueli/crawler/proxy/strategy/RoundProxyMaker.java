package com.xuxueli.crawler.proxy.strategy;

import com.xuxueli.crawler.proxy.ProxyMaker;

import java.net.Proxy;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundProxyMaker extends ProxyMaker {

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public Proxy make() {
        if (super.proxyList==null || super.proxyList.size()==0) {
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
