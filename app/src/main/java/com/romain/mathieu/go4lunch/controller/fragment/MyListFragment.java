package com.romain.mathieu.go4lunch.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.model.CardData;
import com.romain.mathieu.go4lunch.model.MapStreams;
import com.romain.mathieu.go4lunch.model.api.ResponseMap;
import com.romain.mathieu.go4lunch.view.MyAdapter;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class MyListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public static ArrayList<CardData> list = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private MyAdapter adapter;
    Context context;
    private Disposable disposable;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

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
        Stetho.initializeWithDefaults(context);

        Stetho.initializeWithDefaults(context);

        swipeRefreshLayout.setOnRefreshListener(this);

        llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);

        adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);

        // 2 - Call the stream
        this.executeHttpRequestWithRetrofit();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    public void onRefresh() {
        mProgressBar.setVisibility(View.VISIBLE);
        this.executeHttpRequestWithRetrofit();
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

    }

    //-----------------------------------||
    //                                   ||
    //          HTTP (RxJava)            ||
    //                                   ||
    //-----------------------------------||


    // 1 - Execute our Stream
    private void executeHttpRequestWithRetrofit() {


        String location_chezmoi = "49.4996529, 5.7571362";
        String location_paris = "48.866667, 2.333333";
        String location_renaud = "49.117299, 6.088523";
        String radius = "1500";
        String type = "restaurant";
        String keyword = "";
//        String API_KEY = MyConstant.API_KEY;
        String API_KEY = "AIzaSyBW10_Ie5wh-vwbEXEfWzk2zOFOQ_xfDWk";
        this.disposable = MapStreams.streamFetchMap(location_chezmoi, radius, type, keyword, API_KEY).subscribeWith(
                new DisposableObserver<ResponseMap>() {
                    @Override
                    public void onNext(ResponseMap section) {
//                         1.3 - Update UI with topstories
                        updateUIWithListOfArticle(section);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
        );
    }

    private void updateUIWithListOfArticle(ResponseMap response) {

        if (list != null) {
            list.clear();
        }

        int num_results = response.getResults().size();
        for (int i = 0; i < num_results; i++) {

            String name = response.getResults().get(i).getName();
            String adresse = response.getResults().get(i).getVicinity();
            String horary = "Fermé";
//            if (response.getResults().get(i).getOpeningHours().getOpenNow()){
//                horary = "Ouvert";
//            }
            String distance = "200m";
            String numberWorkmates = "5";
            double rating = response.getResults().get(i).getRating();
            String photoRef;

            if (response.getResults().get(i).getPhotos() == null) {
                photoRef = "https://image.noelshack.com/fichiers/2018/17/7/1524955130-empty-image-thumb2.png";
            } else {
                photoRef = response.getResults().get(i).getPhotos().get(0).getPhotoReference();
            }

            list.add(new CardData(
                    name + " ",
                    adresse + " ",
                    horary + " ",
                    distance + " ",
                    numberWorkmates + " ",
                    rating,
                    photoRef
            ));
        }
        adapter.notifyDataSetChanged();

    }


    // 5 - Dispose subscription
    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

}
