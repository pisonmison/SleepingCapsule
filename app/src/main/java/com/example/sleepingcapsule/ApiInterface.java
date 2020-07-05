package com.example.sleepingcapsule;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("api/Themes?access_token=12345")
    Call<List<Themes>> getPosts();


}
