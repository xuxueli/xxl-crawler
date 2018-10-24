package com.xuxueli.crawler.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * io util
 *
 * @author xuxueli 2017-11-08 13:22:58
 */
public class IOUtil {
    private static Logger logger = LoggerFactory.getLogger(IOUtil.class);

    /**
     * String 2 InputStream
     *
     * @param str
     * @return InputStream
     */
    public static InputStream toInputStream(String str, String encoding) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(str.getBytes((encoding!=null)?encoding:"UTF-8"));
            return inputStream;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * InputStream 2 String
     *
     * @param inputStream
     * @return String
     * @throws IOException
     */
    public static String toString(InputStream inputStream, String encoding){
        try {
            StringBuffer  stringBuffer  =  new  StringBuffer();
            InputStreamReader inread = new InputStreamReader(inputStream, (encoding!=null)?encoding:"UTF-8");

            char[] b = new char[2048];
            for  (int n; (n = inread.read(b)) != -1;)  {
                stringBuffer.append(new  String(b,  0,  n));
            }

            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
