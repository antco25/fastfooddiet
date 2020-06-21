package com.example.fastfooddiet.viewcomponent

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.fastfooddiet.R
import android.view.inputmethod.InputMethodManager


class CustomSearchView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    constructor(
        context: Context?, attrs: AttributeSet?,
        defStyleAttr: Int, defStyleRes: Int
    )
            : super(context, attrs, defStyleAttr, defStyleRes)


    private val editText: EditText
    private val closeIcon: ImageView

    init {
        View.inflate(context, R.layout.view_custom_search, this).apply {
            editText = findViewById(R.id.custom_searchView_text)
            closeIcon = findViewById(R.id.custom_searchView_close)

            setupEditText(editText, closeIcon)
            setupCloseIcon(closeIcon, editText)
        }

        this.apply {
            setBackgroundResource(R.drawable.dr_rounded_border)
            isFocusableInTouchMode = true
            isFocusable = true
        }
    }

    private fun setupEditText(editText: EditText, closeIcon: ImageView) {

        /*
            Handle when text is changed
         */
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                queryTextListener?.apply {
                    onQueryTextChange(text)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        /*
            Handle 'Enter' press
        */
        editText.setOnEditorActionListener { textView, actionId, keyEvent ->

            //Handle 'enter' on soft and hard keyboards
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                keyEvent?.action == KeyEvent.ACTION_DOWN) {

                queryTextListener?.apply {
                    val text = textView.text.toString()
                    onQueryTextSubmit(text)
                }
            }

            true
        }

        /*
            Show close icon only when focused
        */
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showKeyBoard(true)
                closeIcon.visibility = View.VISIBLE
            } else if (editText.text.isEmpty()) {
                closeIcon.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupCloseIcon(closeIcon: ImageView, editText: EditText) {

        //Set listener
        closeIcon.setOnClickListener {
            if (editText.text.isEmpty())
                clearSearchViewFocus()
            else {
                editText.text.clear()
                if (!editText.hasFocus())
                    closeIcon.visibility = View.INVISIBLE
            }

        }
    }

    private var queryTextListener : OnQueryTextListener? = null

    fun setOnQueryTextListener(listener: OnQueryTextListener) {
        queryTextListener = listener
    }

    interface OnQueryTextListener {
        fun onQueryTextSubmit(query: String): Boolean
        fun onQueryTextChange(newText: String): Boolean
    }

    fun setQuery(query : String) {
        editText.setText(query)

        //Set initial visibility status
        if (query.isEmpty()) {
            closeIcon.visibility = View.INVISIBLE
        }
        else {
            closeIcon.visibility = View.VISIBLE
        }
    }

    fun clearSearchViewFocus() {
        editText.clearFocus()
        showKeyBoard(false)
    }

    fun requestSearchViewFocus() {
        editText.requestFocus()
        showKeyBoard(true)
    }

    fun isSearchViewFocused() : Boolean {
        return editText.isFocused
    }

    private fun showKeyBoard(showKeyBoard : Boolean) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager

        if (showKeyBoard)
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        else
            imm.hideSoftInputFromWindow(windowToken, 0)
    }

}