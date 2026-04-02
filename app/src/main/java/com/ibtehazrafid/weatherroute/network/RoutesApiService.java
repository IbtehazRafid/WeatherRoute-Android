package com.ibtehazrafid.weatherroute.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
public interface RoutesApiService {
    @POST("directions/v2:computeRoutes")
    Call<RoutesResponse> computeRoutes(
            @Body RoutesRequest request,
            @Header("X-Goog-Api-Key") String apiKey,
            @Header("X-Goog-FieldMask") String fieldMask
    );
}
