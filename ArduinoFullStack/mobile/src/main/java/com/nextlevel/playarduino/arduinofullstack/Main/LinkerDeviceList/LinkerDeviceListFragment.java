package com.nextlevel.playarduino.arduinofullstack.Main.LinkerDeviceList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nextlevel.playarduino.arduinofullstack.ArduinoFullStack;
import com.nextlevel.playarduino.arduinofullstack.Base.BaseFragment;
import com.nextlevel.playarduino.arduinofullstack.DataBase.DataHelper;
import com.nextlevel.playarduino.arduinofullstack.Models.CommanderDevice;
import com.nextlevel.playarduino.arduinofullstack.Models.LinkerDevice;
import com.nextlevel.playarduino.arduinofullstack.R;
import com.nextlevel.playarduino.arduinofullstack.Utility.AnimationUtils;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;

import java.util.List;

/**
 * Created by sukumar on 8/14/17.
 */

public class LinkerDeviceListFragment extends BaseFragment {

    LinearLayout mLinkerDeviceList;
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

        mLinkerDeviceList =(LinearLayout) mRootView.findViewById(R.id.linker_device_list);

        CommanderDevice commanderDevice = ((ArduinoFullStack) getActivity().getApplicationContext()).getCommanderDevice();

        if(commanderDevice.getRootUser().getmLinkerDevices()!=null) {

            mLinkerDevices = commanderDevice.getRootUser().getmLinkerDevices();
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for(int i=0; i<mLinkerDevices.size(); i++){
                final LinkerDevice linkerDevice = mLinkerDevices.get(i);
                View linkerView = inflater.inflate(R.layout.linker_device_list_row,null);
                mLinkerDeviceList.addView(linkerView, i);

                TextView linkerDeviceName = (TextView) linkerView.findViewById(R.id.linker_device_name);
                TextView linkerDeviceRootUser = (TextView) linkerView.findViewById(R.id.linker_device_root_user);
                TextView adminText = (TextView) linkerView.findViewById(R.id.admin_text);
                ImageView linkerDeviceStatusIndicator = (ImageView) linkerView.findViewById(R.id.device_status_indicator);
                TextView mLastMessage = (TextView) linkerView.findViewById(R.id.last_message);
                Button expandCollapseButton = (Button) linkerView.findViewById(R.id.expand_collapse_button);

                linkerDeviceName.setText(linkerDevice.getDeviceName());
                linkerDeviceRootUser.setText(linkerDevice.getRootUser().getUserName());
                if (Constants.PERMISSION_LEVEL_ROOT.equals(linkerDevice.getRelationToCurrentUser())) {
                    adminText.setVisibility(View.VISIBLE);
                    adminText.setText(getResources().getString(R.string.root_user));
                    adminText.setTextColor(getResources().getColor(R.color.red));
                } else if (Constants.PERMISSION_LEVEL_ADMIN.equals(linkerDevice.getRelationToCurrentUser())) {
                    adminText.setVisibility(View.VISIBLE);
                    adminText.setText(getResources().getString(R.string.admin_user));
                }
                linkerDeviceStatusIndicator.setSelected(linkerDevice.isAlive());

                //TODO : check time stamp whether to display last command sent or last message received from
                //Linker device for now showing the last command received by the LinkerDevice.

                mLastMessage.setText(linkerDevice.getInput());

                linkerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        Fragment fragment = null;
                        if (fragment == null) {
                            fragment = new LinkerDeviceDetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(LinkerDeviceDetailsFragment.LINKER_DEVICE_DETAILS, new Gson().toJson(linkerDevice));
                            fragment.setArguments(bundle);
                            fragmentManager.beginTransaction()
                                    .add(R.id.fragments, fragment)
                                    .commit();
                        }

                    }
                });


                final RecyclerView recyclerView =(RecyclerView) linkerView.findViewById(R.id.subscribed_users);
                SubscribedUserListAdapter linkerDeviceListAdapter = new SubscribedUserListAdapter(getActivity(),linkerDevice.getSubscribedUsers(),recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(linkerDeviceListAdapter);

                expandCollapseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean visibility = "-".equals(((Button) v).getText());
                        if(visibility){
                            AnimationUtils.collapse(recyclerView);
                            //recyclerView.setVisibility(View.GONE);
                           /* recyclerView.animate()
                                    .setDuration(500)
                                    .alpha(0.0f)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            recyclerView.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    })
                                    .start();*/
                            ((Button) v).setText("+");
                        }else {
                            AnimationUtils.expand(recyclerView);
                           // recyclerView.setAlpha(0.0f);
                           // recyclerView.setVisibility(View.VISIBLE);
                            /*recyclerView.animate()
                                    .setDuration(500)
                                    .alpha(1.0f)
                                    .start();*/
                            ((Button) v).setText("-");
                        }

                    }
                });
            }
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

        //TODO-LOGIC : need to refactor following code too because main RecyclerView has been refactored.
      /*  new Thread(new Runnable() {
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
        }).start(); */

    }


}
