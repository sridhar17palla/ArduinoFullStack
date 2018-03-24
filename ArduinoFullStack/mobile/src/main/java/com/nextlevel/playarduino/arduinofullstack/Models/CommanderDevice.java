package com.nextlevel.playarduino.arduinofullstack.Models;

/**
 * Created by sukumar on 8/7/17.
 */

public class CommanderDevice {

    private RootUser mRootUser;
    /* Paid or free version user. */
    private String mUserType;

    public RootUser getRootUser() {
        return mRootUser;
    }

    public void setRootUser(RootUser mRootUser) {
        this.mRootUser = mRootUser;
    }

    public String getUserType() {
        return mUserType;
    }

    public void setUserType(String mUserType) {
        this.mUserType = mUserType;
    }
}
