## 面向对象的分布式爬虫框架 xxl-crawler

>An object-oriented crawler framework.

## 特性
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

### 快速入门

#### 第一步：定义 "页面对象VO"
> 可参考测试代码：com.xuxueli.crawler.test.XxlCrawlerTest
```java
// PageSelect 注解：从页面中抽取出多个VO对象；
@PageSelect(".body")
    public static class PageVo {

        @PageFieldSelect(value = ".blog-heading .title")
        private String title;

        @PageFieldSelect("#read")
        private int read;

        @PageFieldSelect(".comment-content")
        private List<String> comment;

        // set get
    }
```

#### 第二步：创建爬虫对象
```java
XxlCrawler crawler = new XxlCrawler.Builder()
        .setUrls(new HashSet<String>(Arrays.asList("https://my.oschina.net/xuxueli/blog")))
        .setWhiteUrlRegexs(new HashSet<String>(Arrays.asList("https://my\\.oschina\\.net/xuxueli/blog/\\d+")))
        .setThreadCount(3)
        .setPageParser(new PageParser<PageVo>() {
            @Override
            public void parse(String url, Document html, PageVo pageVo) {
                // 解析封装 PageVo 对象
                System.out.println(url + "：" + pageVo.toString());
            }
        })
        .build();

crawler.start(true);
```

#### 第三步：启动爬虫
```java
crawler.start(true);
```


#### 第四步：停止爬虫
```java
crawler.stop(true);
```

#### 其他：爬虫参考示例
- 1、爬取页面数据并封装VO对象
- 2、爬取页面，下载Html文件
- 3、爬取页面，下载图片文件

(上述示例代码位于 /test 目录下)


## TODO
- 1、超时重试机制；



