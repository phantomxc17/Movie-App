package com.example.movieapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.adapters.MovieRecyclerView;
import com.example.movieapp.adapters.OnMovieListener;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.viewmodels.MovieListViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnMovieListener{
    RecyclerView.LayoutManager layoutManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;
    private MovieListViewModel movieListViewModel;

    boolean isPopular = true;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Setup SearchView


        // Find RecyclerView in the inflated view
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);


        // Initialize ViewModel
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        // Get popular movies
        movieListViewModel.searchMoviePop(1);




        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ConfigureRecyclerView(view);
        ObserveAnyChange();
        ObservePopularMovies();

    }



    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(movieModels != null){
                    for(MovieModel movieModel: movieModels){
                        Log.v("Tagy", "OnChanged: " + movieModel.getTitle());

                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }
            }
        });
    }
    private void ObservePopularMovies(){
        movieListViewModel.getPop().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if(movieModels != null){
                    for(MovieModel movieModel: movieModels){
                        Log.v("Tagy", "OnChanged: " + movieModel.getTitle());

                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }
            }
        });
    }





    public HomeFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private void ConfigureRecyclerView(View view) {
        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerView);


        movieRecyclerAdapter = new MovieRecyclerView(this);

        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // RecyclerView OnScrollListener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollHorizontally(1)) {
                    // Jika tidak bisa menggulir ke bawah lagi, cari halaman berikutnya
                    movieListViewModel.searchNextPagePop();
                }
                if (!recyclerView.canScrollHorizontally(-1)) {
                    // Jika tidak bisa menggulir ke atas lagi, cari halaman sebelumnya
                    movieListViewModel.searchBeforePagePop();
                }
            }
        });
    }

    public void onMovieClick(int position) {

//        Toast.makeText(this, "The Position : " + position, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(requireActivity(), MovieDetails.class);
        intent.putExtra("movie", movieRecyclerAdapter.getSelectedMovie(position));
        startActivity(intent);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onCategoryClick(String category) {

    }


}