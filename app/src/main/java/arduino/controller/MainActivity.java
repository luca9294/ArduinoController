package arduino.controller;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import arduino.controller.BtConnectionService.LocalBinder;

public class MainActivity extends AppCompatActivity {
    private static final int BT_ENABLE_REQUEST = 10; // This is the code we use for BT Enable
    private static final String TAG = "BlueTest5-MainActivity";
    private int mMaxChars = 50000;//Default
    private BluetoothSocket mBTSocket;
    private boolean mIsUserInitiatedDisconnect = false;
    private boolean mIsBluetoothConnected = false;
    private BluetoothAdapter mBTAdapter;
    private List<BluetoothDevice> ls;
    private List<String> ls_string;
    private int checked = 0;
    private BtConnectionService localService;
    private boolean isBound = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        ls = new ArrayList<BluetoothDevice>();
        ls_string = new ArrayList<String>();
        Button btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBluehtoothActive()){
                    showMsgBox("Error", "Bluetooth not enabled");
                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBT, BT_ENABLE_REQUEST);
                }
                else
                {
                    try {
                        new SearchDevices().execute().get();
                        if (ls.size() == 0)
                            showMsgBox("Error", "Not device paired");
                        else
                            openCheckBoxesDialog();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                }
            }
        }
    });
    }

    //Check whether the bluetooht is active or not
    private boolean isBluehtoothActive(){
            if (!mBTAdapter.isEnabled())
                return false;
            else
                return true;
        }

    private void showMsgBox(String title, String text){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void openCheckBoxesDialog()
    {
        final CharSequence[] dialogList=  ls_string.toArray(new CharSequence[ls_string.size()]);
        final AlertDialog.Builder builderDialog = new AlertDialog.Builder(MainActivity.this);
        builderDialog.setTitle("Paired Devices");
        builderDialog.setSingleChoiceItems(dialogList, checked, null);
        final Context c = this;
        final Intent intent1 = new Intent (this,DrawerActivity.class);
        builderDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                localService.setBtDevice(ls.get(checked), c);
              localService.connectBtDevice();
                if (localService.getActualState() == BtConnectionService.ConnectionState.CONNECTED)
                {
                    startActivity(intent1);
                }
                else{

                    showMsgBox("Error", "Device not connected");
                }

            }
        });




        builderDialog.setNegativeButton("Cancel", null);
        AlertDialog alert = builderDialog.create();
        alert.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    private class SearchDevices extends AsyncTask<Void, Void, List<BluetoothDevice>> {

        @Override
        protected List<BluetoothDevice> doInBackground(Void... params) {
            Set<BluetoothDevice> pairedDevices = mBTAdapter.getBondedDevices();
            List<BluetoothDevice> listDevices = new ArrayList<BluetoothDevice>();
            ls.clear();
            ls_string.clear();
            for (BluetoothDevice device : pairedDevices) {
                ls.add(device);
                ls_string.add(device.getName());
            }
            return ls;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BtConnectionService.class);
        bindService(intent, connection, this.getApplicationContext().BIND_AUTO_CREATE);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            localService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };
}
