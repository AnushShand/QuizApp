package com.example.quizapp.screens.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding:FragmentGameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_game,container,false)
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        binding.gameViewModel=viewModel
        binding.lifecycleOwner = this

        viewModel.currentQuestionNo.observe(viewLifecycleOwner){
            (activity as AppCompatActivity).supportActionBar?.title = "Question $it/10"
        }


        viewModel.gameOver.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(GameFragmentDirections.actionGameFragmentToScoreFragment())
            }
        }
        viewModel.incorrectOption.observe(viewLifecycleOwner) {
            when (it) {
                0 -> {
                    binding.option1.setIncorrect(true)
                }
                1 -> {
                    binding.option2.setIncorrect(true)
                }
                2 -> {
                    binding.option3.setIncorrect(true)
                }
                3 -> {
                    binding.option4.setIncorrect(true)
                }
                else -> {}
            }
        }

        viewModel.correctOption.observe(viewLifecycleOwner) {
            when (it) {
                0 -> {
                    binding.option1.setCorrect(true)
                    binding.option1.isClickable = false
                }
                1 -> {
                    binding.option2.setCorrect(true)
                    binding.option2.isClickable = false
                }
                2 -> {
                    binding.option3.setCorrect(true)
                    binding.option3.isClickable = false
                }
                3 -> {
                    binding.option4.setCorrect(true)
                    binding.option4.isClickable = false
                }
                else -> {}
            }
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseTimer()
        Log.i("GameFragment","Paused")
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTimer()
        Log.i("GameFragment","Resumed")
    }

}