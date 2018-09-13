package com.example.nathan.loadtracker.jobsession;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.nathan.loadtracker.Item;
import com.example.nathan.loadtracker.R;
import com.example.nathan.loadtracker.arrayadapters.JobSessionArrayAdapter;
import com.example.nathan.loadtracker.models.JobSession;

/**
 * Created by Nathan on 2017-10-20.
 */

public class JobSessionItem implements Item {

    private final String title;
    private final String created_date;
    private final String totalLoads;

    public JobSessionItem(JobSession js) {
        this.title = js.getJobTitle();
        this.created_date = js.getCreated();
        this.totalLoads = String.valueOf(js.getTotalLoads());
    }

    @Override
    public int getViewType() {
        return JobSessionArrayAdapter.RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.jobsessionrow, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView sessionTitle = (TextView) view.findViewById(R.id.sessionTitle);
        TextView created = (TextView) view.findViewById(R.id.createdTextView);
        TextView totalLoads = (TextView) view.findViewById(R.id.totalLoadsTextView);

        sessionTitle.setText(this.title);
        created.setText(this.created_date);
        totalLoads.setText(this.totalLoads);

        return view;
    }
}
