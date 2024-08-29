package com.sandeveloper.jsscolab.domain.Utility

import android.app.Activity
import com.sandeveloper.jsscolab.R

object AppUtils {

    fun slideRight(activity: Activity) {
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
    }

    fun slideLeft(activity: Activity) {
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
    }

    fun fadeIn(activity: Activity) {
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}