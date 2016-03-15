package com.leminhtuan.myapplication.message_schedual.events;

/**
 * Created by leminhtuan on 3/15/16.
 */
public class TimePickedEvent {
    private int hourOfDay;
    private int minute;

    public TimePickedEvent(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    public TimePickedEvent() {
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
