package com.romain.mathieu.go4lunch.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.romain.mathieu.go4lunch.R;

import java.util.Collections;

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

        if (this.isCurrentUserLogged()) this.startMainActivity();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_login;
    }


    @OnClick(R.id.btn_login_google)
    public void onClickLoginGoogleButton() {

        this.startSignInActivityGooglel();
    }

    @OnClick(R.id.btn_login_email)
    public void onClickLoginButton() {
        // 3 - Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivityEmail();
    }

    // --------------------
    // NAVIGATION
    // --------------------

    private void startSignInActivityGooglel() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
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

    // 3 - Launching Profile Activity
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}