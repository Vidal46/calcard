package br.com.calcard.android.app.ui.redirection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.calcard.android.app.api.Api
import br.com.calcard.android.app.model.DataSugestionCpf
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RedirectionViewModel : ViewModel() {

    var sugestionCpf = MutableLiveData<DataSugestionCpf>()
    var handlerError = MutableLiveData<Boolean>()
    var serverError = MutableLiveData<Boolean>()

    val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    fun checkSugestion(accessToken: String, cpf: String) {
        val cpfStr = removePunctuation(cpf)
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.sugestionCheck(accessToken, cpfStr)
        call.enqueue(object : Callback<DataSugestionCpf> {
            override fun onResponse(call: Call<DataSugestionCpf>, response: Response<DataSugestionCpf>) {
                if (response.isSuccessful) {
                    sugestionCpf.value = response.body()
                } else {
                    handlerError.value = true
                }
            }

            override fun onFailure(call: Call<DataSugestionCpf>, t: Throwable) {
                serverError.value = true
            }
        })
    }


    private fun removePunctuation(value: String?): String {
        return value?.replace("\\D".toRegex(), "") ?: ""
    }

    private fun removeN(value: String?): String {
        return value?.replace("\n", "") ?: ""
    }
}