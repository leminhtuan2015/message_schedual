package com.leminhtuan.myapplication.message_schedual.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by leminhtuan on 3/15/16.
 */
public class MessageSchedual implements Serializable{
    private long id;
    private HashSet<Contact> contacts;
    private String text;
    private Date date;
    private int status;
    private int repeatNumber;
    private int delayMinus;
    private int sentNumber;
    private int failNumber;

    public MessageSchedual(long id, HashSet<Contact> contacts, String text, Date date, int status, int repeatNumber, int delayMinus, int sentNumber, int failNumber) {
        this.id = id;
        this.contacts = contacts;
        this.text = text;
        this.date = date;
        this.status = status;
        this.repeatNumber = repeatNumber;
        this.delayMinus = delayMinus;
        this.sentNumber = sentNumber;
        this.failNumber = failNumber;
    }

    public MessageSchedual(HashSet<Contact> contacts, String text, Date date, int status, int repeatNumber, int delayMinus, int sentNumber, int failNumber) {
        this.contacts = contacts;
        this.text = text;
        this.date = date;
        this.status = status;
        this.repeatNumber = repeatNumber;
        this.delayMinus = delayMinus;
        this.sentNumber = sentNumber;
        this.failNumber = failNumber;
    }

    public HashSet<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(HashSet<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRepeatNumber() {
        return repeatNumber;
    }

    public void setRepeatNumber(int repeatNumber) {
        this.repeatNumber = repeatNumber;
    }

    public int getDelayMinus() {
        return delayMinus;
    }

    public void setDelayMinus(int delayMinus) {
        this.delayMinus = delayMinus;
    }

    public int getSentNumber() {
        return sentNumber;
    }

    public void setSentNumber(int sentNumber) {
        this.sentNumber = sentNumber;
    }

    public int getFailNumber() {
        return failNumber;
    }

    public void setFailNumber(int failNumber) {
        this.failNumber = failNumber;
    }
}
