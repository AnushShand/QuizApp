package com.example.quizapp.screens.game

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class GameViewModel : ViewModel() {
    companion object{
        private const val ONE_SECOND=1000L
        private const val COUNTDOWN_TIME=35000L
        private const val MAX_QUESTIONS=10
    }

    data class Question(val text:String,val choices:MutableList<String>)

    private lateinit var questionList:MutableList<Question>
    private lateinit var timer:CountDownTimer

    private val _currentQuestionNo=MutableLiveData<Int>()
    val currentQuestionNo:LiveData<Int>
        get()=_currentQuestionNo

    private val _currentTime=MutableLiveData<Long>()
    val currentTime:LiveData<Long>
        get()=_currentTime

    private val _timeLeft=MutableLiveData<Long>()
    val timeLeft:LiveData<Long>
        get()=_timeLeft

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

    private val _gameOver=MutableLiveData<Boolean>()
    val gameOver:LiveData<Boolean>
        get()=_gameOver

    private val _callLifeline=MutableLiveData<Boolean>()
    val callLifeline:LiveData<Boolean>
        get()=_callLifeline

    private val _lifelineUsed=MutableLiveData(mutableMapOf("Skip" to false,"Fifty" to false,"Time" to false))
    val lifelineUsed:LiveData<MutableMap<String,Boolean>>
        get()=_lifelineUsed

    private val _optionStatus=MutableLiveData(mutableListOf("Neutral","Neutral","Neutral","Neutral"))
    val optionStatus:LiveData<MutableList<String>>
        get()=_optionStatus

    init{
        _score.value=0
        _totalTime.value=0L
        _currentQuestionNo.value=0
        _timeLeft.value= COUNTDOWN_TIME
        resetQuestions()
        nextQuestion()
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

    private fun nextQuestion()
    {
        resetOptions()
        _currentAnswer.value=questionList[0].choices[0]
        questionList[0].choices.shuffle()
        _currentQuestion.value=questionList.removeAt(0)

        _currentQuestionNo.value=currentQuestionNo.value!!+1
        if(currentQuestionNo.value!!> MAX_QUESTIONS)
            _gameOver.value=true
    }
    private fun resetOptions()
    {
        val temp=optionStatus.value
        for (i in 0..3)
            temp!![i]="Neutral"
        _optionStatus.value=temp
    }


    fun checkAnswer(optionSelected:Int)
    {
        if(_currentQuestion.value!!.choices[optionSelected]==_currentAnswer.value)
            onCorrect(optionSelected)
        else
            onIncorrect(optionSelected)
    }

    private fun onCorrect(optionSelected: Int=-1)
    {
        _totalTime.value=totalTime.value!!+(timeLeft.value!!/ONE_SECOND-currentTime.value!!)
        Log.i("Game",totalTime.value.toString())
        _score.value=score.value!!+1
        val temp = optionStatus.value
        temp!![optionSelected] = "Correct"
        _optionStatus.value = temp
        timer.cancel()
        Handler(Looper.getMainLooper()).postDelayed({
            nextQuestion()
            restartTimer()}, 1000)
    }

    private fun onIncorrect(optionSelected: Int=-1) {
        val correctIndex = _currentQuestion.value!!.choices.indexOf(_currentAnswer.value)
        val temp = optionStatus.value
        temp!![correctIndex] = "Correct"
        if(optionSelected>=0) {
            temp[optionSelected] = "Incorrect"
        }
        _optionStatus.value = temp
        timer.cancel()
        Handler(Looper.getMainLooper()).postDelayed({ _gameOver.value=true }, 2000)
    }


    fun pauseTimer()
    {
        _timeLeft.value=currentTime.value!!*1000
        timer.cancel()
    }
    private fun restartTimer()
    {
        _timeLeft.value= COUNTDOWN_TIME
        timer.cancel()
        startTimer()
    }
    fun startTimer()
    {
        timer=object: CountDownTimer(timeLeft.value!!, ONE_SECOND)
        {
            override fun onTick(p0: Long) {
                _currentTime.value=p0/ ONE_SECOND
            }
            override fun onFinish() {
                onIncorrect()
            }
        }.start()
    }
    fun doneNavigating(){
        _gameOver.value=false
    }

    //Lifelines - Need to know how to move to another class
    fun timeLifeline()
    {
        val temp=lifelineUsed.value
        temp!!["Time"]=true
        _lifelineUsed.value=temp

        timer.cancel()
        _timeLeft.value=_timeLeft.value!!+60000
        startTimer()
    }

    fun skipLifeline()
    {
        val temp=lifelineUsed.value
        temp!!["Skip"]=true
        _lifelineUsed.value=temp
        _currentQuestionNo.value=currentQuestionNo.value!!-1
        restartTimer()
        nextQuestion()
    }
    fun fiftyLifeline()
    {
        val lifelineTemp=lifelineUsed.value
        lifelineTemp!!["Fifty"]=true
        _lifelineUsed.value=lifelineTemp

        val correctIndex=_currentQuestion.value!!.choices.indexOf(_currentAnswer.value)
        val incorrectIndices=(0..3).toMutableList()
        incorrectIndices.remove(correctIndex)
        incorrectIndices.shuffle()

        val optionTemp=optionStatus.value
        optionTemp!![incorrectIndices[0]]="Invisible"
        optionTemp[incorrectIndices[1]]="Invisible"
        _optionStatus.value=optionTemp

    }
    fun callLifeline()
    {
        _callLifeline.value=true
    }

    override fun onCleared() {
        Log.i("GameViewModel","Cleared")
        super.onCleared()
        timer.cancel()
    }
}
