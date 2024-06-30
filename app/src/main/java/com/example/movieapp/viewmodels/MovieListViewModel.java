package com.example.movieapp.viewmodels;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepository movieRepository;

    public MovieListViewModel() {

        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }
    public LiveData<List<MovieModel>> getPop(){
        return movieRepository.getPop();
    }

    // Calling method in View Model
    public void searchMovieApi(String query, int pageNumber){
        movieRepository.searchMovieApi(query, pageNumber);
    }

    public void searchMoviePop(int pageNumber){
        movieRepository.searchMoviePop(pageNumber);
    }



    public void searchNextPage(){
        movieRepository.searchNextPage();

    }

    public void searchBeforePage() {
        movieRepository.searchBeforePage();
    }

    public void searchNextPagePop(){
        movieRepository.searchNextPagePop();

    }

    public void searchBeforePagePop() {
        movieRepository.searchBeforePagePop();
    }
}
