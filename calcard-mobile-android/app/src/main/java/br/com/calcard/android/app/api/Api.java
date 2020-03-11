package br.com.calcard.android.app.api;


import java.util.List;
import java.util.Map;

import br.com.calcard.android.app.BuildConfig;
import br.com.calcard.android.app.model.Authorization;
import br.com.calcard.android.app.model.Bill;
import br.com.calcard.android.app.model.BiometryDTO;
import br.com.calcard.android.app.model.Cards;
import br.com.calcard.android.app.model.Continent;
import br.com.calcard.android.app.model.Countries;
import br.com.calcard.android.app.model.DataSugestionCpf;
import br.com.calcard.android.app.model.Detail;
import br.com.calcard.android.app.model.Expenses;
import br.com.calcard.android.app.model.FullInsurance;
import br.com.calcard.android.app.model.Goals;
import br.com.calcard.android.app.model.Insurance;
import br.com.calcard.android.app.model.InsuranceDetail;
import br.com.calcard.android.app.model.InvoiceInstallment;
import br.com.calcard.android.app.model.InvoiceSumary;
import br.com.calcard.android.app.model.LastTransaction;
import br.com.calcard.android.app.model.Limits;
import br.com.calcard.android.app.model.MigrationElegible;
import br.com.calcard.android.app.model.MobileMigrationRequestDTO;
import br.com.calcard.android.app.model.Profile;
import br.com.calcard.android.app.model.RegisterRequest;
import br.com.calcard.android.app.model.RequestSaleInsuranceDTO;
import br.com.calcard.android.app.model.SaleInsurance;
import br.com.calcard.android.app.model.SaveGoals;
import br.com.calcard.android.app.model.SecureList;
import br.com.calcard.android.app.model.Timeline;
import br.com.calcard.android.app.model.TokenSms;
import br.com.calcard.android.app.model.UnlockCard;
import br.com.calcard.android.app.model.UpdateUser;
import br.com.calcard.android.app.model.Version;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;

public interface Api {

