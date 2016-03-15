package com.leminhtuan.myapplication.message_schedual.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.leminhtuan.myapplication.R;
import com.leminhtuan.myapplication.message_schedual.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leminhtuan on 3/17/16.
 */
public class SuggesstionContactAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private int layoutResourceId;
    private List<Contact> contacts = new ArrayList<Contact>();
    private List<Contact> contactList = new ArrayList<Contact>();
    private List<Contact> suggestions = new ArrayList<Contact>();

    public SuggesstionContactAdapter(Context context, int resource, List<Contact> contacts) {
        super(context, resource, contacts);
        this.context = context;
        this.layoutResourceId = resource;
        this.contacts = contacts;
        this.contactList.addAll(contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layoutResourceId, null);
        }

        Contact contact = getItem(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView number = (TextView) convertView.findViewById(R.id.number);
        TextView type = (TextView) convertView.findViewById(R.id.type);

        name.setText(contact.getName());
        number.setText(contact.getNumber());
        type.setText(contact.getType());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String name = ((Contact)(resultValue)).getName();
            return name;
        }

        @Override
        protected Filter.FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Contact contact : contactList) {
                    if (contact.getName().toLowerCase().contains(constraint.toString().toLowerCase())
                            || contact.getNumber().contains(constraint.toString())) {
                        suggestions.add(contact);
                    }
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
            ArrayList<Contact> filteredList = (ArrayList<Contact>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Contact c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
