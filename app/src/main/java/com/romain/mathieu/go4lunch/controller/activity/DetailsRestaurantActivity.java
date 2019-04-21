package com.romain.mathieu.go4lunch.controller.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.model.User;
import com.romain.mathieu.go4lunch.model.UserHelper;
import com.romain.mathieu.go4lunch.model.api.placeDetails.ResponseDetails;
import com.romain.mathieu.go4lunch.model.request.MapStreams;
import com.romain.mathieu.go4lunch.utils.SharedPreferencesUtils;
import com.romain.mathieu.go4lunch.view.UserViewHolder;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailsRestaurantActivity extends BaseActivity {


    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter<User, UserViewHolder> adapter;

    public static HashMap<String, Boolean> hashMapLike = new HashMap<>();
    private static final int RC_CALL_PERMS = 330;
    private static final String PERMS = Manifest.permission.CALL_PHONE;

    @BindView(R.id.restaurant_image_view)
    ImageView img;
    @BindView(R.id.details_restaurant_name)
    TextView restaurantName;
    @BindView(R.id.details_retaurant_address)
    TextView restaurantAdresse;
    @BindView(R.id.like)
    ImageView like;
    boolean isLike = false;

    private Disposable disposable;
    String name, adresse, placeID, urlImg;
    private String phoneNumber, webSite, opening_hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        placeID = intent.getStringExtra("placeID");
        isLike = intent.getBooleanExtra("isLike", false);

        this.executeHttpRequestWithRetrofit();

        this.adapterWorkmates();
        if (SharedPreferencesUtils.containsHashMap(this)) {
            hashMapLike = SharedPreferencesUtils.getHashMap(this);
        }


        recyclerView = findViewById(R.id.restaurant_attendees_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_details_restaurant;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.saveLikeBdd();
        this.disposeWhenDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    private void saveLikeBdd() {

        UserHelper.restaurantLiked(hashMapLike, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener())
                .addOnSuccessListener(aVoid -> {
                });
    }


    public void updateUi() {

        // TextView !
        restaurantName.setText(name);
        restaurantAdresse.setText(adresse);

        // Image !
        Glide.with(DetailsRestaurantActivity.this)
                .load(urlImg)
                .into(img);

        // Like !
        if (isLike) {
            like.setVisibility(View.VISIBLE);
        } else {
            like.setVisibility(View.INVISIBLE);
        }

        // user reserved

    }

    //-----------------------------------||
    //                                   ||
    //          HTTP (RxJava)            ||
    //                                   ||
    //-----------------------------------||


    // 1 - Execute our Stream
    private void executeHttpRequestWithRetrofit() {

        String fields = "formatted_phone_number,website,opening_hours,formatted_address,name,photos";
        String API_KEY = "AIzaSyBW10_Ie5wh-vwbEXEfWzk2zOFOQ_xfDWk";

        this.disposable = MapStreams.streamFetchDetails(placeID, fields, API_KEY).subscribeWith(
                new DisposableObserver<ResponseDetails>() {
                    @Override
                    public void onNext(ResponseDetails response) {
                        name = response.getResult().getName();
                        adresse = response.getResult().getFormattedAddress();
                        phoneNumber = response.getResult().getFormattedPhoneNumber();
                        webSite = response.getResult().getWebsite();
                        String photoReference = response.getResult().getPhotos().get(1).getPhotoReference();
                        String key = "&key=AIzaSyBW10_Ie5wh-vwbEXEfWzk2zOFOQ_xfDWk";
                        urlImg = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + key;
                        opening_hours = " ";
                        updateUi();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                }
        );
    }

    // 5 - Dispose subscription
    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    //-----------------------------------||
    //              ACTION               ||
    //-----------------------------------||

    @OnClick(R.id.btn_call)
    @AfterPermissionGranted(RC_CALL_PERMS)
    public void onClickButtonCall() {
        if (phoneNumber != null) {
            if (!EasyPermissions.hasPermissions(this, PERMS)) {
                EasyPermissions.requestPermissions(this, "", RC_CALL_PERMS, PERMS);
                return;
            }
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);

        } else {
            Toast.makeText(this, "Ce restaurant ne possède pas de numéro de téléphone", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_like)
    public void onClickButtonLike() {

        if (isLike) {
            like.setVisibility(View.INVISIBLE);
            isLike = false;
        } else {
            like.setVisibility(View.VISIBLE);
            isLike = true;
        }

        hashMapLike.put(name, isLike);
        SharedPreferencesUtils.saveHashMap(this);

    }

    @OnClick(R.id.btn_website)
    public void onClickButtonWebsite() {
        if (webSite != null) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("website", webSite);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Ce restaurant ne possède pas de site internet", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.fab_book)
    public void onClickFab() {
        UserHelper.reservedRestaurant(placeID, this.getCurrentUser().getUid()).addOnFailureListener(this.onFailureListener())
                .addOnSuccessListener(aVoid -> {
                });
    }


    //-----------------------------------||
    //                                   ||
    //          ADAPTER FIRESTORE        ||
    //                                   ||
    //-----------------------------------||

    private void adapterWorkmates() {

        Query query = UserHelper.getUsersCollection().orderBy("username", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User user) {

                if (placeID.equals(user.getReservedRestaurant())) {
                    holder.setUserName(user.getUsername());
                    holder.setChoice(user.getUsername() + " is joining !");

                    Glide.with(DetailsRestaurantActivity.this)
                            .load(user.getUrlPicture())
                            .placeholder(R.drawable.ic_edit_photo)
                            .apply(RequestOptions.circleCropTransform())
                            .into(holder.getPhoto());
                } else {
                    holder.setVisibilityRecyclerView(View.INVISIBLE);
                }
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates, parent, false);
                return new UserViewHolder(view);
            }
        };
    }
}
