package com.nextlevel.playarduino.arduinofullstack.Models;

import com.nextlevel.playarduino.arduinofullstack.Main.Schema.Schema;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sukumar on 8/7/17.
 */

public class LinkerDevice {

    /* every linker should have one RootUser */
    private RootUser mRootUser;
    /* Connected Arduino Device */
    private ArduinoDevice mArduinoDevice;
    /* the way linker should operate like Power Saving mode, Data usage etc */
    private String mOperationMode;

    private String mDeviceName;

    private Schema[] mSchemaList;

    ///////////////////Communication variables////////////////
    private String input;

    private String output;

    private boolean alive;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    ///////////////////////////////////////////////////////////

    /* This variable indicates the relation of this LinkerDevice object to current user.
    * It can take values as "Root", "Admin", "User"*/
    private String mRelationToCurrentUser = Constants.PERMISSION_LEVEL_USER;

    private List<User> mSubscribedUsers = new ArrayList<User>();

    public RootUser getRootUser() {
        return mRootUser;
    }

    public void setRootUser(RootUser mRootUser) {
        this.mRootUser = mRootUser;
    }

    public ArduinoDevice getArduinoDevice() {
        return mArduinoDevice;
    }

    public void setArduinoDevice(ArduinoDevice mArduinoDevice) {
        this.mArduinoDevice = mArduinoDevice;
    }

    public String getOperationMode() {
        return mOperationMode;
    }

    public void setOperationMode(String mOperationMode) {
        this.mOperationMode = mOperationMode;
    }

    public List<User> getSubscribedUsers() {
        return mSubscribedUsers;
    }

    public void setSubscribedUsers(List<User> mSubscribedUsers) {
        this.mSubscribedUsers = mSubscribedUsers;
    }

    public void addSubscribedUser(User user) {
        mSubscribedUsers.add(user);
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }

    public String getRelationToCurrentUser() {
        return mRelationToCurrentUser;
    }

    public void setRelationToCurrentUser(String mRelationToCurrentUser) {
        this.mRelationToCurrentUser = mRelationToCurrentUser;
    }

    public Schema[] getSchemaList() {
        return mSchemaList;
    }

    public void setSchemaList(Schema[] mSchemaList) {
        this.mSchemaList = mSchemaList;
    }

}
