package com.romain.mathieu.go4lunch.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.model.User;
import com.romain.mathieu.go4lunch.model.UserHelper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));

        this.updateUIWhenCreating();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_user_profile;
    }

    //FOR DESIGN
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.profile_activity_imageview_profile)
    ImageView imageViewProfile;
    @BindView(R.id.profile_activity_edit_text_username)
    EditText editTextUsername;
    @BindView(R.id.profile_activity_text_view_email)
    TextView textViewEmail;
    @BindView(R.id.profile_activity_check_box_is_x)
    CheckBox checkBoxIsX;
    @BindView(R.id.profile_activity_progress_bar)
    ProgressBar progressBar;

    private static final int UPDATE_USERNAME = 30;
    private static final int DELETE_USER_TASK = 20;


    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.profile_activity_check_box_is_x)
    public void onClickSwitchIsOn() {
        this.updateUserIsX();
    }

    @OnClick(R.id.profile_activity_button_update)
    public void onClickUpdateButton() {
        this.updateUsernameInFirebase();
    }

    @OnClick(R.id.profile_activity_button_delete)
    public void onClickDeleteButton() {
        // Build an AlertDialog for the About section
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        // Set Title and Message content
        builder.setTitle("supprimer son compte");
        builder.setMessage("Voulez vous vraiment supprimer votre compte ?");
        // Neutral button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.setPositiveButton("OK", (dialog, which) -> {
            ProfileActivity.this.deleteUserFromFirebase();
            dialog.cancel();
        });

        builder.show();
    }

    // --------------------
    // REST REQUESTS
    // --------------------

    private void deleteUserFromFirebase() {
        if (this.getCurrentUser() != null) {
            UserHelper.deleteUser(this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener());
            AuthUI.getInstance()
                    .delete(this)
                    .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK));


            //4 - We also delete user from firestore storage
        }
    }

    // 3 - Update User Username
    private void updateUsernameInFirebase() {

        this.progressBar.setVisibility(View.VISIBLE);
        String username = this.editTextUsername.getText().toString();

        UserHelper.updateUsername(username, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener())
                .addOnSuccessListener(this.updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK));
    }

    // 2 - Update User Mentor (is or not)
    private void updateUserIsX() {
        if (this.getCurrentUser() != null) {
            UserHelper.updateIsX(this.getCurrentUser().getUid(), this.checkBoxIsX.isChecked()).addOnFailureListener(this.onFailureListener());
        }
    }

    // --------------------
    // UI
    // --------------------

    // 1 - Update UI when activity is creating
    private void updateUIWhenCreating() {
//      Get picture URL from Firebase
        Glide.with(this)
                .load(this.getCurrentUser().getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(imageViewProfile);


//      Get email from Firebase
        String email = this.getCurrentUser().getEmail();
        this.textViewEmail.setText(email);
//      7 - Get additional data from Firestore (isMentor & Username)
        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
            User currentUser = documentSnapshot.toObject(User.class);
            String username = currentUser.getUsername();
            checkBoxIsX.setChecked(currentUser.getIsX());
            editTextUsername.setText(username);

        });
    }

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return aVoid -> {
            switch (origin) {
                // 8 - Hiding Progress bar after request completed
                case UPDATE_USERNAME:
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intentUpdate = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intentUpdate);
                    break;
                case DELETE_USER_TASK:
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intentDelete = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intentDelete);
                    break;
                default:
                    break;
            }
        };
    }
}