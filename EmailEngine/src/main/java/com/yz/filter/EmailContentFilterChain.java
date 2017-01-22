package com.yz.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zilongye on 17/1/22.
 */
public class EmailContentFilterChain {

    private List<ParserFilter> filters = new ArrayList<ParserFilter>();
    private int index = 0;

    public EmailContentFilterChain addFilter(ParserFilter filter){
        filters.add(filter);
        return this;
    }

    public String doParse(String content){
        if(index == filters.size()) return content;
        //得到当前过滤器
        ParserFilter f = filters.get(index);
        index++;
        return f.parser(content, this);
    }

}
