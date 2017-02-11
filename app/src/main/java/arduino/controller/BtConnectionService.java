package arduino.controller;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.os.Binder;
import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;


public class BtConnectionService extends Service {
    public enum ConnectionState { CONNECTED, DISCONNECTED};
    private BluetoothDevice bDevice;
    private BluetoothSocket mBTSocket;
    private ConnectionState actualState = ConnectionState.DISCONNECTED;
    private Context context;
    private final IBinder binder = new LocalBinder();
    private final char TERMINATOR = '/';
    private String strReceived = "";


    public class LocalBinder extends Binder {
        BtConnectionService getService() {
            return BtConnectionService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public void setBtDevice(BluetoothDevice bDevice, Context context)
    {
        this.bDevice = bDevice;
        this.context= context;
    }

    //Connects the chosen Bluetooth Device
    public void connectBtDevice(){
       // new BtConnectionService.ConnectBT().execute().get();

        if (mBTSocket == null || actualState ==  ConnectionState.DISCONNECTED) {
            String bDeviceUUID = "";
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String UUDID = preferences.getString("keySummary", ""); //"" is the default String to return if the preference isn't found
            UUID uuid  = java.util.UUID.fromString(UUDID);
            try {
                mBTSocket = bDevice.createRfcommSocketToServiceRecord(uuid);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                mBTSocket.connect();
                actualState = ConnectionState.CONNECTED;
            } catch (IOException e) {
                e.printStackTrace();
            }
          //  new ReadInput();
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
    public ConnectionState getActualState()
    {
        return actualState;
    }

    //Writes the input string to the Bluetooth device
    public void writeString (String s){
        try {
            mBTSocket.getOutputStream().write(s.getBytes());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //A Thread that continuously reads some inputs
    private class ReadInput implements Runnable {

        private boolean bStop = false;
        private Thread t;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
            InputStream inputStream;
            try {
                inputStream = mBTSocket.getInputStream();
                while (!bStop) {
                    byte[] buffer = new byte[256];
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        int i = 0;
						/*
						 * This is needed because new String(buffer) is taking the entire buffer i.e. 256 chars on Android 2.3.4 http://stackoverflow.com/a/8843462/1287554
						 */
                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        final String strInput = new String(buffer, 0, i);
                        strReceived = strInput;
                    }
                    Thread.sleep(500);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void stop() {
            bStop = true;
        }

    }

}