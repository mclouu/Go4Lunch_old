package com.romain.mathieu.go4lunch.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.model.User;
import com.romain.mathieu.go4lunch.model.UserHelper;

import java.util.Collections;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

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
            showSnackbar("Vous n'êtes pas connecté :(", R.id.loginLayout);
        }
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


    // 1 - Http request that create user in firestore
    private void createUserInFirestore() {
        if (this.getCurrentUser() != null) {
            String urlPicture;
            if (LoginActivity.this.getCurrentUser().getPhotoUrl() != null)
                urlPicture = LoginActivity.this.getCurrentUser().getPhotoUrl().toString();
            else urlPicture = null;
            String username = LoginActivity.this.getCurrentUser().getDisplayName();
            String uid = LoginActivity.this.getCurrentUser().getUid();

            Log.e("TDB", username + uid);
            UserHelper.getUser(uid).addOnSuccessListener(documentSnapshot -> {
                User currentUser = documentSnapshot.toObject(User.class);
                String userUid = "1";
                if (currentUser != null) {
                    userUid = currentUser.getUid();
                }
                Log.e("TDB", uid + " n'est pas égal à " + userUid);
                if (!uid.equals(userUid)) {
                    UserHelper.createUser(uid, username, urlPicture).addOnFailureListener(LoginActivity.this.onFailureListener());
                    Log.e("TDB", "user créer ");
                    Log.e("TDB", uid + username + urlPicture);

                } else {
                    Log.e("TDB", "user non créer ");

                }

            });
        }
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
                this.createUserInFirestore();
                startActivityIfConnected();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar("Re try", R.id.loginLayout);
                    return;
                }

                if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    // User has not internet
                    showSnackbar("Vous n'êtes pas connecté à internet :(", R.id.loginLayout);
                    return;
                }
                // Other error
                Log.e("tdb", "Sign-in error: ", response.getError());
                showSnackbar("Error :(", R.id.loginLayout);
                Crashlytics.logException(new RuntimeException("Google Sign-in error: " + response.getError()));


            }
        }
    }
}