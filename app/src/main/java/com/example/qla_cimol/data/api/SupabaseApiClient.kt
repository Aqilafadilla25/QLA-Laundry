package com.example.qla_cimol.data.api

import com.example.qla_cimol.data.model.OrderModel
import com.example.qla_cimol.data.model.ServiceModel
import com.example.qla_cimol.data.model.UserModel
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface SupabaseApiService {
    // 1. REGISTER USER (INSERT)
    @POST("user")
    suspend fun registerUser(@Body user: UserModel): Response<Unit>

    // 2. CRUD FITUR 1: SERVICES
    @GET("service")
    suspend fun getServices(): List<ServiceModel>

    @POST("service")
    suspend fun createService(@Body service: ServiceModel): Response<Unit>

    @PATCH("service")
    suspend fun updateService(
        @Query("id") idFilter: String,
        @Body service: ServiceModel
    ): Response<Unit>

    @DELETE("service")
    suspend fun deleteService(
        @Query("id") idFilter: String
    ): Response<Unit>

    // 3. CRUD FITUR 2: ORDERS
    @GET("orders")
    suspend fun getOrders(): List<OrderModel>

    @POST("orders")
    suspend fun createOrder(@Body order: OrderModel): Response<Unit>

    @PATCH("orders")
    suspend fun updateOrder(
        @Query("id") idFilter: String,
        @Body order: OrderModel
    ): Response<Unit>

    @DELETE("orders")
    suspend fun deleteOrder(
        @Query("id") idFilter: String
    ): Response<Unit>
}

object SupabaseApiClient {
    private const val BASE_URL = "https://qofmkdmisqwztztnbnjz.supabase.co/rest/v1/"
    private const val API_KEY = "sb_publishable_znPRzCq2LkK2iXrOCNo-lA_rqaxmf6i"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer $API_KEY")
                .addHeader("Accept-Profile", "LaundryExpress")
                .addHeader("Content-Profile", "LaundryExpress")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()

    val apiService: SupabaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApiService::class.java)
    }
}