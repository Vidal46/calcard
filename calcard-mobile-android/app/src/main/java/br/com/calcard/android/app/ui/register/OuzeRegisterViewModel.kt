package br.com.calcard.android.app.ui.register

import androidx.lifecycle.ViewModel
import br.com.calcard.android.app.api.Api
import br.com.calcard.android.app.model.RegisterRequest
import br.com.calcard.android.app.utils.Constants
import com.google.firebase.iid.FirebaseInstanceId
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class OuzeRegisterViewModel() : ViewModel() {

    val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    private fun getRegisterRequest(deviceId: String): RegisterRequest {
        val register = RegisterRequest()
//        register.cpf = cpf.get()!!.replace(".", "").replace("-", "")
//        register.token = token.get()
//        register.password = password.get()
        register.deviceId = deviceId
        register.firebaseToken = FirebaseInstanceId.getInstance().token
        return register
    }

    fun register(deviceId: String) {
        val registerRequest = getRegisterRequest(deviceId)

        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.register(Constants.BEARER_API, registerRequest)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })

    }

}