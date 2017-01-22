package com.yz.filter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by zilongye on 17/1/22.
 * 签名过滤器
 */
public class SignFilter implements ParserFilter{

    @Override
    public String parser(String content, EmailContentFilterChain filterChain) {
        Document doc = Jsoup.parse(content);
        //按照签名规则读取
        Element element = doc.select("hr").first();
        //如果父节点就是body，则直接截取
        if (element != null) {
            if (element.parent().tagName().equals("body")) {
                element.before("<clear/>");
            } else {
                for (Element e : element.parents()) {
                    if (e.parent() != null) {
                        if (e.parent().parent() != null && e.parent().parent().tagName().equals("body")) {
                            e.before("<clear/>");
                            break;
                        }
                    }
                }
            }
        }
        Element et = doc.select("clear").first();
        if (et != null) {
            while (et.nextElementSibling() != null) {
                et.nextElementSibling().remove();
            }
            et.remove();
        }
        return filterChain.doParse(doc.outerHtml());
    }
}
