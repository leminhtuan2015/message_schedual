package com.leminhtuan.myapplication.message_schedual.events;

/**
 * Created by leminhtuan on 3/15/16.
 */
public class DatePickedEvent {
    private int year;
    private int month;
    private int day;

    public DatePickedEvent(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DatePickedEvent() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
