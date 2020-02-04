package com.pashaz.tests.ui.map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pashaz.tests.R;

public class HomeFragment extends Fragment implements OnMapReadyCallback {
  private static View view;
  private BroadcastReceiver broadcastReceiver;
  GoogleMap googleMaps;
  LatLng loc;


  SharedPreferences sPref;
  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    //чтобы не перезагружало карту
    if (view != null) {
      ViewGroup parent = (ViewGroup) view.getParent();
      if (parent != null)
        parent.removeView(view);
    }
    try {
      view = inflater.inflate(R.layout.fragment_home, container, false);
    } catch (InflateException e) {
    }

    SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager()
      .findFragmentById(R.id.mapFragment);
    mMapFragment.getMapAsync(this);
    sPref =  getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
    FloatingActionButton but_get_location = view.findViewById(R.id.fab_location);
    //обработчик нажатия для нахождения местоположения
    View.OnClickListener oclBtnOk = new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        googleMaps.clear();
        Marker ham = googleMaps.addMarker(new MarkerOptions()
          .position(loc).title("My position"));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

      }
    };

    but_get_location.setOnClickListener(oclBtnOk);
    ////
    return view;
  }

  //получаем последние кооринаты ,устанавливаем маркер и уентрируем карту по ним
  @Override
  public void onMapReady(GoogleMap googleMap) {


    googleMaps = googleMap;
    LatLng Kyiv = new LatLng(Float.parseFloat(loadText("c1")),
      Float.parseFloat(loadText("c0")));
    googleMaps.addMarker(new MarkerOptions().position(Kyiv).title("My position"));
    googleMaps.moveCamera(CameraUpdateFactory.newLatLng(Kyiv));
  }
  //прослушиваем текущие координати и перемещаем курсор
  @Override
  public void onResume() {
    super.onResume();
    if (broadcastReceiver == null) {
      broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


          String[] coordintes = String.valueOf(intent.getExtras().get("coordinates")).split(" ");
          Log.e("ww", coordintes[0]);
          Log.e("ww", coordintes[1]);
          googleMaps.clear();
          loc = new LatLng(Double.parseDouble(coordintes[1]), Double.parseDouble(coordintes[0]));

          Marker ham = googleMaps.addMarker(new MarkerOptions()
            .position(loc).title("My position"));
          googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        }
      };
    }
    getActivity().registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (broadcastReceiver != null) {
      getActivity().unregisterReceiver(broadcastReceiver);
    }
  }

  //функция для хранения данных
  String loadText(String s1) {
    sPref = getActivity().getSharedPreferences("preferences", Activity.MODE_PRIVATE);
    String savedText = sPref.getString(s1, "");
    return  savedText;

  }
}
