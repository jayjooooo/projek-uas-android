package com.example.sportapp

import android.content.Context
import android.util.AttributeSet
import android.animation.ValueAnimator
import androidx.core.animation.addListener

class TypingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    private var onTypingAnimationEnd: (() -> Unit)? = null

    fun setTypingAnimation(text: CharSequence, duration: Long, onAnimationEnd: (() -> Unit)? = null) {
        this.onTypingAnimationEnd = onAnimationEnd

        val animator = ValueAnimator.ofInt(0, text.length)
        animator.duration = duration
        animator.addUpdateListener { animation ->
            val index = animation.animatedValue as Int
            this.text = text.subSequence(0, index)
        }
        animator.addListener(onEnd = {
            onTypingAnimationEnd?.invoke()
        })
        animator.start()
    }
}
