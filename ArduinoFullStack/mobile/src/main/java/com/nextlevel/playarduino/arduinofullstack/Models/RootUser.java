package com.nextlevel.playarduino.arduinofullstack.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sukumar on 8/11/17.
 */

public class RootUser extends AdminUser {

    private List<LinkerDevice> mLinkerDevices = new ArrayList<>();

    public List<LinkerDevice> getmLinkerDevices() {
        return mLinkerDevices;
    }

    public void setmLinkerDevices(List<LinkerDevice> mLinkerDevices) {
        this.mLinkerDevices = mLinkerDevices;
    }

    public void addLinkerDevice(LinkerDevice linkerDevice){
        mLinkerDevices.add(linkerDevice);
    }

}
