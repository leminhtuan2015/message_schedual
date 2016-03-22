package com.leminhtuan.myapplication.message_schedual.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.ex.chips.BaseRecipientAdapter;
import com.android.ex.chips.RecipientEditTextView;
import com.android.ex.chips.recipientchip.DrawableRecipientChip;
import com.leminhtuan.myapplication.R;
import com.leminhtuan.myapplication.message_schedual.daos.MessageSchedualDAO;
import com.leminhtuan.myapplication.message_schedual.events.DatePickedEvent;
import com.leminhtuan.myapplication.message_schedual.events.NumberPickedEvent;
import com.leminhtuan.myapplication.message_schedual.events.TimePickedEvent;
import com.leminhtuan.myapplication.message_schedual.fragments.DatePickerFragment;
import com.leminhtuan.myapplication.message_schedual.fragments.NumberPickerFragment;
import com.leminhtuan.myapplication.message_schedual.fragments.TimePickerFragment;
import com.leminhtuan.myapplication.message_schedual.models.Contact;
import com.leminhtuan.myapplication.message_schedual.models.MessageSchedual;
import com.leminhtuan.myapplication.message_schedual.utils.HardCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.HashSet;

public class CreateActivity extends AppCompatActivity {
//    private MultiAutoCompleteTextView suggesstion_contact;
    private RecipientEditTextView suggesstion_contact_lib;
    private TextView input_text;
    private Button spinner_date;
    private Button spinner_time;
    private Spinner spinner_repeat_at;
    private Button spinner_repeat_time;
    private Switch switch_repeat_time;
    private Switch switch_repeat_at;
    private TextView text_repeat_time;
    private TextView text_spinner_repeat_at;
    private Calendar calendar = Calendar.getInstance();

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
//        suggesstion_contact = (MultiAutoCompleteTextView) findViewById(R.id.suggesstion_contact);
        input_text = (TextView) findViewById(R.id.input_text);
        spinner_date = (Button) findViewById(R.id.spinner_date);
        spinner_time = (Button) findViewById(R.id.spinner_time);
        spinner_repeat_at = (Spinner) findViewById(R.id.spinner_repeat_at);
        spinner_repeat_time = (Button) findViewById(R.id.spinner_repeat_time);
        switch_repeat_at = (Switch) findViewById(R.id.switch_repeat_at);
        switch_repeat_time = (Switch) findViewById(R.id.switch_repeat_time);
        text_repeat_time = (TextView) findViewById(R.id.text_repeat_time);
        text_spinner_repeat_at = (TextView) findViewById(R.id.text_spinner_repeat_at);

        visible(switch_repeat_at.isChecked());

        spinner_repeat_time.setText(HardCode.VALUE_0);
        spinner_repeat_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumberPickerDialog(HardCode.REPEAT);
            }
        });
        spinner_repeat_time.setEnabled(switch_repeat_time.isChecked());

        switch_repeat_time.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinner_repeat_time.setEnabled(isChecked);
                if (isChecked)
                    spinner_repeat_time.setText(HardCode.VALUE_1);
                else
                    spinner_repeat_time.setText(HardCode.VALUE_0);
            }
        });

        switch_repeat_at.setSelected(false);
        switch_repeat_at.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spinner_repeat_at.setEnabled(isChecked);
                visible(isChecked);
            }
        });

        input_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!input_text.getText().toString().isEmpty()) {
                    input_text.setError(null);
                } else {
                    input_text.setError(HardCode.REQUIRED);
                }
            }
        });

        setDate(Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR));
        spinner_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        setTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE));
        spinner_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this, R.layout.spinner_repeat_at, HardCode.Time_CATEGORY);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_repeat_at.setAdapter(adapter_state);
        spinner_repeat_at.setEnabled(switch_repeat_at.isChecked());

