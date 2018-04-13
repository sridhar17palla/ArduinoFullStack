package com.nextlevel.playarduino.arduinofullstack.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.nextlevel.playarduino.arduinofullstack.Base.BaseActivity;
import com.nextlevel.playarduino.arduinofullstack.Main.LinkerDeviceList.LinkerDeviceListFragment;
import com.nextlevel.playarduino.arduinofullstack.R;

import java.util.Observable;

public class HomeActivity extends BaseActivity{
       // implements NavigationView.OnNavigationItemSelectedListener {

    private Context mContext;

    private LinkerDeviceListFragment mLinkerDeviceListFragment;
    //private PubNubFragment mPubNubFragment;
    private BluetoothFragment mBluetoothFragment;
    private UsbFragment mUsbFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        mContext = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*TODO-UI : We are following UI as per Whatsapp. So NavigationView may not be required.
            Comment Hamburger icon and NavigationView for now.
         */
       /* NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

     /*TODO-UI : We are following UI as per Whatsapp. So NavigationView may not be required.
            Comment Hamburger icon, NavigationView and its related code for now.
            */
   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.log_screen) {
            startActivity(new Intent(this, TerminalFragment.class));
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
*/
    private class MainViewPagerAdapter extends FragmentPagerAdapter {

        public MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    if(mLinkerDeviceListFragment ==null) {
                        mLinkerDeviceListFragment= new LinkerDeviceListFragment();
                    }
                    return mLinkerDeviceListFragment;
                    /*if(mPubNubFragment ==null) {
                        mPubNubFragment = new PubNubFragment();
                    }
                    return mPubNubFragment;*/
                case 1:
                    if(mBluetoothFragment ==null) {
                        mBluetoothFragment = new BluetoothFragment();
                    }
                    return mBluetoothFragment;
                case 2:
                    if(mUsbFragment==null){
                        mUsbFragment = new UsbFragment();
                    }
                    return mUsbFragment;
                default:
                    if(mLinkerDeviceListFragment ==null) {
                        mLinkerDeviceListFragment= new LinkerDeviceListFragment();
                    }
                    return mLinkerDeviceListFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

         @Override
         public CharSequence getPageTitle(int position) {
             return super.getPageTitle(position);
         }
     }


    //TODO-LOGIC need to establish communication between Arduino module
    // and other components via RxJava.
    @Override
    public void onArduinoDataReceived(String data) {
        /*if(mPubNubFragment ==null) {
            mPubNubFragment = new PubNubFragment();
        }*/
        if(mBluetoothFragment ==null) {
            mBluetoothFragment = new BluetoothFragment();
        }
        if(mUsbFragment==null){
            mUsbFragment = new UsbFragment();
        }
       // mPubNubFragment.onArduinoDataReceived(data);

        //TODO: NEED TO IMPLEMENT onArduinoDataReceived in LINKERDEVICELISTFRAGMENT
        mBluetoothFragment.onArduinoDataReceived(data);
        mUsbFragment.onArduinoDataReceived(data);
    }

    @Override
    public void update(Observable o, final Object arg) {

    }
}
