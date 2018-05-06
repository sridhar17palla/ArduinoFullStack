package com.nextlevel.playarduino.arduinofullstack.SignInModule;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.nextlevel.playarduino.arduinofullstack.DataBase.DataHelper;
import com.nextlevel.playarduino.arduinofullstack.Models.RootUser;
import com.nextlevel.playarduino.arduinofullstack.R;
import com.nextlevel.playarduino.arduinofullstack.Utility.Utils;

/**
 * Created by sukumar on 3/24/18.
 */

public class SignInHelper {

    public static final String TAG = SignInHelper.class.getName();

    static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;

    SignInActivity mSignInActivity;

    SignInHelper(SignInActivity activity) {
        mSignInActivity = activity;
    }


    void doGoogleSingIn() {
         /*Configure sign-in to request the user's ID, email address, and basic profile.
        ID and basic profile are included in DEFAULT_SIGN_IN.
         */
         /*TODO : This should take account details from application DataBase.
         If not available then take from default google account.*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        /* Build a GoogleApiClient with access to the Google Sign-In API
        and the options specified by gso.
         */
        mGoogleApiClient = new GoogleApiClient.Builder(mSignInActivity)
                .enableAutoManage(mSignInActivity, mSignInActivity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            /*If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            and the GoogleSignInResult will be available instantly.
             */
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            /* If the user has not previously signed in on this device or the sign-in has expired,
            this asynchronous branch will attempt to sign in the user silently.
            Cross-device single sign-on will occur in this branch.
             */
            mSignInActivity.showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    mSignInActivity.hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        Log.d(TAG, "StatusCode:" + result.getStatus().getStatusCode()
                + ",  get statusMessage" + result.getStatus().getStatusMessage());
        //TODO-LOGIC : "getSignInAccount()" method returns NULL, if google SignIn is not done yet. Need to Handle this.
        GoogleSignInAccount acct = result.getSignInAccount();

        if (acct == null) {
            AlertDialog errorDialog = Utils.showErrorDialog(mSignInActivity,R.string.ERROR_TYPE_authentication,R.string.google_sign_in_not_yet_done);
            errorDialog.show();
            return;
        }

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            RootUser rootUser = new RootUser();
            if (acct.getGivenName() != null && !acct.getGivenName().isEmpty())
                rootUser.setUserName(acct.getGivenName());
            rootUser.setDisplayUserName(acct.getDisplayName());
            rootUser.setEmail(acct.getEmail());
            DataHelper.getInstance(mSignInActivity.getApplicationContext()).updateCommanderDevice(rootUser);

            mSignInActivity.updateUI(true, acct.getDisplayName());
        } else {
            // Signed out, show unauthenticated UI.
            mSignInActivity.updateUI(false, acct.getDisplayName());
        }
    }

    void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mSignInActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        mSignInActivity.signOutUpdateUI();
                    }
                });
    }
}
