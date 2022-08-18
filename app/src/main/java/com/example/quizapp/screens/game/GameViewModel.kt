package com.example.quizapp.screens.game
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    companion object{
        private const val ONE_SECOND=1000L
        private const val COUNTDOWN_TIME=35000L
    }

    data class Question(val text:String,val choices:MutableList<String>)
    private lateinit var questionList:MutableList<Question>

    private val timer:CountDownTimer

    private val _currentQuestionNo=MutableLiveData<Int>()
    val currentQuestionNo:LiveData<Int>
        get()=_currentQuestionNo

    private val _currentTime=MutableLiveData<Long>()
    val currentTime:LiveData<Long>
        get()=_currentTime

    private val _totalTime=MutableLiveData<Long>()
    val totalTime:LiveData<Long>
        get()=_totalTime

    private val _score=MutableLiveData<Int>()
    val score:LiveData<Int>
        get()=_score

    private val _currentQuestion=MutableLiveData<Question>()
    val currentQuestion:LiveData<Question>
        get()=_currentQuestion

    private val _currentAnswer=MutableLiveData<String>()
    val currentAnswer:LiveData<String>
        get()=_currentAnswer

    private val _incorrectOption=MutableLiveData<Int>()
    val incorrectOption:LiveData<Int>
        get()=_incorrectOption

    private val _correctOption=MutableLiveData<Int>()
    val correctOption:LiveData<Int>
        get()=_correctOption

    private val _gameOver=MutableLiveData<Boolean>()
    val gameOver:LiveData<Boolean>
        get()=_gameOver

    init{
        _score.value=0
        _totalTime.value=0L
        _currentQuestionNo.value=0
        resetQuestions()
        nextQuestion()
        timer=object: CountDownTimer(COUNTDOWN_TIME, 1000)
        {
            override fun onTick(p0: Long) {
                _currentTime.value=p0/ 1000
            }
            override fun onFinish() {
                onIncorrect()
            }
        }.start()
    }

    private fun nextQuestion()
    {
        _currentAnswer.value=questionList[0].choices[0]
        questionList[0].choices.shuffle()
        _currentQuestion.value=questionList.removeAt(0)

        _currentQuestionNo.value=currentQuestionNo.value!!+1
        if(currentQuestionNo.value!!>10)
            _gameOver.value=true
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

    private fun onCorrect()
    {
        _totalTime.value=totalTime.value!!+(COUNTDOWN_TIME/ONE_SECOND-currentTime.value!!)
        _score.value=score.value!!+1
        nextQuestion()
        timer.cancel()
        timer.start()
    }

    private fun onIncorrect(optionSelected: Int=-1) {
        timer.cancel()
        _correctOption.value=_currentQuestion.value!!.choices.indexOf(_currentAnswer.value)
        _incorrectOption.value=optionSelected
        Handler().postDelayed({ _gameOver.value=true }, 3000)
    }

    override fun onCleared() {
        Log.i("GameViewModel","Cleared")
        super.onCleared()
        timer.cancel()
    }
}