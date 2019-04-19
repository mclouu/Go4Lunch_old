package com.romain.mathieu.go4lunch.controller.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.model.User;
import com.romain.mathieu.go4lunch.model.UserHelper;

import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ProfileActivity extends BaseActivity {



    // 1 - STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int UPDATE_PROFILE = 30;
    private static final int DELETE_USER_TASK = 20;
    private static final int UPDATE_PHOTO_USER_TASK = 10;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;

    // 2 - Uri of image selected by user
    private Uri uriImageSelectedFromPhone;

    Intent intentMainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));
        btnUpdate.setClickable(true);

        this.updateUIWhenCreating();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_user_profile;
    }

    //-----------------------------------||
    //              FOR DESIGN           ||
    //-----------------------------------||
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
    @BindView(R.id.profile_activity_button_update)
    Button btnUpdate;


    //-----------------------------------||
    //              ACTION               ||
    //-----------------------------------||

    @OnClick(R.id.profile_activity_imageview_profile)
    @AfterPermissionGranted(RC_IMAGE_PERMS)
    public void onClickAddFile() {
        this.chooseImageFromPhone();
    }

    @OnClick(R.id.profile_activity_check_box_is_x)
    public void onClickSwitchIsOn() {
        this.updateUserIsX();
    }

    @OnClick(R.id.profile_activity_button_update)
    public void onClickUpdateButton() {
        progressBar.setVisibility(View.VISIBLE);
        btnUpdate.setClickable(false);
        //TODO : changer la couleur du bouton !!
        this.uploadPhotoInFirebaseAndUpdatePhoto();
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
            deleteUserFromFirebase();
            dialog.cancel();
        });

        builder.show();
    }

    //-----------------------------------||
    //              FIREBASE             ||
    //       Delete and update Users     ||
    //-----------------------------------||

    // 1 - Delete user from database
    private void deleteUserFromFirebase() {
        if (this.getCurrentUser() != null) {
            progressBar.setVisibility(View.VISIBLE);

            //4 - We also delete user from firestore storage
            UserHelper.deleteUser(this.getCurrentUser().getUid())
                    .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(DELETE_USER_TASK))
                    .addOnFailureListener(onFailureListener());
        }
    }

    // 2 - Update User Username
    private void updateUsernameInFirebase() {
        String username = this.editTextUsername.getText().toString();
        UserHelper.updateUsername(username, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener())
                .addOnSuccessListener(this.updateUIAfterRESTRequestsCompleted(UPDATE_PROFILE));
    }

    // 3 - Update User Mentor (is or not)
    private void updateUserIsX() {
        boolean checkBox = this.checkBoxIsX.isChecked();

        UserHelper.updateIsX(checkBox, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener())
                .addOnSuccessListener(this.updateUIAfterRESTRequestsCompleted(UPDATE_PROFILE));
    }

    //---------------------------||
    //            UI             ||
    //---------------------------||

    // 1 - Update UI when activity is creating
    private void updateUIWhenCreating() {
        // Get email from Firebase
        String email = this.getCurrentUser().getEmail();
        this.textViewEmail.setText(email);
        // 2 - Get Username and boolean data from Firestore
        UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
            User currentUser = documentSnapshot.toObject(User.class);
            String username = currentUser.getUsername();
            checkBoxIsX.setChecked(currentUser.getIsX());
            editTextUsername.setText(username);
            Glide.with(ProfileActivity.this)
                    .load(currentUser.getUrlPicture())
                    .placeholder(R.drawable.ic_edit_photo)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewProfile);
        });
    }


    //---------------------------------||
    //       FILE MANAGER (1)          ||
    //  UPLOAD IMAGE INTO DATABASE (3) ||
    //---------------------------------||

    private void chooseImageFromPhone() {
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, "Go4Lunch recupère seulement les images que VOUS envoyez", RC_IMAGE_PERMS, PERMS);
            return;
        }
        // 1 - Launch an "Selection Image" Activity
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RC_CHOOSE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2 - Calling the appropriate method after activity result
        this.handleResponse(requestCode, resultCode, data);
    }

    // 3 - response after user has chosen or not a picture
    private void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                this.uriImageSelectedFromPhone = data.getData();

                Glide.with(this) //SHOWING PREVIEW OF IMAGE
                        .load(this.uriImageSelectedFromPhone)
                        .placeholder(R.drawable.ic_edit_photo)
                        .apply(RequestOptions.circleCropTransform())
                        .into(this.imageViewProfile);
            } else {
                Toast.makeText(this, "Aucune image selectioné", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 4 - Upload a picture in Firebase
    private void uploadPhotoInFirebaseAndUpdatePhoto() {
        if (uriImageSelectedFromPhone != null) {
            String uuid = UUID.randomUUID().toString(); // GENERATE UNIQUE STRING
            // A - UPLOAD TO GCS
            final StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);

            mImageRef.putFile(uriImageSelectedFromPhone).addOnSuccessListener(this, taskSnapshot ->
                    mImageRef.getDownloadUrl().addOnSuccessListener(uri ->
                            UserHelper.updateUserPhoto(Objects.requireNonNull(uri).toString(), ProfileActivity.this.getCurrentUser().getUid())
                                    .addOnSuccessListener(updateUIAfterRESTRequestsCompleted(UPDATE_PHOTO_USER_TASK))
                    )
            );
        }
    }

    //---------------------------||
    //         ON SUCCESS        ||
    //---------------------------||

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return aVoid -> {
            switch (origin) {
                // 8 - Hiding Progress bar after request completed
                case DELETE_USER_TASK:
                    AuthUI.getInstance()
                            .delete(this)
                            .addOnFailureListener(onFailureListener());
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intentLoginActivity = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intentLoginActivity);
                    break;
                case UPDATE_PROFILE:
                    if (uriImageSelectedFromPhone == null) {
                        intentMainActivity = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(intentMainActivity);
                    }
                    break;
                case UPDATE_PHOTO_USER_TASK:
                    intentMainActivity = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intentMainActivity);
                    break;
                default:
                    break;
            }
        };
    }

    //---------------------------||
    //         PERMISSIONS       ||
    //---------------------------||
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 2 - Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}