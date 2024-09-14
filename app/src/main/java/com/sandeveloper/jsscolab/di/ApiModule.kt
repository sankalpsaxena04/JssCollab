package com.sandeveloper.jsscolab.di

import android.content.Context
import com.sandeveloper.jsscolab.domain.Api.*
import com.sandeveloper.jsscolab.domain.HelperClasses.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://BASE_URL" + "/api/")
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    @Named("authClient")
    fun provideAuthOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("defaultClient")
    fun provideDefaultOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest.url.newBuilder().addPathSegment("auth").build()
            val newRequest = originalRequest.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }.build()
    }

    private fun <T> createServiceWithPathPrefix(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient,
        serviceClass: Class<T>,
        pathPrefix: String
    ): T {
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
        retrofitBuilder: Retrofit.Builder,
        @Named("defaultClient") okHttpClient: OkHttpClient // Use named qualifier here
    ): AuthApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProfileApi(
        retrofitBuilder: Retrofit.Builder,
        @Named("authClient") okHttpClient: OkHttpClient // Use named qualifier here
    ): ProfileApi {
        return createServiceWithPathPrefix(retrofitBuilder, okHttpClient, ProfileApi::class.java, "profile")
    }

    @Singleton
    @Provides
    fun provideSwapApi(
        retrofitBuilder: Retrofit.Builder,
        @Named("authClient") okHttpClient: OkHttpClient // Use named qualifier here
    ): SwapApi {
        return createServiceWithPathPrefix(retrofitBuilder, okHttpClient, SwapApi::class.java, "swap")
    }

    @Singleton
    @Provides
    fun provideCoshopApi(
        retrofitBuilder: Retrofit.Builder,
        @Named("authClient") okHttpClient: OkHttpClient // Use named qualifier here
    ): CoshopApi {
        return createServiceWithPathPrefix(retrofitBuilder, okHttpClient, CoshopApi::class.java, "coshop")
    }

    @Singleton
    @Provides
    fun provideAppsApi(
        retrofitBuilder: Retrofit.Builder,
        @Named("authClient") okHttpClient: OkHttpClient // Use named qualifier here
    ): appsApi {
        return createServiceWithPathPrefix(retrofitBuilder, okHttpClient, appsApi::class.java, "apps")
    }

    @Singleton
    @Provides
    fun provideMessageApi(
        retrofitBuilder: Retrofit.Builder,
        @Named("authClient") okHttpClient: OkHttpClient // Use named qualifier here
    ): MessageApi {
        return createServiceWithPathPrefix(retrofitBuilder, okHttpClient, MessageApi::class.java, "messages")
    }
}
