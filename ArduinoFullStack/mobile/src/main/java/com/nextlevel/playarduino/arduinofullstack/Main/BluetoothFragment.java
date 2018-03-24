package com.nextlevel.playarduino.arduinofullstack.Main;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nextlevel.playarduino.arduinofullstack.Base.BaseFragment;
import com.nextlevel.playarduino.arduinofullstack.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by sukumar on 1/18/17.
 */

public class BluetoothFragment extends BaseFragment {

    public static final String TAG = "BluetoothFragment";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private Handler mHandler; // handler that gets info from Bluetooth service

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private LinearLayout mPairedBtContainer, mNewBtDeviceContainer;
    private View mRootView;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    private BluetoothSocket mSocket;

    private Set<BluetoothDevice> allBluetoothDevices = new HashSet<>();

    // Defines several constants used when transmitting messages between the
    // service and the UI.
    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                allBluetoothDevices.add(device);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                BluetoothClass bluetoothClass = device.getBluetoothClass(); //Gets the type of Bluetooth device.
                int deviceType = bluetoothClass.getDeviceClass();//Gets the type of Bluetooth device.

                View bluetoothView = getActivity().getLayoutInflater().inflate(R.layout.bluetooth_device_layout, mNewBtDeviceContainer, false);
                TextView btDeviceType = (TextView) bluetoothView.findViewById(R.id.bluetooth_device_type);
                btDeviceType.setText(deviceType + "");
                TextView btDeviceName = (TextView) bluetoothView.findViewById(R.id.bluetooth_device_name);
                btDeviceName.setText(deviceName);
                TextView btDeviceMacAdd = (TextView) bluetoothView.findViewById(R.id.bluetooth_mac_address);
                btDeviceMacAdd.setText(deviceHardwareAddress);
                mNewBtDeviceContainer.addView(bluetoothView);
                bluetoothView.setOnClickListener(deviceOnClickListener);
                mRootView.findViewById(R.id.no_new_bt_devices).setVisibility(View.INVISIBLE);
            }
        }
    };

    View.OnClickListener deviceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String mac;
            TextView btDeviceMacAdd = (TextView) v.findViewById(R.id.bluetooth_mac_address);
            mac = (String) btDeviceMacAdd.getText();
            for(BluetoothDevice device: allBluetoothDevices){
                if(device.getAddress().equalsIgnoreCase(mac)){
                    mDevice = device;
                    new ConnectThread(mDevice).start();
                    break;
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        mRootView = inflater.inflate(R.layout.bluetooth_fragment, container, false);
        mPairedBtContainer = (LinearLayout) mRootView.findViewById(R.id.paired_bt_device_container);
        mNewBtDeviceContainer = (LinearLayout) mRootView.findViewById(R.id.new_bt_device_container);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);
        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialBluetoothSetup();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    public void initialBluetoothSetup() {
        mRootView.findViewById(R.id.no_paired_bt_devices).setVisibility(View.VISIBLE);
        mRootView.findViewById(R.id.no_new_bt_devices).setVisibility(View.VISIBLE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            // Device does not support Bluetooth
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    allBluetoothDevices.add(device);
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    BluetoothClass bluetoothClass = device.getBluetoothClass(); //Gets the type of Bluetooth device.
                    int deviceType = bluetoothClass.getDeviceClass();//Gets the type of Bluetooth device.

                    View bluetoothView = getActivity().getLayoutInflater().inflate(R.layout.bluetooth_device_layout, mPairedBtContainer, false);
                    TextView btDeviceType = (TextView) bluetoothView.findViewById(R.id.bluetooth_device_type);
                    btDeviceType.setText(deviceType + "");
                    TextView btDeviceName = (TextView) bluetoothView.findViewById(R.id.bluetooth_device_name);
                    btDeviceName.setText(deviceName);
                    TextView btDeviceMacAdd = (TextView) bluetoothView.findViewById(R.id.bluetooth_mac_address);
                    btDeviceMacAdd.setText(deviceHardwareAddress);
                    mPairedBtContainer.addView(bluetoothView);
                    bluetoothView.setOnClickListener(deviceOnClickListener);
                    mRootView.findViewById(R.id.no_paired_bt_devices).setVisibility(View.INVISIBLE);
                }
            }
            //cancel any prior BT device discovery
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            //re-start discovery
            mBluetoothAdapter.startDiscovery();
        }
    }


    private void manageBtConnectedSocket(){

    }

    private class ConnectThread extends Thread {

        public ConnectThread(BluetoothDevice btDevice) {
            // Use a temporary object that is later assigned to mSocket
            // because mSocket is final.
            BluetoothDevice device;
            BluetoothSocket bluetoothSocket = null;
            device = btDevice;
            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                bluetoothSocket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mSocket = bluetoothSocket;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageBtConnectedSocket();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream inputStream;
        private final OutputStream outputStream;
        private byte[] buffer; // buffer store for the stream
        private BluetoothSocket socket;
        public ConnectedThread(BluetoothSocket btSocket) {
            socket = btSocket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        public void run() {
            buffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = inputStream.read(buffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = mHandler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1,
                            buffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                outputStream.write(bytes);

                // Share the sent message with the UI activity.
                Message writtenMsg = mHandler.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, buffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                mHandler.sendMessage(writeErrorMsg);
            }
        }
    }

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
