package com.leminhtuan.myapplication.message_schedual.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.leminhtuan.myapplication.R;
import com.leminhtuan.myapplication.message_schedual.models.Contact;
import com.leminhtuan.myapplication.message_schedual.models.MessageSchedual;
import com.leminhtuan.myapplication.message_schedual.utils.DateConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leminhtuan on 3/15/16.
 */
public class ListMessageAdapter extends ArrayAdapter<MessageSchedual> {

    public ListMessageAdapter(Context context, int resource, List<MessageSchedual> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.message_row, null);
        }

        MessageSchedual messageSchedual = getItem(position);

        if (messageSchedual != null) {
            TextView time = (TextView) v.findViewById(R.id.text_view_time);
            TextView text = (TextView) v.findViewById(R.id.text_view_text);
            TextView number = (TextView) v.findViewById(R.id.text_view_number);
            TextView delayMinus = (TextView) v.findViewById(R.id.text_view_delay_minus);
            TextView status = (TextView) v.findViewById(R.id.text_view_status);

            if (time != null) {
                time.setText("" + DateConverter.parse(messageSchedual.getDate()));
            }

            if (text != null) {
                text.setText("Text : " + messageSchedual.getText());
            }

            if (number != null) {
                List<String> contactInfo = new ArrayList<String >();
                for(Contact contact: messageSchedual.getContacts()){
                    contactInfo.add(contact.getName() + "<" + contact.getNumber() + ">");
                }
                number.setText("To : " + TextUtils.join("; ", contactInfo));
            }

            if (status != null) {
                int sentNumber = messageSchedual.getSentNumber();
                int failNumber = messageSchedual.getFailNumber();
                int repeatNumber = messageSchedual.getRepeatNumber();
                int pendingNumber = repeatNumber - sentNumber - failNumber;
                String statusView = "";

                if(repeatNumber == 0)
                    statusView = " Sent: " + sentNumber;
                else
                    statusView = "Repeat time: " + repeatNumber + " Pending: " + pendingNumber + " Sent: " + sentNumber + " Fail :" + failNumber;

                status.setText("" + statusView);
            }

            if(delayMinus != null){
                delayMinus.setText("Repeat minus: " +messageSchedual.getDelayMinus());
            }
        }

        return v;
    }
}
