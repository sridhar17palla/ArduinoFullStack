package com.nextlevel.playarduino.arduinofullstack.Utility;

/**
 * Created by sukumar on 1/1/17.
 */

public class Constants {

    ////////////////////USER TYPES ////////////////////////////

    public static final String USER_TYPE_PAID_USER = "paid user";
    public static final String USER_TYPE_FREE_USER = "free user";


    public static final String PERMISSION_LEVEL_ROOT = "root";
    public static final String PERMISSION_LEVEL_ADMIN = "admin";
    public static final String PERMISSION_LEVEL_USER = "user";




    public static final String MESSAGE = "message";
    public static final String DEVICE_DETAILS = "device details";
    public static final String DEVICE_CONNECTION_DETAILS = "device connection details";
    public interface ACTION {
        public static final String BROADCAST_NOTIFICATION = "com.nextlevel.playarduino.arduinofullstack";
        public static String SEND_ACTION = "com.nextlevel.playarduino.arduinofullstack.action.send";
        public static String RECEIVED_ACTION = "com.nextlevel.playarduino.arduinofullstack.action.received";
        public static String PREV_ACTION = "com.nextlevel.playarduino.arduinofullstack.action.prev";
        public static String RECONNECT_ACTION = "com.nextlevel.playarduino.arduinofullstack.action.reconnect";
        public static String NEXT_ACTION = "com.nextlevel.playarduino.arduinofullstack.action.next";
        public static String STARTFOREGROUND_ACTION = "com.nextlevel.playarduino.arduinofullstack.action.startforeground";
        public static String STOP_FOREGROUND_ACTION = "com.nextlevel.playarduino.arduinofullstack.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
