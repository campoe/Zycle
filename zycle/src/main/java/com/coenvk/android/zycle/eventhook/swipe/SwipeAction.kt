package com.coenvk.android.zycle.eventhook.swipe

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.coenvk.android.zycle.ZycleDsl

class SwipeAction internal constructor(
    internal val listener: OnSwipeListener? = null,
    internal val drawable: Drawable? = null,
    internal val textHolder: TextHolder? = null,
    @ColorInt internal val background: Int = Color.DKGRAY
) {

    @ZycleDsl
    class Builder : com.coenvk.android.zycle.util.Builder<SwipeAction> {

        private var listener: OnSwipeListener? = null
        private var drawable: Drawable? = null
        private var textHolder: TextHolder? = null

        @ColorInt
        private var background: Int = Color.DKGRAY

        fun onSwiped(f: (position: Int) -> Unit) =
            apply {
                listener = object : OnSwipeListener {
                    override fun onSwiped(position: Int) {
                        f(position)
                    }
                }
            }

        fun drawable(context: Context, @DrawableRes drawableRes: Int) =
            apply { this.drawable = ContextCompat.getDrawable(context, drawableRes) }

        fun drawable(drawable: Drawable) =
            apply { this.drawable = drawable }

        fun background(@ColorInt background: Int) =
            apply { this.background = background }

        fun background(context: Context, @ColorRes backgroundRes: Int) =
            apply { this.background = ContextCompat.getColor(context, backgroundRes) }

        fun text(
            context: Context,
            @StringRes stringRes: Int,
            @ColorRes color: Int = android.R.color.white,
            spSize: Float = 16f,
            typeface: Typeface = Typeface.DEFAULT
        ) =
            apply {
                val paint = TextPaint().apply {
                    isAntiAlias = true
                    this.color = ContextCompat.getColor(context, color)
                    this.typeface = typeface
                    textSize = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,
                        spSize,
                        context.resources.displayMetrics
                    )
                }
                this.textHolder = TextHolder(context, stringRes, paint)
            }

        fun text(
            context: Context,
            text: String,
            @ColorRes color: Int = android.R.color.white,
            spSize: Float = 16f,
            typeface: Typeface = Typeface.DEFAULT
        ) =
            apply {
                val paint = TextPaint().apply {
                    isAntiAlias = true
                    this.color = ContextCompat.getColor(context, color)
                    this.typeface = typeface
                    textSize = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,
                        spSize,
                        context.resources.displayMetrics
                    )
                }
                this.textHolder = TextHolder(text, paint)
            }

        override fun build(): SwipeAction {
            return SwipeAction(listener, drawable, textHolder, background)
        }

    }

}