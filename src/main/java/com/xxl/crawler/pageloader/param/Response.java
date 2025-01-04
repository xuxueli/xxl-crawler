package com.xxl.crawler.pageloader.param;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

/**
 * response
 *
 * @author xuxueli 2024-01-04
 * @param <T>
 */
public class Response<T> {

    private Request request;                    // load request
    private boolean success;                     // load result

    private Document html;                      // load resule
    private List<Element> parseElementList;     // parse result list, element object
    private List<T> parseVoList;                // parse result list, vo object

    public Response() {
    }
    public Response(Request request, boolean success, Document html, List<Element> parseElementList, List<T> parseVoList) {
        this.request = request;
        this.success = success;
        this.html = html;
        this.parseElementList = parseElementList;
        this.parseVoList = parseVoList;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Document getHtml() {
        return html;
    }

    public void setHtml(Document html) {
        this.html = html;
    }

    public List<Element> getParseElementList() {
        return parseElementList;
    }

    public void setParseElementList(List<Element> parseElementList) {
        this.parseElementList = parseElementList;
    }

    public List<T> getParseVoList() {
        return parseVoList;
    }

    public void setParseVoList(List<T> parseVoList) {
        this.parseVoList = parseVoList;
    }

}
