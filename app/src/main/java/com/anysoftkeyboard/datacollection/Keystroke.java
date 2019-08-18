package com.anysoftkeyboard.datacollection;

import com.anysoftkeyboard.api.KeyCodes;

public class Keystroke {
    public static final String OTHER = "other";
    public static final String CHAR = "char";
    public static final String SEP = "sep";
    public static final String PUN = "pun";
    public static final String NUM = "num";
    public static final String ERR = "err";


    private float pressure;
    private float x;
    private float y;
    private float distance;
    private long startTime;
    private long endTime;
    private String type = Keystroke.OTHER;

    public Keystroke() {
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(int primaryCode) {

        //sep includes space, comma, period
        if (primaryCode == KeyCodes.SPACE || primaryCode == KeyCodes.ENTER) {
            this.type = Keystroke.SEP;

            //err include delete forward delete and delete word
        } else if (primaryCode == KeyCodes.DELETE || primaryCode == KeyCodes.DELETE_WORD || primaryCode == KeyCodes.FORWARD_DELETE) {
            this.type = Keystroke.ERR;

            //number
        } else if (ExtendedKeyCodes.isNumber(primaryCode)) {
            this.type = Keystroke.NUM;

            //character - from a to  z and A to Z
        } else if (ExtendedKeyCodes.isAlphabetic(primaryCode)) {
            this.type = Keystroke.CHAR;

            //punctuation
        } else if (ExtendedKeyCodes.isPunctuation(primaryCode)) {
            this.type = Keystroke.PUN;
        }
    }


    @Override
    public String toString() {
        return "Keystroke{" +
                "pressure=" + pressure +
                ", x=" + x +
                ", y=" + y +
                ", distance=" + distance +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", type='" + type + '\'' +
                '}';
    }
}
