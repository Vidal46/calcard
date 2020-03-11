package br.com.calcard.android.app.ui.invoice;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.calcard.android.app.BuildConfig;
import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.api.Api;
import br.com.calcard.android.app.model.Detail;
import br.com.calcard.android.app.model.InvoiceSumary;
import br.com.calcard.android.app.model.Transaction;
import br.com.calcard.android.app.utils.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoiceViewModel extends ViewModel {
    public List<Transaction> transactions = new ArrayList<>();
    public ObservableField<String> detailsOk = new ObservableField<>("");
    public ObservableArrayList<InvoiceSumary> sumarys = new ObservableArrayList<>();
    public ObservableField<String> sumaryOk = new ObservableField<>("");
    public MutableLiveData<Response<ResponseBody>> response1 = new MutableLiveData<Response<ResponseBody>>();
    public File file;
    Call<Detail> call;


    public void getDetails(String accessToken, String token, String id, String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        call = api.getDetails(accessToken, token, id, query);
        call.enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if (response.code() == 200) {
                    transactions.clear();
                    transactions.addAll(response.body().getTransactions());
                    detailsOk.set("ok");
                } else {
                    detailsOk.set("nOK");
                }

            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    public void getInvoiceSummary(String accessToken, String token) {
        sumarys.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);
        Call<List<InvoiceSumary>> call = api.getInvoiceSummary(accessToken, token, MyApplication.preferences.getString("idAccount", null));
        call.enqueue(new Callback<List<InvoiceSumary>>() {
            @Override
            public void onResponse(Call<List<InvoiceSumary>> call, Response<List<InvoiceSumary>> response) {
                if (response.code() == 200) {
                    sumarys.addAll(response.body());
                    sumaryOk.set("ok");
                } else {
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<List<InvoiceSumary>> call, Throwable t) {
                System.out.println(t.getMessage());
                getInvoiceSummary(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));

            }
        });

    }


    public void downloadInvoice(String dueDate,Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<ResponseBody> call = api.downloadInvoice(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null), dueDate);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(), context);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            target.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf");
                            target.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Intent intent = Intent.createChooser(target, "Open File");
                            try {
                                context.startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                // Instruct the user to install a PDF reader here, or something
                            }
                            return null;
                        }
                    }.execute();
                } else {
                    Toast.makeText(context, "Não foi possível baixar a fatura.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    private boolean writeResponseBodyToDisk(ResponseBody body, Context context) {
        try {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "fatura.pdf");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    void cancelCall() {
        call.cancel();
    }
}