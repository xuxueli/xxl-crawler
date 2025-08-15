## 《Java爬虫框架XXL-CRAWLER》

[![Actions Status](https://github.com/xuxueli/xxl-crawler/workflows/Java%20CI/badge.svg)](https://github.com/xuxueli/xxl-crawler/actions)
[![Maven Central](https://img.shields.io/maven-central/v/com.xuxueli/xxl-crawler)](https://central.sonatype.com/artifact/com.xuxueli/xxl-crawler/)
[![GitHub release](https://img.shields.io/github/release/xuxueli/xxl-crawler.svg)](https://github.com/xuxueli/xxl-crawler/releases)
[![GitHub stars](https://img.shields.io/github/stars/xuxueli/xxl-crawler)](https://github.com/xuxueli/xxl-crawler/)
![License](https://img.shields.io/github/license/xuxueli/xxl-crawler.svg)
[![donate](https://img.shields.io/badge/%24-donate-ff69b4.svg?style=flat-square)](https://www.xuxueli.com/page/donate.html)

[TOCM]

[TOC]

## 一、简介

### 1.1 概述
XXL-CRAWLER 是一个轻量级Java爬虫框架。一行代码开发一个多线程爬虫，全注解方式采集页面数据至Java对象，拥有"多线程、全注解、JS渲染、代理、分布式扩展"等特性；

### 1.2 特性
- 1、简洁：API直观简洁，可快速上手；
- 2、轻量级：底层实现仅强依赖jsoup，简洁高效；
- 3、模块化：模块化的结构设计，可轻松扩展；
- 4、全注解：支持通过注解提取页面数据，高效映射页面数据到PageVO对象，底层自动完成PageVO对象的数据抽取和封装返回；单个页面支持抽取一个或多个PageVO；
- 5、多线程：线程池方式运行，提高采集效率；
- 6、扩散全站：支持以现有URL为起点扩散爬取整站；
- 7、JS渲染：通过扩展 "PageLoader" 模块，支持采集JS动态渲染数据。原生提供 Jsoup(非JS渲染，速度更快)、Selenium+ChromeDriver(JS渲染，兼容性高) 等多种实现，支持自由扩展其他实现；
- 8、代理IP：对抗反采集策略规则WAF；
- 9、动态代理：支持运行时动态调整代理池，以及自定义代理池路由策略；
- 10、失败重试：请求失败后重试，并支持设置重试次数；
- 11、异步：支持同步、异步两种方式运行；
- 12、幂等去重：防止重复爬取；
- 13、URL扩散过滤：支持设置页面白名单正则，过滤URL；
- 14、分布式支持：通过扩展 "RunUrlPool" 模块，并结合Redis或DB共享运行数据可实现分布式。默认提供LocalRunUrlPool单机版爬虫；
- 15、自定义请求信息，如：请求参数、Cookie、Header、UserAgent轮询、Referrer等；
- 16、动态参数：支持运行时动态调整请求参数；
- 17、超时控制：支持设置爬虫请求的超时时间；
- 18、主动停顿：爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；


### 1.3 下载

#### 文档地址

- [中文文档](https://www.xuxueli.com/xxl-crawler/)

#### 源码仓库地址

源码仓库地址 | Release Download
--- | ---
[https://github.com/xuxueli/xxl-crawler](https://github.com/xuxueli/xxl-crawler) | [Download](https://github.com/xuxueli/xxl-crawler/releases)
[https://gitee.com/xuxueli0323/xxl-crawler](https://gitee.com/xuxueli0323/xxl-crawler) | [Download](https://gitee.com/xuxueli0323/xxl-crawler/releases)  


#### 技术交流
- [社区交流](https://www.xuxueli.com/page/community.html)

### 1.4 环境
- JDK：1.8+


## 二、快速入门

### 爬虫示例参考  
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


### 第一步：引入Maven依赖
```
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-crawler</artifactId>
    <version>${最新稳定版}</version>
</dependency>
```

### 第二步：定义 "PageVo/页面数据对象"（可选）

```java
/**
 * @PageSelect： 通过该注解页面元素，单个页面可以匹配多个页面元素生成多个 PageVo 对象。
 * @PageFieldSelect： 通过该注解匹配页面元素的属性，每个页面元素可以匹配多个属性，即对应 PageVo 的多个属性值。
 */
@PageSelect(cssQuery = ".project-title")
public static class GiteeProjectPageVo {

    @PageFieldSelect(cssQuery = ".title")
    private String projectName;

    @PageFieldSelect(cssQuery = ".stars-count", selectType = Const.SelectType.ATTR, selectVal = "data-count")
    private String description;

    @PageFieldSelect(cssQuery = ".title", selectType = Const.SelectType.ATTR, selectVal = "href")
    private String href;

    // set get
}
```

> 在此推荐两款工具，可以直观迅速的获取页面元素的Jquery cssQuery表达式。
> - 1、Chrome DevTools：首先定位元素位置，然后从Element选中选中元素，点击右键选择“Copy + Copy selector”即可;
> - 2、Jquery Selector Helper（Chrome插件）：首先定位元素位置，然后从Element右侧打开Selector界面，然后定位元素即可；


### 第三步：创建并启动爬虫
```java
/**
 * 1、定义 XxlCrawler
 *
 * 说明：XxlCrawler 支持通过一行代码开发并启动一个爬虫，可结合如下示例以及官方文档深入了解。
 */
XxlCrawler crawler = new XxlCrawler.Builder()
        .setUrls("https://gitee.com/explore/all?order=starred&page=1")                              // 设置爬虫入口URL
        .setWhiteUrlRegexs("^https:\\/\\/gitee\\.com\\/explore\\/all\\?order=starred&page=[1-5]$")  // 设置爬虫扩散的URL白名单正则表达式，控制扩散范围
        .setThreadCount(3)                                                                          // 设置爬虫线程池大小
        .setPauseMillis(100)                                                                        // 设置爬虫每次抓取间隔时间，避免对下游压力过大
        .setPageParser(new PageParser<GiteeProjectPageVo>() {
          @Override
          public void afterParse(Response<GiteeProjectPageVo> response) {
            /**
             * 3、获取爬虫结果数据
             *
             * 说明：afterParse 会在多线程爬虫运行过程中实时输出爬虫结果数据，避免结尾一次性反馈造成大对象问题；可实时消费处理数据，如存储在DB等。
             */
            if (response.getParseVoList() != null) {
              for (GiteeProjectPageVo pageVo: response.getParseVoList()) {
                logger.info("response.getHtml().baseUri()={}, PageDataVo={}", response.getHtml().baseUri(), pageVo);
              }
            }
          }
        })
        .build();

crawler.start(true);
```


## 三、总体设计

### 架构图

![输入图片说明](https://www.xuxueli.com/doc/static/xxl-crawler/images/img01.png "在这里输入图片标题")

### 3.1 功能定位

XXL-CRAWLER 是一个轻量级爬虫框架。采用模块化设计，各个模块可灵活进行自定义和扩展。

借助 XXL-CRAWLER，一行代码开发一个分布式爬虫。

### 3.2 核心概念

概念 | 说明
--- | ---
XxlCrawler | 爬虫对象，维护爬虫信息
PageVo | 页面数据对象，一张Web页面可抽取一个或多个PageVo
PageLoader | Wed页面加载器，负责加载页面数据，支持灵活的自定义和扩展
PageParser | Wed页面解析器，绑定泛型PageVO后将会自动抽取页面数据对象，同时支持运行时调整请求参数信息；
NonPageParser ： 非Web页面解析器，如JSON接口等，直接输出响应数据 

### 3.3 爬虫对象：XxlCrawler
功能：爬虫对象，维护爬虫信息，可选属性如下。

方法 | 说明
--- | ---
setUrls | 待爬的URL列表
setAllowSpread | 允许扩散爬取，将会以现有URL为起点扩散爬取整站
setWhiteUrlRegexs | URL白名单正则，非空时进行URL白名单过滤页面
setIfPost | 请求方式：true=POST请求、false=GET请求
setUserAgent | UserAgent
setParamMap | 请求参数
setCookieMap | 请求Cookie
setTimeoutMillis | 超时时间，毫秒
setPauseMillis | 停顿时间，爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
setProxyMaker | 代理生成器，支持设置代理IP，同时支持调整代理池实现动态代理；
setThreadCount | 爬虫并发线程数
setPageParser | 页面解析器
setPageLoader | 页面加载器，默认提供 "JsoupPageLoader" 和 "SeleniumChromePageLoader" 两种实现；
setRunUrlPool  | 设置运行时数据模型，默认提供LocalRunUrlPool单机模型，支持扩展实现分布式模型；
start   | 运行爬虫，可通过入参控制同步或异步方式运行
stop    | 终止爬虫

### 3.4 核心注解：PageSelect

功能：描述页面数据对象，通过该注解从页面抽取PageVo数据信息，可选属性如下。

PageSelect | 说明
--- | ---
cssQuery | CSS选择器, 如 "#body"

### 3.5 核心注解：PageFieldSelect

功能：描述页面数据对象的属性信息，通过该注解从页面抽取PageVo的属性信息，可选属性如下。  
（支持基础数据类型 T ，包括 List<T>）

PageFieldSelect | 说明
--- | ---
cssQuery | CSS选择器, 如 "#title"
selectType | jquery 数据抽取方式，如 ".html()/.text()/.val()/.attr()"等
selectVal | jquery 数据抽取参数，SelectType=ATTR 时有效，如 ".attr("abs:src")"
datePattern | 时间格式化，日期类型数据有效

### 3.6 多线程
以线程池方式并行运行，提供对应API（可参考"章节3.3"）调整线程池大小，提高运行效率；

### 3.7 异步
支持同步、异步两种方式启动运行。

- 同步：将会阻塞业务逻辑，爬虫爬取完全部页面后才会继续执行后续逻辑。
- 异步：不会阻塞业务逻辑，爬虫逻辑以异步方式运行。

### 3.8 动态代理
ProxyMaker（代理生成器）：实现代理支持的组件。支持设置代理IP，同时支持调整代理池实现动态代理；

系统已经提供了两种策略实现；
- RoundProxyMaker（循环代理生成器）: 以循环方式获取代理池中代理；
- RandomProxyMaker（随机代理生成器）: 以随机方式获取代理池中代理；

### 3.9、PageParser
PageParser（页面解析器）：绑定泛型PageVO后将会自动抽取页面数据对象，同时支持运行时调整请求参数信息；

内部方法 | 说明
--- | ---
public void preParse(PageRequest pageRequest) | 可选实现，发起页面请求之前触发调用，可基于此运行时调整请求参数；
public abstract void parse(Document html, Element pageVoElement, T pageVo) | 必须实现，页面抽离封装每个PageVO之后触发调用，可基于此处理PageVO文档或数据；

### 3.10、分布式支持 & RunUrlPool
支持自定义RunUrlPool(运行时数据模型)并结合Redis或DB共享运行数据来实现分布式爬虫。默认提供LocalRunUrlPool实现单机版爬虫。

- RunUrlPool：运行时数据模型，维护爬虫运行时的URL和白名单规则。
    - 单机：单机方式维护爬虫运行数据，默认提供 "LocalRunUrlPool" 的单机版实现。
    - 分布式/集群：集群方式维护爬虫爬虫运行数据，可通过Redis或DB定制实现。

RunUrlPool抽象方法 | 说明
--- | ---
public abstract boolean addUrl(String link); | 新增一个待采集的URL，接口需要做URL去重，爬虫线程将会获取到并进行处理；
public abstract String getUrl(); | 获取一个待采集的URL，并且将它从"待采集URL池"中移除，并且添加到"已采集URL池"中；
public abstract int getUrlNum(); | 获取待采集URL数量；

### 3.11、JS动态渲染 & PageLoader
页面数据通过 "PageLoader" 组件加载，默认使用以下两种实现：
- JsoupPageLoader：速度最快，推荐采用这种方式（不支持JS动态渲染）；
- SeleniumChromePageLoader：支持JS动态渲染，"selenisum + chromedriver" 方案，兼容性较高；

得益于模块化结构设计，可自由扩展其他 "PageLoader" 实现；

注意：
- 1、SeleniumChromePageLoader 为扩展功能，因此maven依赖（selenisum + chromedriver）scope为provided类型，使用时请单独引入；
- 2、JS渲染方式采集数据实用性广，但是也存在缺点，如下：
    - 2.1：JS渲染，速度较慢；
    - 2.2：JS渲染，环境要求较高；
    - 2.3：在需要JS渲染的场景下，推荐做法是：分析页面请求，模拟并主动发起Ajax请求来代替JS引擎自动请求渲染。因为速度更快，更可控；


## 四、版本更新日志
### v1.0.0 Release Notes[2017-09-13]
- 1、面向对象：通过VO对象描述页面信息，提供注解方便的映射页面数据，爬取结果主动封装Java对象返回；
- 2、多线程：线程池方式并行运行；
- 3、异步：支持同步、异步两种方式运行；
- 4、扩散全站：支持以入口URL为起点扩散爬取整站；
- 5、去重：防止重复爬取；
- 6、URL白名单：支持设置页面白名单正则，过滤URL；
- 7、自定义请求信息，如：请求参数、Cookie、Header、UserAgent轮询、Referrer等；
- 8、轻量级：底层实现仅依赖jsoup，简洁高效；
- 9、超时控制：支持设置爬虫请求的超时时间；
- 10、主动停顿：爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
- 11、单个页面支持抽取一个或多个PageVO；

### v1.1.0 Release Notes[2017-11-08]
- 1、页面默认cssQuery调整为html标签；
- 2、升级Jsoup至1.11.1版本；
- 3、修复PageVO注解失效的问题；
- 4、属性注解参数attributeKey调整为selectVal；
- 5、代理IP：对抗反采集策略规则WAF；
- 6、动态代理：支持运行时动态调整代理池，以及自定义代理池路由策略；

### v1.2.0 Release Notes[2017-12-14]
- 1、爬虫Builder底层API优化；
- 2、支持设置请求Headers；
- 3、支持设置多UserAgent轮询；
- 4、失败重试：支持请求失败后主动重试，并支持设置重试次数；
- 5、动态参数：支持运行时动态调整请求参数；
- 6、分布式支持：支持自定义RunUrlPool(运行时数据模型)并结合Redis或DB共享运行数据来实现分布式。默认提供LocalRunUrlPool单机版爬虫。

### v1.2.1 Release Notes[2018-02-07]
- 1、JS渲染：支持JS渲染方式采集数据，可参考 "爬虫示例6"；
- 2、抽象并设计PageLoader，方便自定义和扩展页面加载逻辑，如JS渲染等。底层提供 "JsoupPageLoader(默认/推荐)"，"HtmlUnitPageLoader"两种实现，可自定义其他类型PageLoader如 "Selenium" 等；
- 3、修复Jsoup默认加载1M的限制；
- 4、爬虫线程中断处理优化；

### v1.2.2 Release Notes[2018-10-24]
- 1、系统底层重构，规范包名；
- 2、采集线程白名单过滤优化，避免冗余失败重试；
- 3、增强JS渲染方式采集能力，原生新提供 "SeleniumPhantomjsPageLoader"，支持以 "selenisum + phantomjs" 方式采集页面数据；
- 4、支持采集非Web页面，如JSON接口等，直接输出响应数据；选择 "NonPageParser" 即可；

### v1.3.0 Release Notes[2022-10-16]
- 1、开源协议：由 GPLv3 调整为 Apache2.0 开源协议；
- 2、版本升级：依赖版本升级，如jsoup、htmlunit、selenium等;
- 3、代码重构：优化代码结构，提升系统可维护性；

### v1.4.0 Release Notes[2024-01-05]
- 1、【提升】爬虫JS渲染能力强化：升级提供 "Selenium + ChromeDriver" 方案支持JS渲染，兼容性更高，废弃旧Phantomjs方案。非JS渲染场景仍然Jsoup，速度更快。同时支持自由扩展其他实现。
- 2、【优化】进一步优化 Selenium 兼容问题，完善JS渲染场景下兼容性和性能。
- 3、【重构】重构核心功能模块，提升扩展性；修复历史代码隐藏问题，提升系统稳定习惯。
- 4、【升级】多个依赖升级至更新版本，如jsoup、selenium等。

### v1.4.1 Release Notes[2024-01-06]
- 1、【修复】PageSelect注解取数值逻辑修复；

### v1.5.0 Release Notes[2025-08-16]
- 1、【升级】项目升级JDK17；
- 2、【升级】代码兼容改造，适配JDK17；
- 3、【升级】多个依赖升级至更新版本，如jsoup、selenium等。

### v1.5.1 Release Notes[迭代中]
- 1、【TODO】爬虫扩散规则抽象，支持自定义；包括深度、URL正则、以及自定义编码等；

### TODO LIST
- 1、扩展SelectType；
- 2、协程，搁置，jsoup/htmlunit/selenisum协程兼容性低；
- 3、bloomfilter去重，可选接入，大数据量下推荐；
- 4、对抗爬虫蜜罐，成功率检测，历史数据学习；
- 5、对抗主动休眠防御，Timeout即可；
- 6、页面生僻字中文乱码处理；
- 7、HTTPS站点支持；
- 8、爬虫扩散规则抽象，支持自定义；包括深度、URL正则、以及自定义编码等；
- 9、jsoup支持json请求，如 “Jsoup.connect(url).requestBody(jsonString).execute().body();”
- 10、JsoupUtil.findLinks 可扩展；不限制http前缀；支持业务自定设置link；
- 11、支持登陆态；给出示例；
- 12、afterParse更开放，支持添加URL等；

## 五、其他

### 5.1 项目贡献
欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-crawler/issues/) 讨论新特性或者变更。

### 5.2 用户接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-crawler/issues/1 ) 登记，登记仅仅为了产品推广。

### 5.3 开源协议和版权
产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

- Licensed under the Apache License, Version 2.0.
- Copyright (c) 2015-present, xuxueli.

---
### 捐赠
无论金额多少都足够表达您这份心意，非常感谢 ：）      [前往捐赠](https://www.xuxueli.com/page/donate.html )