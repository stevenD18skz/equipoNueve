package com.example.dogapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dogapp.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatDelegate;

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}