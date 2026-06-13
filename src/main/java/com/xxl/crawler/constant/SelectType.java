package com.xxl.crawler.constant;

/**
 * Clawer Select Type (数据抽取方式)
 */
public enum SelectType {
    // .html()
    HTML,
    // .val()
    VAL,
    // .text()
    TEXT,
    // .toString()
    TOSTRING,
    // .attr("attributeKey")
    ATTR,
    // .hasClass("className")
    HAS_CLASS;
}