package com.pashaz.tests.ui.list.api;

import com.pashaz.tests.ui.list.List_pojo;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ServerApi {


  String BASE_URL = "https://jsonplaceholder.typicode.com";
  @GET("/photos")
  Call<ArrayList<List_pojo>> getPhotos();

}
