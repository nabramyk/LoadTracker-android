package com.example.nathan.loadtracker.load;

import android.provider.BaseColumns;

/**
 * Created by nathanabramyk on 2017-10-16.
 */

public final class LoadContract {
    private LoadContract() {}

    public static class LoadEntry implements BaseColumns {
        public static final String TABLE_NAME = "loads";
        public static final String COLUMN_NAME_JOB_TITLE = "title";
        public static final String COLUMN_NAME_DRIVER = "driver";
        public static final String COLUMN_NAME_UNIT_ID = "unit_id";
        public static final String COLUMN_NAME_MATERIAL = "material";
        public static final String COLUMN_NAME_COMPANY_NAME = "company_name";
        public static final String COLUMN_NAME_TIME_LOADED = "time_loaded";
        public static final String COLUMN_NAME_DATE_LOADED = "date_loaded";
        public static final String COLUMN_NAME_CREATED = "created";
        public static final String COLUMN_NAME_MODIFIED = "modified";
    }
}
