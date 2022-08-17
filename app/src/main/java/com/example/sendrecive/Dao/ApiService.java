package com.example.sendrecive.Dao;



import com.example.sendrecive.Models.ItemsUnit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("GetJrdItemUnit")
    Call<List<ItemsUnit>> GetJrdItemUnit(@Query("CONO") String ComNo, @Query("FROM_DATE") String FROM_DATE, @Query("TO_DATE") String TO_DATE  );

}
