package com.example.duestake.di

import android.content.Context
import androidx.room.Room
import com.example.duestake.api.AuthApi
import com.example.duestake.api.EmptraApi
import com.example.duestake.api.HomeApi
import com.example.duestake.api.OnboardingApi
import com.example.duestake.dao.MyDatabase
import com.example.duestake.dao.UserDao
import com.example.duestake.data.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    @Named("Emptra")
    fun provideEmptraClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("clientId" , Constant.CLIENT_ID)
                .addHeader("secretKey" , Constant.SECRET_KEY)
            chain.proceed(request.build())
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    @Named("Emptra")
    fun provideEmptraRetrofit(@Named("Emptra") client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constant.EMPTRA_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideOnboardingApi(retrofit: Retrofit): OnboardingApi =
        retrofit.create(OnboardingApi::class.java)

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi =
        retrofit.create(HomeApi::class.java)

    @Provides
    @Singleton
    fun provideEmptraApi(@Named("Emptra") retrofit: Retrofit): EmptraApi =
        retrofit.create(EmptraApi::class.java)

    @Provides
    fun provideMyDatabase(@ApplicationContext context: Context): MyDatabase {
        return Room.databaseBuilder(
            context,
            MyDatabase::class.java,
            "my_database"
        ).build()
    }

    @Provides
    fun provideMyDao(myDatabase: MyDatabase): UserDao {
        return myDatabase.userDao()
    }
}