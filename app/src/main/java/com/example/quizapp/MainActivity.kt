package com.example.quizapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.getSupportActionBar()?.hide();
        setContentView(R.layout.activity_main)
    }
}