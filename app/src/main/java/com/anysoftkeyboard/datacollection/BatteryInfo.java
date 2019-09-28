package com.anysoftkeyboard.datacollection;

import android.os.BatteryManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class BatteryInfo {
    boolean isCharging;
    int batteryLevel;
    int batteryStatus;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public BatteryInfo( BatteryManager batteryManager) {
        this.isCharging = batteryManager.isCharging();
        this.batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        this.batteryStatus = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS);
    }

    public boolean isCharging() {
        return isCharging;
    }

    public void setCharging(boolean charging) {
        isCharging = charging;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
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
                ", batteryLevel=" + batteryLevel +
                ", batteryStatus=" + batteryStatus +
                '}';
    }
}
