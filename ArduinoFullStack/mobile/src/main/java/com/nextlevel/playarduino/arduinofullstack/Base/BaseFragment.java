package com.nextlevel.playarduino.arduinofullstack.Base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers.ArduinoService;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;
import com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers.PubNubHelper;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by sukumar on 1/19/17.
 */

public class BaseFragment extends Fragment implements Observer {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        PubNubHelper.getPubNub().addObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        PubNubHelper.getPubNub().deleteObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void sendArduino(String message) {
        // Intent intent = new Intent(Constants.ACTION.BROADCAST_SERVICE_NOTIFICATION);
        Intent intent = new Intent(getActivity(), ArduinoService.class);
        intent.setAction(Constants.ACTION.SEND_ACTION);
        intent.putExtra(Constants.MESSAGE, message);
        // sendBroadcast(intent);
        getActivity().startService(intent);
    }

    public void onArduinoDataReceived(final String data) {
        Log.d("Data Rx from BroadCast", data);
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

    }
}
