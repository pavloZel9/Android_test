package com.pashaz.tests.ui.list;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pashaz.tests.R;
import com.pashaz.tests.ui.list.api.ServerApi;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListFragment extends Fragment {
  ArrayList<List_pojo> list_pojo = new ArrayList<>();

  RecyclerView listview;
  private RecyclerAdapter mAdapter;
  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_list, container, false);
    listview=root.findViewById(R.id.get_recycle_view);
/////////Получаем json данные через Retrofit
    ///pojo-class List_pojo
    //List_Adapter - адаптер заполнения RecyclerView данными
    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(ServerApi.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
      .build();
    ServerApi api = retrofit.create(ServerApi.class);
    Call<ArrayList<List_pojo>> call = api.getPhotos();


    call.enqueue(new Callback<ArrayList<List_pojo>>() {
      @Override
      public void onResponse(Call<ArrayList<List_pojo>> call, Response<ArrayList<List_pojo>> response) {

        list_pojo=response.body();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        listview.setLayoutManager(mLayoutManager);
        listview.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new RecyclerAdapter(getActivity(), list_pojo);
        listview.setAdapter(mAdapter);
      }

      @Override
      public void onFailure(Call<ArrayList<List_pojo>> call, Throwable t) {

      }


    });
    return root;
  }
}
