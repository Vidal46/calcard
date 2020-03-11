package br.com.calcard.android.app.di

import android.content.Context
import br.com.calcard.android.app.api.Api
import br.com.calcard.android.app.api.AppService
import br.com.calcard.android.app.utils.typedef.IntegerConstants
import com.google.gson.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        gsonBuilder.registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext? -> Date(json.asJsonPrimitive.asLong) } as JsonDeserializer<Date>)
        return gsonBuilder.create()
    }

    @Provides
    fun provideOkhttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
                .readTimeout(IntegerConstants.SECONDS_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .connectTimeout(IntegerConstants.SECONDS_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(IntegerConstants.SECONDS_TIMEOUT.toLong(), TimeUnit.SECONDS)
        return client.build()
    }

    @Provides
    @Singleton
    fun provideApi(gson: Gson, okHttpClient: OkHttpClient): Api {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(Api::class.java)
    }

    @Singleton
    @Provides
    fun providesAppService(api: Api, context: Context): AppService {
        return AppService(api, context)
    }

}