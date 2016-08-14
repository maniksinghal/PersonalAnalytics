package com.wordpress.randomexplorations.personalanalytics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by maniksin on 8/13/16.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    public DatabaseManager(Context ctx) {
        super(ctx, Schema.DB_NAME, null, Schema.VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("this", Schema.DataEntry.QUERY_CREATE);
        db.execSQL(Schema.DataEntry.QUERY_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int old_ver, int new_ver) {
        // Not used right now
        db.execSQL(Schema.DataEntry.QUERY_DROP);
        db.execSQL(Schema.DataEntry.QUERY_CREATE);
    }

    private void create_schema_if_not_exists(SQLiteDatabase db) {

        /*
         * Try fetching 1 row from the table and handle exception in case the table
         * does not exist
         */
        Cursor c = null;
        try {
            c = db.query(Schema.TABLE_MAIN_DATA, null, null, null, null, null, null, "1");
        } catch (SQLiteException e) {
            Log.d("this", "Exception during get_data_table_entry_str,  is: " + e.getMessage());
            if (e.getMessage().contains("no such table")) {
                // Table does not exist
                onUpgrade(db, 1, 1);
                Log.d("this", "Created schema");
            }
        }


    }

    public void insert_to_data_entry_table(DataEntryTable tbl_entry) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        create_schema_if_not_exists(db);
        //onUpgrade(db, 0, 1);  // Hack to clear the database??

        values.put(Schema.DataEntry.COLUMN_CONTENT, tbl_entry.content);
        values.put(Schema.DataEntry.COLUMN_DURATION, tbl_entry.duration);
        values.put(Schema.DataEntry.COLUMN_END_TIME, tbl_entry.end_time);
        values.put(Schema.DataEntry.COLUMN_START_TIME, tbl_entry.start_time);
        values.put(Schema.DataEntry.COLUMN_DURATION, tbl_entry.duration);
        values.put(Schema.DataEntry.COLUMN_OWNER, tbl_entry.owner);
        values.put(Schema.DataEntry.COLUMN_SYNC_TIME, tbl_entry.sync_time);
        values.put(Schema.DataEntry.COLUMN_SYNC_SERVER, tbl_entry.sync_server);
        values.put(Schema.DataEntry.COLUMN_SOURCE, tbl_entry.source);
        values.put(Schema.DataEntry.COLUMN_EXTRA_CSV, tbl_entry.extra_csv);

        Long row = null;
        try {
            row = db.insert(Schema.TABLE_MAIN_DATA, null, values);
        } catch (SQLiteException e) {
            Log.d("this", "Exception during insert_to_data_entry_table,  is: " + e.getMessage());
            return;
        }

        if (row == -1) {
            // Insertion failed
            Log.d("this", "Error: Insertion failed into database");
        }
        Log.d("this", "Inserting row: " + String.valueOf(row));
    }

    public String get_data_table_entry_str(int row_index) {
        SQLiteDatabase db = this.getReadableDatabase();

        /*
         * Check if table exists!!
         */
        Cursor c = null;
        try {
            c = db.query(Schema.TABLE_MAIN_DATA, null, null, null, null, null, null);
        } catch (SQLiteException e) {
            Log.d("this", "Exception during get_data_table_entry_str,  is: " + e.getMessage());
            if (e.getMessage().contains("no such table")) {
                return "Empty Database, Schema not present";
            }
        }
        c.moveToFirst();
        c.move(row_index);

        if (c.getCount() == 0) {
            return "Empty Database";
        }

        String str = "Owner: ";
        str += c.getString(c.getColumnIndexOrThrow(Schema.DataEntry.COLUMN_OWNER));
        str += "\nSource: ";
        str += c.getString(c.getColumnIndexOrThrow(Schema.DataEntry.COLUMN_SOURCE));
        str += "\nContent: ";
        str += c.getString(c.getColumnIndexOrThrow(Schema.DataEntry.COLUMN_CONTENT));
        str += "\nExtra-CSV: ";
        str += c.getString(c.getColumnIndexOrThrow(Schema.DataEntry.COLUMN_EXTRA_CSV));
        str += "\nStart_time: ";
        str += c.getInt(c.getColumnIndexOrThrow(Schema.DataEntry.COLUMN_START_TIME));
        str += "\nDuration: ";
        str += c.getInt(c.getColumnIndexOrThrow(Schema.DataEntry.COLUMN_DURATION));
        str += "\nEndTime: ";
        str += c.getInt(c.getColumnIndexOrThrow(Schema.DataEntry.COLUMN_END_TIME));

        c.close();

        return str;
    }
}
