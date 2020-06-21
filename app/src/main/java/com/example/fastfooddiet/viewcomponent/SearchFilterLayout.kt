package com.example.fastfooddiet.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.marginEnd
import com.example.fastfooddiet.R

class SearchFilterLayout : LinearLayout {

    private var setCategoryViewsLater : (() -> Unit)? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?,
                defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    fun setCategoryViews(texts : List<String>, layoutInflater : LayoutInflater) {
        removeAllViews()
        val layoutWidth = measuredWidth
        var totalChildrenWidth = 0

        /*
        This function cannot be called before the layout width has been measured.
        So call it again later
         */

        if (layoutWidth == 0) {
            doOnPreDraw { setCategoryViews(texts, layoutInflater) }
            return
        }

        /*
        Measure cumulative children width and add only the number of children that can fit
        inside the layout. If the number of children exceeds amount that can fit, the last
        child will be truncated to [...]
         */
        for (text in texts) {

            //Create child as text view
            val textView = (layoutInflater.inflate(R.layout.item_category_filter,
                this, false) as TextView).apply {
                setText(text)
                measure(0,0)
            }

            val childWidth = textView.measuredWidth + textView.marginEnd

            //Check if child fits inside layout
            if ( totalChildrenWidth + childWidth > layoutWidth) {
                //If not, check if  it fits when text is [...]
                textView.apply {
                    setText("...")
                    measure(0,0)
                }

                val newWidth = textView.measuredWidth + textView.marginEnd

                //If child still doesn't fit, don't add it and change text
                //of last added child to [...]
                if (totalChildrenWidth + newWidth > layoutWidth) {
                    val lastTextView = (getChildAt(childCount - 1) as TextView)
                    lastTextView.setText("...")
                    break
                }
                //If it fits, add child as [...]
                else {
                    addView(textView)
                    break
                }

            }
            //Add child to layout if it fits
            else {
                addView(textView)
                totalChildrenWidth += childWidth
            }
        }

        setCategoryViewsLater = null
    }

}