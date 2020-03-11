package br.com.calcard.android.app.ui.insurance

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.calcard.android.app.BuildConfig
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.api.Api
import br.com.calcard.android.app.model.*
import br.com.calcard.android.app.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.util.*
import java.util.concurrent.TimeUnit

class InsuranceViewModel : ViewModel() {

    var contactableInsurancesLiveData = MutableLiveData<MutableList<Contractable>>()

    var mutableInsurance = MutableLiveData<Insurance>()

    var contractedInsurancesLiveData = MutableLiveData<MutableList<Contracted>>()

    var insuranceDetailLiveData = MutableLiveData<InsuranceDetail>()

    var insuranceLiveData = MutableLiveData<Insurance>()

    var cancelInsuranceDetailLiveData = MutableLiveData<SaleInsurance>()

    var cancelInsuranceFlagLiveData = MutableLiveData<Boolean>()

    var wellContractedInsuranceLiveData = MutableLiveData<Boolean>()

    var insuranceContractPdfLiveData = MutableLiveData<Boolean>()

    var fullInsuranceLiveData = MutableLiveData<FullInsurance>()

    var connectionError = MutableLiveData<Boolean>()

    var errorHandler = MutableLiveData<String>()

    lateinit var file: File


    val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()

    fun getInsuranceDetail(accessToken: String, insuranceId: Long) {
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.getInsuranceDetails(accessToken, MyApplication.preferences.getString("tokenUser", null), insuranceId)
        call.enqueue(object : Callback<InsuranceDetail> {
            override fun onResponse(call: Call<InsuranceDetail>, response: Response<InsuranceDetail>) {
                if (response.isSuccessful) {
                    insuranceDetailLiveData.value = response.body()
                } else {
                    errorHandler.value = response.message()
                }
            }

            override fun onFailure(call: Call<InsuranceDetail>, t: Throwable) {
                connectionError.value = true
            }
        })
    }

    fun getInsuranceId(accessToken: String, insuranceId: Long) {
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.getInsuranceId(accessToken, MyApplication.preferences.getString("tokenUser", null), insuranceId)
        call.enqueue(object : Callback<FullInsurance> {
            override fun onResponse(call: Call<FullInsurance>, response: Response<FullInsurance>) {
                if (response.isSuccessful) {
                    fullInsuranceLiveData.value = response.body()
                } else {
                    errorHandler.value = response.message()
                }
            }

            override fun onFailure(call: Call<FullInsurance>, t: Throwable) {
                connectionError.value = true
            }

        })
    }

    fun getInsuranceContract(insuranceId: Long, context: Context) {
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val api = retrofit.create(Api::class.java)
        val call = api.getInsuranceContract(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), insuranceId)
        call.enqueue(object : Callback<ResponseBody> {
            @SuppressLint("StaticFieldLeak")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "server contacted and has file")
                    insuranceContractPdfLiveData.value = true
                    object : AsyncTask<Void, Void, Void>() {
                        override fun doInBackground(vararg voids: Void): Void? {
                            val writtenToDisk = writeResponseBodyToDisk(response.body()!!)
                            Log.d(TAG, "file download was a success? $writtenToDisk")
                            val target = Intent(Intent.ACTION_VIEW)
                            target.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf")
                            target.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            val intent = Intent.createChooser(target, "Open File")

                            context.startActivity(intent)
                            try {
                            } catch (e: ActivityNotFoundException) {
                                // Instruct the user to install a PDF reader here, or something
                            }

                            return null
                        }
                    }.execute()
                } else {
                    Log.d(TAG, "server contact failed")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                connectionError.value = true
            }
        })

    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        try {
            file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator + "Contrato.pdf")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)

                while (true) {
                    val read = inputStream.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()

                    Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize")
                }

                outputStream.flush()

                return true
            } catch (e: IOException) {
                println(e.message)
                return false
            } finally {
                inputStream?.close()

                outputStream?.close()
            }
        } catch (e: IOException) {
            return false
        }

    }


    fun getInsuranceSaleDetail(accessToken: String, insuranceId: Long) {
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.getInsuranceSaleDetails(accessToken, MyApplication.preferences.getString("tokenUser", null), insuranceId)
        call.enqueue(object : Callback<SaleInsurance> {
            override fun onResponse(call: Call<SaleInsurance>, response: Response<SaleInsurance>) {
                if (response.isSuccessful) {
                    cancelInsuranceDetailLiveData.value = response.body()
                } else {
                    errorHandler.value = response.message()
                }
            }

            override fun onFailure(call: Call<SaleInsurance>, t: Throwable) {
                connectionError.value = true
            }
        })
    }

    fun contractInsurance(accessToken: String, requestSaleInsuranceDTO: RequestSaleInsuranceDTO) {
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.contractInsurance(accessToken, MyApplication.preferences.getString("tokenUser", null), requestSaleInsuranceDTO)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    wellContractedInsuranceLiveData.value = response.isSuccessful
                } else {
                    val jObjError = JSONArray(response.errorBody()!!.string()).getJSONObject(0)
                    errorHandler.value = (Objects.requireNonNull<JSONObject>(jObjError).getString("message"))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                connectionError.value = true
            }
        })
    }

    fun getAvailableInsurances(accessToken: String, accountId: String) {
        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.getInsurances(accessToken, MyApplication.preferences.getString("tokenUser", null), accountId)
        call.enqueue(object : Callback<Insurance> {
            override fun onResponse(call: Call<Insurance>, response: Response<Insurance>) {
                if (response.isSuccessful) {
                    insuranceLiveData.value = response.body()
                } else {
                    val jObjError = JSONArray(response.errorBody()!!.string()).getJSONObject(0)
                    errorHandler.value = (Objects.requireNonNull<JSONObject>(jObjError).getString("message"))
                }
            }

            override fun onFailure(call: Call<Insurance>, t: Throwable) {
                connectionError.value = true
            }

        })
    }

    fun deleteInsurance(accessToken: String, accountId: String, saleInsuranceId: Long) {

        val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(Api::class.java)
        val call = api.deleteInsurance(accessToken, MyApplication.preferences.getString("tokenUser", null), accountId, saleInsuranceId.toString())
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    cancelInsuranceFlagLiveData.value = true
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                connectionError.value = true
            }
        })
    }

}