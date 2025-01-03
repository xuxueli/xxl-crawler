package com.xuxueli.crawler.loader.strategy;

import com.xuxueli.crawler.loader.PageLoader;
import com.xuxueli.crawler.model.PageRequest;
import com.xuxueli.crawler.util.UrlUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * "selenisum + chrome" page loader
 *
 * // TODO, selenium not support feature like : paramMap、headerMap、ifPost
 *
 * @author xuxueli 2018-10-16
 */
public class SeleniumChromePageLoader extends PageLoader {
    private static Logger logger = LoggerFactory.getLogger(SeleniumChromePageLoader.class);

    private String driverPath;
    public SeleniumChromePageLoader(String driverPath) {
        this.driverPath = driverPath;
        // init driver
        System.setProperty("webdriver.chrome.driver", driverPath);
    }

    @Override
    public Document load(PageRequest pageRequest) {
        if (!UrlUtil.isUrl(pageRequest.getUrl())) {
            return null;
        }

        // init chromeOptions
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        // set user-agent
        if (pageRequest.getUserAgent() != null) {
            chromeOptions.addArguments("--user-agent=" + pageRequest.getUserAgent());
        } else {
            chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
        }

        /*dcaps.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, !pageRequest.isValidateTLSCertificates());

        if (pageRequest.getProxy() != null) {
            dcaps.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);   // Deprecated
            dcaps.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
            System.setProperty("http.nonProxyHosts", "localhost");
            dcaps.setCapability(CapabilityType.PROXY, pageRequest.getProxy());
        }*/

        // webDriver
        WebDriver webDriver = new ChromeDriver(chromeOptions);

        // set referrer
        if (pageRequest.getReferrer() != null && !pageRequest.getReferrer().isEmpty()) {
            ((JavascriptExecutor) webDriver).executeScript("document.referrer = '"+ pageRequest.getReferrer() + "';");
        }


        // set Cookie
        if (pageRequest.getCookieMap()!=null&&!pageRequest.getCookieMap().isEmpty()) {
            for (Map.Entry<String, String> item: pageRequest.getCookieMap().entrySet()) {
                Cookie cookie = new Cookie.Builder(item.getKey(), item.getValue())
                        .domain(pageRequest.getUrl())
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

        try {
            // driver run
            webDriver.get(pageRequest.getUrl());

            if (pageRequest.getCookieMap() != null && !pageRequest.getCookieMap().isEmpty()) {
                for (Map.Entry<String, String> item: pageRequest.getCookieMap().entrySet()) {
                    webDriver.manage().addCookie(new Cookie(item.getKey(), item.getValue()));
                }
            }

            webDriver.manage().timeouts().implicitlyWait(pageRequest.getTimeoutMillis(), TimeUnit.MILLISECONDS);
            webDriver.manage().timeouts().pageLoadTimeout(pageRequest.getTimeoutMillis(), TimeUnit.MILLISECONDS);
            webDriver.manage().timeouts().setScriptTimeout(pageRequest.getTimeoutMillis(), TimeUnit.MILLISECONDS);

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
