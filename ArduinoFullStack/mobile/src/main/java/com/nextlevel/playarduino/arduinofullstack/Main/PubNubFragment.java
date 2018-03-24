package com.nextlevel.playarduino.arduinofullstack.Main;

import android.app.Activity;
import android.content.Context;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nextlevel.playarduino.arduinofullstack.ArduinoFullStack;
import com.nextlevel.playarduino.arduinofullstack.Base.BaseFragment;
import com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers.PubNubHelper;
import com.nextlevel.playarduino.arduinofullstack.R;

import java.util.Observable;

/**
 * Created by sukumar on 1/18/17.
 */

public class PubNubFragment extends BaseFragment {

    private EditText mPublishMessage, mPublishChannel, mPublishChannelGroup, mSubscribeChannel;
    private TextView mReceivedMessage;
    private Button mPublishButton, mSubscribeButton;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        mContext = getActivity();
        View rootView = inflater.inflate(R.layout.andruino_fragment, container, false);
        mReceivedMessage = (TextView) rootView.findViewById(R.id.received_message);

        mPublishMessage = (EditText) rootView.findViewById(R.id.publish_message);
        mPublishChannel = (EditText) rootView.findViewById(R.id.channal_name);
        mPublishChannelGroup = (EditText) rootView.findViewById(R.id.channal_group_name);
        mSubscribeChannel = (EditText) rootView.findViewById(R.id.subscribe_channel);

        mPublishButton = (Button) rootView.findViewById(R.id.publish_button);
        mSubscribeButton= (Button) rootView.findViewById(R.id.subscribe_button);
        mPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPublish(v);
            }
        });
        mSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubscribe(v);
            }
        });
        return rootView;
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

    public void onPublish(View v) {
        String channelName = mPublishChannel.getText().toString();
        if (ArduinoFullStack.mPubNubHelper == null) {
            ArduinoFullStack.mPubNubHelper = PubNubHelper.getPubNub();
        }
        if (channelName == null || channelName.isEmpty()) {
            ArduinoFullStack.mPubNubHelper.onPublish(mPublishMessage.getText().toString());
        } else {
            ArduinoFullStack.mPubNubHelper.onPublish(channelName, mPublishMessage.getText().toString());
        }
    }

    public void onSubscribe(View v) {
        String channelName = mSubscribeChannel.getText().toString();
        if (ArduinoFullStack.mPubNubHelper == null) {
            ArduinoFullStack.mPubNubHelper = PubNubHelper.getPubNub();
        }
        if(channelName!=null && !channelName.isEmpty()){
            ArduinoFullStack.mPubNubHelper.onSubscribe(channelName);
        }
    }

    @Override
    public void update(Observable o, final Object arg) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mReceivedMessage.setText((String) arg);
                sendArduino((String) arg);
            }
        });
    }
}
