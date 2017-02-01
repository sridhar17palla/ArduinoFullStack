package com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import java.util.Set;

/**
 * Created by sukumar on 1/17/17.
 */

public class ArduinoBlueToothDrivers {
    private static ArduinoBlueToothDrivers mArduinoDrivers;
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;

    private ArduinoBlueToothDrivers(Context context) {
        mContext = context;
        init(mContext);
    }

    public static ArduinoBlueToothDrivers getArduino(Context context) {
        if (mArduinoDrivers == null) {
            mArduinoDrivers = new ArduinoBlueToothDrivers(context);
            return mArduinoDrivers;
        } else {
            return mArduinoDrivers;
        }
    }

    public void init(Context context) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         //   mContext.startActivity(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    public void query() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    }

}
