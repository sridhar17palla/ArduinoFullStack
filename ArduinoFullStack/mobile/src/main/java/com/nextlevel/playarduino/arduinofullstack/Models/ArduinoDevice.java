package com.nextlevel.playarduino.arduinofullstack.Models;

import java.util.List;

/**
 * Created by sukumar on 8/7/17.
 */

public class ArduinoDevice {

    //Given by user, Null initially
    private String mDeviceName;
    //Unique Id privided by Manufacturer;
    private String mUUID;
    //Schema of controlling buttons
    private String mSchemeName;
    //Type of Arduino device
    private String mDeviceType;

    /* Way controlling Arduino Device. Like only via Bluetooth, or under same network */
    private String mAccessType;


    public String getmDeviceName() {
        return mDeviceName;
    }

    public void setmDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }

    public String getmUUID() {
        return mUUID;
    }

    public void setmUUID(String mUUID) {
        this.mUUID = mUUID;
    }

    public String getmSchemeName() {
        return mSchemeName;
    }

    public void setmSchemeName(String mSchemeName) {
        this.mSchemeName = mSchemeName;
    }

    public String getmDeviceType() {
        return mDeviceType;
    }

    public void setmDeviceType(String mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    public String getmAccessType() {
        return mAccessType;
    }

    public void setmAccessType(String mAccessType) {
        this.mAccessType = mAccessType;
    }

}
