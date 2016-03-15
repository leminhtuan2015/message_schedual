package com.leminhtuan.myapplication.message_schedual.events;

/**
 * Created by leminhtuan on 3/18/16.
 */
public class NumberPickedEvent {

    private int number;
    private String tag = "";

    public NumberPickedEvent(int number) {
        this.number = number;
    }

    public NumberPickedEvent(int number, String tag) {
        this.number = number;
        this.tag = tag;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
