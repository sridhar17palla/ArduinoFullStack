package com.nextlevel.playarduino.arduinofullstack.Base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers.ArduinoUsbService;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;
import com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers.PubNubHelper;
import com.nextlevel.playarduino.arduinofullstack.R;

import java.util.Observable;
import java.util.Observer;

public class BaseActivity extends AppCompatActivity implements Observer{

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO: Not receiving broadcast message from Service
            Log.d("Received Broadcast",intent.getAction()+"");
             if(intent.getAction().equals(Constants.ACTION.RECEIVED_ACTION)){
                 onArduinoDataReceived(intent.getStringExtra(Constants.MESSAGE));
             }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        PubNubHelper.getPubNub().addObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(Constants.ACTION.BROADCAST_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void onArduinoDataReceived(final String data) {
        Log.d("Data Rx from BroadCast",data);
    }

    protected void sendArduino(String message){
        // Intent intent = new Intent(Constants.ACTION.BROADCAST_SERVICE_NOTIFICATION);
        Intent intent = new Intent(this, ArduinoUsbService.class);
        intent.setAction(Constants.ACTION.SEND_ACTION);
        intent.putExtra(Constants.MESSAGE,message);
        // sendBroadcast(intent);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PubNubHelper.getPubNub().deleteObserver(this);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        Log.d("BaseActivity","update");
    }
}
