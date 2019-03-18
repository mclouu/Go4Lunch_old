package com.romain.mathieu.go4lunch.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romain.mathieu.go4lunch.R;

import butterknife.ButterKnife;


public class MyMapFragment extends Fragment {

    Context context;

    public MyMapFragment() {
        // Required empty public constructor
    }


    public static MyMapFragment newInstance() {
        return (new MyMapFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        context = container.getContext();

        ButterKnife.bind(this, view);
        return view;
    }
}
