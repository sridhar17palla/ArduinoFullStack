package com.nextlevel.playarduino.arduinofullstack.Main;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextlevel.playarduino.arduinofullstack.ArduinoFullStack;
import com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers.ArduinoService;
import com.nextlevel.playarduino.arduinofullstack.Base.BaseFragment;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;
import com.nextlevel.playarduino.arduinofullstack.R;

/**
 * Created by sukumar on 1/18/17.
 */

public class UsbFragment extends BaseFragment {

    private UsbDevice mUsbDevice;
    private UsbDeviceConnection mDeviceConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        startAndroidUsbService();
        return inflater.inflate(R.layout.usb_connect_fragment, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        showArduinoDetails();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void startAndroidUsbService() {
        Intent service = new Intent(getActivity(), ArduinoService.class);
        if (!ArduinoService.IS_SERVICE_RUNNING) {
            service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            ArduinoService.IS_SERVICE_RUNNING = true;
            getActivity().startService(service);
        } /*else {
            service.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
            ArduinoService.IS_SERVICE_RUNNING = false;

        }
        startService(service);*/
    }

    public void showArduinoDetails(){
        String temp = null;
        mUsbDevice =  ((ArduinoFullStack)getActivity().getApplicationContext()).getArduinoUsbDevice();
        mDeviceConnection = ((ArduinoFullStack)getActivity().getApplicationContext()).getArduinoUsbConnection();

        if(mUsbDevice!=null) {
            TextView connection = (TextView) getActivity().findViewById(R.id.usb_connection_status);
            if(mDeviceConnection!=null) {
                connection.setText("Connected");
                connection.setTextColor(Color.GREEN);
            }else {
                connection.setText("Not Connected");
                connection.setTextColor(Color.RED);
            }

            TextView deviceName = (TextView) getActivity().findViewById(R.id.usb_device_name);
            temp = mUsbDevice.getDeviceName();
            deviceName.setText(temp!=null? temp : "N/A");

            TextView productId = (TextView) getActivity().findViewById(R.id.usb_product_id);
            temp = mUsbDevice.getProductId() +"";
            productId.setText(temp!=null? temp : "N/A");

            TextView vendorId = (TextView) getActivity().findViewById(R.id.usb_vendor_id);
            temp = mUsbDevice.getVendorId()+"";
            vendorId.setText(temp!=null? temp : "N/A");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                TextView manufactureName = (TextView) getActivity().findViewById(R.id.usb_manufacture_name);
                temp = mUsbDevice.getManufacturerName();
                manufactureName.setText(temp!=null? temp : "N/A");

                TextView productName = (TextView) getActivity().findViewById(R.id.usb_product_name);
                temp = mUsbDevice.getProductName();
                productName.setText(temp!=null? temp : "N/A");

                TextView serialNumber = (TextView) getActivity().findViewById(R.id.usb_serial_number);
                temp = mUsbDevice.getSerialNumber();
                serialNumber.setText(temp!=null? temp : "N/A");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    TextView deviceVersion = (TextView) getActivity().findViewById(R.id.usb_device_version);
                    temp = mUsbDevice.getVersion();
                    deviceVersion.setText(temp!=null? temp : "N/A");
                }
            }
        }
    }
}
