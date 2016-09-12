package com.wordpress.randomexplorations.personalanalytics;

import android.app.backup.BackupAgent;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.os.ParcelFileDescriptor;

/**
 * Created by maniksin on 9/12/16.
 * NOT IN USE RIGHT NOW..... USING ANDROID AUTOBACKUP introduced in 6.0
 */
public class MyBackupAgent extends BackupAgent {

    public void onBackup (ParcelFileDescriptor oldState,
                   BackupDataOutput data,
                   ParcelFileDescriptor newState) {

    }

    public void onRestore (BackupDataInput data,
                    int appVersionCode,
                    ParcelFileDescriptor newState) {

    }
}
