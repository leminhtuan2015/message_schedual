package com.leminhtuan.myapplication.message_schedual.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.leminhtuan.myapplication.message_schedual.models.MessageSchedual;
import com.leminhtuan.myapplication.message_schedual.receivers.MessageReceiver;

import java.util.Calendar;
import java.util.Date;


public class MessageService {
    public static String MESSAGE = "message";
    public static String SEND_MESSAGE = "send_message";
    private Context context;

    public MessageService(Context context) {
        this.context = context;
    }

    public void sendDelay(MessageSchedual messageSchedual) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = buildIntent(messageSchedual);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(messageSchedual.getId()+""), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerTime = triggerTime(messageSchedual.getDate());

        if(messageSchedual.getDelayMinus() > 0){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, messageSchedual.getDelayMinus() * 1000 * 60, pendingIntent);
        }else{
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }

    public void cancel(MessageSchedual messageSchedual) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = buildIntent(messageSchedual);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(messageSchedual.getId()+""), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        Log.d("MessageSchedual", "Cancled: " + messageSchedual.getId());
    }

    private long triggerTime(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    private Intent buildIntent(MessageSchedual messageSchedual) {
        Intent intent = new Intent(context, MessageReceiver.class);
        intent.putExtra(MESSAGE, messageSchedual);
        intent.setAction(SEND_MESSAGE);
        return intent;
    }
}

