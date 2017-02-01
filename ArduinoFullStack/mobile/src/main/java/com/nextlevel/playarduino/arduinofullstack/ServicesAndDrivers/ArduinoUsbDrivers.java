package com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.CountDownTimer;
import android.util.Log;

import com.felhr.usbserial.UsbSerialDevice;
import com.felhr.usbserial.UsbSerialInterface;
import com.nextlevel.playarduino.arduinofullstack.ArduinoFullStack;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sukumar on 9/27/16.
 */

public class ArduinoUsbDrivers {

    private UsbManager mUsbManager;
    private UsbDevice mDevice;
    private UsbSerialDevice mSerialPort;
    private UsbDeviceConnection mDeviceConnection;
    private Context mContext;
    private static ArduinoUsbDrivers mArduinoDrivers;
    private ArduinoListners mArduinoListners;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static char mCommand;
    /*
        Arduino device has to send 2 bytes of initial data. First byte is command. 2nd byte is
        content lenght.
     */
    public static boolean sReceivingData = false;
    public static boolean sIsRxDataAvailable = false;
    public String mDataReceived = "";
    private static int sRxDataLength = 0;
    private String mRawDataReceived = "";
    private boolean mCurruptDataReceived = false;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private static final String ACTION_USB_PERMISSION = "com.blecentral.USB_PERMISSION";

    private ArduinoUsbDrivers(Context context) {
        mContext = context;
        init(mContext);
    }

    public static ArduinoUsbDrivers getArduino(Context context) {
        if (mArduinoDrivers == null) {
            mArduinoDrivers = new ArduinoUsbDrivers(context);
            return mArduinoDrivers;
        } else {
            return mArduinoDrivers;
        }
    }

    public interface ArduinoListners {
        public void onArduinoDataReceived(String data);
    }

    private UsbSerialInterface.UsbReadCallback mOnReceivedData = new UsbSerialInterface.UsbReadCallback() {
        //Defining a Callback which triggers whenever data is read.
        @Override
        public void onReceivedData(final byte[] arg0) {

            String data = null;
            //TODO: Sometimes Device returns "-1" values. Search for Problem and deal with it
            try {
                data = new String(arg0, "UTF-8");
                if (data == null || data.isEmpty()) {
                    return;
                }
                mRawDataReceived = mRawDataReceived + data;
                Log.d("Raw Data Received", data);
                processRawDataReceived();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    };

    private void processRawDataReceived() {
        //Minimum 3 bytes must be received to start processing data.
        //First byte reserved for command.
        //Second and third for Content length.
        if (mRawDataReceived.length() > 3) {
            if (!sReceivingData) {
                mCommand = mRawDataReceived.charAt(0);
                if (mCommand != 'C') {
                    return;
                }
                sReceivingData = true;
                mCountDownTimer.start();
            }
            //receiving content length.
            if (sRxDataLength == 0) {
                sRxDataLength = Character.getNumericValue(mRawDataReceived.charAt(1));
                sRxDataLength = (sRxDataLength * 10) + Character.getNumericValue(mRawDataReceived.charAt(2));
                Log.d("Length of Data Received", sRxDataLength + "");
            }
            //Checks whether data is corrupt.
            if (sRxDataLength < 0 || mCurruptDataReceived) {
                Log.d("Currupt data received", "Content:" + mRawDataReceived + ",ContentLength:" + sRxDataLength);
                resetReceiver();
                return;
            }
            //receiving data.
            if (mRawDataReceived.length() >= sRxDataLength + 3) {
                Log.d("Entire Data Received", mRawDataReceived);
                sIsRxDataAvailable = true;
                mDataReceived = mRawDataReceived.substring(0, sRxDataLength + 2);
                notifyListners(mDataReceived);
                mCountDownTimer.cancel();
                if (mRawDataReceived.length() != sRxDataLength + 3) {
                    mRawDataReceived = mRawDataReceived.substring(sRxDataLength + 3);
                    Log.d("remaining data", mRawDataReceived);
                    resetReceiver();
                    processRawDataReceived();
                } else {
                    resetReceiver();
                    mRawDataReceived = "";
                }
            }
        }
    }

    //Counter for Receiving data delays. If it takes more than 3 seconds then data is corrupted.
    //Process has to be start from beginning.
    CountDownTimer mCountDownTimer = new CountDownTimer(3000, 2000) {

        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {
            mCurruptDataReceived = false;
        }
    };


    public void init(Context context) {
        mUsbManager = (UsbManager) context.getSystemService(context.USB_SERVICE);
        HashMap<String, UsbDevice> usbDevices = mUsbManager.getDeviceList();
        if (!usbDevices.isEmpty()) {
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                mDevice = entry.getValue();
                if (mDevice.getVendorId() == 0x2341) {//Arduino Vendor ID
                    openArduinoUsbConnection();
                    break;
                } else {
                    mDeviceConnection = null;
                    mDevice = null;
                }
            }
        }
    }


    private void openArduinoUsbConnection() {
        checkUsb();
        mDeviceConnection = mUsbManager.openDevice(mDevice);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mSerialPort = UsbSerialDevice.createUsbSerialDevice(mDevice, mDeviceConnection);
        if (mDeviceConnection != null && mSerialPort != null) {
            if (mSerialPort.open()) { //Set Serial Connection Parameters.
                mSerialPort.setBaudRate(9600);
                mSerialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                mSerialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                mSerialPort.setParity(UsbSerialInterface.PARITY_NONE);
                mSerialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                mSerialPort.read(mOnReceivedData);
                Log.d("SERIAL", "Serial Connection Opened!\n");
                ((ArduinoFullStack) mContext.getApplicationContext()).setArduinoUsbDevice(mDevice);
                ((ArduinoFullStack) mContext.getApplicationContext()).setArduinoUsbConnection(mDeviceConnection);

            } else {
                Log.d("SERIAL", "PORT NOT OPEN");
            }
        } else {
            Log.d("SERIAL", "PORT IS NULL");
        }
    }

    private void checkUsb() {
        UsbManager manager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        if (mDevice != null && !manager.hasPermission(mDevice)) {
            PendingIntent mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
            manager.requestPermission(mDevice, mPermissionIntent);
            return;
        }
    }

    public int sendData(String data) {
        if (mSerialPort != null) {
            resetReceiver();
            mSerialPort.write(data.getBytes());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mSerialPort.write(data.getBytes());
            return 1; //for success
        } else {
            return -1; //for failure
        }
    }

    public void closeArduinoPort() {
        if (mDeviceConnection != null) {
            mDeviceConnection.close();
        }
    }

    public boolean isDeviceConnected() {
        if (mDevice == null || mSerialPort == null) {
            return false;
        }
        return true;
    }

    public void reconnectArduino() {
        closeArduinoPort();
        openArduinoUsbConnection();
    }

    public void setArduinoLister(ArduinoListners arduinoListners) {
        mArduinoListners = arduinoListners;
    }

    private void resetReceiver() {
        sRxDataLength = 0;
        sReceivingData = false;
        mDataReceived = null;
    }

    public void notifyListners(String data) {
        if (mArduinoListners != null) {
            mArduinoListners.onArduinoDataReceived(data);
        }
    }
}
