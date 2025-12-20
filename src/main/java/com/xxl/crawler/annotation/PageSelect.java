package com.xxl.crawler.annotation;

import java.lang.annotation.*;

/**
 * page vo annotation
 *
 * 页面数据对象 注解
 *
 * @author xuxueli 2017-10-17 20:28:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PageSelect {

    /**
     * css selector of page element
     *
     * CSS选择器语法，参考Jsoup - Selector文档：https://jsoup.org/apidocs/org/jsoup/select/Selector.html
     * <pre>
     *     cssQuery="*"                 // any element
     *     cssQuery=".class"            // elements with a class name of "class"
     *     cssQuery="#id"               // element with an ID of "id"
     *     cssQuery="body"              // body element
     * </pre>
     *
     * @return String
     */
    public String cssQuery() default "";

}
