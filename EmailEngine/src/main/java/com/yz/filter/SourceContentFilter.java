package com.yz.filter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by zilongye on 17/1/22.
 * 原邮件内容过滤
 */
public class SourceContentFilter implements ParserFilter{
    @Override
    public String parser(String content, EmailContentFilterChain filterChain) {
        Document doc = Jsoup.parse(content);
        //删除引用块(80%的邮件的原始邮件都有引用快)
        doc.select("blockquote").remove();
        doc.select("includetail").remove();
        //删除所有img标签
        doc.select("img").remove();
        //按照发件人文字出现第一个位置截取
        if (doc.body().outerHtml().contains("发件人") && doc.body().outerHtml().contains("发送时间")) {
            Elements elements = doc.body().children().size() == 1 ? doc.body().children().get(0).children() :
                    doc.body().children();
            for (Element element : elements) {
                if (element.outerHtml().contains("发件人") && element.outerHtml().contains("发送时间")) {
                    element.before("<clear/>");
                    break;
                }
            }
        }
        return filterChain.doParse(doc.outerHtml());
    }
}
