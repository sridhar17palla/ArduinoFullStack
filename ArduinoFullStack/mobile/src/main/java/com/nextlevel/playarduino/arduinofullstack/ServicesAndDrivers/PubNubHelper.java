package com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers;

import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by sukumar on 9/26/16.
 */

public class PubNubHelper extends Observable {

    private static final String PUBLISH_KEY = "pub-c-67b8926d-2ebf-4553-9248-c55d54b2095a";
    private static final String SUBSCRIBE_KEY = "sub-c-138d12f2-d84f-11e5-bdd5-02ee2ddab7fe";
    private static final String BASE_CHANNEL_GROUP = "base channel";
    private static final String DEFAULT_CHANNEL = "channel one";

    private Pubnub pubnub;
    volatile String mPublishedString;
    private static PubNubHelper mPubNubHelper;

    private PubNubHelper() {
        pubNubInitialization();
    }

    public static PubNubHelper getPubNub() {
        if (mPubNubHelper == null) {
            mPubNubHelper = new PubNubHelper();
            return mPubNubHelper;
        } else {
            return mPubNubHelper;
        }
    }

    public Callback callbackAddChannel = new Callback() {
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
            setChanged();
            notifyObservers(mPublishedString);
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


    private void pubNubInitialization() {
        pubnub =  new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);
        pubnub.channelGroupAddChannel(BASE_CHANNEL_GROUP, DEFAULT_CHANNEL, callbackAddChannel);
        Log.d("UUID before set",pubnub.getUUID());
        pubnub.setUUID("sridhar.java.android@gmail.com");
        Log.d("UUID after set",pubnub.getUUID());
        String[] channels = pubnub.getSubscribedChannelsArray();
        for (String channel : channels) {
            Log.d("Subsribed channel Init", channel);
        }
    }

   /* public PNConfiguration configPubNub() throws IOException {
        try {
            PNConfiguration pnconfig = new PNConfiguration();
            Properties prop = new Properties();
            InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties");

            if (inputStream != null) {
                prop.load(inputStream);
            }
            else {
                throw new FileNotFoundException("'config.properties' not found in classpath");
            }

            pnconfg.setPublishKey(prop.getProperty("publishKey"));
            pnconfg.setSubscribeKey(prop.getProperty("subscribeKey"));

            String uuid = prop.getProperty("UUID");
            if (uuid == null || uuid.equals("")) {
                uuid = java.util.UUID.randomUUID().toString();
                prop.setProperty("UUID", uuid);
            }
            pnconfg.setUUID(uuid);

            return pnconfig;
        }
        catch (Exception e) {
            // handle exception
        }
        finally {
            inputStream.close();
        }
    }*/
    public void onPublish(final String strPublish) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pubnub.publish(DEFAULT_CHANNEL, strPublish, callbackPublish);
                Log.d("PUBLISH STRING", strPublish);
            }
        }).start();
    }

    public void onPublish(final String channelName, final String strPublish) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pubnub.publish(channelName, strPublish, callbackPublish);
                Log.d("PUBLISH STRING", strPublish);
            }
        }).start();
    }

    public void onSubscribe(String channelName) {
        Log.d("onSubscribe",channelName);
        try {
            pubnub.subscribe(channelName, callbackSubscribe);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] channels2 = pubnub.getSubscribedChannelsArray();
        for (String channel : channels2) {
            Log.d("Subsribed channel", channel);
        }
    }

    @Override
    public void notifyObservers(Object O) {
        Log.d("PubNubHelper", "notifyObservers");
        super.notifyObservers(O);
    }

    public synchronized void addObserver(Observer O) {
        Log.d("PubNubHelper", "addObserver");
        super.addObserver(O);
    }
}
