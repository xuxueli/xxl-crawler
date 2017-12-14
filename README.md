<p align="center">
    <a href="http://www.xuxueli.com/xxl-crawler/">
        <img src="https://raw.githubusercontent.com/xuxueli/xxl-job/master/doc/images/xxl-logo.jpg" width="150">
    </a>
    <h3 align="center">XXL-CRAWLER</h3>
    <p align="center">
        XXL-CRAWLER, a object-oriented web crawler framework..
        <br>
        <a href="http://www.xuxueli.com/xxl-crawler/"><strong>-- Browse website. --</strong></a>
        <br>
        <br>
        <a href="https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-crawler/">
            <img src="https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-crawler/badge.svg" >
        </a>
         <a href="https://github.com/xuxueli/xxl-crawler/releases">
             <img src="https://img.shields.io/github/release/xuxueli/xxl-crawler.svg" >
         </a>
         <a href="http://www.gnu.org/licenses/gpl-3.0.html">
             <img src="https://img.shields.io/badge/license-GPLv3-blue.svg" >
         </a>
         <a href="http://www.xuxueli.com/page/donate.html">
            <img src="https://img.shields.io/badge/%24-donate-ff69b4.svg?style=flat-square" >
         </a>
    </p>    
</p>


## Introduction

XXL-CRAWLER is a flexible and efficient、object-oriented web crawler framework. One line of code develops a distributed crawler. Features such as "multithreaded、asynchronous、dynamic IP proxy".

XXL-CRAWLER 是一个灵活高效、面向对象的分布式爬虫框架。一行代码开发一个分布式爬虫，拥有"多线程、异步、IP动态代理"等特性；

## Documentation
- [中文文档](http://www.xuxueli.com/xxl-crawler/)


## Features
- 1、面向对象：通过VO对象描述页面信息，提供注解方便的映射页面数据，爬取结果主动封装Java对象返回；
- 2、多线程；
- 3、扩散全站：将会以现有URL为起点扩散爬取整站；
- 4、去重：防止重复爬取；
- 5、URL白名单：支持设置页面白名单正则，过滤URL；
- 6、异步：支持同步、异步两种方式运行；
- 7、自定义请求信息，如：请求参数、Cookie、Header、UserAgent轮询、Referrer等；
- 8、轻量级：底层实现仅依赖jsoup，简洁高效；
- 9、超时控制：支持设置爬虫请求的超时时间；
- 10、主动停顿：爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
- 11、单个页面支持抽取一个或多个PageVO；
- 12、代理IP：对抗反采集策略规则WAF；
- 13、动态代理：支持运行时动态调整代理池，以及自定义代理池路由策略；
- 14、失败重试：请求失败后重试，并支持设置重试次数；
- 15、动态参数：支持运行时动态调整请求参数；
- 16、分布式支持：支持自定义RunData(运行时数据模型)并结合Redis或DB共享运行数据来实现分布式。默认提供LocalRunData单机版爬虫。


## Communication

- [社区交流](http://www.xuxueli.com/page/community.html)


## Contributing
Contributions are welcome! Open a pull request to fix a bug, or open an [Issue](https://github.com/xuxueli/xxl-crawler/issues/) to discuss a new feature or change.

欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-crawler/issues/) 讨论新特性或者变更。

## 接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-crawler/issues/1 ) 登记，登记仅仅为了产品推广。


## Copyright and License
This product is open source and free, and will continue to provide free community technical support. Individual or enterprise users are free to access and use.

- Licensed under the GNU General Public License (GPL) v3.
- Copyright (c) 2015-present, xuxueli.

产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。


## Donate
No matter how much the amount is enough to express your thought, thank you very much ：）     [To donate](http://www.xuxueli.com/page/donate.html )

无论金额多少都足够表达您这份心意，非常感谢 ：）      [前往捐赠](http://www.xuxueli.com/page/donate.html )
