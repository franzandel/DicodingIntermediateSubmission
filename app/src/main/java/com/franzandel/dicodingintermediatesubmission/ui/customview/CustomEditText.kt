package com.franzandel.dicodingintermediatesubmission.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.franzandel.dicodingintermediatesubmission.R
import com.franzandel.dicodingintermediatesubmission.data.consts.ValidationConst
import com.franzandel.dicodingintermediatesubmission.utils.extension.hideKeyboard

/**
 * Created by Franz Andel
 * on 26 April 2022.
 */

class CustomEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), OnTouchListener, TextWatcher {

    enum class InputValidation {
        NONE,
        EMAIL,
        PASSWORD,
        NAME
    }

    private var inputValidation: InputValidation = InputValidation.NONE

    init {
        val tryAttribute = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomEditText,
            0, 0
        )

        try {
            inputValidation = InputValidation.values()[tryAttribute.getInt(
                R.styleable.CustomEditText_inputValidation,
                0
            )]
        } finally {
            tryAttribute.recycle()
        }

        init()
    }

    private val clearButtonImage =
        ContextCompat.getDrawable(context, R.drawable.ic_baseline_close_24) as Drawable

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        setOnTouchListener(this)
        addTextChangedListener(this)
        setOnFocusChangeListener { _, isFocus ->
            if (!isFocus) {
                when (inputValidation) {
                    InputValidation.EMAIL -> validateUsername(text.toString())
                    InputValidation.PASSWORD -> validatePassword(text.toString())
                    InputValidation.NAME -> validateName(text.toString())
                    InputValidation.NONE -> Unit
                }
            }
        }

        setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    (context as AppCompatActivity).hideKeyboard()
                    when (inputValidation) {
                        InputValidation.EMAIL -> validateUsername(text.toString())
                        InputValidation.PASSWORD -> validatePassword(text.toString())
                        InputValidation.NAME -> validateName(text.toString())
                        InputValidation.NONE -> Unit
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun showClearButton() {
        setButtonDrawables(endOfTheText = clearButtonImage)
    }

    private fun hideClearButton() {
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

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (clearButtonImage.intrinsicWidth + paddingStart).toFloat()
                if (event.x < clearButtonEnd) {
                    isClearButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - clearButtonImage.intrinsicWidth).toFloat()
                if (event.x > clearButtonStart) {
                    isClearButtonClicked = true
                }
            }
            if (isClearButtonClicked && error == null) {
                return when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        showClearButton()
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (text != null) {
                            text?.clear()
                        }
                        requestFocus()
                        hideClearButton()
                        true
                    }
                    else -> false
                }
            } else return false
        }
        return false
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString().isNotEmpty()) {
            error = null
            hideClearButton()
            showClearButton()
        } else hideClearButton()
    }

    override fun afterTextChanged(s: Editable) = Unit

    private fun validateUsername(username: String) {
        val errorMessage = if (isUsernameEmpty(username)) {
            R.string.empty_username
        } else if (!isUsernameValid(username)) {
            R.string.invalid_username
        } else {
            ValidationConst.FORM_VALID
        }

        if (errorMessage != ValidationConst.FORM_VALID) {
            error = context.getString(errorMessage)
        }
    }

    private fun validatePassword(password: String) {
        val errorMessage = if (!isPasswordValid(password)) {
            R.string.invalid_password
        } else {
            ValidationConst.FORM_VALID
        }

        if (errorMessage != ValidationConst.FORM_VALID) {
            error = context.getString(errorMessage)
        }
    }

    private fun validateName(name: String) {
        val errorMessage = if (isNameEmpty(name)) {
            R.string.empty_name
        } else {
            ValidationConst.FORM_VALID
        }

        if (errorMessage != ValidationConst.FORM_VALID) {
            error = context.getString(errorMessage)
        }
    }

    private fun isUsernameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isUsernameEmpty(username: String): Boolean {
        return username.isBlank()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isNameEmpty(name: String): Boolean {
        return name.isBlank()
    }
}

