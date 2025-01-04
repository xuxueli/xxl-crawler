package com.xxl.crawler.test.util;

import com.xxl.crawler.util.IOUtil;

/**
 * io util test
 *
 * @author xuxueli 2017-11-08 13:33:04
 */
public class IOUtilTest {

    public static void main(String[] args) {
        System.out.println(IOUtil.toString(IOUtil.toInputStream("qwe123阿斯达", null), null));
    }

}
