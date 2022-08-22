package com.example.quizapp.screens.score

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.quizapp.R
import com.example.quizapp.database.HighScoreDatabase
import com.example.quizapp.databinding.FragmentScoreBinding
import com.example.quizapp.screens.game.GameViewModel

class ScoreFragment : Fragment() {
    private lateinit var binding: FragmentScoreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val application= requireNotNull(this.activity).application
        val dataSource=HighScoreDatabase.getInstance(application).highScoreDao
        val viewModelFactory=ScoreViewModelFactory(dataSource)
        val viewModel=ViewModelProvider(this, factory = viewModelFactory)[ScoreViewModel::class.java]
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_score,container,false)
        binding.scoreViewModel=viewModel
        binding.lifecycleOwner = this
        return binding.root

    }
}