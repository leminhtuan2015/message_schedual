package com.leminhtuan.myapplication.message_schedual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.leminhtuan.myapplication.R;
import com.leminhtuan.myapplication.message_schedual.adapters.SuggesstionContactAdapter;
import com.leminhtuan.myapplication.message_schedual.daos.MessageSchedualDAO;
import com.leminhtuan.myapplication.message_schedual.events.DatePickedEvent;
import com.leminhtuan.myapplication.message_schedual.events.NumberPickedEvent;
import com.leminhtuan.myapplication.message_schedual.events.TimePickedEvent;
import com.leminhtuan.myapplication.message_schedual.fragments.DatePickerFragment;
import com.leminhtuan.myapplication.message_schedual.fragments.NumberPickerFragment;
import com.leminhtuan.myapplication.message_schedual.fragments.TimePickerFragment;
import com.leminhtuan.myapplication.message_schedual.models.Contact;
import com.leminhtuan.myapplication.message_schedual.models.MessageSchedual;
import com.leminhtuan.myapplication.message_schedual.services.ContactService;
import com.leminhtuan.myapplication.message_schedual.utils.HardCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class CreateActivity extends AppCompatActivity {
    private MultiAutoCompleteTextView textViewPhoneNumber;
    private TextView textViewInput;
    private Button buttonTime;
    private Button buttonDate;
    private Button buttonRepeatNumber;
    private Button buttonDelayMinus;
    private Calendar calendar = Calendar.getInstance();
    private HashSet<Contact> contactsSelected = new HashSet<Contact>();
    private int repeatNumber = 0;
    private int delayMinus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Back");
        getViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_create_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.create_done:
                returnResult();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showTimePickerDialog() {
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void showDatePickerDialog() {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showNumberPickerDialog(String tag) {
        DialogFragment numberPickerFragment = new NumberPickerFragment();
        numberPickerFragment.show(getSupportFragmentManager(), tag);
    }

    private void getViews() {
        textViewPhoneNumber = (MultiAutoCompleteTextView) findViewById(R.id.suggesstion_contact_view);
        textViewInput = (TextView) findViewById(R.id.input_text);
        buttonDate = (Button) findViewById(R.id.pick_date);
        buttonTime = (Button) findViewById(R.id.pick_time);
        buttonRepeatNumber = (Button) findViewById(R.id.repeat_number);
        buttonDelayMinus = (Button) findViewById(R.id.delay_minus);
        if(delayMinus > 0){
            buttonRepeatNumber.setEnabled(true);
        } else  {
            repeatNumber = 0;
            buttonRepeatNumber.setEnabled(false);
        }


        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        buttonRepeatNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
;               showNumberPickerDialog(HardCode.numberPickerRepeatNumber);
            }
        });

        buttonDelayMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog(HardCode.numberPickerDelayMinus);
            }
        });

        List<Contact> contacts = ContactService.getContacts(CreateActivity.this);
        SuggesstionContactAdapter adapter = new SuggesstionContactAdapter(this, R.layout.contact_suggestion, contacts);
        textViewPhoneNumber.setAdapter(adapter);
        textViewPhoneNumber.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        textViewPhoneNumber.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = (Contact) parent.getItemAtPosition(position);
                contactsSelected.add(contact);
            }
        });
    }

    @Subscribe
    public void onEvent(TimePickedEvent event) {
        buttonTime.setText(event.getHourOfDay() +" : "+ event.getMinute());
        calendar.set(Calendar.HOUR_OF_DAY, event.getHourOfDay());
        calendar.set(Calendar.MINUTE, event.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    @Subscribe
    public void onEvent(DatePickedEvent event) {
        buttonDate.setText(event.getDay() + ":" + (event.getMonth() + 1) + ":" + event.getYear());
        calendar.set(event.getYear(), event.getMonth(), event.getDay());
    }

    @Subscribe
    public void onEvent(NumberPickedEvent event) {
        if(event.getTag().equals(HardCode.numberPickerDelayMinus)){
            buttonDelayMinus.setText("Delay minus: " + event.getNumber());
            delayMinus = event.getNumber();
            if(delayMinus > 0){
                buttonRepeatNumber.setEnabled(true);
            } else {
                repeatNumber = 0;
                buttonRepeatNumber.setEnabled(false);
            }

        }else if(event.getTag().equals(HardCode.numberPickerRepeatNumber)){
            buttonRepeatNumber.setText(" Repeat time:" + event.getNumber());
            repeatNumber = event.getNumber();
        }
    }

    private void returnResult() {
        String text = textViewInput.getText().toString().trim();

        if(contactsSelected.isEmpty() || text.equals("") || (repeatNumber > 1 && delayMinus == 0)){
            Toast.makeText(this, R.string.insert_data, Toast.LENGTH_LONG).show();
            Snackbar.make(this.getCurrentFocus(), R.string.insert_data, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }else {
            Intent intent = new Intent();
            MessageSchedual messageSchedual = new MessageSchedual(contactsSelected, text, calendar.getTime(), HardCode.PENDING, repeatNumber, delayMinus, 0, 0);
            MessageSchedual messageSchedualSaved = new MessageSchedualDAO(this).create(messageSchedual);
            intent.putExtra("message", messageSchedualSaved);
            setResult(RESULT_OK, intent);
            this.finish();
        }
    }
}
