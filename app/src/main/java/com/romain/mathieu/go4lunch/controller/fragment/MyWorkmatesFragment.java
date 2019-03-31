package com.romain.mathieu.go4lunch.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romain.mathieu.go4lunch.R;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;


public class MyWorkmatesFragment extends Fragment {

    Context context;

    public MyWorkmatesFragment() {
        // Required empty public constructor
    }


    public static MyWorkmatesFragment newInstance() {
        return (new MyWorkmatesFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        context = container.getContext();

        ButterKnife.bind(this, view);
        return view;
    }
}
