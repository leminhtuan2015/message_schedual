package com.leminhtuan.myapplication.message_schedual.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import com.leminhtuan.myapplication.message_schedual.daos.MessageSchedualDAO;
import com.leminhtuan.myapplication.message_schedual.events.MessageSentEvent;
import com.leminhtuan.myapplication.message_schedual.models.Contact;
import com.leminhtuan.myapplication.message_schedual.models.MessageSchedual;
import com.leminhtuan.myapplication.message_schedual.services.MessageService;

import org.greenrobot.eventbus.EventBus;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceive...", "onReceive-------------------------------" + context);
        if (intent.getAction().equals(MessageService.SEND_MESSAGE)) {
            MessageSchedual messageSchedual = (MessageSchedual) intent.getExtras().get(MessageService.MESSAGE);

            if (messageSchedual != null) {
                sendSMS(context, messageSchedual);
            } else {
                Log.d("MessageSchedual", "null ");
            }
        }
    }

    private void sendSMS(Context context, MessageSchedual messageSchedual) {
        MessageSchedual messageSchedualDB = new MessageSchedualDAO(context).get(messageSchedual.getId());

        try {
            SmsManager smsManager = SmsManager.getDefault();
            for (Contact contact: messageSchedualDB.getContacts()){
//                smsManager.sendTextMessage(contact.getNumber(), null, messageSchedualDB.getText(), null, null);
                messageSchedualDB.setSentNumber(messageSchedualDB.getSentNumber() + 1);
                Log.d("Sent", messageSchedualDB.getId() + "");
            }
        } catch(Exception e) {
            messageSchedualDB.setFailNumber(messageSchedualDB.getFailNumber() + 1);
            Log.d("Fell", messageSchedualDB.getId() + "");
        }

        if(messageSchedualDB.getRepeatNumber() == (messageSchedualDB.getSentNumber() + messageSchedualDB.getFailNumber())){
            new MessageService(context).cancel(messageSchedual);
        }

        MessageSchedual messageSchedualSent = new MessageSchedualDAO(context).update(messageSchedualDB);
        EventBus.getDefault().post(new MessageSentEvent(messageSchedualSent));
    }
}