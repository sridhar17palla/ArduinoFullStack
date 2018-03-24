package com.nextlevel.playarduino.arduinofullstack.Main.LinkerDeviceList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nextlevel.playarduino.arduinofullstack.Base.BaseFragment;
import com.nextlevel.playarduino.arduinofullstack.Main.Monitor.TerminalFragment;
import com.nextlevel.playarduino.arduinofullstack.Main.Schema.SchemaEditFragment;
import com.nextlevel.playarduino.arduinofullstack.Main.Schema.SchemaFragment;
import com.nextlevel.playarduino.arduinofullstack.Main.ServerDevice.ServerDeviceFragment;
import com.nextlevel.playarduino.arduinofullstack.Models.LinkerDevice;
import com.nextlevel.playarduino.arduinofullstack.R;
import com.nextlevel.playarduino.arduinofullstack.Utility.Constants;
import com.nextlevel.playarduino.arduinofullstack.WebRTC.CallActivity;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Random;


public class LinkerDeviceDetailsFragment extends BaseFragment {

    public static String TAG = "LinkerDeviceDetailsFragment";
    private static final int CONNECTION_REQUEST = 1;
    private static final int REMOVE_FAVORITE_INDEX = 0;
    
    SchemaEditFragment mSchemaFragment;
    TerminalFragment mTerminalFragment;
    ServerDeviceFragment mServerDeviceFragment;
    LinkerDevice mLinkerDevice;

    private SharedPreferences sharedPref;
    private String keyprefVideoCallEnabled;
    private String keyprefScreencapture;
    private String keyprefCamera2;
    private String keyprefResolution;
    private String keyprefFps;
    private String keyprefCaptureQualitySlider;
    private String keyprefVideoBitrateType;
    private String keyprefVideoBitrateValue;
    private String keyprefVideoCodec;
    private String keyprefAudioBitrateType;
    private String keyprefAudioBitrateValue;
    private String keyprefAudioCodec;
    private String keyprefHwCodecAcceleration;
    private String keyprefCaptureToTexture;
    private String keyprefFlexfec;
    private String keyprefNoAudioProcessingPipeline;
    private String keyprefAecDump;
    private String keyprefOpenSLES;
    private String keyprefDisableBuiltInAec;
    private String keyprefDisableBuiltInAgc;
    private String keyprefDisableBuiltInNs;
    private String keyprefEnableLevelControl;
    private String keyprefDisableWebRtcAGCAndHPF;
    private String keyprefDisplayHud;
    private String keyprefTracing;
    private String keyprefRoomServerUrl;
    private String keyprefRoom;
    private String keyprefRoomList;
    private ArrayList<String> roomList;
    private ArrayAdapter<String> adapter;
    private String keyprefEnableDataChannel;
    private String keyprefOrdered;
    private String keyprefMaxRetransmitTimeMs;
    private String keyprefMaxRetransmits;
    private String keyprefDataProtocol;
    private String keyprefNegotiated;
    private String keyprefDataId;

    static String LINKER_DEVICE_DETAILS = "linkerlevicedetailsfragment.linkerobject";
    private Intent dummyIntent = new Intent();
    /*
    This fragment, shows/edit attributes of LinkerDevice only.
    In this fragment, operator should able to edit all or few attributes of linker device.
    If operator is ROOT, he should be able change the state of the device like
    not available/available/under maintainance etc. Operator should be able to switch available
    features on this device. These features kind of defines LinkerDevice. A simple LED connected
    Arduino, need not have access to all other pins for other users.
     */


