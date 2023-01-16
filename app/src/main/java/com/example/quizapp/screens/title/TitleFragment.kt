package com.example.quizapp.screens.title

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.quizapp.R
import com.example.quizapp.database.SettingsDataStore
import com.example.quizapp.databinding.FragmentTitleBinding
import com.example.quizapp.util.cancelNotifications
import com.example.quizapp.util.sendNotification
import kotlinx.coroutines.launch


class TitleFragment : Fragment() {
    private lateinit var binding:FragmentTitleBinding
    private lateinit var viewModel: SharedNameViewModel
    private lateinit var settingsDataStore: SettingsDataStore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //Set the action-bar
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity).supportActionBar?.title = "QuizApp"


        settingsDataStore = SettingsDataStore(requireContext())
        viewModel=ViewModelProvider(requireActivity())[SharedNameViewModel::class.java]
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_title,container,false)
        binding.playButton.setOnClickListener{playGame()}
        binding.scoreButton.setOnClickListener{viewScore()}
        binding.about.setOnClickListener{findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToAboutFragment())}
        //Enable the Play and HighScore button only after the user has entered a name
        binding.editName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.isEmpty()) {
                    binding.playButton.visibility=View.INVISIBLE
                    binding.scoreButton.visibility=View.INVISIBLE
                } else {
                    binding.playButton.visibility=View.VISIBLE
                    binding.scoreButton.visibility=View.VISIBLE
                }
            }
        })

        //Data Store
        binding.checkRemember.setOnClickListener{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            if(binding.checkRemember.isChecked)
                lifecycleScope.launch {settingsDataStore.storeName(requireContext(),binding.editName.text.toString())}
            else
                lifecycleScope.launch {settingsDataStore.storeName(requireContext(),"")}
        }
        settingsDataStore.nameFlow.asLiveData().observe(viewLifecycleOwner) {
            binding.editName.setText(it.toString())
        }
        return binding.root
    }

    //This function navigates to GameFragment
    private fun playGame()
    {
        viewModel.changeName(binding.editName.text.toString())
        findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToGameFragment())
    }

    //This function navigates to ScoreFragment
    private fun viewScore()
    {
        viewModel.changeName(binding.editName.text.toString())
        findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToScoreFragment())
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Resume Game"
            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    private fun sendPauseNotification()
    {
        createChannel(
            getString(R.string.game_notification_channel_id),
            getString(R.string.game_notification_channel_name)
        )
        val notificationManager = ContextCompat.getSystemService(
            requireContext(),
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification("Return to Game ${binding.editName.text}", requireContext())
    }
    private fun cancelNotification()
    {
        val notificationManager = ContextCompat.getSystemService(
            requireContext(),
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotifications()
    }


    override fun onPause() {
        super.onPause()
        sendPauseNotification()
    }

    override fun onResume() {
        super.onResume()
        cancelNotification()
    }
}