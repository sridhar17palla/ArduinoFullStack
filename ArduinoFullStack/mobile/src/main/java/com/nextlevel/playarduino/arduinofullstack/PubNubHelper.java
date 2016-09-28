package com.nextlevel.playarduino.arduinofullstack;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

/**
 * Created by sukumar on 9/26/16.
 */

public class PubNubHelper {

    private static final String PUBLISH_KEY = "";
    private static final String SUBSCRIBE_KEY = "";
    Pubnub pubnub;
    volatile String mPublishedString;
    TextView mReceivedMessage;
    Activity mActivity;

    public Callback callback = new Callback() {
        @Override
        public void connectCallback(String channel, Object message) {
            Log.d(" ADDCHANNEL : CONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void disconnectCallback(String channel, Object message) {
            Log.d("ADDCHANNEL:DISCONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        public void reconnectCallback(String channel, Object message) {
            Log.d("ADDCHANNEL: RECONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void successCallback(String channel, Object message) {
            Log.d("ADDCHANNEL: ", message.toString());
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            Log.d("ADDCHANNEL: ERROR", channel + " : " + error.toString());
        }
    };

    public Callback callbackSubscribe = new Callback() {
        @Override
        public void connectCallback(String channel, Object message) {
            Log.d("SUBSCRIBE : CONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void disconnectCallback(String channel, Object message) {
            Log.d("SUBSCRIBE : DISCONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        public void reconnectCallback(String channel, Object message) {
            Log.d("SUBSCRIBE : RECONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void successCallback(String channel, Object message) {
            Log.d(" SUBSCRIBE : ", message.toString());
            mPublishedString = message.toString();
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (mPublishedString != null) {
                        mReceivedMessage.setText(mPublishedString);
                    } else {
                        mReceivedMessage.setText("No messages received.");
                    }
                }
            }.execute();

        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            Log.d(" SUBSCRIBE : ERROR", channel + " : " + error.toString());
        }
    };

    public Callback callbackPublish = new Callback() {
        @Override
        public void connectCallback(String channel, Object message) {
            Log.d("PUBLISH : CONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void disconnectCallback(String channel, Object message) {
            Log.d(" PUBLISH : DISCONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        public void reconnectCallback(String channel, Object message) {
            Log.d(" PUBLISH : RECONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void successCallback(String channel, Object message) {
            Log.d("  PUBLISH : " + channel, message.getClass() + " : " + message.toString());
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            Log.d("  PUBLISH : ERROR", channel + " : " + error.toString());
        }
    };


    public void pubNubSetUp(Activity activity) {
        if(activity==null){
            return;
        }
        mActivity = activity;
        pubnub = new Pubnub("pub-c-67b8926d-2ebf-4553-9248-c55d54b2095a", "sub-c-138d12f2-d84f-11e5-bdd5-02ee2ddab7fe");
        String[] channels = pubnub.getSubscribedChannelsArray();
        pubnub.channelGroupAddChannel("base channel", "channel one", callback);
        mReceivedMessage = (TextView) mActivity.findViewById(R.id.received_message);
        try {
            pubnub.subscribe("channel one", callbackSubscribe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPublish(String strPublish){
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(strPublish);
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            pubnub.publish("channel one", params[0], callbackPublish);
            Log.d("PUBLISH STRING", params[0]);
            return resp;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
          /*  try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
            if (mPublishedString != null) {
                mReceivedMessage.setText(mPublishedString);
            } else {
                mReceivedMessage.setText("mPublishedString is null");
            }

        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {

            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }
}
