package com.sandeveloper.jsscolab.domain.HelperClasses

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.sandeveloper.jsscolab.R
import de.hdodenhof.circleimageview.CircleImageView

// Function to convert Base64 string to Bitmap
fun base64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        null
    }
}

// Example function to set the base64 image to CircleImageView
fun setImageToCircleImageView(base64Image: String, circleImageView: CircleImageView) {
    // Convert base64 string to Bitmap
    val bitmap = base64ToBitmap(base64Image)

    // If the conversion was successful, set the image
    if (bitmap != null) {
        circleImageView.setImageBitmap(bitmap)
    } else {
        // Handle error case (e.g. invalid base64 string)
        // Optionally, set a placeholder or error image
        circleImageView.setImageResource(R.drawable.profile_pic_placeholder )
    }
}
