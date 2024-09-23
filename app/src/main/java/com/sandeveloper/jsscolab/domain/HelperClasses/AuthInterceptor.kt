package com.sandeveloper.jsscolab.domain.HelperClasses

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    //logic defined here
    override fun intercept(chain: Interceptor.Chain): Response {
        //getting request and adding header to avery request that is coming
        val request = chain.request().newBuilder()
        val token = PrefManager.getToken()
        request.addHeader("Authorization",token!!)
        return chain.proceed(request.build())
    }
}