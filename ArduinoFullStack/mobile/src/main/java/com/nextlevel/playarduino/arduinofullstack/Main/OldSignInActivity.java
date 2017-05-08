package com.nextlevel.playarduino.arduinofullstack.Main;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nextlevel.playarduino.arduinofullstack.R;

import static com.nextlevel.playarduino.arduinofullstack.R.id.email_address;

public class OldSignInActivity extends AppCompatActivity {

    private EditText mEmailAddressButton, mPasswordButton;
    private Button mFacebookButton, mGoogleButton, mNoSignInButton;
    private ImageButton mEnterButton;
    private TextView mWhySignInButton, mRegisterNowButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_screen);

        mEmailAddressButton = (EditText) findViewById(email_address);
        mPasswordButton = (EditText) findViewById(R.id.password);

        mFacebookButton = (Button) findViewById(R.id.facebook_details);
        mGoogleButton = (Button) findViewById(R.id.google_details);
        mWhySignInButton = (TextView) findViewById(R.id.why_sigh_in);
        mNoSignInButton = (Button) findViewById(R.id.no_sign_in);
        mRegisterNowButton = (TextView) findViewById(R.id.register_now);

        mEmailAddressButton.setOnClickListener(mOnClickListener);
        mPasswordButton.setOnClickListener(mOnClickListener);
        mFacebookButton.setOnClickListener(mOnClickListener);
        mGoogleButton.setOnClickListener(mOnClickListener);
        mWhySignInButton.setOnClickListener(mOnClickListener);
        mNoSignInButton.setOnClickListener(mOnClickListener);
        mRegisterNowButton.setOnClickListener(mOnClickListener);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.email_address:
                    //TODO : implement Google or facebook authorizaion
                    if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Account[] accounts = AccountManager.get(v.getContext()).getAccounts();
                    Log.e("", "Size: " + accounts.length);
                    for (Account account : accounts) {

                        String possibleEmail = account.name;
                        String type = account.type;

                        if (type.equals("com.google")) {
                            String strGmail = possibleEmail;
                            Log.e("", "Emails: " + strGmail);
                            break;
                        }
                    }
                    break;
                case R.id.password:
                    break;
                case R.id.facebook_details:
                    break;
                case R.id.google_details:
                    break;
                case R.id.why_sigh_in:
                    break;
                case R.id.no_sign_in:
                    break;
                case R.id.register_now:
                    break;
                default:
                    break;

            }
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SignIn Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}


