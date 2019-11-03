package com.anysoftkeyboard.datacollection.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.anysoftkeyboard.base.utils.Logger;
import com.anysoftkeyboard.datacollection.database.model.FileInfo;


/**
 * This class create the database that data collection need
 */
public class DataCollectionDatabaseHelper extends SQLiteOpenHelper {

    //Database information
    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "DATA_COLLECTION.DB";

    /**
     * Constructor
     *
     * @param context application context
     */
    public DataCollectionDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create tables
        //create file_info Table
        sqLiteDatabase.execSQL(FileInfo.getCreateFileInfoTableStatement());
        Logger.d("test", "dc-- DataTransmitter fileInfo table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(FileInfo.getDropFileInfoTableStatement());
        onCreate(sqLiteDatabase);

    }
}
