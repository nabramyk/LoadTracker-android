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

public class LoadListItem implements Item {

    private final int id;
    private final String timeLoaded;
    private final String material;
    private final String unitId;

    public LoadListItem(Load load) {
        this.id = load.getId();
        this.timeLoaded = load.getTimeLoaded();
        this.material = load.getMaterial();
        this.unitId = load.getUnitId();
    }

    @Override
    public int getViewType() {
        return TrackingHistoryArrayAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.trackedloadrow, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView trackedLoads = (TextView) view.findViewById(R.id.timeLoaded);
        TextView material = (TextView) view.findViewById(R.id.material);
        TextView unitId = (TextView) view.findViewById(R.id.unitId);

        trackedLoads.setText("Time loaded: " + this.timeLoaded);
        material.setText("Material: " + this.material);
        unitId.setText("Unit ID: " + this.unitId);

        return view;
    }

    public int getId() {
        return id;
    }
}