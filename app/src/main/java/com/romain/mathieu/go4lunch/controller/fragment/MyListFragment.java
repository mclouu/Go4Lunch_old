package com.romain.mathieu.go4lunch.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.stetho.Stetho;
import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.model.CardData;
import com.romain.mathieu.go4lunch.view.MyAdapter;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyListFragment extends Fragment {


    public static ArrayList<CardData> list = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private MyAdapter adapter;
    Context context;

    public MyListFragment() {
        // Required empty public constructor
    }


    public static MyListFragment newInstance() {
        return (new MyListFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        context = container.getContext();

        ButterKnife.bind(this, view);
        Stetho.initializeWithDefaults(Objects.requireNonNull(getActivity()));

        llm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(llm);

        adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);

        return view;
    }


}
