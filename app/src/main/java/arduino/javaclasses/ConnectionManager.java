package arduino.javaclasses;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.UUID;

import arduino.controller.ActionActivity;
import arduino.controller.SettingsActivity;

/**
 * Created by lucapellegrini1 on 22.01.17.
 */

public class ConnectionManager {

    public enum ConnectionState { CONNECTED, DISCONNECTED};
    private BluetoothDevice bDevice;
    private BluetoothSocket mBTSocket;
    private ConnectionState actualState;
    private Context context;


    public ConnectionManager(BluetoothDevice bDevice, Context context){
        this.bDevice = bDevice;
        this.context = context;
        actualState = ConnectionState.DISCONNECTED;
    }

    public void connectBtDevice(){
        new ConnectBT().execute();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... devices) {

            try {
                if (mBTSocket == null || actualState == ConnectionState.DISCONNECTED) {
                    String bDeviceUUID = "";
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                    String UUDID = preferences.getString("keySummary", ""); //"" is the default String to return if the preference isn't found
                    UUID uuid  = java.util.UUID.fromString(UUDID);
                    mBTSocket = bDevice.createRfcommSocketToServiceRecord(uuid);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                    actualState = ConnectionState.CONNECTED;
                }
            } catch (Exception e) {
                // Unable to connect to device
                e.printStackTrace();
                actualState = ConnectionState.DISCONNECTED;
            }
            return null;
        }
    }

    private class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                mBTSocket.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            actualState = ConnectionState.DISCONNECTED;
        }

    }






}
