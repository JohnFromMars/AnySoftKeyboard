package com.anysoftkeyboard.datacollection;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class BatteryInfo {

    private boolean isCharging;
    private float batteryPercentage;
    private int batterLevel;
    private int batteryStatus;

    public BatteryInfo(){ }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public BatteryInfo(BatteryManager batteryManager) {
        this.isCharging = batteryManager.isCharging();
//        this.batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        this.batteryStatus = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS);
    }

    public float getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(float batteryPercetage) {
        this.batteryPercentage = batteryPercetage;
    }

    public int getBatterLevel() {
        return batterLevel;
    }

    public void setBatterLevel(int batterLevel) {
        this.batterLevel = batterLevel;
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }

    public int getBatteryStatus() {
        return batteryStatus;
    }

    public void setBatteryStatus(int batteryStatus) {
        this.batteryStatus = batteryStatus;
    }

    @Override
    public String toString() {
        return "BatteryInfo{" +
                "isCharging=" + isCharging +
                ", batteryPercetage=" + batteryPercentage +
                ", batterLevel=" + batterLevel +
                ", batteryStatus=" + batteryStatus +
                '}';
    }

    public static float getBatteryPercentage(Context context) {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
        return (level / (float) scale);
    }

    public static int getBatteryLevel(Context context) {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);
        return batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
    }
}
