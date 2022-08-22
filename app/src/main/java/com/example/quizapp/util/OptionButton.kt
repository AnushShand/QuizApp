package com.example.quizapp.util
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.example.quizapp.R


class OptionButton:AppCompatButton{
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet)
    private val STATE_CORRECT = intArrayOf(R.attr.state_correct)
    private val STATE_INCORRECT = intArrayOf(R.attr.state_incorrect)
    private var IsCorrect = false
    private var IsIncorrect = false

    override fun onCreateDrawableState(extraSpace: Int): IntArray? {
        val drawableState = super.onCreateDrawableState(extraSpace + 2)
        if (IsCorrect) {
            mergeDrawableStates(drawableState, STATE_CORRECT)
        }
        if (IsIncorrect) {
            mergeDrawableStates(drawableState, STATE_INCORRECT)
        }
        return drawableState
    }
    fun setCorrect(Correct: Boolean) {
        IsCorrect = Correct
        refreshDrawableState()
    }
    fun setIncorrect(Incorrect: Boolean) {
        IsIncorrect = Incorrect
        refreshDrawableState()
    }
}