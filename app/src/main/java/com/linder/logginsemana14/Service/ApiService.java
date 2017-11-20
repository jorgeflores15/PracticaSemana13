package com.linder.logginsemana14.Service;

import com.linder.logginsemana14.model.Denuncia;
import com.linder.logginsemana14.model.Producto;
import com.linder.logginsemana14.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by linderhassinger on 11/13/17.
 */

public interface ApiService {

    String API_BASE_URL = "https://productos-api-linder3hs.c9users.io/";

    @GET("/api/v1/loginUsers")
    Call<List<User>> getUsers();

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<ResponseMessage> loginUser
                (@Field("username")String nombre,
                 @Field("password") String password
    );

    @GET("/api/v1/denuncias")
    Call<List<Denuncia>> getDenuncias();

    @FormUrlEncoded
    @POST("/api/v1/register")
    Call<ResponseMessage> registrarUsuario(
            @Field("username") String username,
            @Field("correo") String correo,
            @Field("password") String password);


    @FormUrlEncoded
    @POST("/api/v1/denuncias")
    Call<ResponseMessage>
    createDenuncia(@Field("titulo")String titulo,
                   @Field("descripcion") String descripcion,
                   @Field("ubicacion") String ubicacion,
                   @Field("lat") float lat,
                   @Field("lng") float lng,
                   @Field("user_id")String user_id);


    @Multipart
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> createDenunciaWithImage(
            @Part("titulo") RequestBody titulo,
            @Part("descripcion") RequestBody descripcion,
            @Part("ubicacion") RequestBody ubicacion,
            @Part("lat") RequestBody lat,
            @Part("lng") RequestBody lng,
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part imagen
    );




}
