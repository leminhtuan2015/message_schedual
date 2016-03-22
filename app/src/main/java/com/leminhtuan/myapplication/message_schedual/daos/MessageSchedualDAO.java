package com.leminhtuan.myapplication.message_schedual.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.leminhtuan.myapplication.message_schedual.models.Contact;
import com.leminhtuan.myapplication.message_schedual.models.MessageSchedual;
import com.leminhtuan.myapplication.message_schedual.utils.DateConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class MessageSchedualDAO extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDBName.db";
    private static final String TABLE_NAME = "messages";
    private Context context = null;

    public MessageSchedualDAO(Context context) {
        super(context, DATABASE_NAME, null, 3);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (id integer primary key, number text,text text,time text, status int, repeatNumber int, delayMinus int, sentNumber int, failNumber int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public MessageSchedual create(MessageSchedual messageSchedual) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = putData(messageSchedual);
        long id = db.insert(TABLE_NAME, null, contentValues);
        messageSchedual.setId(id);
        return messageSchedual;
    }

    public MessageSchedual get(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME + " where id=" + id + "", null);
        res.moveToFirst();

        String numbersText = res.getString(res.getColumnIndex("number"));
        String text = res.getString(res.getColumnIndex("text"));
        String dateString = res.getString(res.getColumnIndex("time"));
        int status = res.getInt(res.getColumnIndex("status"));
        int repeatNumber = res.getInt(res.getColumnIndex("repeatNumber"));
        int delayMinus = res.getInt(res.getColumnIndex("delayMinus"));
        int sentNumber = res.getInt(res.getColumnIndex("sentNumber"));
        int failNumber = res.getInt(res.getColumnIndex("failNumber"));

        Date date = DateConverter.parse(dateString, DateConverter.FORMAT_1);
        HashSet<Contact> contacts = contacts(numbersText);

        MessageSchedual messageSchedual = new MessageSchedual(id, contacts, text, date, status, repeatNumber, delayMinus, sentNumber, failNumber);

        return messageSchedual;
    }

    public MessageSchedual update(MessageSchedual messageSchedual) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = putData(messageSchedual);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{Long.toString(messageSchedual.getId())});
        return messageSchedual;
    }

    public MessageSchedual delete(MessageSchedual messageSchedual) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ? ", new String[]{Long.toString(messageSchedual.getId())});
        return messageSchedual;
    }

    public ArrayList<MessageSchedual> all() {
        ArrayList<MessageSchedual> messageScheduals = new ArrayList<MessageSchedual>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            long id = res.getLong(res.getColumnIndex("id"));
            String numbersText = res.getString(res.getColumnIndex("number"));
            String text = res.getString(res.getColumnIndex("text"));
            String dateString = res.getString(res.getColumnIndex("time"));
            int status = res.getInt(res.getColumnIndex("status"));
            int repeatNumber = res.getInt(res.getColumnIndex("repeatNumber"));
            int delayMinus = res.getInt(res.getColumnIndex("delayMinus"));
            int sentNumber = res.getInt(res.getColumnIndex("sentNumber"));
            int failNumber = res.getInt(res.getColumnIndex("failNumber"));

            Date date = DateConverter.parse(dateString, DateConverter.FORMAT_1);
            HashSet<Contact> contacts = contacts(numbersText);

            MessageSchedual messageSchedual = new MessageSchedual(id, contacts, text, date, status, repeatNumber, delayMinus, sentNumber, failNumber);
            messageScheduals.add(messageSchedual);
            res.moveToNext();
        }
        return messageScheduals;
    }

    private ContentValues putData(MessageSchedual messageSchedual){
        ContentValues contentValues = new ContentValues();
        HashSet<String> numbers = new HashSet<String>();
        for (Contact contact: messageSchedual.getContacts()){
            numbers.add(contact.getName()+"-"+contact.getNumber());
        }
        contentValues.put("number", TextUtils.join(",", numbers));
        contentValues.put("text", messageSchedual.getText());
        contentValues.put("time", messageSchedual.getDate().toString());
        contentValues.put("status", messageSchedual.getStatus());
        contentValues.put("repeatNumber", messageSchedual.getRepeatNumber());
        contentValues.put("delayMinus", messageSchedual.getDelayMinus());
        contentValues.put("sentNumber", messageSchedual.getSentNumber());
        contentValues.put("failNumber", messageSchedual.getFailNumber());
        return contentValues;
    }

    private HashSet<Contact> contacts(String numbersText){
        HashSet<Contact> contacts = new HashSet<Contact>();
        String[] numberArray = TextUtils.split(numbersText, ",");

        for (String number : Arrays.asList(numberArray)) {
            String[] nameAndNumber= number.split("-");
            contacts.add(new Contact(nameAndNumber[0], nameAndNumber[1], null));
        }

        return contacts;
    }
}
