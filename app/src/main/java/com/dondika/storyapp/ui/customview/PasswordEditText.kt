package com.dondika.storyapp.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.dondika.storyapp.R
import com.google.android.material.textfield.TextInputEditText

class PasswordEditText : TextInputEditText, View.OnTouchListener {

    private lateinit var eyeIcon: Drawable

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttrs: Int) : super(context, attrs, defStyleAttrs) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        showEyeButton()
        setBackgroundResource(R.drawable.border_background)
        textSize = 15f
        textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    }

    private fun init(){
        eyeIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye) as Drawable
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().length < 6) showError()
            }
        })
    }

    private fun showError() {
        error = context.getString(R.string.error_password_not_valid)
    }

    private fun showEyeButton() {
        setButtonDrawables(endOfTheText = eyeIcon)
    }

    private fun hideEyeButton() {
        setButtonDrawables()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {

        if (compoundDrawables[2] != null) {
            val eyeButtonStart: Float
            val eyeButtonEnd: Float
            var isEyeButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                eyeButtonEnd = (eyeIcon.intrinsicWidth + paddingStart).toFloat()
                if (event.x < eyeButtonEnd) isEyeButtonClicked = true
            } else {
                eyeButtonStart = (width - paddingEnd - eyeIcon.intrinsicWidth).toFloat()
                if (event.x > eyeButtonStart) isEyeButtonClicked = true
            }

            if (isEyeButtonClicked) {
                return when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        hideEyeButton()
                        if (transformationMethod.equals(HideReturnsTransformationMethod.getInstance())) {
                            transformationMethod = PasswordTransformationMethod.getInstance() // hide password
                            eyeIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye) as Drawable
                            showEyeButton()
                        } else {
                            transformationMethod = HideReturnsTransformationMethod.getInstance() // show password
                            eyeIcon = ContextCompat.getDrawable(context, R.drawable.ic_eye_off) as Drawable
                            showEyeButton()
                        }
                        true
                    }
                    else -> false
                }
            } else return false
        }
        return false

    }


}