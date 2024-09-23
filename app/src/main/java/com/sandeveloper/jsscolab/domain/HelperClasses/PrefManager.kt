package com.sandeveloper.jsscolab.domain.HelperClasses

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.sandeveloper.jsscolab.domain.Constants.Endpoints
import com.sandeveloper.jsscolab.domain.HelperClasses.PrefManager.sharedPreferences
import com.sandeveloper.jsscolab.domain.Modules.Post.Filter
import com.sandeveloper.jsscolab.domain.Modules.Post.Photo
import com.sandeveloper.jsscolab.domain.Modules.Post.Posts
import com.sandeveloper.jsscolab.domain.Modules.Post.Sender
import com.sandeveloper.jsscolab.domain.Modules.Profile.details
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
    fun saveToken(token: String?) {
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
    fun setOtpOrderId(orderId:String){
        editor.putString("orderId",orderId)
        editor.apply()
    }
    fun getOtpOrderId():String?{
        return sharedPreferences.getString("orderId",null)
    }
    fun setPhoneNumber(phone:String){
        editor.putString("phone",phone)
        editor.apply()
    }
    fun getPhoneNumber():String?{
        return sharedPreferences.getString("phone",null)
    }
    fun setTempAuthToken(tempToken:String){
        editor.putString("authtempToken",tempToken)
        editor.apply()
    }
    fun getTempAuthToken():String?{
        return sharedPreferences.getString("authtempToken",null)
    }


    fun setAppList(listOfMap:List<Map<String,List<String>>>){
        val gson = Gson()
        val dataListJson = gson.toJson(listOfMap)
        editor.putString("appsList",dataListJson)
        editor.apply()
    }
    fun getAppList():List<Map<String,List<String>>>{
        val dataListJson = sharedPreferences.getString("appsList", null)
        val gson = Gson()
        val type = object : TypeToken<List<Map<String, List<String>>>>() {}.type
        return if (dataListJson != null) {
            gson.fromJson(dataListJson, type)
        } else {
            emptyList()
        }
    }

    fun getcurrentUserdetails():details{

        val full_name = sharedPreferences.getString(Endpoints.User.FULLNAME, "")
        val email = sharedPreferences.getString(Endpoints.User.EMAIL, "")
        val phone = sharedPreferences.getString(Endpoints.User.PHONE, "")
        val dp_url = sharedPreferences.getString(Endpoints.User.DP_URL, "")
        val admn_no = sharedPreferences.getString(Endpoints.User.ADMN_NUMBER,"")
        val address = sharedPreferences.getString(Endpoints.User.ADDRESS,"")
        return details(email =  email, full_name = full_name, phone = phone, photo = Photo(secure_url = dp_url?:"", public_id = ""), admission_number = admn_no, address = address)
    }

    fun setcurrentUserdetails(user:details){
        editor.putString(Endpoints.User.FULLNAME,user.full_name)
        editor.putString(Endpoints.User.PHONE,user.phone.toString())
        editor.putString(Endpoints.User.EMAIL,user.email)
        editor.putString(Endpoints.User.ADDRESS,user.address)
        editor.putString(Endpoints.User.DP_URL,user.photo?.secure_url)
        editor.putString(Endpoints.User.ADMN_NUMBER,user.admission_number)

        editor.apply()
    }
    fun getUserFCMToken(): String? {
        return sharedPreferences.getString(Endpoints.User.FCM_TOKEN,null)
    }

    fun setUserFCMToken(token : String){
        editor.putString(Endpoints.User.FCM_TOKEN,token)
        editor.apply()
    }

    fun setBroadCategory(category:String){
        editor.putString("broadCategory",category)
        editor.apply()
    }
    fun getBroadCategory():String?{
        return sharedPreferences.getString("broadCategory",null)
    }

    fun setOnClickedPost(post:Posts){
        editor.putString(Endpoints.postCreate.POST_ID,post._id)
        editor.putString(Endpoints.postCreate.POST_DESCRIPTION,post.comment)
        editor.putString(Endpoints.postCreate.POST_CATEGORY,post.category)
        editor.putInt(Endpoints.postCreate.CREATORS_CONTRIBUTION,post.sender_contribution)
        editor.putInt(Endpoints.postCreate.TOTAL_AMOUNT,post.total_required_amount)
        editor.putString(Endpoints.postCreate.EXPIRATION_DATE,post.expiration_date)
        if(post.apps!=null){
            editor.putStringSet(Endpoints.postCreate.POST_APPS,post.apps.toSet())
        }

        if(post.filter!=null){
            editor.putBoolean(Endpoints.postCreate.filter.MYYEAR,post.filter.my_year)
            editor.putStringSet(Endpoints.postCreate.filter.ADDRESS,post.filter.address.toSet())
            editor.putStringSet(Endpoints.postCreate.filter.BRANCH,post.filter.branch.toSet())
        }
        editor.putString(Endpoints.postCreate.Sender.ID,post.sender._id)
        editor.putString(Endpoints.postCreate.Sender.NAME,post.sender.full_name)
        editor.putInt(Endpoints.postCreate.Sender.PHOTO,post.sender.rating)
        editor.putString(Endpoints.postCreate.Sender.ADDRESS,post.sender.address)
        editor.putInt(Endpoints.postCreate.Sender.RATED_COUNT,post.sender.rated_count)
        editor.putString(Endpoints.postCreate.Sender.PHOTO,post.sender.photo?.secure_url?:"")
        editor.putString(Endpoints.postCreate.Sender.ADMISSION_NUMBER,post.sender.admission_number)
        editor.apply()
    }
    fun getOnClickedPost(): Posts {
        val postId = sharedPreferences.getString(Endpoints.postCreate.POST_ID, "") ?: ""
        val postDescription = sharedPreferences.getString(Endpoints.postCreate.POST_DESCRIPTION, "") ?: ""
        val postCategory = sharedPreferences.getString(Endpoints.postCreate.POST_CATEGORY, "") ?: ""
        val creatorsContribution = sharedPreferences.getInt(Endpoints.postCreate.CREATORS_CONTRIBUTION, 0)
        val totalAmount = sharedPreferences.getInt(Endpoints.postCreate.TOTAL_AMOUNT, 0)
        val expirationDate = sharedPreferences.getString(Endpoints.postCreate.EXPIRATION_DATE, "") ?: ""
        val postApps = sharedPreferences.getStringSet(Endpoints.postCreate.POST_APPS, emptySet())?.toList() ?: emptyList()

        val filter = if (sharedPreferences.contains(Endpoints.postCreate.filter.MYYEAR)) {
            Filter(
                my_year = sharedPreferences.getBoolean(Endpoints.postCreate.filter.MYYEAR, false),
                address = sharedPreferences.getStringSet(Endpoints.postCreate.filter.ADDRESS, emptySet())?.toList() ?: listOf(),
                branch = sharedPreferences.getStringSet(Endpoints.postCreate.filter.BRANCH, emptySet())?.toList() ?: listOf()
            )
        } else {
            null
        }

        val senderId = sharedPreferences.getString(Endpoints.postCreate.Sender.ID, "") ?: ""
        val senderName = sharedPreferences.getString(Endpoints.postCreate.Sender.NAME, "") ?: ""
        val senderRating = sharedPreferences.getInt(Endpoints.postCreate.Sender.RATING, 0)
        val senderAddress = sharedPreferences.getString(Endpoints.postCreate.Sender.ADDRESS, "") ?: ""
        val senderRatedCount = sharedPreferences.getInt(Endpoints.postCreate.Sender.RATED_COUNT, 0)
        val senderPhoto = sharedPreferences.getString(Endpoints.postCreate.Sender.PHOTO, "") ?: ""
        val senderAdmissionNumber = sharedPreferences.getString(Endpoints.postCreate.Sender.ADMISSION_NUMBER, "") ?: ""

        val sender = Sender(
            _id = senderId,
            full_name = senderName,
            rating = senderRating,
            address = senderAddress,
            rated_count = senderRatedCount,
            photo = Photo(secure_url = senderPhoto, public_id = ""),
            admission_number = senderAdmissionNumber
        )

        return Posts(
            _id = postId,
            comment = postDescription,
            category = postCategory,
            sender_contribution = creatorsContribution,
            total_required_amount = totalAmount,
            expiration_date = expirationDate,
            apps = postApps,
            filter = filter,
            sender = sender
        )
    }


}