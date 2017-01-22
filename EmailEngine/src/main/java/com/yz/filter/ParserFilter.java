package com.yz.filter;

/**
 * Created by zilongye on 17/1/22.
 *
 *  邮件过滤器。因为各个邮箱供应商规范不同，造成邮件格式不规范，邮件的个人签名，
 *  回复的原邮件内容都会当成邮件内容，所以造成读取邮件正文内容会有偏差。所以用这个类去过滤不需要的东西
 */
public interface ParserFilter {

    String parser(String content,EmailContentFilterChain filterChain);
}
