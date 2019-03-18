package com.romain.mathieu.go4lunch.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.romain.mathieu.go4lunch.R;

import butterknife.ButterKnife;


public class MyListFragment extends Fragment {

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
        return view;
    }
}
