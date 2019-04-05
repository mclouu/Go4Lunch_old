package com.romain.mathieu.go4lunch.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.stetho.Stetho;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.controller.fragment.MyListFragment;
import com.romain.mathieu.go4lunch.controller.fragment.MyMapFragment;
import com.romain.mathieu.go4lunch.controller.fragment.MyWorkmatesFragment;
import com.romain.mathieu.go4lunch.model.User;
import com.romain.mathieu.go4lunch.model.UserHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.drawer_navigation)
    NavigationView drawerNavigation;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    // UI DRAWER USER
    View header;
    TextView userName;
    TextView userEmail;
    ImageView userPhoto;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_map:
                showFragment(new MyMapFragment());
                return true;
            case R.id.navigation_list:
                showFragment(new MyListFragment());
                return true;
            case R.id.navigation_workmates:
                showFragment(new MyWorkmatesFragment());
                return true;

        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);

        setSupportActionBar(toolbar);
        setTitle(getString(R.string.app_name));

        // USER INFO
        header = drawerNavigation.getHeaderView(0);

        userName = header.findViewById(R.id.username);
        userEmail = header.findViewById(R.id.useremail);
        userPhoto = header.findViewById(R.id.userphoto);


        this.updateUIWhenLoging();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        drawerNavigation.setNavigationItemSelectedListener(this);


        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        showFragment(new MyMapFragment());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.activity_main_frame_layout, new MyMapFragment());
        fragmentTransaction.commit();
    }

    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    // 1 - Update UI when activity is creating
    private void updateUIWhenLoging() {

        if (this.getCurrentUser() != null) {

            userEmail.setText(this.getCurrentUser().getEmail());

            UserHelper.getUser(this.getCurrentUser().getUid()).addOnSuccessListener(documentSnapshot -> {
                User currentUser = documentSnapshot.toObject(User.class);
                String username = currentUser.getUsername();
                userName.setText(username);
                Glide.with(this)
                        .load(currentUser.getUrlPicture())
                        .placeholder(R.drawable.imageempty)
                        .apply(RequestOptions.circleCropTransform())
                        .into(userPhoto);
            });
        }
    }


    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_frame_layout, fragment)
                .commit();
    }

    //-------------
    // NAVIGATION DRAWER
    //-------------

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // 6 - Show fragment after user clicked on a menu item
        switch (id) {
            case R.id.menu_drawer_1:
                this.onLunchSelected();
                break;
            case R.id.menu_drawer_2:
                this.onSettingSelected();
                break;
            case R.id.menu_drawer_3:
                this.onLogoutSelected();
                break;
            case R.id.menu_drawer_4:
                this.onEditProfileSelected();
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onEditProfileSelected() {
        Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(myIntent);
    }

    private void onLunchSelected() {
    }

    private void onSettingSelected() {
        Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(myIntent);
    }

    private void onLogoutSelected() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(aVoid -> {
                    Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(myIntent);
                });

    }
}
