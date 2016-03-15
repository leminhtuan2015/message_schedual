package com.leminhtuan.myapplication.message_schedual.services;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.leminhtuan.myapplication.message_schedual.models.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by leminhtuan on 3/18/16.
 */
public class ContactService {
    private static HashMap<String, Contact> contactMapping = null;
    private static List<Contact> contacts = null;


    public static List<Contact> getContacts(Context context){
        if(contacts != null){
            return contacts;
        }
        contacts = new ArrayList<Contact>();
        Cursor contact = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (contact.moveToNext()) {
            String contactName = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String contactId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts._ID));
            String hasPhone = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if ((Integer.parseInt(hasPhone) > 0)){
                // You know have the number so now query it like this
                Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null);
                while (phones.moveToNext()){
                    //store numbers and display a dialog letting the user select which.
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String numberType = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                    Contact contact1 = new Contact();
                    contact1.setName(contactName);
                    contact1.setNumber(phoneNumber);
                    if(numberType.equals("0"))
                        contact1.setType("Work");
                    else if(numberType.equals("1"))
                        contact1.setType("Home");
                    else if(numberType.equals("2"))
                        contact1.setType("Mobile");
                    else
                        contact1.setType("Other");
                    //Then add this map to the list.
                    contacts.add(contact1);
                }
                phones.close();
            }
        }
        contact.close();
        Log.d("Contact", "Loaded'");
        return contacts;
    }

    public static HashMap<String, Contact> contactsMapping(Context context){
        if(contactMapping != null){
            return contactMapping;
        }

        contactMapping = new HashMap<>();
        for (Contact contact : getContacts(context)) {
            contactMapping.put(contact.getNumber(), contact);
        }
        return contactMapping;
    }
}
