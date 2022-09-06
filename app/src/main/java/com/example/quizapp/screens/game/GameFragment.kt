package com.example.quizapp.screens.game

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Configuration
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
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentGameBinding

//This Fragment is used to implement the main quiz screen of the App
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding:FragmentGameBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        } else {
            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        }
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_game,container,false)
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        binding.gameViewModel=viewModel
        binding.lifecycleOwner = this

        viewModel.currentQuestionNo.observe(viewLifecycleOwner){
            (activity as AppCompatActivity).supportActionBar?.title = "Question $it/10"
        }

        //Observe the gamOver live data and when it is set to true, navigate to the GameEndFragment
        viewModel.gameOver.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(GameFragmentDirections.actionGameFragmentToGameEndFragment(viewModel.score.value?:-1,viewModel.totalTime.value?:0L))
                viewModel.doneNavigating()
            }
        }

        //Observe the optionStatus("Neutral","Correct","Incorrect") and set it's visibility accordingly
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
                    val animator = ObjectAnimator.ofFloat(option, View.ROTATION, -360f, 0f)
                    animator.duration = 1000
                    animator.start()
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

        //Observe the lifeline status and set it's visibility based on if it has been used or not
        viewModel.lifelineUsed.observe(viewLifecycleOwner){
            if(it["Time"]!!)
                binding.lifelineTime.visibility=View.GONE
            if(it["Skip"]!!)
                binding.lifelineSkip.visibility=View.GONE
            if(it["Fifty"]!!)
                binding.lifeline50.visibility=View.GONE
        }

        //The call lifeline has been observed separately as it is used to call an Intent
        viewModel.callLifeline.observe(viewLifecycleOwner)
        {
            if(it) {
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + "8344814819")
                ContextCompat.startActivity(container!!.context, dialIntent, savedInstanceState)
                binding.lifelineCall.visibility = View.GONE
            }
        }
        return binding.root
    }

    //This function has been overriden to hide the action bar if the screen is in landscape mode
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        } else {
            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseTimer() //Pause the timer when app is in the
    }

    override fun onResume() {
        super.onResume()
        viewModel.startTimer()
    }
}