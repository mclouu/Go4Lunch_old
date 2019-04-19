package com.romain.mathieu.go4lunch.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.stetho.Stetho;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.model.User;
import com.romain.mathieu.go4lunch.model.UserHelper;
import com.romain.mathieu.go4lunch.view.UserViewHolder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class MyWorkmatesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private Context context;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar mProgressBar;
    private FirestoreRecyclerAdapter<User, UserViewHolder> adapter;

    public MyWorkmatesFragment() {
        // Required empty public constructor
    }


    public static MyWorkmatesFragment newInstance() {
        return (new MyWorkmatesFragment());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_workmates, container, false);
        context = container.getContext();


        mProgressBar = view.findViewById(R.id.progressBar_workmates);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout_workmates);

        this.adapterWorkmates();
        Stetho.initializeWithDefaults(context);

        swipeRefreshLayout.setOnRefreshListener(this);


        recyclerView = view.findViewById(R.id.recyclerView_workmates);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);


        return view;
    }

    public void onRefresh() {
        mProgressBar.setVisibility(View.VISIBLE);
        this.adapterWorkmates();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
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

    private void adapterWorkmates() {

        Query query = UserHelper.getUsersCollection().orderBy("username", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User user) {
                holder.setUserName(user.getUsername());
                holder.setChoice(user.getUsername() + " is eating (TDB)");

                Glide.with(MyWorkmatesFragment.this)
                        .load(user.getUrlPicture())
                        .placeholder(R.drawable.ic_edit_photo)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.getPhoto());
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmates, parent, false);
                return new UserViewHolder(view);
            }
        };
        mProgressBar.setVisibility(View.INVISIBLE);
    }


}
