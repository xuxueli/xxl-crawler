package com.xuxueli.crawler.annotation;

import java.lang.annotation.*;

/**
 * page select
 *
 * @author xuxueli 2017-10-17 20:28:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface PageFieldSelect {

    /**
     * css query string, like "#title"
     *
     * @return
     */
    public String value() default "";

    /**
     * value type, like "html、val、text（default）"
     *
     * @return
     */
    public String valType() default "text";

    /**
     * data patttern
     *
     * @return
     */
    String datePattern() default "yyyy-MM-dd HH:mm:ss";

}
