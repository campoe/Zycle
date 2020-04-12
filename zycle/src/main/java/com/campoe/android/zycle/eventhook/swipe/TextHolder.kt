package com.campoe.android.zycle.eventhook.swipe

import android.content.Context
import android.text.TextPaint
import androidx.annotation.StringRes

internal class TextHolder(
    internal val text: String,
    internal val paint: TextPaint = TextPaint()
) {

    constructor(
        context: Context,
        @StringRes stringRes: Int,
        textPaint: TextPaint = TextPaint()
    ) : this(context.getString(stringRes), textPaint)

}