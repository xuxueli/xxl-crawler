## 《面向对象的分布式爬虫框架XXL-CRAWLER》

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-crawler/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.xuxueli/xxl-crawler/)
[![GitHub release](https://img.shields.io/github/release/xuxueli/xxl-crawler.svg)](https://github.com/xuxueli/xxl-crawler/releases)
[![License](https://img.shields.io/badge/license-GPLv3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)

## 一、简介

### 1.1 概述
XXL-CRAWLER 是一个灵活高效、面向对象的分布式爬虫框架。一行代码开发一个分布式爬虫；

### 1.2 特性
- 1、面向对象：通过VO对象描述页面信息，提供注解方便的映射页面数据，爬取结果主动封装Java对象返回；
- 2、多线程；
- 3、扩散全站：将会以现有URL为起点扩散爬取整站；
- 4、去重：防止重复爬取；
- 5、URL白名单：支持设置页面白名单正则，过滤URL；
- 6、异步：支持同步、异步两种方式运行；
- 7、自定义请求信息，如：请求参数、Cookie、userAgent等；
- 8、轻量级：底层实现仅依赖jsoup，简洁高效；
- 9、超时控制：支持设置爬虫请求的超时时间；
- 10、主动停顿：爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
- 11、单个页面支持抽取多个PageVO；

### 1.4 下载

#### 文档地址

- [中文文档](http://www.xuxueli.com/xxl-crawler/)

#### 源码仓库地址

源码仓库地址 | Release Download
--- | ---
[https://github.com/xuxueli/xxl-crawler](https://github.com/xuxueli/xxl-crawler) | [Download](https://github.com/xuxueli/xxl-crawler/releases)
[https://gitee.com/xuxueli0323/xxl-crawler](https://gitee.com/xuxueli0323/xxl-crawler) | [Download](https://gitee.com/xuxueli0323/xxl-crawler/releases)  


#### 技术交流
- [社区交流](http://www.xuxueli.com/page/community.html)

### 1.5 环境
- JDK：1.7+


## 二、快速入门

### 爬虫示例参考  
(爬虫示例代码位于 /test 目录下)
- 1、爬取页面数据并封装VO对象
- 2、爬取页面，下载Html文件
- 3、爬取页面，下载图片文件

### 第一步：引入Maven依赖
```
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-crawler</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 第二步：定义 "PageVo/页面数据对象"（可选）
```java
// PageSelect 注解：从页面中抽取出多个VO对象；
@PageSelect(".body")
public static class PageVo {

    @PageFieldSelect(cssQuery = ".blog-heading .title")
    private String title;

    @PageFieldSelect(cssQuery = "#read")
    private int read;

    @PageFieldSelect(cssQuery = ".comment-content")
    private List<String> comment;

    // set get
}
```

### 第三步：创建爬虫
```java
XxlCrawler crawler = new XxlCrawler.Builder()
    .setUrls(new HashSet<String>(Arrays.asList("https://my.oschina.net/xuxueli/blog")))
    .setWhiteUrlRegexs(new HashSet<String>(Arrays.asList("https://my\\.oschina\\.net/xuxueli/blog/\\d+")))
    .setThreadCount(3)
    .setPageParser(new PageParser<PageVo>() {
        @Override
        public void parse(Document html, PageVo pageVo) {
            // 解析封装 PageVo 对象
            String pageUrl = html.baseUri();
            System.out.println(pageUrl + "：" + pageVo.toString());
        }
    })
    .build();
```

### 第四步：启动爬虫
```java
crawler.start(true);
```


## 三、总体设计
### 3.1 功能定位

XXL-CRAWLER 是一个灵活高效、面向对象的Web爬虫框架。；

借助 XXL-CRAWLER，一行代码开发一个分布式爬虫。

### 3.2 核心概念

概念 | 说明
--- | ---
XxlCrawler | 爬虫对象，维护爬虫信息
PageVo | 页面数据对象，一张Web页面可抽取一个或多个PageVo
PageParser | 页面解析器，绑定泛型PageVO后将会自动抽取页面数据对象

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
setThreadCount | 爬虫并发线程数
setPageParser | 页面解析器

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


## 四、版本更新日志
### 版本 V1.0.0，新特性[2017-09-13]
- 1、面向对象：通过VO对象描述页面信息，提供注解方便的映射页面数据，爬取结果主动封装Java对象返回；
- 2、多线程；
- 3、扩散全站：将会以现有URL为起点扩散爬取整站；
- 4、去重：防止重复爬取；
- 5、URL白名单：支持设置页面白名单正则，过滤URL；
- 6、异步：支持同步、异步两种方式运行；
- 7、自定义请求信息，如：请求参数、Cookie、userAgent等；
- 8、轻量级：底层实现仅依赖jsoup，简洁高效；
- 9、超时控制：支持设置爬虫请求的超时时间；
- 10、主动停顿：爬虫线程处理完页面之后进行主动停顿，避免过于频繁被拦截；
- 11、单个页面支持抽取多个PageVO；

### 版本 V1.1.0，新特性[规划中]
- 1、页面默认cssQuery调整为html标签；
- 2、升级Jsoup至1.11.1版本；
- 3、修复PageVO注解失效的问题；
- 4、属性注解参数attributeKey调整为selectVal；


### TODO LIST
- 1、爬虫超时重试；
- 2、扩展SelectType，比如HasClass等；


## 五、其他

### 5.1 项目贡献
欢迎参与项目贡献！比如提交PR修复一个bug，或者新建 [Issue](https://github.com/xuxueli/xxl-crawler/issues/) 讨论新特性或者变更。

### 5.2 用户接入登记
更多接入的公司，欢迎在 [登记地址](https://github.com/xuxueli/xxl-crawler/issues/1 ) 登记，登记仅仅为了产品推广。

### 5.3 开源协议和版权
产品开源免费，并且将持续提供免费的社区技术支持。个人或企业内部可自由的接入和使用。

- Licensed under the GNU General Public License (GPL) v3.
- Copyright (c) 2015-present, xuxueli.

---
### 捐赠
无论金额多少都足够表达您这份心意，非常感谢 ：）    [XXL系列捐赠记录](http://www.xuxueli.com/page/donate.html )

微信：<img src="https://raw.githubusercontent.com/xuxueli/xxl-job/master/doc/images/donate-wechat.png" width="200">
支付宝：<img src="https://raw.githubusercontent.com/xuxueli/xxl-job/master/doc/images/donate-alipay.jpg" width="200">
