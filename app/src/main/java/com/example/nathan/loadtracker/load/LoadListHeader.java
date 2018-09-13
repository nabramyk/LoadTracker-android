package com.example.nathan.loadtracker.load;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.nathan.loadtracker.Item;
import com.example.nathan.loadtracker.R;
import com.example.nathan.loadtracker.arrayadapters.TrackingHistoryArrayAdapter;

/**
 * Created by nathanabramyk on 2017-10-19.
 */

//Subclass
public class LoadListHeader implements Item {

    private final String date;

    public LoadListHeader(String date) {
        this.date = date;
    }

    @Override
    public int getViewType() {
        return TrackingHistoryArrayAdapter.RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {

        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.trackedloadheader, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView dateLoadedHeader = (TextView) view.findViewById(R.id.dateLoadedHeader);

        dateLoadedHeader.setText(date);

        return view;
    }
}
