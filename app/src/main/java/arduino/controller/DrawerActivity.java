package arduino.controller;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sp;
    private BtConnectionService localService;
    private boolean isBound = false;
    private Fragment fragment;
    private boolean t = true;
    private Timer myTimer;
    private Context context;
    private int itemId = 0;
    private SharedPreferences sharedpreferences;
    private static final String MY_PREFS_NAME = "MyPrefsFile";
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        context = this.getApplicationContext();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        View navHeaderView= navigationView.getHeaderView(0);
        TextView tx = (TextView) navHeaderView.findViewById(R.id.deviceStr);
        tx.setText(restoredText);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        localService.disconnectBtDevice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item ccolicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action__refresh){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(fragment);

            switch (itemId) {
                case R.id.nav_temperature:
                    fragment = new TemperatureFragment();
                    localService.setWStop(false);
                    break;
                case R.id.nav_controller:
                    fragment = new ControllerFragment();
                    localService.setWStop(false);
                    break;
                case R.id.nav_brightness:
                    fragment = new BrightnessFragment();
                    localService.setWStop(false);
                    break;
                case R.id.nav_humidity:
                    fragment = new HumidityFragment();
                    localService.setWStop(false);
                    break;
                case R.id.nav_command:
                    fragment = new CommandsFragment();
                    localService.setWStop(true);
                    break;

            }


            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object

        this.itemId = itemId;
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_temperature:
                fragment = new TemperatureFragment();
                localService.setWStop(false);
                break;
            case R.id.nav_controller:
                fragment = new ControllerFragment();
                localService.setWStop(false);
                break;
            case R.id.nav_brightness:
                fragment = new BrightnessFragment();
                localService.setWStop(false);
                break;
            case R.id.nav_humidity:
                fragment = new HumidityFragment();
                localService.setWStop(false);
                break;
            case R.id.nav_command:
                fragment = new CommandsFragment();
                localService.setWStop(true);
                break;
            case R.id.nav_dashboarb:
                fragment = new DashboardFragment();
                localService.setWStop(false);
                break;

        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (localService == null){
        Intent intent = new Intent(this, BtConnectionService.class);
        bindService(intent, connection, this.getApplicationContext().BIND_AUTO_CREATE);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        localService.setWStop(false);
        localService.writeString("y");
        localService.setWStop(true);
        if (isBound) {
            //unbindService(connection);
            //   isBound = false;
        }
        localService.disconnectBtDevice();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            BtConnectionService.LocalBinder binder = (BtConnectionService.LocalBinder) service;
            localService = binder.getService();
            isBound = true;
            fragment = new ControllerFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //  isBound = false;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (localService != null) {
            if (localService.getActualState() == BtConnectionService.ConnectionState.DISCONNECTED) {
                localService.connectBtDevice();
            }

            if (localService.getActualState() == BtConnectionService.ConnectionState.DISCONNECTED) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        localService.disconnectBtDevice();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        localService.setWStop(false);
        localService.writeString("y");
        localService.setWStop(true);
        localService.disconnectBtDevice();
    }

    public BtConnectionService getLocalService(){

        return localService;
    }

}










