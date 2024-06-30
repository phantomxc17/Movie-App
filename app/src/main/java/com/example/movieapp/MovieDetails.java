package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieapp.models.MovieModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MovieDetails extends AppCompatActivity {

    // Widgets
    private ImageView imageViewDetails;
    private TextView titleDetails, descDetails;
    private RatingBar ratingBarDetails;
    private MovieModel movieModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        //create/check watchlist file
        File path = getFilesDir();
        if (!new File(path, "LET").exists()) {
            new File(path, "LET").mkdir();
        }
        File letDirectory = new File(path, "LET");
        if (!(new File(letDirectory, "/watchlist.txt").exists())) {
            Log.d("readFile", "file created");
            try {
                new File(path,"/watchlist.txt").createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            Log.d("readFile","File existed");
        }
        File file = new File(letDirectory, "watchlist.txt");

        imageViewDetails = findViewById(R.id.imageViewDetails);
        titleDetails = findViewById(R.id.textView_title_details);
        descDetails = findViewById(R.id.textView_desc_details);
        ratingBarDetails = findViewById(R.id.ratingBar_details);

        // Get movie data from intent
        movieModel = getIntent().getParcelableExtra("movie");
        if (movieModel != null) {
            // Populate UI with movie data
            populateMovieDetails(movieModel);

            // Initialize and set click listener for bookmark button
            Button bookmarkButton = findViewById(R.id.bookmarkButton);
            bookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call a method to handle bookmarking the movie
                    bookmarkMovie(movieModel,file);
                }
            });
        } else {
            // Handle case when movieModel is null
            Log.e("MovieDetails", "MovieModel is null");
            finish(); // Finish the activity if movieModel is null
        }
    }

    private void populateMovieDetails(MovieModel movieModel) {
        titleDetails.setText(movieModel.getTitle());
        descDetails.setText(movieModel.getMovie_overview());
        ratingBarDetails.setRating(movieModel.getVote_average() / 2);
        ratingBarDetails.setIsIndicator(true);

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500/" + movieModel.getPoster_path())
                .into(imageViewDetails);
    }

    private void bookmarkMovie(MovieModel movie, File file) {
        try {
            FileWriter fileWriter = new FileWriter(file,true);
            fileWriter.append(String.valueOf(movie.getTitle())).append("#").append(movie.getPoster_path()).append("#").append(movie.getMovie_overview()).append("#").append(movie.getRelease_date()).append("#").append(movie.getOriginal_language()).append("#").append(String.valueOf(movie.getVote_average())).append("#").append("\n");
            fileWriter.close();
            // After bookmarking, navigate to WatchListFragment and pass movie ID
            Bundle bundle = new Bundle();
            bundle.putString("movie_id", movie.getMovie_id());

            // Create an instance of WatchListFragment
            WatchListFragment watchListFragment = new WatchListFragment();
            watchListFragment.setArguments(bundle);
            Toast.makeText(this, "Movie Bookmarked", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
