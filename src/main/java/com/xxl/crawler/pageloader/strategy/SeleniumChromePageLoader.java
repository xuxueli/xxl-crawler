package com.xxl.crawler.pageloader.strategy;

import com.xxl.crawler.exception.XxlCrawlerException;
import com.xxl.crawler.pageloader.PageLoader;
import com.xxl.crawler.pageloader.param.Request;
import com.xxl.crawler.util.FileUtil;
import com.xxl.crawler.util.UrlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * "selenisum + chrome" page loader
 *
 * @author xuxueli 2018-10-16
 */
public class SeleniumChromePageLoader extends PageLoader {
    private static Logger logger = LoggerFactory.getLogger(SeleniumChromePageLoader.class);

    private final String driverPath;
    public SeleniumChromePageLoader(String driverPath) {
        this.driverPath = driverPath;
    }

    @Override
    public Document load(Request request) {
        if (!UrlUtil.isUrl(request.getUrl())) {
            return null;
        }

        // init driver
        if (!FileUtil.exists(driverPath)) {
            throw new XxlCrawlerException("webdriver.chrome.driver not found.");
        }
        System.setProperty("webdriver.chrome.driver", driverPath);

        // ChromeOptions
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        // set user-agent
        if (request.getUserAgent() != null) {
            chromeOptions.addArguments("--user-agent=" + request.getUserAgent());
        } else {
            chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
        }
        // set tls
        if (request.isValidateTLSCertificates()) {
            chromeOptions.addArguments("--ignore-certificate-errors");
        }
        // set Proxy
        if (request.getProxy() != null) {
            chromeOptions.setCapability("proxy", request.getProxy());
        }

        // webDriver
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        // set referrer
        if (request.getReferrer() != null && !request.getReferrer().isEmpty()) {
            ((JavascriptExecutor) webDriver).executeScript("document.referrer = '"+ request.getReferrer() + "';");
        }

        try {
            // set Cookie
            if (request.getCookieMap()!=null&&!request.getCookieMap().isEmpty()) {
                for (Map.Entry<String, String> item: request.getCookieMap().entrySet()) {
                    Cookie cookie = new Cookie.Builder(item.getKey(), item.getValue())
                            .domain(request.getUrl())
                            .path("/")
                            .expiresOn(new Date(System.currentTimeMillis() + 2 * 3600 * 1000)) // 2 hour from now
                            .isSecure(false)
                            .isHttpOnly(true)
                            .build();
                    webDriver.manage().addCookie(cookie);
                }
                // refresh to ennable cookie
                webDriver.navigate().refresh();
            }

            // driver run
            webDriver.get(request.getUrl());


            webDriver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(request.getTimeoutMillis()));
            webDriver.manage().timeouts().scriptTimeout(Duration.ofMillis(request.getTimeoutMillis()));
            webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(request.getTimeoutMillis()));

            String pageSource = webDriver.getPageSource();
            if (pageSource != null) {
                Document html = Jsoup.parse(pageSource);
                return html;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            webDriver.quit();
        }
        return null;
    }

}
