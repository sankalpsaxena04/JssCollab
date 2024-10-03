package com.sandeveloper.jsscolab.di

import com.sandeveloper.jsscolab.domain.Interfaces.AuthRepository
import com.sandeveloper.jsscolab.domain.Interfaces.PostRepository
import com.sandeveloper.jsscolab.domain.Interfaces.ProfileRepository
import com.sandeveloper.jsscolab.domain.Interfaces.SwapRepository
import com.sandeveloper.jsscolab.domain.Repositories.ChatRepository
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitAuthRepository
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitPostRepository
import com.sandeveloper.jsscolab.domain.Repositories.RetrofitSwapRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindsPostRepository(retrofitPostRepository: RetrofitPostRepository): PostRepository

    @Singleton
    @Binds
    abstract fun bindsProfileRepository(retrofitAuthRepository: RetrofitAuthRepository): ProfileRepository

    @Singleton
    @Binds
    abstract fun bindsAuthRepository(retrofitAuthRepository: RetrofitAuthRepository): AuthRepository


    @Singleton
    @Binds
    abstract fun bindsSwapRepository(swapRepository: RetrofitSwapRepository): SwapRepository


}