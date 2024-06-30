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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchListFragment extends Fragment implements OnMovieListener {
    RecyclerView.LayoutManager layoutManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;
    private MovieListViewModel movieListViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_watch_list, container, false);

        // Find RecyclerView in the inflated view
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // Initialize ViewModel
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConfigureRecyclerView(view);
        loadWatchlist();
    }

    private void loadWatchlist() {
        List<MovieModel> watchlist = new ArrayList<>();
        Set<String> movieTitles = new HashSet<>();
        File path = getActivity().getFilesDir();
        File letDirectory = new File(path, "LET");

        if (!letDirectory.exists()) {
            letDirectory.mkdir();
        }

        File file = new File(letDirectory, "watchlist.txt");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineParts = line.split("#");
                String title = lineParts[0];
                String posterPath = lineParts[1];
                String movieOverview = lineParts[2];
                String releaseDate = lineParts[3];
                String originalLanguage = lineParts[4];
                String voteAverage = lineParts[5];

                if (!movieTitles.contains(title)) {
                    MovieModel movie = new MovieModel("", "", "","",0,"","");
                    movie.setTitle(title);
                    movie.setPoster_path(posterPath);
                    movie.setMovie_overview(movieOverview);
                    movie.setRelease_date(releaseDate);
                    movie.setVote_average(Float.parseFloat(voteAverage));
                    movie.setOriginal_language(originalLanguage);

                    watchlist.add(movie);
                    movieTitles.add(title);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        movieRecyclerAdapter.setmMovies(watchlist);
    }

    private void ObserveAnyChange() {
        movieListViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null) {
                    for (MovieModel movieModel : movieModels) {
                        Log.v("Tagy", "OnChanged: " + movieModel.getTitle());
                        movieRecyclerAdapter.setmMovies(movieModels);
                    }
                }
            }
        });
    }

    public WatchListFragment() {
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
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        movieRecyclerAdapter = new MovieRecyclerView(this);

        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // RecyclerView OnScrollListener
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    // If cannot scroll down anymore, load next page
                    movieListViewModel.searchNextPage();
                }
                if (!recyclerView.canScrollVertically(-1)) {
                    // If cannot scroll up anymore, load previous page
                    movieListViewModel.searchBeforePage();
                }
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(requireActivity(), MovieDetails.class);
        intent.putExtra("movie", movieRecyclerAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        // Handle category click event if needed
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
}
