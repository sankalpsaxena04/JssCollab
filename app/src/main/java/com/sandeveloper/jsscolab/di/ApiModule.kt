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
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("BASE_URL")
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    @Named("authClient")
    fun provideAuthOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    @Named("defaultClient")
    fun provideDefaultOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideAuthApi(
        retrofitBuilder: Retrofit.Builder,
        @Named("defaultClient") okHttpClient: OkHttpClient
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
        @Named("authClient") okHttpClient: OkHttpClient
    ): ProfileApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(ProfileApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSwapApi(
        retrofitBuilder: Retrofit.Builder,
        @Named("authClient") okHttpClient: OkHttpClient
    ): SwapApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(SwapApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCoshopApi(
        retrofitBuilder: Retrofit.Builder,
        @Named("authClient") okHttpClient: OkHttpClient
    ): CoshopApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(CoshopApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAppsApi(
        retrofitBuilder: Retrofit.Builder,
        @Named("authClient") okHttpClient: OkHttpClient
    ): appsApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(appsApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMessageApi(
        retrofitBuilder: Retrofit.Builder,
        @Named("authClient") okHttpClient: OkHttpClient
    ): MessageApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(MessageApi::class.java)
    }

    @Singleton
    @Provides
    fun providesNotificationApi(
        retrofitBuilder: Builder,
        @Named("authClient") okHttpClient: OkHttpClient
    ):NotificationApiService{
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(NotificationApiService::class.java)
    }
}
