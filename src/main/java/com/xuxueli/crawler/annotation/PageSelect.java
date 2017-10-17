package com.xuxueli.crawler.annotation;

import java.lang.annotation.*;

/**
 * page select
 * 页面VO对象注解
 *
 *      1、一个页面可能对应多个VO对象；
 *
 * @author xuxueli 2017-10-17 20:28:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PageSelect {

    /**
     * css选择器字符串，对应 "页面VO对象"
     *
     * @return
     */
    public String value() default "";

}
