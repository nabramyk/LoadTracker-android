package com.example.nathan.loadtracker.arrayadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.nathan.loadtracker.Item;

import java.util.List;

/**
 * Created by nathanabramyk on 2017-10-19.
 */
public class TrackingHistoryArrayAdapter extends ArrayAdapter<Item> {

    private LayoutInflater mInflater;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    public TrackingHistoryArrayAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.mInflater = LayoutInflater.from(context);
    }

    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getItem(position).getView(mInflater, convertView);
    }
}
