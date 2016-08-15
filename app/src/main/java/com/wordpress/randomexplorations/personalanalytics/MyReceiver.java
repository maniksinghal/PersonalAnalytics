package com.wordpress.randomexplorations.personalanalytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    /*
     * MUST MATCH the ACTION specified in MANIFEST
     */
    public static final String CUSTOM_ANALYTICS_ACTION =
            "com.wordpress.randomexplorations.personalanalytics.UPDATE_EVENT";

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("this", "Received some intent");
        if (action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            // Received an SMS
            handle_sms_actions(context, intent);
        } else if (CUSTOM_ANALYTICS_ACTION.equals(action)) {
            handle_custom_actions(context, intent);
        }
    }


    private void handle_sms_actions(Context context, Intent intent) {

        String action = intent.getAction();
        if (!Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(action)) {
            return;
        }

        Log.d("this", "Received SMS");

        final Bundle bundle = intent.getExtras();

        String senderNum = null;
        String message = null;

        // Extract message and phone number
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    senderNum = phoneNumber;
                    message = currentMessage.getMessageBody();
                    Log.d("this", "senderNum: "+ senderNum + "; message: " + message);

                    DataEntryTable tbl = new DataEntryTable();
                    tbl.content = message;
                    tbl.owner = phoneNumber;
                    tbl.start_time = Schema.DataEntry.get_current_time();
                    tbl.source = Schema.DataEntry.SOURCE_SMS_SERVICE;

                    DatabaseManager dbm = new DatabaseManager(context);
                    dbm.insert_to_data_entry_table(tbl);

                    // handle only one message
                    break;

                } // end for loop
            } // bundle is null
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }

        return;
    }

    private void handle_custom_actions(Context ctx, Intent in) {

        Log.d("this", "Received custom intent");

        DataEntryTable tbl = new DataEntryTable();
        tbl.content = in.getStringExtra(Schema.DataEntry.COLUMN_CONTENT);
        tbl.owner = in.getStringExtra(Schema.DataEntry.COLUMN_OWNER);
        tbl.start_time = in.getLongExtra(Schema.DataEntry.COLUMN_START_TIME, 0);
        tbl.end_time = in.getLongExtra(Schema.DataEntry.COLUMN_END_TIME, 0);
        tbl.source = in.getStringExtra(Schema.DataEntry.COLUMN_SOURCE);
        tbl.type = in.getStringExtra(Schema.DataEntry.COLUMN_TYPE);
        tbl.sub_type = in.getStringExtra(Schema.DataEntry.COLUMN_SUBTYPE);
        tbl.extra_csv = in.getStringExtra(Schema.DataEntry.COLUMN_EXTRA_CSV);
        tbl.duration = in.getLongExtra(Schema.DataEntry.COLUMN_DURATION, 0);

        Log.d("this", "Start_time: " + tbl.start_time + ", End_time: " + tbl.end_time);

        DatabaseManager dbm = new DatabaseManager(ctx);
        dbm.insert_to_data_entry_table(tbl);
    }

}
