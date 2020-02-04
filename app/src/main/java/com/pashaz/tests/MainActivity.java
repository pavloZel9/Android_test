package com.pashaz.tests;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.MenuItem;

import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.pashaz.tests.service.GPSTracker;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

  private AppBarConfiguration mAppBarConfiguration;
  GPSTracker gpsTracker;


  SharedPreferences sPref;
  private BroadcastReceiver broadcastReceiver;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //Инициализируемо наши переменние


    init();
    ////////////////////////////////////////////////////
    //ui.map
    //Содержит наш fragment для работы с картой,использовал стандартоное решение googlemaps
    //ключ лежит в res/values/map_api.xml
    //Запуск нашей службы в foreground
    Intent serviceIntent = new Intent(this, GPSTracker.class);
    serviceIntent.putExtra("inputExtra", "Foreground Service in Android");
    ContextCompat.startForegroundService(this, serviceIntent);
    ////


  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // TODO Auto-generated method stub
    Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
    //обработка нашего меню для старта сервиса и его остановки

    if (item.getTitle() != null) {
      if (item.getTitle().equals("Start")) {
        Intent serviceIntent = new Intent(this, GPSTracker.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
      } else {



        Intent serviceIntent = new Intent(this, GPSTracker.class);
        stopService(serviceIntent);

      }
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    return NavigationUI.navigateUp(navController, mAppBarConfiguration)
      || super.onSupportNavigateUp();
  }

public void init(){

  Toolbar toolbar = findViewById(R.id.toolbar);
  setSupportActionBar(toolbar);
  FloatingActionButton fab = findViewById(R.id.fab_location);

  DrawerLayout drawer = findViewById(R.id.drawer_layout);
  NavigationView navigationView = findViewById(R.id.nav_view);
  // Passing each menu ID as a set of Ids because each
  // menu should be considered as top level destinations.
  mAppBarConfiguration = new AppBarConfiguration.Builder(
    R.id.nav_maps, R.id.nav_list, R.id.nav_info)
    .setDrawerLayout(drawer)
    .build();
  NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
  NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
  NavigationUI.setupWithNavController(navigationView, navController);
}




}
