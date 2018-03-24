package com.nextlevel.playarduino.arduinofullstack.DataBase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nextlevel.playarduino.arduinofullstack.ArduinoFullStack;
import com.nextlevel.playarduino.arduinofullstack.Models.CommanderDevice;
import com.nextlevel.playarduino.arduinofullstack.Models.LinkerDevice;
import com.nextlevel.playarduino.arduinofullstack.Models.RootUser;
import com.nextlevel.playarduino.arduinofullstack.Models.User;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;
import com.nextlevel.playarduino.arduinofullstack.Utility.Utils;

import java.util.List;


public class DataHelper {

    private static DataHelper mDataHelper = new DataHelper();
    private static Context mContext;

    private DataHelper(){

    }

    public static DataHelper getInstance(Context context){
        mContext = context;
        return mDataHelper;
    }

    // Write a message to the database
    private static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference mRoot = mDatabase.getReference();
    private static DatabaseReference mAdminNode;
    private static DatabaseReference mLinkerDevices;


    /* root -> user email -> LINKER DEVICES */

    /* Fetching Linker device is two step process.
    * 1. Fetch Linkert device all names. These names are stored in array of String just under
    * <user_name_email> with Key "LinkerDeviceNames".
    * 2. Fetch Linker device object using LinkerDevice names */

//TODO: Seems like sometimes "fetchLinkerDevice" method is not fetching any data(in time) from Firebase. In this, data should be displayed from the local database.
    public static void fetchLinkerDevice() {
        final CommanderDevice commanderDevice = ((ArduinoFullStack)mContext.getApplicationContext()).getCommanderDevice();
        if(commanderDevice.getRootUser()!=null) {

            String email = commanderDevice.getRootUser().getEmail();
            String[] set = email.split("@");
            String formattedEmail = set[0];
            mAdminNode = mRoot.child(formattedEmail);
            mLinkerDevices = mAdminNode.child("LINKER DEVICES");
            mLinkerDevices.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildren()!=null) {
                        for (DataSnapshot linkerDevices : dataSnapshot.getChildren()) {
                            LinkerDevice linkerDevice = linkerDevices.getValue(LinkerDevice.class);
                            commanderDevice.getRootUser().addLinkerDevice(linkerDevice);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


                ValueEventListener subscribedUserListner = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };


            });

            setAliveListner();

        }else {
            Log.e("fetchLinkerDevice", "AdminUser is NULL");
        }
    }




    public static void saveLinkerDeviceDemo(){
        CommanderDevice commanderDevice = ((ArduinoFullStack)mContext.getApplicationContext()).getCommanderDevice();
        if(commanderDevice.getRootUser()!=null) {
            String email = commanderDevice.getRootUser().getEmail();
            String[] set = email.split("@");
            String formattedEmail = set[0];
            mAdminNode = mRoot.child(formattedEmail);
            mLinkerDevices = mAdminNode.child("LINKER DEVICES");

            LinkerDevice demoLinkerDevice1 = new LinkerDevice();
            demoLinkerDevice1.setDeviceName("HOME");
            demoLinkerDevice1.setRootUser(commanderDevice.getRootUser());
            demoLinkerDevice1.setOperationMode("DEMO");
            demoLinkerDevice1.setRelationToCurrentUser(Constants.PERMISSION_LEVEL_ADMIN);
            demoLinkerDevice1.setAlive(true);

            User user1 = new User();
            user1.setUserName("User1");
            user1.setDisplayUserName("User1");

            User user2 = new User();
            user2.setUserName("User2");
            user2.setDisplayUserName("User2");

            demoLinkerDevice1.addSubscribedUser(user1);
            demoLinkerDevice1.addSubscribedUser(user2);


            mLinkerDevices.child(demoLinkerDevice1.getDeviceName()).setValue(demoLinkerDevice1);

            //-------------------------------------------

            LinkerDevice demoLinkerDevice2 = new LinkerDevice();
            demoLinkerDevice2.setDeviceName("OFFICE");
            demoLinkerDevice2.setRootUser(commanderDevice.getRootUser());
            demoLinkerDevice2.setOperationMode("DEMO");
            demoLinkerDevice2.setRelationToCurrentUser(Constants.PERMISSION_LEVEL_ROOT);
            demoLinkerDevice2.setAlive(true);

            User user3 = new User();
            user3.setUserName("User3");
            user3.setDisplayUserName("User3");

            User user4 = new User();
            user4.setUserName("User4");
            user4.setDisplayUserName("User4");

            User user5 = new User();
            user5.setUserName("User5");
            user5.setDisplayUserName("User5");

            demoLinkerDevice2.addSubscribedUser(user3);
            demoLinkerDevice2.addSubscribedUser(user4);
            demoLinkerDevice2.addSubscribedUser(user5);

            mLinkerDevices.child(demoLinkerDevice2.getDeviceName()).setValue(demoLinkerDevice2);

            //---------------------------------------------
            LinkerDevice demoLinkerDevice3 = new LinkerDevice();
            demoLinkerDevice3.setDeviceName("VILLAGE");
            demoLinkerDevice3.setRootUser(commanderDevice.getRootUser());
            demoLinkerDevice3.setOperationMode("DEMO");
            demoLinkerDevice3.setRelationToCurrentUser(Constants.PERMISSION_LEVEL_ADMIN);
            demoLinkerDevice3.setAlive(true);

            User user6 = new User();
            user6.setUserName("User6");
            user6.setDisplayUserName("User6");

            User user7 = new User();
            user7.setUserName("UseR7");
            user7.setDisplayUserName("User7");

            demoLinkerDevice3.addSubscribedUser(user6);
            demoLinkerDevice3.addSubscribedUser(user7);


            mLinkerDevices.child(demoLinkerDevice3.getDeviceName()).setValue(demoLinkerDevice3);


        }
    }



    //////////////////////////////SHARED PREFERENCE //////////////////////////////////

    public void updateCommanderDevice(RootUser rootUser){
        CommanderDevice commanderDevice = ((ArduinoFullStack)mContext.getApplicationContext()).getCommanderDevice();
        if(commanderDevice.getRootUser()==null){
            commanderDevice.setRootUser(rootUser);
        }
        //TODO : need to think whether to store ROOT USER object in Shared Preference or not.

        //Fetching LinkerDevice stored on this user name from FireBase.
        fetchLinkerDevice();
        //Saving DEMO data
        //saveLinkerDeviceDemo();


    }


    public static void updateLinkerDevice(LinkerDevice linkerDevice) {
        mLinkerDevices.child(linkerDevice.getDeviceName()).setValue(linkerDevice);
    }

    /*
    If this is a linker device, set listners for "alive" boolean of LinkerDevice object and
    turn them to true if set as false.
     */
    private static void setAliveListner(){

        List<LinkerDevice> linkerDevices = ((ArduinoFullStack)mContext.getApplicationContext()).getCommanderDevice().getRootUser().getmLinkerDevices();

                for(LinkerDevice linkerDevice : linkerDevices) {
                    final DatabaseReference isAliveListner = mLinkerDevices.child(linkerDevice.getDeviceName()).child("alive");
                    isAliveListner.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Boolean isAlive = dataSnapshot.getValue(Boolean.class);
                            if (!isAlive && Utils.isThisLinkerDevice) {
                                isAliveListner.setValue(true);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

    }


}
