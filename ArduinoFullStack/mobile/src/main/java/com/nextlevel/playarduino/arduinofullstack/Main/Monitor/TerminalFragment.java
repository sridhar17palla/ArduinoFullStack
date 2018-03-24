package com.nextlevel.playarduino.arduinofullstack.Main.Monitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.nextlevel.playarduino.arduinofullstack.Base.BaseFragment;
import com.nextlevel.playarduino.arduinofullstack.R;
import com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers.ArduinoService;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class TerminalFragment extends BaseFragment {

    ListView mLogsListView;
    List mLogs = new ArrayList<String>();
    ArrayAdapter<String> mLogsArrayAdaper;
    Button mConnectionTest , mDisconnect;
    Activity mActivity;

    View mRootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.arduino_log_screen, container, false);
        mLogsListView = (ListView)mRootView.findViewById(R.id.logs_list_view);
        mLogsArrayAdaper = new ArrayAdapter<String>(this.getActivity(),R.layout.log_screen_list_view,mLogs);
        mLogsListView.setAdapter(mLogsArrayAdaper);
        mConnectionTest = (Button)mRootView.findViewById(R.id.connection_test);

        mConnectionTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendArduino("Test");
                mLogsListView.invalidate();
            }
        });
        mDisconnect = (Button)mRootView.findViewById(R.id.disconnect);
        mDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectArduino();
            }
        });
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
        Intent intent = new Intent(this.getActivity(), ArduinoService.class);
        intent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
        this.getActivity().startService(intent);
    }
}
