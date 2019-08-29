package com.anysoftkeyboard.datacollection.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.anysoftkeyboard.base.utils.Logger;
import com.anysoftkeyboard.datacollection.database.model.FileInfo;

/**
 * This class is database access object of file_info table in the data collection database
 */
public class FileInfoDao {

    private Context context;
    private DataCollectionDatabaseHelper dataBaseHelper;
    private SQLiteDatabase database;

    /**
     * Constructor
     *
     * @param context application context
     */
    public FileInfoDao(Context context) {
        this.context = context;
    }

    /**
     * Open the data collection database
     *
     * @return FileInfoDao object for further actions
     */
    public FileInfoDao open() {
        dataBaseHelper = new DataCollectionDatabaseHelper(this.context);
        database = dataBaseHelper.getWritableDatabase();
        Logger.d("test", "dc-- DataTransmitter open database");
        return this;
    }

    /**
     * close the connection of database
     */
    public void close() {
        Logger.d("test", "dc-- DataTransmitter close database");
        dataBaseHelper.close();
        database.close();
    }

    /**
     * insert FileInfo data into data collection database
     *
     * @param fileInfo FileInfo object
     */
    public void insert(FileInfo fileInfo) {
        database = dataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FileInfo.FILE_INFO_FILE_NAME, fileInfo.getFileName());
        contentValues.put(FileInfo.FILE_INFO_HAS_SENT, fileInfo.isHasSent());
        contentValues.put(FileInfo.FILE_INFO_SIZE, fileInfo.getSize());
        database.insert(FileInfo.FILE_INFO_TABLE_NAME, null, contentValues);
        Logger.d("test", "dc-- DataTransmitter finish insert");

    }

    /**
     * Query the FileInfo according to the given file name.
     * Return null if there is no record found in the database
     *
     * @param fileName file name
     * @return FileInfo Object for the data collection database
     */
    public FileInfo getFileInfoByFileName(String fileName) {
        FileInfo fileInfo = null;

        Cursor result = database.rawQuery("select * from " + FileInfo.FILE_INFO_TABLE_NAME + " where " + FileInfo.FILE_INFO_FILE_NAME + " = ?", new String[]{fileName});
        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            fileInfo = new FileInfo(result.getString(result.getColumnIndex(FileInfo.FILE_INFO_FILE_NAME)));
            fileInfo.setId(result.getInt(result.getColumnIndex(FileInfo.FILE_INFO_ID)));
            fileInfo.setHasSent(result.getInt(result.getColumnIndex(FileInfo.FILE_INFO_HAS_SENT)) > 0);
            fileInfo.setSize(result.getInt(result.getColumnIndex(FileInfo.FILE_INFO_SIZE)));
        }

        if(result != null){
            result.close();
        }

        Logger.d("test", "dc-- DataTransmitter getFile finish");
        return fileInfo;
    }

    /**
     * delete the FileInfo record in the database according to the given file name
     *
     * @param fileName file name
     */
    public void deleteFileInfoByFileName(String fileName) {
        database.delete(FileInfo.FILE_INFO_TABLE_NAME, FileInfo.FILE_INFO_FILE_NAME + " = ?", new String[]{fileName});
        Logger.d("test", "dc-- DataTransmitter finish delete");
    }

}
