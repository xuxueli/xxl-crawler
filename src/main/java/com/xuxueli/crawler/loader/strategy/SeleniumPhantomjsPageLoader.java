package com.xuxueli.crawler.loader.strategy;

import com.xuxueli.crawler.loader.PageLoader;
import com.xuxueli.crawler.model.PageLoadInfo;
import com.xuxueli.crawler.util.UrlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * "selenisum + phantomjs" page loader
 *
 * @author xuxueli 2018-10-16
 */
public class SeleniumPhantomjsPageLoader extends PageLoader {
    private static Logger logger = LoggerFactory.getLogger(SeleniumPhantomjsPageLoader.class);

    private String driverPath;
    public SeleniumPhantomjsPageLoader(String driverPath) {
        this.driverPath = driverPath;
    }

    @Override
    public Document load(PageLoadInfo pageLoadInfo) {
        if (!UrlUtil.isUrl(pageLoadInfo.getUrl())) {
            return null;
        }

        // driver init
        DesiredCapabilities dcaps = new DesiredCapabilities();
        dcaps.setCapability("acceptSslCerts", pageLoadInfo.isValidateTLSCertificates());        // ssl证书支持
        //dcaps.setCapability("takesScreenshot", false);                        // 截屏支持
        dcaps.setCapability("cssSelectorsEnabled", true);   // css搜索支持
        dcaps.setJavascriptEnabled(true);                                       // js支持
        if (driverPath!=null && driverPath.trim().length()>0) {
            dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, driverPath);    //驱动支持
        }
        WebDriver webDriver = new PhantomJSDriver(dcaps);

        try {

            // TODO, selenium not support feature like : paramMap、headerMap、userAgent、referrer、ifPost、proxy、、、、、、

            // driver run
            webDriver.get(pageLoadInfo.getUrl());

            if (pageLoadInfo.getCookieMap() != null && !pageLoadInfo.getCookieMap().isEmpty()) {
                for (Map.Entry<String, String> item: pageLoadInfo.getCookieMap().entrySet()) {
                    webDriver.manage().addCookie(new Cookie(item.getKey(), item.getValue()));
                }
            }

            webDriver.manage().timeouts().implicitlyWait(pageLoadInfo.getTimeoutMillis(), TimeUnit.MILLISECONDS);
            webDriver.manage().timeouts().pageLoadTimeout(pageLoadInfo.getTimeoutMillis(), TimeUnit.MILLISECONDS);
            webDriver.manage().timeouts().setScriptTimeout(pageLoadInfo.getTimeoutMillis(), TimeUnit.MILLISECONDS);

            String pageSource = webDriver.getPageSource();
            if (pageSource != null) {
                Document html = Jsoup.parse(pageSource);
                return html;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
        return null;
    }

}
