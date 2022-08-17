package com.example.quizapp.screens.game

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentGameBinding
import com.example.quizapp.OptionButton

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding:FragmentGameBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_game,container,false)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.gameViewModel=viewModel
        binding.setLifecycleOwner(this)
        viewModel.incorrectOption.observe(viewLifecycleOwner, Observer { it->

            Log.i("GameViewModel",it.toString())
            when(it)
            {
                0-> {
                    binding.option1.setIncorrect(true)
                }
                1-> {
                    binding.option2.setIncorrect(true)
                }
                2-> {
                    binding.option3.setIncorrect(true)
                }
                3-> {
                    binding.option4.setIncorrect(true)
                }
                else->{}
            }
        })
        return binding.root
    }
}