    String BASE_URL = BuildConfig.ENDPOINT;

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/generate-token")
    Call<TokenSms> generateToken(@Header("access-token") String token,
                                 @Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/register")
    Call<ResponseBody> register(@Header("access-token") String token,
                                @Body RegisterRequest registerRequest);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("migration/{cpf}/biometry")
    Call<Void> checkPicture(@Header("access-token") String token,
                            @Path("cpf") String cpf,
                            @Body BiometryDTO biometryDTO);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("migration/{cpf}")
    Call<MigrationElegible> cpfElegible(@Header("access-token") String token,
                                        @Path("cpf") String cpf);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("suggestion/cpf/{cpf}/action")
    Call<DataSugestionCpf> sugestionCheck(@Header("access-token") String token,
                                          @Path("cpf") String cpf);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("migration/{cpf}/migrate")
    Call<Void> checkPassword(@Header("access-token") String token,
                             @Path("cpf") String cpf,
                             @Body MobileMigrationRequestDTO mobileMigrationRequestDTO);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("insurance/account/{accountId}")
    Call<Insurance> getInsurances(@Header("access-token") String token,
                                  @Header("Authorization") String tokenUser,
                                  @Path("accountId") String accountId);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("insurance/account/{accountId}/sale-insurance/{saleInsuranceId}")
    Call<Void> deleteInsurance(@Header("access-token") String token,
                               @Header("Authorization") String tokenUser,
                               @Path("accountId") String accountId,
                               @Path("saleInsuranceId") String saleInsuranceId);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("insurance/{insuranceId}")
    Call<InsuranceDetail> getInsuranceDetails(@Header("access-token") String token,
                                              @Header("Authorization") String tokenUser,
                                              @Path("insuranceId") Long insuranceId);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("insurance/sale-insurance/{saleInsuranceId}")
    Call<FullInsurance> getInsuranceId(@Header("access-token") String token,
                                       @Header("Authorization") String tokenUser,
                                       @Path("saleInsuranceId") Long saleInsuranceId);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("insurance/sale-insurance/{saleInsuranceId}")
    Call<SaleInsurance> getInsuranceSaleDetails(@Header("access-token") String token,
                                                @Header("Authorization") String tokenUser,
                                                @Path("saleInsuranceId") Long saleInsuranceId);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @Streaming
    @GET("insurance/{insuranceId}/contract")
    Call<ResponseBody> getInsuranceContract(@Header("access-token") String token,
                                            @Header("Authorization") String tokenUser,
                                            @Path("insuranceId") Long insuranceId);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("insurance")
    Call<Void> contractInsurance(@Header("access-token") String token,
                                 @Header("Authorization") String tokenUser,
                                 @Body RequestSaleInsuranceDTO requestSaleInsuranceDTO);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @GET("version/update-check")
    Call<Version> updateCheck(@Header("access-token") String token,
                              @Header("Authorization") String tokenUser,
                              @Query("version") String version);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("user/profile")
    Call<Profile> profile(@Header("access-token") String token,
                          @Header("Authorization") String tokenUser);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("user/profile")
    Call<Profile> avatarUpdate(@Header("access-token") String token,
                               @Header("Authorization") String tokenUser,
                               @Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("card")
    Call<List<Cards>> getCards(@Header("access-token") String token,
                               @Header("Authorization") String tokenUser);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("account/{id}/limits")
    Call<Limits> getLimits(@Header("access-token") String token,
                           @Header("Authorization") String tokenUser,
                           @Path("id") String id);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("invoice/account/{id}/summary?status=ALL")
    Call<List<InvoiceSumary>> getInvoiceSummary(@Header("access-token") String token,
                                                @Header("Authorization") String tokenUser,
                                                @Path("id") String id);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("account/{id}/due-date")
    Call<ResponseBody> changeDueDate(@Header("access-token") String token,
                                     @Header("Authorization") String tokenUser,
                                     @Path("id") String id,
                                     @Query("newDueDay") String query);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("card/{id}/unlock")
    Call<ResponseBody> unlock(@Header("access-token") String token,
                              @Header("Authorization") String tokenUser,
                              @Path("id") String id,
                              @Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("account/{id}/timeline")
    Call<Timeline> getTimeline(@Header("access-token") String token,
                               @Header("Authorization") String tokenUser,
                               @Path("id") String id,
                               @QueryMap Map<String, String> options);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("expenses")
    Call<List<Expenses>> getExpenses(@Header("access-token") String token,
                                     @Header("Authorization") String tokenUser);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("invoice/account/{id}/details")
    Call<Detail> getDetails(@Header("access-token") String token,
                            @Header("Authorization") String tokenUser,
                            @Path("id") String id,
                            @Query("dueDate") String query);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/validate-token")
    Call<ResponseBody> validateToken(@Header("access-token") String token,
                                     @Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("user/forgot-password")
    Call<ResponseBody> changePassword(@Header("access-token") String token,
                                      @Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("account/{id}/expenses")
    Call<List<Goals>> getGoals(@Header("access-token") String token,
                               @Header("Authorization") String tokenUser,
                               @Path("id") String id);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("account/{id}/expenses")
    Call<ResponseBody> saveGoal(@Header("access-token") String token,
                                @Header("Authorization") String tokenUser,
                                @Path("id") String id,
                                @Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("insurance/price")
    Call<List<SecureList>> getInsurance(@Header("access-token") String token,
                                        @Header("Authorization") String tokenUser,
                                        @Query("accountId") String id);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("address/continents")
    Call<List<Continent>> getContinents(@Header("access-token") String token,
                                        @Header("Authorization") String tokenUser);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("address/countries")
    Call<List<Countries>> getCountries(@Header("access-token") String token,
                                       @Header("Authorization") String tokenUser,
                                       @Query("continentCode") String code);


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("travel-notification")
    Call<ResponseBody> postTravelNotification(@Header("access-token") String token,
                                              @Header("Authorization") String tokenUser,
                                              @Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("card/{id}/cancel")
    Call<ResponseBody> cancelCard(@Header("access-token") String token,
                                  @Header("Authorization") String tokenUser,
                                  @Path("id") String id,
                                  @Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("card/{id}/password")
    Call<ResponseBody> changePasswordCard(@Header("access-token") String token,
                                          @Header("Authorization") String tokenUser,
                                          @Path("id") String id,
                                          @Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @Streaming
    @GET("contract?contractType=CONTRATO_PROPOSTA")
    Call<ResponseBody> donwloadContract(@Header("access-token") String token,
                                        @Header("Authorization") String tokenUser);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("card/{id}/lock")
    Call<ResponseBody> lock(@Header("access-token") String token,
                            @Header("Authorization") String tokenUser,
                            @Path("id") String id,
                            @Body RequestBody body);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("user")
    Call<ResponseBody> updateUser(@Header("access-token") String token,
                                  @Header("Authorization") String tokenUser,
                                  @Body UpdateUser updateUser);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("invoice/{dueDate}/installment?size=6")
    Call<List<InvoiceInstallment>> getInstallmentInvoice(@Header("access-token") String token,
                                                         @Header("Authorization") String tokenUser,
                                                         @Path("dueDate") String dueDate);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("invoice/{dueDate}/bill/details")
    Call<Bill> getBill(@Header("access-token") String token,
                       @Header("Authorization") String tokenUser,
                       @Path("dueDate") String dueDate,
                       @Query("amount") String amount);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @Streaming
    @GET("invoice/{dueDate}")
    Call<ResponseBody> downloadInvoice(@Header("access-token") String token,
                                       @Header("Authorization") String tokenUser,
                                       @Path("dueDate") String dueDate);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @Streaming
    @GET("insurance/{insuranceId}/contract")
    Call<ResponseBody> downloadInsuranceContract(@Header("access-token") String token,
                                                 @Header("Authorization") String tokenUser,
                                                 @Path("insuranceId") String insuranceId);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("account/{id}/expenses")
    Observable<List<Goals>> doRequestGoals(@Header("access-token") String token,
                                           @Header("Authorization") String tokenUser,
                                           @Path("id") String id);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("account/{id}/expenses")
    Observable<ResponseBody> doSaveGoal(@Header("access-token") String token,
                                        @Header("Authorization") String tokenUser,
                                        @Path("id") String id,
                                        @Body SaveGoals goal);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("account/{id}/limits")
    Observable<Limits> doGetLimits(@Header("access-token") String token,
                                   @Header("Authorization") String tokenUser,
                                   @Path("id") String id);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("card/{id}/lock")
    Observable<ResponseBody> doCardBlock(@Header("access-token") String token,
                                         @Header("Authorization") String tokenUser,
                                         @Path("id") String id);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("card/{id}/unlock")
    Observable<ResponseBody> doUnlockCard(@Header("access-token") String token,
                                          @Header("Authorization") String tokenUser,
                                          @Path("id") String id,
                                          @Body UnlockCard unlockCard);

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("account/{id}/last-transaction")
    Call<LastTransaction> lastTransaction(@Header("access-token") String token,
                                          @Header("Authorization") String tokenUser,
                                          @Path("id") String id);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("authorization")
    Call<Authorization> authorize(@Header("access-token") String access_token,
                                        @Field("username") String username,
                                        @Field("password") String password,
                                        @Field("grant_type") String grant_type,
                                        @Field("deviceDTO.appVersion") String appVersion,
                                        @Field("deviceDTO.deviceId") String deviceId,
                                        @Field("deviceDTO.os") String os,
                                        @Field("deviceDTO.osVersion") String osVersion,
                                        @Field("deviceDTO.manufacturer") String manufacturer,
                                        @Field("deviceDTO.model") String model);
}
