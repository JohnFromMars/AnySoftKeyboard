package com.anysoftkeyboard.datacollection;



import com.google.gson.Gson;

import java.util.ArrayList;

/**
 *
 */
public class DataCollection {
    private String deviceId;
    private String startPoint;
    private String endPoint;
    private BatteryInfo batteryInfo;
    private ArrayList<Word> words;
    private ArrayList<Keystroke> keystrokes;



    public DataCollection() {
        words = new ArrayList<>();
        keystrokes = new ArrayList<>();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public void addWord(Word word) {
        this.words.add(word);
    }

    public void addKeystroke(Keystroke keystroke) {
        this.keystrokes.add(keystroke);
    }

    public Keystroke getLastKeystroke() {
        return keystrokes.get(this.keystrokes.size() - 1);
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public BatteryInfo getBatteryInfo() {
        return batteryInfo;
    }

    public void setBatteryInfo(BatteryInfo batteryInfo) {
        this.batteryInfo = batteryInfo;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public ArrayList<Keystroke> getKeystrokes() {
        return keystrokes;
    }

    public void setKeystrokes(ArrayList<Keystroke> keystrokes) {
        this.keystrokes = keystrokes;
    }

    public String toJsonString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
