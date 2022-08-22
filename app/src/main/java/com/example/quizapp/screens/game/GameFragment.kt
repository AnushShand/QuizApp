package com.example.quizapp.screens.game

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizapp.OptionButton
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
        viewModel.optionStatus.observe(viewLifecycleOwner){
            for (i in 0..3)
            {
                val option = when(i){
                    0-> binding.option1
                    1-> binding.option2
                    2-> binding.option3
                    else-> binding.option4
                }
                if(it[i]=="Neutral")
                {
                    option.setIncorrect(false)
                    option.setCorrect(false)
                    option.visibility=View.VISIBLE
                    option.isClickable=true
                }
                else if(it[i]=="Correct")
                {
                    option.setCorrect(true)
                    option.isClickable=false
                }
                else if(it[i]=="Incorrect")
                {
                    option.isClickable=false
                    option.setIncorrect(true)
                }
                else
                {
                    option.visibility=View.INVISIBLE
                }
            }
        }
        viewModel.lifelineUsed.observe(viewLifecycleOwner){
            if(it["Call"]!!) {
                binding.lifelineCall.visibility = View.GONE
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + "8344814819")
                ContextCompat.startActivity(container!!.context,dialIntent,savedInstanceState)
            }
            if(it["Time"]!!)
                binding.lifelineTime.visibility=View.GONE
            if(it["Skip"]!!)
                binding.lifelineSkip.visibility=View.GONE
            if(it["Fifty"]!!)
                binding.lifeline50.visibility=View.GONE
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseTimer()
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTimer()
    }


}