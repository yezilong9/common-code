package com.yz.config;

import java.util.List;

/**
 * Created by zilongye on 17/1/22.
 */
public class EmailMessage {

    private List<String> sendTo;
    private String subject;
    private String content;
    private List<String> files;
    private List<String> cc;


    public List<String> getSendTo() {
        return sendTo;
    }

    public void setSendTo(List<String> sendTo) {
        this.sendTo = sendTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }
}
