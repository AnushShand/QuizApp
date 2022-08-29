package com.example.quizapp.screens.score

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.adapter.ScoreAdapter
import com.example.quizapp.database.HighScoreDatabase
import com.example.quizapp.databinding.FragmentScoreBinding

//This Fragment is used to implement the high-score screen of the App
class ScoreFragment : Fragment() {
    private lateinit var binding: FragmentScoreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        (activity as AppCompatActivity).supportActionBar?.title = "High Scores"
        val application= requireNotNull(this.activity).application
        val dataSource=HighScoreDatabase.getInstance(application).highScoreDao
        val viewModelFactory=ScoreViewModelFactory(dataSource)
        val viewModel=ViewModelProvider(this, factory = viewModelFactory)[ScoreViewModel::class.java]
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_score,container,false)
        binding.scoreViewModel=viewModel
        binding.lifecycleOwner = this
        binding.playAgain.setOnClickListener{findNavController().navigate(ScoreFragmentDirections.actionScoreFragmentToGameFragment())}

        viewModel.getData()
        viewModel.data.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter =
                ScoreAdapter(this.requireContext(), it,it.size.toLong())
        }
        return binding.root
    }
}