package com.example.nathan.loadtracker.jobsession;

import android.provider.BaseColumns;

/**
 * Created by nathanabramyk on 2017-10-16.
 */

public final class JobSessionContract {
    private JobSessionContract() {}

    public static class JobSessionEntry implements BaseColumns {
        public static final String TABLE_NAME = "jobSessions";
        public static final String COLUMN_NAME_JOB_TITLE = "title";
        public static final String COLUMN_NAME_JOB_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CREATED = "created";
        public static final String COLUMN_NAME_MODIFIED = "modified";
        public static final String COLUMN_NAME_CLOSED = "closed";
        public static final String COLUMN_TOTAL_LOADS = "totalLoads";
    }
}