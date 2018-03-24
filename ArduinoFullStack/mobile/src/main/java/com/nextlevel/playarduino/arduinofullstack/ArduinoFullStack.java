package com.nextlevel.playarduino.arduinofullstack;

import android.app.Application;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;

import com.nextlevel.playarduino.arduinofullstack.Models.CommanderDevice;
import com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers.PubNubHelper;

/**
 * Created by sukumar on 1/8/17.
 */

public class ArduinoFullStack extends Application {

    private CommanderDevice mCommanderDevice = new CommanderDevice();

    public static PubNubHelper mPubNubHelper;
    public UsbDevice mUsbDevice;
    public UsbDeviceConnection mDeviceConnection;


    public UsbDevice getArduinoUsbDevice() {
        return mUsbDevice;
    }

    public void setArduinoUsbDevice(UsbDevice mUsbDevice) {
        this.mUsbDevice = mUsbDevice;
    }

    public UsbDeviceConnection getArduinoUsbConnection() {
        return mDeviceConnection;
    }

    public void setArduinoUsbConnection(UsbDeviceConnection mDeviceConnection) {
        this.mDeviceConnection = mDeviceConnection;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPubNubHelper = PubNubHelper.getPubNub();
    }

    public CommanderDevice getCommanderDevice() {
        return mCommanderDevice;
    }

    public void setCommanderDevice(CommanderDevice mCommanderDevice) {
        this.mCommanderDevice = mCommanderDevice;
    }


}