//        List<Contact> contacts = ContactService.getContacts(CreateActivity.this);
//        SuggesstionContactAdapter adapter = new SuggesstionContactAdapter(this, R.layout.contact_suggestion, contacts);
//        suggesstion_contact.setAdapter(adapter);
//        suggesstion_contact.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
//        suggesstion_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Contact contact = (Contact) parent.getItemAtPosition(position);
//                contactsSelected.add(contact);
//            }
//        });
//        suggesstion_contact.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!suggesstion_contact.getText().toString().isEmpty()) {
//                    suggesstion_contact.setError(null);
//                } else {
//                    suggesstion_contact.setError(HardCode.REQUIRED);
//                }
//            }
//        });

        suggesstion_contact_lib = (RecipientEditTextView) findViewById(R.id.suggesstion_contact_lib);
        suggesstion_contact_lib.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        suggesstion_contact_lib.setAdapter(new BaseRecipientAdapter(BaseRecipientAdapter.QUERY_TYPE_PHONE, this));
        suggesstion_contact_lib.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!suggesstion_contact_lib.getText().toString().isEmpty()) {
                    suggesstion_contact_lib.setError(null);
                } else {
                    suggesstion_contact_lib.setError(HardCode.REQUIRED);
                }
            }
        });
    }

    @Subscribe
    public void onEvent(TimePickedEvent event) {
        setTime(event.getHourOfDay(), event.getMinute());
        calendar.set(Calendar.HOUR_OF_DAY, event.getHourOfDay());
        calendar.set(Calendar.MINUTE, event.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private void setTime(int hourOfDay, int minus) {
        String hourOfDay1 = String.format("%02d", (hourOfDay));
        String minus1 = String.format("%02d", (minus));
        spinner_time.setText(hourOfDay1 +" : "+ minus1);
    }

    @Subscribe
    public void onEvent(DatePickedEvent event) {
        setDate(event.getDay(), event.getMonth(), event.getYear());
        calendar.set(event.getYear(), event.getMonth(), event.getDay());
    }

    private void setDate(int day, int month, int year) {
        String month1 = String.format("%02d", (month + 1));
        spinner_date.setText(day + " : " + month1 + " : " + year);
    }

    @Subscribe
    public void onEvent(NumberPickedEvent event) {
        spinner_repeat_time.setText(event.getNumber() + "");
    }

    private void returnResult() {
        String text = input_text.getText().toString().trim();
        HashSet<Contact> contactsSelected = new HashSet<Contact>();

        for (DrawableRecipientChip drawableRecipientChip : suggesstion_contact_lib.getSortedRecipients()) {
            contactsSelected.add(new Contact(drawableRecipientChip.getDisplay().toString(), drawableRecipientChip.getValue().toString(), drawableRecipientChip.getOriginalText().toString()));
        }

//        if(suggesstion_contact.getText().toString().isEmpty()){
//            suggesstion_contact.setError(HardCode.REQUIRED);
//            return;
//        }

        if(suggesstion_contact_lib.getText().toString().isEmpty()){
            suggesstion_contact_lib.setError(HardCode.REQUIRED);
            return;
        }

        if(input_text.getText().toString().isEmpty()){
            input_text.setError(HardCode.REQUIRED);
            return;
        }

        int repeatTime = Integer.parseInt(spinner_repeat_time.getText().toString());
        int delayMinus = 0;

        if(switch_repeat_at.isChecked()){
            delayMinus = caculateRepeat(spinner_repeat_at.getSelectedItem().toString());
        }

        Log.d("repeatTime" , "" + repeatTime);
        Log.d("delayMinus" , "" + delayMinus);
        Log.d("suggesstion_contact_lib", contactsSelected.size() + "");

        Intent intent = new Intent();
        MessageSchedual messageSchedual = new MessageSchedual(contactsSelected, text, calendar.getTime(), HardCode.PENDING, repeatTime, delayMinus, 0, 0);
        MessageSchedual messageSchedualSaved = new MessageSchedualDAO(this).create(messageSchedual);
        intent.putExtra("message", messageSchedualSaved);
        setResult(RESULT_OK, intent);
        this.finish();

    }

    private int caculateRepeat(String selectedItem) {
        int delayMinus = 1;

        if(selectedItem.equals(HardCode.Every_5_Minutes))
            delayMinus = 5;
        else if(selectedItem.equals(HardCode.Every_15_Minutes))
            delayMinus = 15;
        else if(selectedItem.equals(HardCode.Every_30_Minutes))
            delayMinus = 30;
        else if(selectedItem.equals(HardCode.Every_45_Minutes))
            delayMinus = 45;
        else if(selectedItem.equals(HardCode.Every_hour))
            delayMinus = 60;
        else if(selectedItem.equals(HardCode.Every_day))
            delayMinus = 24 * 60;
        else if(selectedItem.equals(HardCode.Every_week))
            delayMinus = 7 * 24 * 60;
        else if(selectedItem.equals(HardCode.Every_month))
            delayMinus = 30 * 24 * 60;
        else if(selectedItem.equals(HardCode.Every_year))
            delayMinus = 365 * 24 * 60;

        return delayMinus;
    }

    private void visible(Boolean isChecked) {
        if(isChecked){
            spinner_repeat_time.setVisibility(View.VISIBLE);
            switch_repeat_time.setVisibility(View.VISIBLE);
            text_repeat_time.setVisibility(View.VISIBLE);
        } else {
            spinner_repeat_time.setVisibility(View.GONE);
            switch_repeat_time.setVisibility(View.GONE);
            text_repeat_time.setVisibility(View.GONE);
        }
    }
}
