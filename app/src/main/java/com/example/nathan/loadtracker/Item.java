package com.example.nathan.loadtracker;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by nathanabramyk on 2017-10-19.
 */

public interface Item {
    int getViewType();
    View getView(LayoutInflater inflater, View convertView);
}
