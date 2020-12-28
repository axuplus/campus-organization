package com.sms.service.utils;

public class SmsDto {
    private String token;
    private String action;
    private String className;
    private String schoolName;
    private String content;
    private Long sendTime;
    private String senderName;
    private String studentName;
    private String sourceMobile;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }



    public String getSchoolName() {
        return schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSourceMobile() {
        return sourceMobile;
    }

    public void setSourceMobile(String sourceMobile) {
        this.sourceMobile = sourceMobile;
    }
}
