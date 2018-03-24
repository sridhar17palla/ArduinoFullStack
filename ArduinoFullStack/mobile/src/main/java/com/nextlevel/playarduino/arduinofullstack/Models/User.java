package com.nextlevel.playarduino.arduinofullstack.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sukumar on 8/7/17.
 */

public class User {

    private String mDisplayUserName;

    private String mUserName;

    private String mEmail;

    /* Paid or free version etc. */
    private String mUserType;
    /*Root or Admin or User */
    private String mPermissionLevel;

    ////////////////////////Communication Variables ////////////////////////

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

    //////////////////////////////////////////////////////////////////////


    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getUserType() {
        return mUserType;
    }

    public void setUserType(String mUserType) {
        this.mUserType = mUserType;
    }

    public String getDisplayUserName() {
        return mDisplayUserName;
    }

    public void setDisplayUserName(String mDisplayUserName) {
        this.mDisplayUserName = mDisplayUserName;
    }

    public String getPermissionLevel() {
        return mPermissionLevel;
    }

    public void setPermissionLevel(String mPermissionLevel) {
        this.mPermissionLevel = mPermissionLevel;
    }
}
