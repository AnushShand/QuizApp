package com.example.quizapp.screens.gameEnd

import com.example.quizapp.screens.score.ScoreViewModel
import com.example.quizapp.screens.score.ScoreViewModelFactory
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizapp.R
import com.example.quizapp.database.HighScoreDatabase
import com.example.quizapp.databinding.FragmentGameEndBinding

class GameEndFragment : Fragment() {
    private lateinit var binding: FragmentGameEndBinding
    private lateinit var viewModel:GameEndViewModel
    val args:GameEndFragmentArgs by navArgs()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        val application= requireNotNull(this.activity).application
        val dataSource=HighScoreDatabase.getInstance(application).highScoreDao
        val viewModelFactory= GameEndViewModelFactory(args.score,args.timeTaken,dataSource)
        viewModel=ViewModelProvider(this, factory = viewModelFactory)[GameEndViewModel::class.java]
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_game_end,container,false)
        binding.endViewModel=viewModel
        binding.lifecycleOwner = this
        viewModel.databaseOperations()
        (activity as AppCompatActivity).supportActionBar?.title = "Well Played!"
        //This code is for testing must be change when ui is changed
        binding.textView.text=args.score.toString()
        binding.button.setOnClickListener(){findNavController().navigate(GameEndFragmentDirections.actionGameEndFragmentToScoreFragment())}


        return binding.root
    }

}