    View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        // Get setting keys.
        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        keyprefVideoCallEnabled = getString(R.string.pref_videocall_key);
        keyprefScreencapture = getString(R.string.pref_screencapture_key);
        keyprefCamera2 = getString(R.string.pref_camera2_key);
        keyprefResolution = getString(R.string.pref_resolution_key);
        keyprefFps = getString(R.string.pref_fps_key);
        keyprefCaptureQualitySlider = getString(R.string.pref_capturequalityslider_key);
        keyprefVideoBitrateType = getString(R.string.pref_maxvideobitrate_key);
        keyprefVideoBitrateValue = getString(R.string.pref_maxvideobitratevalue_key);
        keyprefVideoCodec = getString(R.string.pref_videocodec_key);
        keyprefHwCodecAcceleration = getString(R.string.pref_hwcodec_key);
        keyprefCaptureToTexture = getString(R.string.pref_capturetotexture_key);
        keyprefFlexfec = getString(R.string.pref_flexfec_key);
        keyprefAudioBitrateType = getString(R.string.pref_startaudiobitrate_key);
        keyprefAudioBitrateValue = getString(R.string.pref_startaudiobitratevalue_key);
        keyprefAudioCodec = getString(R.string.pref_audiocodec_key);
        keyprefNoAudioProcessingPipeline = getString(R.string.pref_noaudioprocessing_key);
        keyprefAecDump = getString(R.string.pref_aecdump_key);
        keyprefOpenSLES = getString(R.string.pref_opensles_key);
        keyprefDisableBuiltInAec = getString(R.string.pref_disable_built_in_aec_key);
        keyprefDisableBuiltInAgc = getString(R.string.pref_disable_built_in_agc_key);
        keyprefDisableBuiltInNs = getString(R.string.pref_disable_built_in_ns_key);
        keyprefEnableLevelControl = getString(R.string.pref_enable_level_control_key);
        keyprefDisableWebRtcAGCAndHPF = getString(R.string.pref_disable_webrtc_agc_and_hpf_key);
        keyprefDisplayHud = getString(R.string.pref_displayhud_key);
        keyprefTracing = getString(R.string.pref_tracing_key);
        keyprefRoomServerUrl = getString(R.string.pref_room_server_url_key);
        keyprefRoom = getString(R.string.pref_room_key);
        keyprefRoomList = getString(R.string.pref_room_list_key);
        keyprefEnableDataChannel = getString(R.string.pref_enable_datachannel_key);
        keyprefOrdered = getString(R.string.pref_ordered_key);
        keyprefMaxRetransmitTimeMs = getString(R.string.pref_max_retransmit_time_ms_key);
        keyprefMaxRetransmits = getString(R.string.pref_max_retransmits_key);
        keyprefDataProtocol = getString(R.string.pref_data_protocol_key);
        keyprefNegotiated = getString(R.string.pref_negotiated_key);
        keyprefDataId = getString(R.string.pref_data_id_key);



