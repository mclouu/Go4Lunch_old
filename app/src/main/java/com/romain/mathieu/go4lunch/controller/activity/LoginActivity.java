package com.romain.mathieu.go4lunch.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.romain.mathieu.go4lunch.R;

import java.util.Collections;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.btn_login_google)
    Button btnLoginGoogle;

    //FOR DATA
    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (this.isCurrentUserLogged()) {
            this.startActivityIfConnected();
        } else {
            showSnackbar("Vous êtes pas connecté :(");
        }
    }

    private void showSnackbar(String str) {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.loginLayout),
                str, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_login;
    }


    @OnClick(R.id.btn_login_email)
    public void onClickLogginEmailButton() {
        this.startSignInActivityEmail();
    }

    @OnClick(R.id.btn_login_google)
    public void onClickLoginGoogleButton() {

        this.startSignInActivityGooglel();
    }

    // --------------------
    // NAVIGATION
    // --------------------

    private void startSignInActivityGooglel() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_info_black)
                        .build(),
                RC_SIGN_IN);
    }

    // 2 - Launch Sign-In Activity with Email
    private void startSignInActivityEmail() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_info_black)
                        .build(),
                RC_SIGN_IN);
    }

    private void startActivityIfConnected() {
        Intent myintent = new Intent(this, MainActivity.class);
        startActivity(myintent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                startActivityIfConnected();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar("ressayez :/");
//                    Toast.makeText(this, "re try !", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    // User has not internet
                    showSnackbar("Vous n'êtes pas connecté à internet :(");
                    return;
                }
                // Other error
                Log.e("tdb", "Sign-in error: ", response.getError());
                Toast.makeText(this, "Erreur :(", Toast.LENGTH_SHORT).show();
                Crashlytics.logException(new RuntimeException("Google Sign-in error: " + response.getError()));


            }
        }
    }
}