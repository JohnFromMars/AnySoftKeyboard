package com.anysoftkeyboard.datacollection.database.model;

/**
 * file info table class
 */
public class FileInfo {

    /***** table information *****/
    //file_info table name
    public static final String FILE_INFO_TABLE_NAME = "file_info";
    //file_info table column
    public static final String FILE_INFO_ID = "id";
    public static final String FILE_INFO_FILE_NAME = "file_name";
    public static final String FILE_INFO_HAS_SENT = "has_sent";
    public static final String FILE_INFO_SIZE = "size";

    //data fields
    private int id;
    private String fileName;
    private boolean hasSent; // default value is false.
    private int size;


    /**
     * Constructor
     *
     * @param fileName the name of the saving file
     */
    public FileInfo(String fileName) {
        this.fileName = fileName;
        this.hasSent = false;
    }

    public FileInfo(String fileName, int size){
        this(fileName);
        this.size = size;
    }

    public static String getCreateFileInfoTableStatement() {
        return "create table " + FILE_INFO_TABLE_NAME + "(" + FILE_INFO_ID + " integer primary key autoincrement, " + FILE_INFO_FILE_NAME + " text unique not null, " + FILE_INFO_HAS_SENT + " integer not null, " + FILE_INFO_SIZE + " integer);";
    }

    public static String getDropFileInfoTableStatement() {
        return "DROP TABLE IF EXISTS " + FILE_INFO_TABLE_NAME;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isHasSent() {
        return hasSent;
    }

    public void setHasSent(boolean hasSent) {
        this.hasSent = hasSent;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", hasSent=" + hasSent +
                ", size=" + size +
                '}';
    }
}
