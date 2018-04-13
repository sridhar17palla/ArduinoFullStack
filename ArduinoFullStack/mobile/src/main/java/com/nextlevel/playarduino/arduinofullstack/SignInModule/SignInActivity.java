package com.nextlevel.playarduino.arduinofullstack.SignInModule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nextlevel.playarduino.arduinofullstack.Main.HomeActivity;
import com.nextlevel.playarduino.arduinofullstack.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private Unbinder unbinder;

    @BindView(R.id.status)
    TextView mStatusTextView;

    @BindView(R.id.sign_in_button)
    SignInButton mSingInButton;

    @BindView(R.id.sign_out_button)
    Button mSingOutButton;

    @BindView(R.id.sign_out_and_disconnect)
    LinearLayout mSignOutAndDisconnect;

    private ProgressDialog mProgressDialog;

    private SignInHelper mSignInHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_layout);

        unbinder = ButterKnife.bind(this);

        mSingInButton.setOnClickListener(this);
        mSingOutButton.setOnClickListener(this);

        mSingInButton.setSize(SignInButton.SIZE_STANDARD);

        mSignInHelper = new SignInHelper(this);
        mSignInHelper.doGoogleSingIn();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }

    //NEEDTOKNOW when does this method gets called.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);*/
        if (requestCode == SignInHelper.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            mSignInHelper.handleSignInResult(result);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /* An unresolvable error has occurred and
        Google APIs (including Sign-In) will not be available.
         */
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                mSignInHelper.signIn();
                break;
            case R.id.sign_out_button:
                mSignInHelper.signOut();
                break;
        }
    }

    void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    void updateUI(boolean signedIn, String accountName) {
        if (signedIn) {
            mStatusTextView.setText(getString(R.string.signed_in_format, accountName));
            mSingInButton.setVisibility(View.GONE);
            mSignOutAndDisconnect.setVisibility(View.VISIBLE);
            Toast.makeText(this, getString(R.string.sign_in_success_message) + accountName, Toast.LENGTH_SHORT).show();

            Intent homeActivityIntent = new Intent(this, HomeActivity.class);
            startActivity(homeActivityIntent);
        } else {
            Toast.makeText(this, getString(R.string.sign_in_failure_message) + accountName, Toast.LENGTH_SHORT).show();

            mStatusTextView.setText(R.string.signed_out);
            mSingInButton.setVisibility(View.VISIBLE);
            mSignOutAndDisconnect.setVisibility(View.GONE);
        }
    }

    void signOutUpdateUI(){
        Toast.makeText(this, getString(R.string.sign_in_signout_message), Toast.LENGTH_SHORT).show();
        mStatusTextView.setText(R.string.signed_out);
        mSingInButton.setVisibility(View.VISIBLE);
        mSignOutAndDisconnect.setVisibility(View.GONE);
    }
}
