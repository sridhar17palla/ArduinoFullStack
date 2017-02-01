package com.nextlevel.playarduino.arduinofullstack.Monitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.nextlevel.playarduino.arduinofullstack.Base.BaseActivity;
import com.nextlevel.playarduino.arduinofullstack.R;
import com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers.ArduinoUsbService;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class LogScreen extends BaseActivity {

    ListView mLogsListView;
    List mLogs = new ArrayList<String>();
    ArrayAdapter<String> mLogsArrayAdaper;
    Button mConnectionTest , mDisconnect;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arduino_log_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActivity = this;
        mLogsListView = (ListView)findViewById(R.id.logs_list_view);
        mLogsArrayAdaper = new ArrayAdapter<String>(this,R.layout.log_screen_list_view,mLogs);
        mLogsListView.setAdapter(mLogsArrayAdaper);
        mConnectionTest = (Button)findViewById(R.id.connection_test);

        mConnectionTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendArduino("Test");
                mLogsListView.invalidate();
            }
        });
        mDisconnect = (Button)findViewById(R.id.disconnect);
        mDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectArduino();
            }
        });

    }

    @Override
    public void onArduinoDataReceived(final String data){
        super.onArduinoDataReceived(data);
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogs.add("Arduino :"+data);
                mLogsArrayAdaper.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void sendArduino(String message){
        super.sendArduino(message);
        mLogs.add("Sent :"+ "Test");
    }
    public void disconnectArduino(){
        Intent intent = new Intent(this, ArduinoUsbService.class);
        intent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
        startService(intent);
    }
}
