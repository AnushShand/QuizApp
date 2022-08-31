package com.example.quizapp.screens.gameEnd

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.quizapp.R
import com.example.quizapp.database.HighScoreDatabase
import com.example.quizapp.databinding.FragmentGameEndBinding
import com.example.quizapp.screens.title.SharedNameViewModel
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

//This Fragment is used to implement the game end screen of the App
class GameEndFragment : Fragment() {
    private lateinit var binding: FragmentGameEndBinding
    private lateinit var viewModel:GameEndViewModel
    private lateinit var sharedNameViewModel: SharedNameViewModel
    private val args:GameEndFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity).supportActionBar?.title = "Well Played!"
        val application= requireNotNull(this.activity).application
        val dataSource=HighScoreDatabase.getInstance(application).highScoreDao
        sharedNameViewModel= ViewModelProvider(requireActivity())[SharedNameViewModel::class.java]
        val viewModelFactory= GameEndViewModelFactory(sharedNameViewModel.name.value.toString(),args.score,args.timeTaken,dataSource)
        viewModel=ViewModelProvider(this, factory = viewModelFactory)[GameEndViewModel::class.java]
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_game_end,container,false)
        binding.endViewModel=viewModel
        binding.lifecycleOwner = this
        binding.currentNameText.text=sharedNameViewModel.name.value.toString()
        binding.currentScoreText.text=args.score.toString()
        binding.highScoreButton.setOnClickListener{findNavController().navigate(GameEndFragmentDirections.actionGameEndFragmentToScoreFragment())}
        binding.takeImageButton.setOnClickListener{capturePhoto()}
        binding.shareButton.setOnClickListener{share(container)}

        viewModel.insertLauncher()
        return binding.root
    }

    //This function creates a temporary file to store the screenshot
    private fun createImageFile():File{
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HH").format(Date())
        val storageDir: File = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("SS_$timeStamp",".jpeg",storageDir)
    }

    private fun share(container:ViewGroup?)
    {
        val ss: Bitmap = getScreenShotFromView(container!!.rootView)
        val tempFile:File=createImageFile()
        val stream = FileOutputStream(tempFile)
        ss.compress(Bitmap.CompressFormat.JPEG,90,stream)
        stream.close()
        shareImageandText(FileProvider.getUriForFile(container.context,"com.example.quizapp.fileprovider",tempFile))
        tempFile.delete()
    }
    private fun shareImageandText(ssUri:Uri) {
        Log.i("GameEnd",ssUri.toString())
        val uri: Uri = ssUri
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, "Try out this amazing QuizApp")
        intent.putExtra(Intent.EXTRA_SUBJECT, "I am GOAT of QuizApp")
        intent.type = "image/jpeg"
        startActivity(Intent.createChooser(intent, "Share Via"))
    }

    private fun getScreenShotFromView(v: View): Bitmap{
        var screenshot: Bitmap? = null
        try {
            screenshot = Bitmap.createBitmap(v.measuredWidth, (v.measuredHeight*0.65).toInt(), Bitmap.Config.ARGB_8888)
            val canvas = Canvas(screenshot)
            v.draw(canvas)
        } catch (e: Exception) {
            Log.e("GameEndFragment", "Failed to capture screenshot because:" + e.message)
        }
        return screenshot!!
    }

    private fun capturePhoto()
    {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        getPhotoResult.launch(cameraIntent)
        binding.shareButton.visibility=View.VISIBLE
    }
    private val getPhotoResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if (it.resultCode == Activity.RESULT_OK) {
                val imageBitmap = it.data?.extras?.get("data") as Bitmap
                binding.imageView.setImageBitmap(imageBitmap)
            }
        }
}