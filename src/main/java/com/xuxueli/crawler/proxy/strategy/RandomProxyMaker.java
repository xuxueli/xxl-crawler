package com.xuxueli.crawler.proxy.strategy;

import com.xuxueli.crawler.proxy.ProxyMaker;

import java.net.Proxy;
import java.util.Random;

/**
 * proxy macker, rancom strategy
 *
 * @author xuxueli 2017-11-07 20:06:54
 */
public class RandomProxyMaker extends ProxyMaker {

    private Random random = new Random();

    @Override
    public Proxy make() {
        if (super.proxyList==null || super.proxyList.size()==0) {
            return null;
        }

        if (super.proxyList.size() == 1) {
            super.proxyList.get(0);
        }

        return super.proxyList.get(random.nextInt(super.proxyList.size()));
    }

}
