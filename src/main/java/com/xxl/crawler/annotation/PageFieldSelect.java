package com.xxl.crawler.annotation;

import com.xxl.crawler.constant.Const;

import java.lang.annotation.*;

/**
 * page vo field annotation
 *
 * 页面数据对象的属性信息 （支持基础数据类型 T ，包括 List<T>）
 *
 * @author xuxueli 2017-10-17 20:28:11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface PageFieldSelect {

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

    /**
     * jquery data-extraction-type，like ".html()/.text()/.val()/.attr() ..."
     *
     * <pre>
     *     selectType = SelectType.HTML                             // html of element
     *     selectType = SelectType.VAL                              // val of element
     *     selectType = SelectType.TEXT                             // text of element
     *     selectType = SelectType.TOSTRING                         // toString of element
     *     selectType = SelectType.ATTR, selectVal="href"           // attr of element
     *     selectType = SelectType.HAS_CLASS, selectVal="abs:src"   // hasClass of element
     * </pre>
     *
     * @see Const.SelectType
     *
     * @return SelectType
     */
    public Const.SelectType selectType() default Const.SelectType.TEXT;

    /**
     * jquery data-extraction-value
     *
     *  1、effect when SelectType=ATTR/HAS_CLASS, like ".attr("abs:src")"
     *
     * @return String
     */
    public String selectVal() default "";

    /**
     * data patttern, valid when date data
     *
     *  1、时间格式化，日期类型数据有效
     *
     * @return String
     */
    String datePattern() default "yyyy-MM-dd HH:mm:ss";

}
