package com.example.quizapp.screens.game
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapp.OptionButton
import com.example.quizapp.R


class GameViewModel : ViewModel() {
    data class Question(val text:String,val choices:MutableList<String>)
    private lateinit var questionList:MutableList<Question>

    private val _currentQuestion=MutableLiveData<Question>()
    val currentQuestion:LiveData<Question>
        get()=_currentQuestion

    private val _currentAnswer=MutableLiveData<String>()
    val currentAnswer:LiveData<String>
        get()=_currentAnswer

    private val _incorrectOption=MutableLiveData<Int>()
    val incorrectOption:LiveData<Int>
        get()=_incorrectOption

    init{
        resetQuestions()
        nextQuestion()
    }
    private fun nextQuestion()
    {
        _currentAnswer.value=questionList[0].choices[0]
        questionList[0].choices.shuffle()
        _currentQuestion.value=questionList.removeAt(0)
    }
    private fun resetQuestions()
    {
        //First Option will always be the correct answer before shuffling
        questionList= mutableListOf(
            Question("What is the capital of India?", mutableListOf("Delhi","Mumbai","Chennai","Bangalore")),
            Question("How many legs does a spider have?", mutableListOf("Eight","Six","Four","Ten")),
            Question("What colors are the stars on the American flag?", mutableListOf("White","Black","Red","Blue")),
            Question("What type of fish is Nemo?", mutableListOf("Clown Fish","Star Fish","Shark","Piranha")),
            Question("Who won the FIFA World Cup 2018?", mutableListOf("France","Croatia","Brazil","Argentina")),
            Question("Which country will host the FIFA World Cup 2022?", mutableListOf("Qatar","Brazil","UAE","Italy")),
            Question("Who is the current president of India?", mutableListOf("Droupadi Murmu","Narendra Modi","Ram Nath Kovind","Pranab Mukherjee")),
            Question("How many bones does an adult human have?", mutableListOf("206","204","207","307")),
            Question("What is the language of Lakshadweep,a Union Territory of India?", mutableListOf("Malyalam","Hindi","Telugu","Tamil")),
            Question("What is September 27 celebrated every year as?", mutableListOf("World Tourism Day","National Integration Day","Teacher's Day","Mother's Day")),
            Question("What event is Good Friday observed to commemorate?", mutableListOf("Crucifixion of Jesus Christ","Birth of Jesus Christ","Re-birth of jesus christ","Birth of St. Peter")),
            Question("What is the capital of Rajasthan?", mutableListOf("Jaipur","Udaipur","Jaisalmer","Ahmedabad"))
        )
        questionList.shuffle()
    }
    fun checkAnswer(optionSelected:Int)
    {
        if(_currentQuestion.value!!.choices[optionSelected]==_currentAnswer.value)
            onCorrect()
        else
            onIncorrect(optionSelected)

    }
    private fun onCorrect(){
        nextQuestion()
    }

    private fun onIncorrect(optionSelected: Int=-1) {
        _incorrectOption.value=optionSelected
        Log.i("GameViewModel","Incorrect Answer")
    }

    override fun onCleared() {
        super.onCleared()
    }
}