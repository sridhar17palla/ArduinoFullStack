package com.nextlevel.playarduino.arduinofullstack;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sukumar on 9/27/16.
 */

public class ArduinoDrivers {

    UsbManager usbManager;
    UsbDevice device;
    UsbSerialDevice serialPort;
    UsbDeviceConnection connection;
    MainActivity activity;
    private static ArduinoDrivers arduinoDrivers;

    private ArduinoDrivers(Context context) {
       // this.activity=(MainActivity)activity;
        //init(context);
    }

    public static ArduinoDrivers getArduino(Context context) {
        if (arduinoDrivers == null) {
            arduinoDrivers = new ArduinoDrivers(context);
            return arduinoDrivers;
        } else {
            return arduinoDrivers;
        }
    }

    public void init(Context context) {
        usbManager = (UsbManager) context.getSystemService(context.USB_SERVICE);
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                if (device.getVendorId() == 0x2341) {//Arduino Vendor ID
                    openArduinoConnection();
                    break;
                } else {
                    connection = null;
                    device = null;
                }
            }
        }
    }

    private void openArduinoConnection() {
        connection = usbManager.openDevice(device);
        serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
        if (serialPort != null) {
            if (serialPort.open()) { //Set Serial Connection Parameters.
                serialPort.setBaudRate(9600);
                serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                serialPort.read(mCallback);
                Log.d("SERIAL", "Serial Connection Opened!\n");

            } else {
                Log.d("SERIAL", "PORT NOT OPEN");
            }
        } else {
            Log.d("SERIAL", "PORT IS NULL");
        }
    }

    public void sendData(String data) {
        if (serialPort != null) {
            serialPort.write(data.getBytes());
        }
    }

    public void closeArduinoPort() {
        if (serialPort != null) {
            serialPort.close();
        }
    }

    UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() { //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(byte[] arg0) {
            String data = null;
            try {
                data = new String(arg0, "UTF-8");
                data.concat("/n");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

}
