package com.xuxueli.crawler.annotation;

import java.lang.annotation.*;

/**
 * page select
 *
 * @author xuxueli 2017-10-17 20:28:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface PageSelect {

    public String value() default "";

    String datePattern() default "yyyy-MM-dd HH:mm:ss";

}
