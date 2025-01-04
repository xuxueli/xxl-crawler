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

XXL-CRAWLER is a lightweight crawler framework. A line of code to develop a multi-threaded crawler, fully annotated way to collect page data to Java objects, with "multi-threaded, fully annotated, JS rendering, proxy, distributed extension" and other features;

XXL-CRAWLER 是一个轻量级爬虫框架。一行代码开发一个多线程爬虫，全注解方式采集页面数据至Java对象，拥有"多线程、全注解、JS渲染、代理、分布式扩展"等特性；

## Code Examples    
>注意：仅供学习测试使用，如有侵犯请联系删除

如下测试代码可以前往仓库查看：[测试代码目录](https://github.com/xuxueli/xxl-crawler/tree/master/src/test/java/com/xxl/crawler/test)

| 序号 | 爬虫名称                        | 功能描述                                                                                | 测试用例代码文件         |
|----|-----------------------------|-------------------------------------------------------------------------------------|------------------|
| 1  | Gitee高星项目数据爬虫【页面提取数据】       | 一行代码启动多线程爬虫，分页方式扩散爬取“Gitee开源项目列表”，通过“注解式”自动提取页面数据，封装成PageVo输出；                      | XxlCrawlerTest01 |
| 2  | Gitee页面下载爬虫【页面下载】           | 爬取“Gitee开源项目列表”，获取相关页面html原始数据，下载本地生成html文件；                                        | XxlCrawlerTest02 |
| 3  | 网易图片下载爬虫【图片下载】              | 爬取“网易新闻文章图片”，下载图片文件至本地；                                                             | XxlCrawlerTest03 |
| 4  | 百度新闻爬虫【接口提取数据】              | 爬取非Web页面，本案例为JSON接口，直接输出响应数据                                                        | XxlCrawlerTest04 |
| 5  | 电商商品价格爬虫【JS渲染方式；Selenium集成】 | 爬虫获取电商商品价格，由于价格异步渲染；该方案使用 Selenium + ChromeDriver 方式JS渲染，模拟浏览器行为采集数据；               | XxlCrawlerTest05 |
| 6  | 代理方式爬取数据【Proxy代理方式】         | 爬取目标页面数据，通过代理进行；可突破访问限制、保障数据安全；                                                     | XxlCrawlerTest06 |
| 7  | 集群方式爬取数据【Redis集群方式】       | 爬取目标页面数据，通过集群方式进行；集群中多个XxlCrawler共享RunUrlPool，协同扩散URL并消费待采集任务，提升采集效率。               | XxlCrawlerTest07 |


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
