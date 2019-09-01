package com.anysoftkeyboard.datacollection;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.anysoftkeyboard.base.utils.Logger;
import com.anysoftkeyboard.datacollection.database.dao.FileInfoDao;
import com.anysoftkeyboard.datacollection.database.model.FileInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.UUID;

import static com.anysoftkeyboard.base.Charsets.UTF8;

public class DataTransmitter implements Runnable {

    private Context context;
    private DataCollection dataCollection;
    private FileInfoDao fileInfoDao;

    public DataTransmitter(Context context, DataCollection dataCollection) {
        this.context = context;
        this.dataCollection = dataCollection;
        fileInfoDao = new FileInfoDao(context);
    }


    /**
     * This method save the data from DataCollection as file
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveData(FileInfo fileInfo) throws IOException {

        File file = new File(this.context.getFilesDir(), fileInfo.getFileName());
        BufferedWriter writer = Files.newBufferedWriter(file.toPath(), UTF8);
        writer.write(this.dataCollection.getKeystrokes().toString() + this.dataCollection.getWords().toString());
        writer.close();

        //Test reading
//        BufferedReader reader = Files.newBufferedReader(file.toPath(), UTF8);
//        Logger.d("test", "dc-- DataTransmitter read in string length=%d", reader.readLine().length());
//        Logger.d("test", "dc-- DataTransmitter actual string length=%d", this.dataCollection.getKeystrokes().toString().length() + this.dataCollection.getWords().toString().length());
//        reader.close();
    }

    /**
     * This method perform the transmit file process, please refer the activity diagram of transmit file process
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void run() {
        transmitProcess();
//        testProcess();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void transmitProcess() {
        boolean findAllFileInfo = true;

        //create a file info data
        FileInfo fileInfo = new FileInfo(UUID.randomUUID().toString(), dataCollection.toString().length());

        //try to save the data collection object as file first
        //then upload the file, finally delete it
        try {
            saveData(fileInfo);
            Logger.d(this.getClass().getName(), "dc-- DataTransmitter file saved");
            uploadFile(fileInfo);
            Logger.d(this.getClass().getName(), "dc-- DataTransmitter file uploaded");
            if(deleteFile(fileInfo)){
                Logger.d(this.getClass().getName(), "dc-- DataTransmitter file deleted");
            }



        } catch (ConnectException e) {
            Logger.d(this.getClass().getName(), "dc-- DataTransmitter file cannot be uploaded due to some reasons.");
            //something happen with internet connection, do not need to upload unuploaded files.
            findAllFileInfo = false;
            //save the file record into database
            fileInfoDao.open();
            fileInfoDao.insert(fileInfo);
            fileInfoDao.close();

        } catch (IOException e) {
            // if exception occur when saving the file
            // save the file record into database
            Logger.w(this.getClass().getName(), "dc-- DataTransmitter file cannot be saved as file, will not save this file. ");
            findAllFileInfo = true; //still need to check all unuploaded files and try to upload them

        } finally {
            //find all unuploaded files and try to upload them.
            if (findAllFileInfo) {
                fileInfoDao.open();
                ArrayList<FileInfo> fileInfos = fileInfoDao.findAllFileInfoByHasSent(false);
                Logger.d(this.getClass().getName(), "dc-- DataTransmitter (file) loop size = %d", fileInfos.size());
                try {
                    for (FileInfo file : fileInfos) {
                        //upload the file
                        uploadFile(file);
                        Logger.d(this.getClass().getName(), "dc-- DataTransmitter file uploaded");
                        // delete the file it self
                        if (deleteFile(file)) {
                            //delete the file in database
                            fileInfoDao.deleteFileInfo(file);
                            Logger.d(this.getClass().getName(), "dc-- DataTransmitter file delete success");
                        }
                    }

                } catch (ConnectException e) {
                    Logger.d(this.getClass().getName(), "dc-- DataTransmitter file cannot be uploaded due to some reasons. for loop exist");

                } finally {
                    Logger.d(this.getClass().getName(), "dc-- DataTransmitter fileDao close.");
                    fileInfoDao.close();
                }
            }
        }
    }

    /**
     * Upload the file to backend server
     *
     * @param fileInfo file information
     * @throws ConnectException internet connection exception
     */
    private void uploadFile(FileInfo fileInfo) throws ConnectException {

    }


    /**
     * delete the file on disk. return true if delete success
     *
     * @param fileInfo file information
     * @return return true if delete success
     * *
     */
    private boolean deleteFile(FileInfo fileInfo) {
        File file = new File(this.context.getFilesDir(), fileInfo.getFileName());
        return file.delete();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void testProcess() {
//        FileInfo fileInfo1 = new FileInfo("1", 123);
//        try {
//            saveData(fileInfo1);
//            Logger.d(this.getClass().getName(), "dc-- DataTransmitter delete file = %s", deleteFile(fileInfo1));

//        } catch (IOException e) {
//            Logger.d(this.getClass().getName(), "dc-- fail to save file");
//        }


//        FileInfo fileInfo2 = new FileInfo("2", 123);
//        FileInfo fileInfo3 = new FileInfo("3", 123);
//
//        fileInfoDao.open();
//        fileInfoDao.insert(fileInfo1);
//        fileInfoDao.insert(fileInfo2);
//        fileInfoDao.insert(fileInfo3);
//
//        Logger.d(this.getClass().getName(), "dc-- DataTransmitter all unuploaded = %s", fileInfoDao.findAllFileInfoByHasSent(false));
//
//        fileInfoDao.deleteFileInfo(fileInfo1);
//        fileInfoDao.deleteFileInfo(fileInfo2);
//        fileInfoDao.deleteFileInfo(fileInfo3);
//
//        Logger.d(this.getClass().getName(), "dc-- DataTransmitter all unuploaded = %s", fileInfoDao.findAllFileInfoByHasSent(false));

    }
}
