package com.sandeveloper.jsscolab.domain.HelperClasses

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import java.util.concurrent.ConcurrentHashMap

object PrefManager {
    val selectedPosition = MutableLiveData<Int>()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    val list= MutableLiveData<List<String>>()
    const val JWT_TOKEN = "JWT_TOKEN"

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(Endpoints.SharedPref.SHAREDPREFERENCES, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }
    fun saveToken(token: String) {
        editor.putString(JWT_TOKEN, token)
        editor.apply()
    }
    fun getToken(): String? {
        return sharedPreferences.getString(JWT_TOKEN, null)

    }


}