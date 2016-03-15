package com.leminhtuan.myapplication.message_schedual.events;

import com.leminhtuan.myapplication.message_schedual.models.MessageSchedual;

/**
 * Created by leminhtuan on 3/19/16.
 */
public class MessageSentEvent {

    private MessageSchedual messageSchedual;

    public MessageSentEvent(MessageSchedual messageSchedual) {
        this.messageSchedual = messageSchedual;
    }

    public MessageSchedual getMessageSchedual() {
        return messageSchedual;
    }

    public void setMessageSchedual(MessageSchedual messageSchedual) {
        this.messageSchedual = messageSchedual;
    }
}
