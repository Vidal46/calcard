package br.com.calcard.android.app.ui.visa

import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.calcard.android.app.BuildConfig
import br.com.calcard.android.app.api.Api
import br.com.calcard.android.app.model.BiometryDTO
import br.com.calcard.android.app.model.DeviceDTO
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.model.MobileMigrationRequestDTO
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class VisaMigrationViewModel : ViewModel() {
    val URL_ = "https://api.calcard.com.br/calcard-mobile-app/3.0.0/"

    var cpfEnable = MutableLiveData<MigrationElegible>()
    var connectionError = MutableLiveData<Boolean>()
    var errorHandler = MutableLiveData<String>()

    var codeOk = MutableLiveData<Boolean>()

    fun getCpfEdible(accessToken: String, cpf: String) {
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

        val cpfStr = removePunctuation(cpf)
        val retrofit = Retrofit.Builder()
                .baseUrl(URL_)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.cpfElegible(accessToken, cpfStr)
        call.enqueue(object : Callback<MigrationElegible> {
            override fun onResponse(call: Call<MigrationElegible>, response: Response<MigrationElegible>) {
                if (response.code() == 202) {
                    cpfEnable.value = response.body()
                } else {
                    val error = getErrorMessage(response.errorBody()!!)
                    if (error.contains("dois")) {
                        errorHandler.value = "1000"
                    } else if (error.contains("tentativas de biometria")) {
                        errorHandler.value = "10000"
                    } else if (error.contains("envios de token")) {
                        errorHandler.value = "100000"
                    } else if (error.contains("expirado")) {
                        errorHandler.value = "100001"
                    } else if (error.contains("favor tirar nova foto")) {
                        errorHandler.value = "10001"
                    } else if (error.contains("telefone celular")) {
                        errorHandler.value = "10002"
                    } else {
                        errorHandler.value = response.code().toString()
                    }
                }
            }

            override fun onFailure(call: Call<MigrationElegible>, t: Throwable) {
                connectionError.value = true
            }

        })
    }


    fun sendPicture(accessToken: String, imageStr: String, cpf: String, deviceId: String) {
        val cpfStr = removePunctuation(cpf)
        val imageTurn = "data:image/jpeg;base64,${imageStr}"
        val biometryDTO = BiometryDTO(object : DeviceDTO(BuildConfig.VERSION_NAME, deviceId,
                Build.MANUFACTURER, Build.MODEL, "Android", Build.VERSION.RELEASE) {}, imageTurn)
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(URL_)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.checkPicture(accessToken, cpfStr, biometryDTO)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {
                    codeOk.value = true
                } else {
                    val est = JsonParser().parse(response.errorBody()?.string()) as JsonArray
                    errorHandler.value = removeInvertedCommas(est.get(0).asJsonObject.get("message").toString())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                connectionError.value = true
            }

        })
    }

    private fun getErrorMessage(errorBody: ResponseBody): String {
        var errorMessage: String
        try {
            errorMessage = errorBody.string()
        } catch (e: IOException) {
            errorMessage = e.message.toString()
        }

        if (errorMessage.contains("90")) {
            errorMessage = "Cliente distante da câmera, tire nova foto. Distância entre os olhos menor do que 90."
        } else if (errorMessage.contains(" dois minutos")) {
            errorMessage = " Favor aguardar dois minutos antes de pedir um novo código."
        } else if (errorMessage.contains("Face não encontrada")) {
            errorMessage = "Face não encontrada. Tirar nova foto."
        } else if (errorMessage.contains("cortada")) {
            errorMessage = "A imagem não está boa para cadastro, possivelmente a foto está cortada. Tire uma novo foto."
        } else if (errorMessage.contains("expirado")) {
            errorMessage = "Token inválido ou já expirado."
        } else if (errorMessage.contains("tentativas")) {
            errorMessage = "Você já superou o máximo de tentativas de biometria, favor dirigir-se à loja mais próxima para efetuar a migração."
        } else if (errorMessage.contains("Já existe uma migração")) {
            errorMessage = "Já existe uma migração sendo efetuada, aguarde o processo terminar ou entre em contato com o SAC Calcard."
        }
        return errorMessage
    }


    private fun removePunctuation(value: String?): String {
        return value?.replace("\\D".toRegex(), "") ?: ""
    }

    private fun removeN(value: String?): String {
        return value?.replace("\n", "") ?: ""
    }

    private fun removeInvertedCommas(value: String?): String {
        return value?.replace("\"", "") ?: ""
    }


    fun checkCodePassword(accessToken: String, pw: String, sms: String, cpf: String, deviceId: String) {
        var cpfStr = removePunctuation(cpf)
        var pwStr = removeN(pw)
        var dao = MobileMigrationRequestDTO(object : DeviceDTO(BuildConfig.VERSION_NAME, deviceId,
                Build.MANUFACTURER, Build.MODEL, "Android", Build.VERSION.RELEASE) {}, pwStr, sms)
        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(URL_)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.checkPassword(accessToken, cpfStr, dao)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    codeOk.value = true
                } else {
                    val est = JsonParser().parse(response.errorBody()?.string()) as JsonArray
                    errorHandler.value = removeInvertedCommas(est.get(0).asJsonObject.get("message").toString())
                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                connectionError.value = true
            }

        })
    }


}