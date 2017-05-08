package com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers;

import android.util.Log;

/*import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;*/

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;


/**
 * Created by sukumar on 9/26/16.
 */

public class PubNubHelper extends Observable {
    private static final String TAG = PubNubHelper.class.getName();

    private static final String PUBLISH_KEY = "pub-c-67b8926d-2ebf-4553-9248-c55d54b2095a";
    private static final String SUBSCRIBE_KEY = "sub-c-138d12f2-d84f-11e5-bdd5-02ee2ddab7fe";
    private static final String BASE_CHANNEL_GROUP = "base channel";
    private static final String DEFAULT_CHANNEL = "channel one";
    private static final List<String> MULTI_CHANNELS = new ArrayList<String>();

    private PubNub pubnub;
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

    /*public Callback callbackAddChannel = new Callback() {
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
    }*/

    @Override
    public void notifyObservers(Object O) {
        Log.d("PubNubHelper", "notifyObservers");
        super.notifyObservers(O);
    }

    public synchronized void addObserver(Observer O) {
        Log.d("PubNubHelper", "addObserver");
        super.addObserver(O);
    }

    SubscribeCallback mSubscribeCallback = new SubscribeCallback() {
        @Override
        public void status(PubNub pubnub, PNStatus status) {
        /*
        switch (status.getCategory()) {
             // for common cases to handle, see: https://www.pubnub.com/docs/java/pubnub-java-sdk-v4
             case PNStatusCategory.PNConnectedCategory:
             case PNStatusCategory.PNUnexpectedDisconnectCategory:
             case PNStatusCategory.PNReconnectedCategory:
             case PNStatusCategory.PNDecryptionErrorCategory:
         }
        */

            // no status handling for simplicity
        }

        @Override
        public void message(PubNub pubnub, PNMessageResult message) {
            try {
                Log.v("Subscribe callback", "message : " + message.getMessage().getAsString());
                mPublishedString = message.getMessage().getAsString();
                setChanged();
                notifyObservers(mPublishedString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            // no presence handling for simplicity
        }
    };

    private final void pubNubInitialization() {
        PNConfiguration config = new PNConfiguration();

        config.setPublishKey(PUBLISH_KEY);
        config.setSubscribeKey(SUBSCRIBE_KEY);
        config.setUuid("sridhar.java.android@gmail.com");
        pubnub = new PubNub(config);
        pubnub.addListener(mSubscribeCallback);
        for (String channel : pubnub.getSubscribedChannels()) {
            Log.d("Subscribed channel init", channel);
        }
    }

    public void onPublish(final String strPublish) {
        onPublish(DEFAULT_CHANNEL, strPublish);
    }

    public void onPublish(final String channelName, final String strPublish) {
        pubnub.publish().channel(channelName).message(strPublish).async(
                new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        try {
                            if (!status.isError()) {
                                Log.v(TAG, "publish result :" + result);
                            } else {
                                Log.v(TAG, "publish result :" + result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void onSubscribe(String channelName) {
        MULTI_CHANNELS.add(channelName);
        pubnub.subscribe().channels(MULTI_CHANNELS).execute();
        for (String channel : pubnub.getSubscribedChannels()) {
            Log.d("Subscribed channel", channel);
        }
    }
}
