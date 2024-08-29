package com.sandeveloper.jsscolab.di

import android.content.Context
import com.sandeveloper.jsscolab.domain.Api.*
import com.sandeveloper.jsscolab.domain.HelperClasses.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    private var context: Context? = null

    fun AppModule(context: Context?) {
        this.context = context
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("BASE_URL"+"/api")
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideDefaultOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain->
            val originalRequest = chain.request()
            val newUrl = originalRequest.url.newBuilder().addPathSegment("auth").build()
            val newRequest = originalRequest.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)

        }.build()
    }

    private fun createServiceWithPathPrefix(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient,
        serviceClass: Class<*>,
        pathPrefix: String
    ): Any {
        return retrofitBuilder
            .client(okHttpClient.newBuilder().addInterceptor { chain ->
                val originalRequest = chain.request()
                val newUrl = originalRequest.url.newBuilder().addPathSegment(pathPrefix).build()
                val newRequest = originalRequest.newBuilder().url(newUrl).build()
                chain.proceed(newRequest)
            }.build())
            .build()
            .create(serviceClass)
    }

    @Singleton
    @Provides
    fun provideAuthApi(
        retrofitBuilder: Retrofit.Builder
    ): AuthApi {
        return retrofitBuilder
            .client(provideDefaultOkHttpClient())
            .build()
            .create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): ProfileApi {
        return createServiceWithPathPrefix(retrofitBuilder, okHttpClient, ProfileApi::class.java, "profile") as ProfileApi
    }

    @Singleton
    @Provides
    fun provideSwapApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): swapApi {
        return createServiceWithPathPrefix(retrofitBuilder, okHttpClient, swapApi::class.java, "swap") as swapApi
    }

    @Singleton
    @Provides
    fun provideCoshopApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): CoshopApi {
        return createServiceWithPathPrefix(retrofitBuilder, okHttpClient, CoshopApi::class.java, "coshop") as CoshopApi
    }

    @Singleton
    @Provides
    fun provideAppsApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): appsApi {
        return createServiceWithPathPrefix(retrofitBuilder, okHttpClient, appsApi::class.java, "apps") as appsApi
    }

    @Singleton
    @Provides
    fun provideMessageApi(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): messageApi {
        return createServiceWithPathPrefix(retrofitBuilder, okHttpClient, messageApi::class.java, "messages") as messageApi
    }

    @Provides
    @Singleton
    fun provideContext(): Context? {
        return context
    }
}
