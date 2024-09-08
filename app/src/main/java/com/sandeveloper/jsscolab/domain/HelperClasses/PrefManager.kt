package com.sandeveloper.jsscolab.domain.HelperClasses

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager.sharedPreferences
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

    fun getAppMode(): String? {
        return sharedPreferences.getString("appMode", Endpoints.ONLINE_MODE)
    }

    fun setAppMode(mode:String) {
        if (mode==Endpoints.OFFLINE_MODE){
            setShakePref(false)
        }
        editor.putString("appMode", mode)
        editor.apply()
    }

    fun hasOfflineDialogBeenShown(): Boolean {
        return sharedPreferences.getBoolean("offlineDialogShown", false)
    }

    fun setOfflineDialogShown(isshown:Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("offlineDialogShown", isshown)
        editor.apply()
    }

    fun setShakePref(bool:Boolean) {
        editor.putBoolean("shake", bool)
        editor.apply()
    }

    fun getShakeSensitivity(): Int {
        return sharedPreferences.getInt("shakesensi", 2)
    }

    fun setShakeSensitivity(sensi:Int) {
        editor.putInt("shakesensi", sensi)
        editor.apply()
    }

    fun setSelectedCategory(category:String){
        editor.putString("postCategorySelected",category)
        editor.apply()
    }
    fun getSelectedCategory():String?{
        return sharedPreferences.getString("postCategorySelected",null)
    }
}