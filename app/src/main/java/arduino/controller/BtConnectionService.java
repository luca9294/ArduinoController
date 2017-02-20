package arduino.controller;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

import android.os.Binder;
import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;


public class BtConnectionService extends Service {
    public enum ConnectionState {CONNECTED, DISCONNECTED}

    ;
    private BluetoothDevice bDevice;
    private BluetoothSocket mBTSocket;
    private ConnectionState actualState = ConnectionState.DISCONNECTED;
    private Context context;
    private final IBinder binder = new LocalBinder();
    private final char TERMINATOR = '/';
    private String strReceived = "";
    private boolean wStop = false;
    private boolean flag = false;
    private ReadR r;


    public class LocalBinder extends Binder {
        BtConnectionService getService() {
            return BtConnectionService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public void setBtDevice(BluetoothDevice bDevice, Context context) {
        this.bDevice = bDevice;
        this.context = context;
    }

    //Connects the chosen Bluetooth Device
    public void connectBtDevice() {
        // new BtConnectionService.ConnectBT().execute().get();

        if (mBTSocket == null || actualState == ConnectionState.DISCONNECTED) {
            String bDeviceUUID = "";
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String UUDID = preferences.getString("keySummary", ""); //"" is the default String to return if the preference isn't found
            UUID uuid = java.util.UUID.fromString(UUDID);
            try {
                mBTSocket = bDevice.createRfcommSocketToServiceRecord(uuid);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                mBTSocket.connect();
                actualState = ConnectionState.CONNECTED;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //Disconnects the chosen Bluetooth Device
    public void disconnectBtDevice() {
        try {
            mBTSocket.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        actualState = ConnectionState.DISCONNECTED;
    }

    //Gets the actual status of the connection
    public ConnectionState getActualState() {
        return actualState;
    }

    //Writes the input string to the Bluetooth device
    public void writeString(String s) {
        try {
            mBTSocket.getOutputStream().write(s.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setWStop(boolean wStop) {
        this.wStop = wStop;
        if (r == null && !wStop) {
            r = new ReadR();
            r.execute();
        }
        else if (!wStop && r.getStatus() == AsyncTask.Status.FINISHED){
            r = new ReadR();
            r.execute();
        }
    }

    public String getString() {
        while (strReceived.equals("")) {
        }
        String temp = strReceived;
        strReceived = "";

        return temp;
    }


    private class ReadR extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            InputStream inputStream;
            try {
                inputStream = mBTSocket.getInputStream();
                String tempString = "";
                while (!wStop) {
                    byte[] buffer = new byte[256];
                    if (inputStream.available() > 0) {
                        int temp = inputStream.available();
                        inputStream.read(buffer);
                        int i = 0;
                        for (i = 0; i < 10 && buffer[i] != 0; i++) {
                        }
                        final String strInput = new String(buffer, 0, i);
                        if (strInput.charAt(strInput.length() - 1) == '/' && tempString == "")
                            strReceived = strInput;
                        else {
                            tempString += strInput;
                            if (tempString.charAt(tempString.length() - 1) == '/') {
                                strReceived = tempString;
                                tempString = "";
                            }
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }


}