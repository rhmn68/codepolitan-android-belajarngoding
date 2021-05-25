package com.codepolitan.belajarngoding.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoSwipeViewPager: ViewPager {
    private var isEnable = false

    constructor(context: Context) : super(context)
    constructor(
            context: Context,
            attrs: AttributeSet?
    ) : super(context, attrs) {
        isEnable = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (isEnable) {
            super.onTouchEvent(ev)
        } else false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (isEnable) {
            super.onInterceptTouchEvent(ev)
        } else false
    }

    fun setPagingEnabled(isEnable: Boolean) {
        this.isEnable = isEnable
    }
}