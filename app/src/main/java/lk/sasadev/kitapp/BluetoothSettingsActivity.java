package lk.sasadev.kitapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothSettingsActivity extends AppCompatActivity {

    Switch bluetoothSwitch;
    ListView pairedDeviceList;
    ListView availableDeviceList;
    ImageButton imageButtonSearch;
    TextView pairedDevicesTitleText;
    TextView connectionStatusText;
    TextView connectedDeviceNameText;
    ArrayAdapter<String> listAdapter;
    ArrayAdapter<String> listAdapter2;
    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> btPairedDevicesArray;
    List<String> btPairedDevices = new ArrayList<String>();
    List<String> btAvailableDevices = new ArrayList<String>();
    BroadcastReceiver btStateBroadCastReceiver;
    BroadcastReceiver btDiscoveryStateBroadCastReceiver;
    BluetoothDevice remoteBtDevice;
    boolean search = true;

    UUID uuid= UUID.fromString("0000110E-0000-1000-8000-00805F9B34FB");



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_settings);

        setUi();
        setSwitchListener();
        setSearchButtonListener();
        setBtAdapter();
        getPairedDevices();
        setBroadCastReceivers();
        setListOnClickListeners();

    }

    public void setTheme(){
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    //night_mode_yes set appTheme
                    setTheme(R.style.AppTheme);
                } else {
                    //night_mode_no set appThemeDay
                    setTheme(R.style.AppThemeDay);
                }


                ActionBar actionBar = getSupportActionBar();
                //enable back button in action bar
                actionBar.setDisplayHomeAsUpEnabled(true);

            }   //Set elements according to selected theme

    public void setUi(){
        bluetoothSwitch = findViewById(R.id.switchBluetooth);
        pairedDeviceList = findViewById(R.id.listPairedDevices);
        availableDeviceList = findViewById(R.id.listAvailableDevice);
        imageButtonSearch = findViewById(R.id.imageButtonSearch);
        pairedDevicesTitleText = findViewById(R.id.textViewPairedDevices);
        connectionStatusText = findViewById(R.id.textViewConnectionStatus);
        connectedDeviceNameText = findViewById(R.id.textViewConnectedDevice);

    }   //Cast ui elements

    public void setSwitchListener(){
        bluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btAdapter.enable();
                    pairedDeviceList.setVisibility(View.VISIBLE);

                }
                if (!isChecked) {
                    btAdapter.disable();
                    pairedDeviceList.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void setSearchButtonListener(){
        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search){
                    btAdapter.startDiscovery();
                    imageButtonSearch.setImageResource(R.drawable.ic_close_black_24dp);
                    search=!search;
                }
                else{
                    btAdapter.cancelDiscovery();
                    imageButtonSearch.setImageResource(R.drawable.ic_search_black_24dp);
                    search=!search;
                }

            }
        });
    }

    public void setBtAdapter(){

                btAdapter = BluetoothAdapter.getDefaultAdapter();

                if (btAdapter.isEnabled()) {
                    bluetoothSwitch.setChecked(true);

                }
                if (!btAdapter.isEnabled()) {
                    bluetoothSwitch.setChecked(false);
                }
                if (btAdapter == null) {
                    Toast.makeText(this, "Device does not support bluetooth!", Toast.LENGTH_LONG).show();
                    bluetoothSwitch.setEnabled(false);
                    pairedDevicesTitleText.setText("Bluetooth Hardware is Unavailable");
                }

            }  //Get bluetooth adapter and set bluetooth switch status according to bt adapter status

    public void getPairedDevices() {
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listAdapter.clear();
        btPairedDevicesArray = btAdapter.getBondedDevices();
        if (btPairedDevicesArray.size() > 0) {
            for (BluetoothDevice device : btPairedDevicesArray) {
                btPairedDevices.add(device.getAddress());
                listAdapter.add(device.getName());
                pairedDeviceList.setAdapter(listAdapter);
            }
        }
    }   //Get paired devices from bt adapter and put resulted device array into paired device list

    public void setBroadCastReceivers(){

        btStateBroadCastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //String previousStateExtra = BluetoothAdapter.EXTRA_PREVIOUS_STATE;
                String stateExtra = BluetoothAdapter.EXTRA_STATE;
                //int previousState = intent.getIntExtra(previousStateExtra, -1);
                int state = intent.getIntExtra(stateExtra, -1);
                switch (state) {
                    case (BluetoothAdapter.STATE_TURNING_ON): {
                        Toast.makeText(context, "Bluetooth is turning on", Toast.LENGTH_SHORT).show();
                    }
                    case (BluetoothAdapter.STATE_ON): {
                        getPairedDevices();
                    }
                    case (BluetoothAdapter.STATE_TURNING_OFF): {
                        Toast.makeText(context, "Bluetooth is turning off", Toast.LENGTH_SHORT).show();
                    }
                    case (BluetoothAdapter.STATE_OFF): {

                    }

                }

            }
        };

        btDiscoveryStateBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(intent.getAction())){
                    Toast.makeText(context,"Discovery Started",Toast.LENGTH_SHORT).show();
                    listAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
                    listAdapter2.clear();
                    btAvailableDevices.clear();
                }

                if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    remoteBtDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //get name and MAC address of the found device
                    if(remoteBtDevice!=null) {
                        String deviceName = remoteBtDevice.getName();
                        String deviceHardwareAddress = remoteBtDevice.getAddress();
                        //add device address to an array for access when connecting
                        btAvailableDevices.add(deviceHardwareAddress);
                        //add device name to list adapter to set listView for user to choose
                        listAdapter2.add(deviceName);
                    }
                    //set device list from list adapter
                    availableDeviceList.setAdapter(listAdapter2);// MAC address

                }

                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
                    imageButtonSearch.setImageResource(R.drawable.ic_search_black_24dp);
                    search=!search;
                    Toast.makeText(context,"Discovery finished",Toast.LENGTH_SHORT).show();
                }

                if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(intent.getAction())){

                    BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String btDeviceName = btDevice.getName();
                    Toast.makeText(context,"Connected to : "+btDeviceName,Toast.LENGTH_SHORT).show();
                    connectionStatusText.setText("Connected to :");
                    connectedDeviceNameText.setText(btDeviceName);

                }

                if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(intent.getAction())){
                    Toast.makeText(context,"Bluetooth device is disconnected",Toast.LENGTH_SHORT).show();
                    connectionStatusText.setText("Disconnected");
                    connectedDeviceNameText.setVisibility(View.INVISIBLE);
                }
            }
        };


        registerReceiver(btStateBroadCastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(btDiscoveryStateBroadCastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        registerReceiver(btDiscoveryStateBroadCastReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        registerReceiver(btDiscoveryStateBroadCastReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        registerReceiver(btDiscoveryStateBroadCastReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
        registerReceiver(btDiscoveryStateBroadCastReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));


    }

    public void setListOnClickListeners(){

       pairedDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String device = btPairedDevices.get(position);
               BluetoothDevice remoteDevice = btAdapter.getRemoteDevice(device);
               BluetoothConnectThread bluetoothConnectThread = new BluetoothConnectThread(remoteDevice);
               bluetoothConnectThread.run();




           }
       });

        availableDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String device = btAvailableDevices.get(position);
                BluetoothDevice remoteDevice = btAdapter.getRemoteDevice(device);
                BluetoothConnectThread bluetoothConnectThread = new BluetoothConnectThread(remoteDevice);
                bluetoothConnectThread.run();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(btStateBroadCastReceiver);
        unregisterReceiver(btDiscoveryStateBroadCastReceiver);
    }


    private class BluetoothConnectThread extends Thread {
        BluetoothSocket mmSocket;
        BluetoothDevice mmDevice;
        BluetoothSocket mmFallbackSocket;

        String TAG = "btThread";

        public BluetoothConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = mmDevice.createInsecureRfcommSocketToServiceRecord(uuid);
                Log.e(TAG, "Socket's created");
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            if(btAdapter.isDiscovering()){
                btAdapter.cancelDiscovery();
            }


            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e(TAG, "Socket's connected");
            } catch (IOException connectException) {
                Log.e(TAG, "Socket's connection failed  try fallback");

                Class<?> clazz = mmSocket.getRemoteDevice().getClass();
                Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};

                try {
                    Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                    Object[] params = new Object[]{Integer.valueOf(1)};
                    mmFallbackSocket = (BluetoothSocket) m.invoke(mmSocket.getRemoteDevice(), params);
                    mmFallbackSocket.connect();
                    mmSocket = mmFallbackSocket;
                    Log.e(TAG, "fallback  connection established.");
                } catch (Exception e2) {
                    Log.e(TAG, "Couldn't fallback while establishing Bluetooth connection.");
                    try {
                        mmSocket.close();
                        Log.e(TAG, "Socket's closed");
                    } catch (IOException closeException) {
                        Log.e(TAG, "Could not close the client socket");
                    }
                }
                // Unable to connect; close the socket and return.

                return;
            }
            Log.e(TAG, "Connection attempt succeeded " + mmSocket.getRemoteDevice().getName());
            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            //manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }
}


