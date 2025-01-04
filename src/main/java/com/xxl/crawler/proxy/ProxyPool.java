package com.xxl.crawler.proxy;

import java.net.Proxy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * proxy pool
 *
 * @author xuxueli 2017-11-07 19:52:33
 */
public abstract class ProxyPool {

    protected List<Proxy> proxyList = new CopyOnWriteArrayList<Proxy>();            // 请求代理池，对抗反采集策略规则WAF

    public ProxyPool addProxy(Proxy proxy) {
        this.proxyList.add(proxy);
        return this;
    }

    public ProxyPool addProxyList(List<Proxy> proxyList) {
        this.proxyList.addAll(proxyList);
        return this;
    }

    public ProxyPool clear() {
        this.proxyList.clear();
        return this;
    }

    /**
     * make proxy
     *
     * @return Proxy
     */
    public abstract Proxy getProxy();

}
