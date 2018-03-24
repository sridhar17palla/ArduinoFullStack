package com.nextlevel.playarduino.arduinofullstack.Main.LinkerDeviceList;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nextlevel.playarduino.arduinofullstack.ArduinoFullStack;
import com.nextlevel.playarduino.arduinofullstack.Base.BaseFragment;
import com.nextlevel.playarduino.arduinofullstack.DataBase.DataHelper;
import com.nextlevel.playarduino.arduinofullstack.Models.CommanderDevice;
import com.nextlevel.playarduino.arduinofullstack.Models.LinkerDevice;
import com.nextlevel.playarduino.arduinofullstack.R;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;

import java.util.List;

/**
 * Created by sukumar on 8/14/17.
 */

public class LinkerDeviceListFragment extends BaseFragment {

    RecyclerView mLinkerDeviceList;
    LinkerDeviceListAdapter mLinkerDeviceListAdapter;
    View mRootView;

    List<LinkerDevice> mLinkerDevices;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.linker_device_list_fragment, container, false);
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mLinkerDeviceList =(RecyclerView) mRootView.findViewById(R.id.linker_device_list);

        CommanderDevice commanderDevice = ((ArduinoFullStack) getActivity().getApplicationContext()).getCommanderDevice();

        if(commanderDevice.getRootUser().getmLinkerDevices()!=null) {

            mLinkerDevices = commanderDevice.getRootUser().getmLinkerDevices();
            mLinkerDeviceListAdapter = new LinkerDeviceListAdapter(getActivity(),commanderDevice.getRootUser().getmLinkerDevices());
            mLinkerDeviceList.setLayoutManager(new LinearLayoutManager(getContext()));
            mLinkerDeviceList.setAdapter(mLinkerDeviceListAdapter);
            mRootView.findViewById(R.id.no_linker_device_message).setVisibility(View.GONE);
        }else {
            mRootView.findViewById(R.id.no_linker_device_message).setVisibility(View.VISIBLE);
        }
        //updateAllRootDeviceStatus();
    }

    //Shouldn't be called from mainthread
    /* This method sets Linked Device object variable - "alive" to false. If the LinkerDevice
    is active, it sets this boolean to "true" again. That event is catched by all the User device.
    * */
    public void updateDeviceStatus(LinkerDevice linkerDevice){
        List<LinkerDevice> mLinkerDeviceList = ((ArduinoFullStack) getActivity().getApplicationContext()).getCommanderDevice().getRootUser().getmLinkerDevices();

        for(LinkerDevice linkerDevice1: mLinkerDeviceList) {

            if(linkerDevice.getDeviceName().equalsIgnoreCase(linkerDevice1.getDeviceName())
                    && linkerDevice.getRootUser().equals(linkerDevice1.getRootUser())){
                linkerDevice1.setAlive(false);
                DataHelper.updateLinkerDevice(linkerDevice1);
            }
        }
    }

    public void updateAllRootDeviceStatus(){
        for(LinkerDevice linkerDevice: mLinkerDevices) {
            if(Constants.PERMISSION_LEVEL_ROOT.equals(linkerDevice.getRelationToCurrentUser())) {
                updateDeviceStatus(linkerDevice);
            }
        }

        /* Calling NotifyDataSetChanged after waiting for 2 seconds because after setting "alive" boolean to false,
        linker device might have set it back to true again*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mRootView.post(new Runnable() {
                    @Override
                    public void run() {
                        mLinkerDeviceListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }


}
