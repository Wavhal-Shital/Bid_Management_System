package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Email {

     @Id
     @GeneratedValue
    private Long id;
    private String messageId;
    private String threadId;
    private String subject;
    private String sender;
    private String recipients;
    private String cc;
    private String bcc;
    @Column(columnDefinition="TEXT")
    private String body;
    private LocalDateTime receivedTime;
    @Column(columnDefinition="jsonb")
    private String headers;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public String getThreadId() {
        return threadId;
    }
    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getRecipients() {
        return recipients;
    }
    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }
    public String getCc() {
        return cc;
    }
    public void setCc(String cc) {
        this.cc = cc;
    }
    public String getBcc() {
        return bcc;
    }
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public LocalDateTime getReceivedTime() {
        return receivedTime;
    }
    public void setReceivedTime(LocalDateTime receivedTime) {
        this.receivedTime = receivedTime;
    }
    public String getHeaders() {
        return headers;
    }
    public void setHeaders(String headers) {
        this.headers = headers;
    }
    
}
