package com.xuxueli.crawler.annotation;

import java.lang.annotation.*;

/**
 * page field select
 * 页面VO对象的属性信息
 *
 * @author xuxueli 2017-10-17 20:28:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface PageFieldSelect {

    /**
     * css query string, like "#title"
     * css选择器，对应 "页面VO对象的属性" , 如 "#title"
     *
     * @return
     */
    public String value() default "";

    /**
     * css query value type, like "html、val、text（default）"
     * css选择器返回的数据类型，如 "html、val、text（default）"
     *
     * @return
     */
    public String valType() default "text";

    /**
     * data patttern
     * 时间格式化，日期类型数据有效
     *
     * @return
     */
    String datePattern() default "yyyy-MM-dd HH:mm:ss";

}
