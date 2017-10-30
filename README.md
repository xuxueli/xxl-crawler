## 面向对象的分布式爬虫框架 xxl-crawler

>An object-oriented crawler framework.

## 特性
- 1、面向对象：通过VO对象描述页面信息，提供注解方便的映射页面数据，爬取结果主动封装Java对象返回；
- 2、多线程；
- 3、扩散全站：将会以现有URL为起点扩散爬取整站；
- 4、去重：防止重复爬取；
- 5、URL白名单：支持设置页面白名单正则，过滤URL；
- 6、同步运行、异步运行；
- 7、自定义请求信息，如：请求参数、Cookie、userAgent等；
- 8、轻量级：底层实现仅依赖jsoup，简洁高效；

### 快速入门


#### 第一步：定义 "页面对象VO"
> 可参考测试代码：com.xuxueli.crawler.test.XxlCrawlerTest
```java
// PageSelect 注解：从页面中抽取出多个VO对象；
@PageSelect(".body")
public static class PageVo {

    // PageFieldSelect 注解：从页面中，为每个VO对象抽取出属性数据 
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
    // 设置入口URL
    .setUrls(new HashSet<String>(Arrays.asList("https://my.oschina.net/xuxueli/blog")))
    // 设置爬取页面地址白名单正则
    .setWhiteUrlRegexs(new HashSet<String>(Arrays.asList("https://my\\.oschina\\.net/xuxueli/blog/\\d+")))
    // 设置爬虫并发线程
    .setThreadCount(5)
    // 设置页面数据解析器，通过解析器绑定 "页面对象VO"，爬虫将会自动封装VO对象数据，方便操作
    .setPageParser(new PageParser<PageVo>() {
        @Override
        public void parse(String url, Document html, PageVo pageVo) {
            System.out.println("-------------------");
            System.out.println(url);
            System.out.println(pageVo.toString());
        }
    })
    .build();
```

#### 第三步：启动爬虫
```java
crawler.start();
```


#### 第四步：停止爬虫
```java
crawler.stop(true);
```

## TODO

