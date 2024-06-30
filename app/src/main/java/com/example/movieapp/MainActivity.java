package com.example.movieapp;



import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



import android.annotation.SuppressLint;

import android.os.Bundle;

import com.example.movieapp.adapters.OnMovieListener;
import com.example.movieapp.databinding.ActivityMainBinding;


import java.util.List;

public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Bottom Navigation Menu
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.Home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.searchMovie) {
                replaceFragment(new SearchFragment());
            }else if (item.getItemId() == R.id.watchlist) {
                replaceFragment(new WatchListFragment());
            }
            return true;
        });


    }


    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

}