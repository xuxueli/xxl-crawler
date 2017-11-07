package com.xuxueli.crawler.proxy;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * proxy macker
 *
 * @author xuxueli 2017-11-07 19:52:33
 */
public abstract class ProxyMaker {

    protected List<Proxy> proxyList = new ArrayList<Proxy>();            // 请求代理池，对抗反采集策略规则WAF

    public ProxyMaker(List<Proxy> proxyList) {
        this.proxyList = proxyList;
    }

    public void setProxyList(List<Proxy> proxyList) {
        this.proxyList = proxyList;
    }

    public void addProxyList(List<Proxy> proxyList) {
        this.proxyList.addAll(proxyList);
    }

    /**
     * make proxy
     *
     * @return
     */
    public abstract Proxy make();

}