        mLinkerDevice = new Gson().fromJson(getArguments().getString(LINKER_DEVICE_DETAILS), LinkerDevice.class);
        mRootView = inflater.inflate(R.layout.linker_device_details_view, container, false);
        ViewPager viewPager = (ViewPager) mRootView.findViewById(R.id.device_viewpager);
        viewPager.setAdapter(new DeviceViewPagerAdapter(getActivity().getSupportFragmentManager()));
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        displayLinkerDeviceDetails();
    }

    private void displayLinkerDeviceDetails() {
        if (mLinkerDevice != null) {
            TextView deviceName = (TextView) mRootView.findViewById(R.id.linker_device_name);
            deviceName.setText(mLinkerDevice.getDeviceName());

            ImageView statusIndicator = (ImageView) mRootView.findViewById(R.id.device_status_indicator);
            statusIndicator.setSelected(mLinkerDevice.isAlive());

            TextView rootUserName = (TextView) mRootView.findViewById(R.id.linker_device_root_user);
            rootUserName.setText(mLinkerDevice.getRootUser().getDisplayUserName());

            TextView recentMessage = (TextView) mRootView.findViewById(R.id.last_message);
            recentMessage.setText(mLinkerDevice.getInput());

            TextView recentMessageTime = (TextView) mRootView.findViewById(R.id.last_message_time);
            recentMessageTime.setText(mLinkerDevice.getInput()); //TODO : need to check this data.

            TextView adminText = (TextView) mRootView.findViewById(R.id.admin_text);
            adminText.setText(mLinkerDevice.getInput()); //TODO : need to check this data.

            if (Constants.PERMISSION_LEVEL_ROOT.equals(mLinkerDevice.getRelationToCurrentUser())) {
                adminText.setVisibility(View.VISIBLE);
                adminText.setText(getResources().getString(R.string.root_user));
                adminText.setTextColor(getResources().getColor(R.color.red));
            } else if (Constants.PERMISSION_LEVEL_ADMIN.equals(mLinkerDevice.getRelationToCurrentUser())) {
                adminText.setVisibility(View.VISIBLE);
                adminText.setText(getResources().getString(R.string.admin_user));
            }
        } else {
            Logger.e(TAG, "LinkerDevice is null");
        }
    }


    private class DeviceViewPagerAdapter extends FragmentPagerAdapter {

        public DeviceViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    if (mSchemaFragment == null) {
                        //TODO DESIGN 2 : NEED TO SHOW VOID MESSAGE WHEN REMOTE IS NOT CONFIGURED INITIALLY.
                        mSchemaFragment = new SchemaFragment();
                        connectToRoom("wow100",false,false,false,0);
                    }
                    return mSchemaFragment;
                case 1:
                    if (mTerminalFragment == null) {
                        mTerminalFragment = new TerminalFragment();
                    }
                    return mTerminalFragment;
                case 2:
                    if (mServerDeviceFragment == null) {
                        mServerDeviceFragment = new ServerDeviceFragment();
                    }
                    return mServerDeviceFragment;
                default:
                    if (mSchemaFragment == null) {
                        mSchemaFragment = new SchemaFragment();
                    }
                    return mTerminalFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }




    //TODO : Move below code to some WebRTC Utility class.
    //////////////////////////////////////////WEBRTC/////////////////////////////////////////////


    private void connectToRoom(String roomId, boolean commandLineRun, boolean loopback,
                               boolean useValuesFromIntent, int runTimeMs) {
        //this.commandLineRun = commandLineRun;

        // roomId is random for loopback.
        if (loopback) {
            roomId = Integer.toString((new Random()).nextInt(100000000));
        }

        String roomUrl = sharedPref.getString(
                keyprefRoomServerUrl, getString(R.string.pref_room_server_url_default));

        // Video call enabled flag.
        boolean videoCallEnabled = sharedPrefGetBoolean(R.string.pref_videocall_key,
                CallActivity.EXTRA_VIDEO_CALL, R.string.pref_videocall_default, useValuesFromIntent);

        // Use screencapture option.
        boolean useScreencapture = sharedPrefGetBoolean(R.string.pref_screencapture_key,
                CallActivity.EXTRA_SCREENCAPTURE, R.string.pref_screencapture_default, useValuesFromIntent);

        // Use Camera2 option.
        boolean useCamera2 = sharedPrefGetBoolean(R.string.pref_camera2_key, CallActivity.EXTRA_CAMERA2,
                R.string.pref_camera2_default, useValuesFromIntent);

        // Get default codecs.
        String videoCodec = sharedPrefGetString(R.string.pref_videocodec_key,
                CallActivity.EXTRA_VIDEOCODEC, R.string.pref_videocodec_default, useValuesFromIntent);
        String audioCodec = sharedPrefGetString(R.string.pref_audiocodec_key,
                CallActivity.EXTRA_AUDIOCODEC, R.string.pref_audiocodec_default, useValuesFromIntent);

        // Check HW codec flag.
        boolean hwCodec = sharedPrefGetBoolean(R.string.pref_hwcodec_key,
                CallActivity.EXTRA_HWCODEC_ENABLED, R.string.pref_hwcodec_default, useValuesFromIntent);

        // Check Capture to texture.
        boolean captureToTexture = sharedPrefGetBoolean(R.string.pref_capturetotexture_key,
                CallActivity.EXTRA_CAPTURETOTEXTURE_ENABLED, R.string.pref_capturetotexture_default,
                useValuesFromIntent);

        // Check FlexFEC.
        boolean flexfecEnabled = sharedPrefGetBoolean(R.string.pref_flexfec_key,
                CallActivity.EXTRA_FLEXFEC_ENABLED, R.string.pref_flexfec_default, useValuesFromIntent);

        // Check Disable Audio Processing flag.
        boolean noAudioProcessing = sharedPrefGetBoolean(R.string.pref_noaudioprocessing_key,
                CallActivity.EXTRA_NOAUDIOPROCESSING_ENABLED, R.string.pref_noaudioprocessing_default,
                useValuesFromIntent);

        // Check Disable Audio Processing flag.
        boolean aecDump = sharedPrefGetBoolean(R.string.pref_aecdump_key,
                CallActivity.EXTRA_AECDUMP_ENABLED, R.string.pref_aecdump_default, useValuesFromIntent);

        // Check OpenSL ES enabled flag.
        boolean useOpenSLES = sharedPrefGetBoolean(R.string.pref_opensles_key,
                CallActivity.EXTRA_OPENSLES_ENABLED, R.string.pref_opensles_default, useValuesFromIntent);

        // Check Disable built-in AEC flag.
        boolean disableBuiltInAEC = sharedPrefGetBoolean(R.string.pref_disable_built_in_aec_key,
                CallActivity.EXTRA_DISABLE_BUILT_IN_AEC, R.string.pref_disable_built_in_aec_default,
                useValuesFromIntent);

        // Check Disable built-in AGC flag.
        boolean disableBuiltInAGC = sharedPrefGetBoolean(R.string.pref_disable_built_in_agc_key,
                CallActivity.EXTRA_DISABLE_BUILT_IN_AGC, R.string.pref_disable_built_in_agc_default,
                useValuesFromIntent);

        // Check Disable built-in NS flag.
        boolean disableBuiltInNS = sharedPrefGetBoolean(R.string.pref_disable_built_in_ns_key,
                CallActivity.EXTRA_DISABLE_BUILT_IN_NS, R.string.pref_disable_built_in_ns_default,
                useValuesFromIntent);

        // Check Enable level control.
        boolean enableLevelControl = sharedPrefGetBoolean(R.string.pref_enable_level_control_key,
                CallActivity.EXTRA_ENABLE_LEVEL_CONTROL, R.string.pref_enable_level_control_key,
                useValuesFromIntent);

        // Check Disable gain control
        boolean disableWebRtcAGCAndHPF = sharedPrefGetBoolean(
                R.string.pref_disable_webrtc_agc_and_hpf_key, CallActivity.EXTRA_DISABLE_WEBRTC_AGC_AND_HPF,
                R.string.pref_disable_webrtc_agc_and_hpf_key, useValuesFromIntent);

        // Get video resolution from settings.
        int videoWidth = 0;
        int videoHeight = 0;
        if (useValuesFromIntent) {
            videoWidth = dummyIntent.getIntExtra(CallActivity.EXTRA_VIDEO_WIDTH, 0);
            videoHeight = dummyIntent.getIntExtra(CallActivity.EXTRA_VIDEO_HEIGHT, 0);
        }
        if (videoWidth == 0 && videoHeight == 0) {
            String resolution =
                    sharedPref.getString(keyprefResolution, getString(R.string.pref_resolution_default));
            String[] dimensions = resolution.split("[ x]+");
            if (dimensions.length == 2) {
                try {
                    videoWidth = Integer.parseInt(dimensions[0]);
                    videoHeight = Integer.parseInt(dimensions[1]);
                } catch (NumberFormatException e) {
                    videoWidth = 0;
                    videoHeight = 0;
                    //Log.e(TAG, "Wrong video resolution setting: " + resolution);
                }
            }
        }

        // Get camera fps from settings.
        int cameraFps = 0;
        if (useValuesFromIntent) {
            cameraFps = dummyIntent.getIntExtra(CallActivity.EXTRA_VIDEO_FPS, 0);
        }
        if (cameraFps == 0) {
            String fps = sharedPref.getString(keyprefFps, getString(R.string.pref_fps_default));
            String[] fpsValues = fps.split("[ x]+");
            if (fpsValues.length == 2) {
                try {
                    cameraFps = Integer.parseInt(fpsValues[0]);
                } catch (NumberFormatException e) {
                    cameraFps = 0;
                    //Log.e(TAG, "Wrong camera fps setting: " + fps);
                }
            }
        }

        // Check capture quality slider flag.
        boolean captureQualitySlider = sharedPrefGetBoolean(R.string.pref_capturequalityslider_key,
                CallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED,
                R.string.pref_capturequalityslider_default, useValuesFromIntent);

        // Get video and audio start bitrate.
        int videoStartBitrate = 0;
        if (useValuesFromIntent) {
            videoStartBitrate = dummyIntent.getIntExtra(CallActivity.EXTRA_VIDEO_BITRATE, 0);
        }
        if (videoStartBitrate == 0) {
            String bitrateTypeDefault = getString(R.string.pref_maxvideobitrate_default);
            String bitrateType = sharedPref.getString(keyprefVideoBitrateType, bitrateTypeDefault);
            if (!bitrateType.equals(bitrateTypeDefault)) {
                String bitrateValue = sharedPref.getString(
                        keyprefVideoBitrateValue, getString(R.string.pref_maxvideobitratevalue_default));
                videoStartBitrate = Integer.parseInt(bitrateValue);
            }
        }

        int audioStartBitrate = 0;
        if (useValuesFromIntent) {
            audioStartBitrate = dummyIntent.getIntExtra(CallActivity.EXTRA_AUDIO_BITRATE, 0);
        }
        if (audioStartBitrate == 0) {
            String bitrateTypeDefault = getString(R.string.pref_startaudiobitrate_default);
            String bitrateType = sharedPref.getString(keyprefAudioBitrateType, bitrateTypeDefault);
            if (!bitrateType.equals(bitrateTypeDefault)) {
                String bitrateValue = sharedPref.getString(
                        keyprefAudioBitrateValue, getString(R.string.pref_startaudiobitratevalue_default));
                audioStartBitrate = Integer.parseInt(bitrateValue);
            }
        }

        // Check statistics display option.
        boolean displayHud = sharedPrefGetBoolean(R.string.pref_displayhud_key,
                CallActivity.EXTRA_DISPLAY_HUD, R.string.pref_displayhud_default, useValuesFromIntent);

        boolean tracing = sharedPrefGetBoolean(R.string.pref_tracing_key, CallActivity.EXTRA_TRACING,
                R.string.pref_tracing_default, useValuesFromIntent);

        // Get datachannel options
        boolean dataChannelEnabled = sharedPrefGetBoolean(R.string.pref_enable_datachannel_key,
                CallActivity.EXTRA_DATA_CHANNEL_ENABLED, R.string.pref_enable_datachannel_default,
                useValuesFromIntent);
        boolean ordered = sharedPrefGetBoolean(R.string.pref_ordered_key, CallActivity.EXTRA_ORDERED,
                R.string.pref_ordered_default, useValuesFromIntent);
        boolean negotiated = sharedPrefGetBoolean(R.string.pref_negotiated_key,
                CallActivity.EXTRA_NEGOTIATED, R.string.pref_negotiated_default, useValuesFromIntent);
        int maxRetrMs = sharedPrefGetInteger(R.string.pref_max_retransmit_time_ms_key,
                CallActivity.EXTRA_MAX_RETRANSMITS_MS, R.string.pref_max_retransmit_time_ms_default,
                useValuesFromIntent);
        int maxRetr =
                sharedPrefGetInteger(R.string.pref_max_retransmits_key, CallActivity.EXTRA_MAX_RETRANSMITS,
                        R.string.pref_max_retransmits_default, useValuesFromIntent);
        int id = sharedPrefGetInteger(R.string.pref_data_id_key, CallActivity.EXTRA_ID,
                R.string.pref_data_id_default, useValuesFromIntent);
        String protocol = sharedPrefGetString(R.string.pref_data_protocol_key,
                CallActivity.EXTRA_PROTOCOL, R.string.pref_data_protocol_default, useValuesFromIntent);

        // Start AppRTCMobile activity.
        //Log.d(TAG, "Connecting to room " + roomId + " at URL " + roomUrl);
        if (validateUrl(roomUrl)) {
            Uri uri = Uri.parse(roomUrl);
            Bundle bundle = new Bundle();
            //Intent bundle = new Intent(this, CallActivity.class);
            bundle.putString(SchemaFragment.ROOM_URI,roomUrl);
            bundle.putString(CallActivity.EXTRA_ROOMID, roomId);
            bundle.putBoolean(CallActivity.EXTRA_LOOPBACK, loopback);
            bundle.putBoolean(CallActivity.EXTRA_VIDEO_CALL, videoCallEnabled);
            bundle.putBoolean(CallActivity.EXTRA_SCREENCAPTURE, useScreencapture);
            bundle.putBoolean(CallActivity.EXTRA_CAMERA2, useCamera2);
            bundle.putInt(CallActivity.EXTRA_VIDEO_WIDTH, videoWidth);
            bundle.putInt(CallActivity.EXTRA_VIDEO_HEIGHT, videoHeight);
            bundle.putInt(CallActivity.EXTRA_VIDEO_FPS, cameraFps);
            bundle.putBoolean(CallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED, captureQualitySlider);
            bundle.putInt(CallActivity.EXTRA_VIDEO_BITRATE, videoStartBitrate);
            bundle.putString(CallActivity.EXTRA_VIDEOCODEC, videoCodec);
            bundle.putBoolean(CallActivity.EXTRA_HWCODEC_ENABLED, hwCodec);
            bundle.putBoolean(CallActivity.EXTRA_CAPTURETOTEXTURE_ENABLED, captureToTexture);
            bundle.putBoolean(CallActivity.EXTRA_FLEXFEC_ENABLED, flexfecEnabled);
            bundle.putBoolean(CallActivity.EXTRA_NOAUDIOPROCESSING_ENABLED, noAudioProcessing);
            bundle.putBoolean(CallActivity.EXTRA_AECDUMP_ENABLED, aecDump);
            bundle.putBoolean(CallActivity.EXTRA_OPENSLES_ENABLED, useOpenSLES);
            bundle.putBoolean(CallActivity.EXTRA_DISABLE_BUILT_IN_AEC, disableBuiltInAEC);
            bundle.putBoolean(CallActivity.EXTRA_DISABLE_BUILT_IN_AGC, disableBuiltInAGC);
            bundle.putBoolean(CallActivity.EXTRA_DISABLE_BUILT_IN_NS, disableBuiltInNS);
            bundle.putBoolean(CallActivity.EXTRA_ENABLE_LEVEL_CONTROL, enableLevelControl);
            bundle.putBoolean(CallActivity.EXTRA_DISABLE_WEBRTC_AGC_AND_HPF, disableWebRtcAGCAndHPF);
            bundle.putInt(CallActivity.EXTRA_AUDIO_BITRATE, audioStartBitrate);
            bundle.putString(CallActivity.EXTRA_AUDIOCODEC, audioCodec);
            bundle.putBoolean(CallActivity.EXTRA_DISPLAY_HUD, displayHud);
            bundle.putBoolean(CallActivity.EXTRA_TRACING, tracing);
            bundle.putBoolean(CallActivity.EXTRA_CMDLINE, commandLineRun);
            bundle.putInt(CallActivity.EXTRA_RUNTIME, runTimeMs);

            bundle.putBoolean(CallActivity.EXTRA_DATA_CHANNEL_ENABLED, dataChannelEnabled);

            if (dataChannelEnabled) {
                bundle.putBoolean(CallActivity.EXTRA_ORDERED, ordered);
                bundle.putInt(CallActivity.EXTRA_MAX_RETRANSMITS_MS, maxRetrMs);
                bundle.putInt(CallActivity.EXTRA_MAX_RETRANSMITS, maxRetr);
                bundle.putString(CallActivity.EXTRA_PROTOCOL, protocol);
                bundle.putBoolean(CallActivity.EXTRA_NEGOTIATED, negotiated);
                bundle.putInt(CallActivity.EXTRA_ID, id);
            }

            if (useValuesFromIntent) {
                if (dummyIntent.hasExtra(CallActivity.EXTRA_VIDEO_FILE_AS_CAMERA)) {
                    String videoFileAsCamera =
                            dummyIntent.getStringExtra(CallActivity.EXTRA_VIDEO_FILE_AS_CAMERA);
                    bundle.putString(CallActivity.EXTRA_VIDEO_FILE_AS_CAMERA, videoFileAsCamera);
                }

                if (dummyIntent.hasExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE)) {
                    String saveRemoteVideoToFile =
                            dummyIntent.getStringExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE);
                    bundle.putString(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE, saveRemoteVideoToFile);
                }

                if (dummyIntent.hasExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH)) {
                    int videoOutWidth =
                            dummyIntent.getIntExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH, 0);
                    bundle.putInt(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_WIDTH, videoOutWidth);
                }

                if (dummyIntent.hasExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT)) {
                    int videoOutHeight =
                            dummyIntent.getIntExtra(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT, 0);
                    bundle.putInt(CallActivity.EXTRA_SAVE_REMOTE_VIDEO_TO_FILE_HEIGHT, videoOutHeight);
                }
            }

            //startActivityForResult(bundle, CONNECTION_REQUEST);

            mSchemaFragment.setArguments(bundle);
        }
    }

    /**
     * Get a value from the shared preference or from the bundle, if it does not
     * exist the default is used.
     */
    private String sharedPrefGetString(
            int attributeId, String intentName, int defaultId, boolean useFromIntent) {
        String defaultValue = getString(defaultId);
        if (useFromIntent) {
            String value = dummyIntent.getStringExtra(intentName);
            if (value != null) {
                return value;
            }
            return defaultValue;
        } else {
            String attributeName = getString(attributeId);
            return sharedPref.getString(attributeName, defaultValue);
        }
    }

    /**
     * Get a value from the shared preference or from the bundle, if it does not
     * exist the default is used.
     */
    private boolean sharedPrefGetBoolean(
            int attributeId, String intentName, int defaultId, boolean useFromIntent) {
        boolean defaultValue = Boolean.valueOf(getString(defaultId));
        if (useFromIntent) {
            return dummyIntent.getBooleanExtra(intentName, defaultValue);
        } else {
            String attributeName = getString(attributeId);
            return sharedPref.getBoolean(attributeName, defaultValue);
        }
    }

    /**
     * Get a value from the shared preference or from the bundle, if it does not
     * exist the default is used.
     */
    private int sharedPrefGetInteger(
            int attributeId, String intentName, int defaultId, boolean useFromIntent) {
        String defaultString = getString(defaultId);
        int defaultValue = Integer.parseInt(defaultString);
        if (useFromIntent) {
            return dummyIntent.getIntExtra(intentName, defaultValue);
        } else {
            String attributeName = getString(attributeId);
            String value = sharedPref.getString(attributeName, defaultString);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
               // Log.e(TAG, "Wrong setting for:" + attributeName + ":" + value);
                return defaultValue;
            }
        }
    }

    private boolean validateUrl(String url) {
        if (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
            return true;
        }

        new AlertDialog.Builder(getContext())
                .setTitle(getText(R.string.invalid_url_title))
                .setMessage(getString(R.string.invalid_url_text, url))
                .setCancelable(false)
                .setNeutralButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                .create()
                .show();
        return false;
    }

}
