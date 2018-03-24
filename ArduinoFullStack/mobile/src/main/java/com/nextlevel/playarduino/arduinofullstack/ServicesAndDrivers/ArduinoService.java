package com.nextlevel.playarduino.arduinofullstack.ServicesAndDrivers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.app.Notification;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;

import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;
import com.nextlevel.playarduino.arduinofullstack.R;
import com.nextlevel.playarduino.arduinofullstack.Main.HomeActivity;

import java.util.Observable;
import java.util.Observer;


/**
 * Created by sukumar on 12/25/16.
 */

public class ArduinoService extends Service implements ArduinoUsbDrivers.ArduinoListners, Observer {
    public final static String LOG_TAG = "ArduinoService";
    public static boolean IS_SERVICE_RUNNING = false;
    private ArduinoUsbDrivers mArduinoDrivers;
    private boolean mUseDummyArduino = true;

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        arduinoDriversInitialization();
        PubNubHelper pubNubHelper = PubNubHelper.getPubNub();
        pubNubHelper.addObserver(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent != null && intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received STARTFOREGROUND_ACTION Intent ");

            Intent notificationIntent = new Intent(this, HomeActivity.class);
            notificationIntent.setAction(Constants.ACTION.SEND_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Intent reconnectIntent = new Intent(this, ArduinoService.class);
            reconnectIntent.setAction(Constants.ACTION.RECONNECT_ACTION);
            PendingIntent reconnect = PendingIntent.getService(this, 0,
                    reconnectIntent, 0);

            Intent disconnectIntent = new Intent(this, ArduinoService.class);
            disconnectIntent.setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
            PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                    disconnectIntent, 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("ArduinoFullStack")
                    .setTicker("Connected to Arduino")
                    .setContentText("ArduinoFullStack")
                    .setSmallIcon(R.drawable.arduino_notification)
                    .setContentIntent(notificationPendingIntent)
                    .setOngoing(true)
                    .addAction(android.R.drawable.ic_media_play, "Reconnect", reconnect)
                    .addAction(android.R.drawable.ic_media_next, "Disconnect",
                            pnextIntent).build();
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);
        } else if (intent != null && intent.getAction().equals(Constants.ACTION.SEND_ACTION)) {
            Log.i(LOG_TAG, "Received SEND_ACTION Intent");
            sendDataToArduino(intent.getStringExtra(Constants.MESSAGE));

        } else if (intent != null && intent.getAction().equals(Constants.ACTION.RECONNECT_ACTION)) {
            Log.i(LOG_TAG, "Received RECONNECT_ACTION Intent");
            mArduinoDrivers.reconnectArduino();

        } else if (intent != null && intent.getAction().equals(Constants.ACTION.STOP_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received STOP_FOREGROUND_ACTION Intent");
            if (mArduinoDrivers != null && mArduinoDrivers.isDeviceConnected()) {
                mArduinoDrivers.closeArduinoPort();
            }
            stopForeground(true);
            stopSelf();
        }
        return START_REDELIVER_INTENT;
    }


    private void arduinoDriversInitialization() {
        if(!mUseDummyArduino) {
            mArduinoDrivers = ArduinoUsbDrivers.getArduino(this);
            mArduinoDrivers.setArduinoLister(this);
            if (!mArduinoDrivers.isDeviceConnected()) {
                mArduinoDrivers.init(this);
            } else {
                Log.d(LOG_TAG, "No device connected");
            }
        }else{
            Log.d(LOG_TAG, "Using Dummy Arduino");
        }
    }


    @Override
    public void onDestroy() {
        PubNubHelper.getPubNub().deleteObserver(this);
        super.onDestroy();
    }

    private void sendDataToArduino(String data){
        if(mUseDummyArduino){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onArduinoDataReceived(data);

        }else{
            mArduinoDrivers.sendData(data);
        }
    }

    @Override
    public void onArduinoDataReceived(String data) {




        Intent intent = new Intent(Constants.ACTION.BROADCAST_NOTIFICATION);
        intent.setAction(Constants.ACTION.RECEIVED_ACTION);
        intent.putExtra(Constants.MESSAGE, data);
        sendBroadcast(intent);
        //PubNubHelper.getPubNub().onPublish(data);
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

        // returns negative values if no Arduino is found
        if (mArduinoDrivers.sendData((String) arg) < 0) {
            stopForeground(true);
            stopSelf();
        }
    }
}
