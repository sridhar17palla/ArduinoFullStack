package com.nextlevel.playarduino.arduinofullstack;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    PubNubHelper mPubNubHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
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

        if (id == R.id.nav_camera) {
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
    protected void onResume() {
        super.onResume();
      //  pubNubSetUp();
    }

   /* public void pubNubSetUp() {
        pubnub = new Pubnub("pub-c-67b8926d-2ebf-4553-9248-c55d54b2095a", "sub-c-138d12f2-d84f-11e5-bdd5-02ee2ddab7fe");
        String[] channels = pubnub.getSubscribedChannelsArray();
        pubnub.channelGroupAddChannel("base channel", "channel one", callback);
        mReceivedMessage = (TextView) findViewById(R.id.received_message);
        try {
            pubnub.subscribe("channel one", callbackSubscribe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Callback callback = new Callback() {
        @Override
        public void connectCallback(String channel, Object message) {
            Log.d(" ADDCHANNEL : CONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void disconnectCallback(String channel, Object message) {
            Log.d("ADDCHANNEL:DISCONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        public void reconnectCallback(String channel, Object message) {
            Log.d("ADDCHANNEL: RECONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void successCallback(String channel, Object message) {
            Log.d("ADDCHANNEL: ", message.toString());
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            Log.d("ADDCHANNEL: ERROR", channel + " : " + error.toString());
        }
    };

    public Callback callbackSubscribe = new Callback() {
        @Override
        public void connectCallback(String channel, Object message) {
            Log.d("SUBSCRIBE : CONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void disconnectCallback(String channel, Object message) {
            Log.d("SUBSCRIBE : DISCONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        public void reconnectCallback(String channel, Object message) {
            Log.d("SUBSCRIBE : RECONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void successCallback(String channel, Object message) {
            Log.d(" SUBSCRIBE : ", message.toString());
            mPublishedString = message.toString();
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (mPublishedString != null) {
                        mReceivedMessage.setText(mPublishedString);
                    } else {
                        mReceivedMessage.setText("No messages received.");
                    }
                }
            }.execute();

        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            Log.d(" SUBSCRIBE : ERROR", channel + " : " + error.toString());
        }
    };

    public Callback callbackPublish = new Callback() {
        @Override
        public void connectCallback(String channel, Object message) {
            Log.d("PUBLISH : CONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void disconnectCallback(String channel, Object message) {
            Log.d(" PUBLISH : DISCONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        public void reconnectCallback(String channel, Object message) {
            Log.d(" PUBLISH : RECONNECT", channel
                    + " : " + message.getClass() + " : "
                    + message.toString());
        }

        @Override
        public void successCallback(String channel, Object message) {
            Log.d("  PUBLISH : " + channel, message.getClass() + " : " + message.toString());
        }

        @Override
        public void errorCallback(String channel, PubnubError error) {
            Log.d("  PUBLISH : ERROR", channel + " : " + error.toString());
        }
    };


    public void onPublish(View v) {
        message = (EditText) findViewById(R.id.publish_message);
        if(mPubNubHelper==null) {
            mPubNubHelper = new PubNubHelper();
        }
        mPubNubHelper.onPublish(message.toString());
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            pubnub.publish("channel one", params[0], callbackPublish);
            Log.d("PUBLISH STRING", params[0]);
            return resp;
        }

               @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
          /*  try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            if (mPublishedString != null) {
                mReceivedMessage.setText(mPublishedString);
            } else {
                mReceivedMessage.setText("mPublishedString is null");
            }

        }

           }

    public void onRefresh(View v) {
        if (mPublishedString != null) {
            mReceivedMessage.setText(mPublishedString);
        } else {
            mReceivedMessage.setText("No messages received.");
        }
    }
    */
}
