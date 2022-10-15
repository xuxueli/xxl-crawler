<p align="center">
    <img src="https://www.xuxueli.com/doc/static/xxl-job/images/xxl-logo.jpg" width="150">
    <h3 align="center">XXL-CRAWLER</h3>
    <p align="center">
        XXL-CRAWLER, a distributed web crawler framework.
        <br>
        <a href="https://www.xuxueli.com/xxl-crawler/"><strong>-- Home Page --</strong></a>
        <br>
        <br>
        <a href="https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-crawler/">
            <img src="https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-crawler/badge.svg" >
        </a>
         <a href="https://github.com/xuxueli/xxl-crawler/releases">
             <img src="https://img.shields.io/github/release/xuxueli/xxl-crawler.svg" >
         </a>
         <img src="https://img.shields.io/github/license/xuxueli/xxl-crawler.svg" >
         <a href="https://www.xuxueli.com/page/donate.html">
            <img src="https://img.shields.io/badge/%24-donate-ff69b4.svg?style=flat-square" >
         </a>
    </p>    
</p>


## Introduction

XXL-CRAWLER is a distributed web crawler framework. One line of code develops a distributed crawler. Features such as "multithreaded、asynchronous、dynamic IP proxy、distributed、javascript-rendering".

XXL-CRAWLER 是一个分布式爬虫框架。一行代码开发一个分布式爬虫，拥有"多线程、异步、IP动态代理、分布式、JS渲染"等特性；

## Documentation
- [中文文档](https://www.xuxueli.com/xxl-crawler/)


## Features
- 1、简洁：API直观简洁，可快速上手；
- 2、轻量级：底层实现仅强依赖jsoup，简洁高效；
- 3、模块化：模块化的结构设计，可轻松扩展
- 4、面向对象：支持通过注解，方便的映射页面数据到PageVO对象，底层自动完成PageVO对象的数据抽取和封装返回；单个页面支持抽取一个或多个PageVO
- 5、多线程：线程池方式运行，提高采集效率；
- 6、分布式支持：通过扩展 "RunData" 模块，并结合Redis或DB共享运行数据可实现分布式。默认提供LocalRunData单机版爬虫。
- 7、JS渲染：通过扩展 "PageLoader" 模块，支持采集JS动态渲染数据。原生提供 Jsoup(非JS渲染，速度更快)、HtmlUnit(JS渲染)、Selenium+Phantomjs(JS渲染，兼容性高) 等多种实现，支持自由扩展其他实现。
- 8、失败重试：请求失败后重试，并支持设置重试次数；
- 9、代理IP：对抗反采集策略规则WAF；
- 10、动态代理：支持运行时动态调整代理池，以及自定义代理池路由策略；
- 11、异步：支持同步、异步两种方式运行；
- 12、扩散全站：支持以现有URL为起点扩散爬取整站；
- 13、去重：防止重复爬取；
- 14、URL白名单：支持设置页面白名单正则，过滤URL；
- 15、自定义请求信息，如：请求参数、Cookie、Header、UserAgent轮询、Referrer等；
- 16、动态参数：支持运行时动态调整请求参数；
- 17、超时控制：支持设置爬虫请求的超时时间；
- 18、主动停顿：爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；


## Communication

- [社区交流](https://www.xuxueli.com/page/community.html)


## Contributing
Contributions are welcome! Open a pull request to fix a bug, or open an [Issue](https://github.com/xuxueli/xxl-crawler/issues/) to discuss a new feature or change.

欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-crawler/issues/) 讨论新特性或者变更。

## 接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-crawler/issues/1 ) 登记，登记仅仅为了产品推广。


## Copyright and License
This product is open source and free, and will continue to provide free community technical support. Individual or enterprise users are free to access and use.

- Licensed under the Apache License, Version 2.0.
- Copyright (c) 2015-present, xuxueli.

产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。


## Donate
No matter how much the amount is enough to express your thought, thank you very much ：）     [To donate](https://www.xuxueli.com/page/donate.html )

无论金额多少都足够表达您这份心意，非常感谢 ：）      [前往捐赠](https://www.xuxueli.com/page/donate.html )
