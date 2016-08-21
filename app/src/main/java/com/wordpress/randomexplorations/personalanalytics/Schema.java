package com.wordpress.randomexplorations.personalanalytics;

import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by maniksin on 8/13/16.
 */
public final class Schema {

    public Schema() {}

    public static final int VERSION = 1;
    public static final String DB_NAME = "personalAnalytics.db";

    public static final String TABLE_MAIN_DATA = "main_data";

    /*
     *  DATA_ENTRY_TABLE
     *  Source: SMS/...
     *  Type:     Server internal use
     *  Subtype:  Server internal use
     *  Owner: SMS sender/...
     *  Content
     *  Flags: Not used as of now
     *  SyncServer: Server with which sync is performed
     *
     *  // All absolute times are in seconds since epoch format
     *  // All durations are in seconds
     *  StartTime
     *  EndTime:   Any of EndTime/Duration (mutually exclusive)
     *  Duration:  Any of EndTime/Duration (mutually exclusive)
     *  SyncTime:  null if not sync'd yet
     *
     */
    public static class DataEntry implements BaseColumns {
        public static final String  COLUMN_SOURCE = "source";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_SUBTYPE = "sub_type";
        public static final String COLUMN_OWNER = "owner";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_SYNC_SERVER = "sync_server";
        public static final String COLUMN_SYNC_TIME = "sync_time";
        public static final String COLUMN_FLAGS = "flags";
        public static final String COLUMN_EXTRA_CSV = "extra_csv";

        /*
         * Enumerations for source
         */
        public static final String SOURCE_SMS_SERVICE = "sms_service";


        public static final String QUERY_CREATE = "CREATE TABLE " + Schema.TABLE_MAIN_DATA +
        "( " +
                COLUMN_SOURCE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_SUBTYPE + " TEXT, " +
                COLUMN_OWNER + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_START_TIME + " LONG, " +
                COLUMN_END_TIME + " LONG, " +
                COLUMN_DURATION+ " LONG, " +
                COLUMN_SYNC_SERVER + " TEXT, " +
                COLUMN_SYNC_TIME + " INTEGER, " +
                COLUMN_FLAGS + " INTEGER, " +
                COLUMN_EXTRA_CSV + " TEXT" +
                //"PRIMARY KEY (" + COLUMN_START_TIME + ")" +
        ");";

        public static final String QUERY_DROP = "DROP TABLE IF EXISTS " + Schema.TABLE_MAIN_DATA;

        public static long get_current_time() {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            return cal.getTimeInMillis() / 1000;
        }

        public static List<String> get_fields() {
            List<String> arr = new ArrayList<String>();
            arr.add(COLUMN_SOURCE);
            arr.add(COLUMN_TYPE);
            arr.add(COLUMN_SUBTYPE);
            arr.add(COLUMN_OWNER);
            arr.add(COLUMN_CONTENT);
            arr.add(COLUMN_START_TIME);
            arr.add(COLUMN_END_TIME);
            arr.add(COLUMN_DURATION);
            arr.add(COLUMN_SYNC_SERVER);
            arr.add(COLUMN_SYNC_TIME);
            arr.add(COLUMN_FLAGS);
            arr.add(COLUMN_EXTRA_CSV);
            return arr;
        }

        public static boolean is_column_long(String col) {
            if (col.equals(COLUMN_START_TIME) || col.equals(COLUMN_END_TIME) ||
                    col.equals(COLUMN_DURATION) || col.equals(COLUMN_SYNC_TIME)) {
                return true;
            } else {
                return false;
            }
        }

    }
}
