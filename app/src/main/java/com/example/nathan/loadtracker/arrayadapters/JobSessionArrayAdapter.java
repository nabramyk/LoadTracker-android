package com.example.nathan.loadtracker.arrayadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.nathan.loadtracker.Item;

import java.util.List;

public class JobSessionArrayAdapter extends ArrayAdapter<Item> {

    private LayoutInflater mInflater;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    public JobSessionArrayAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.mInflater = LayoutInflater.from(context);
    }

    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }
}
