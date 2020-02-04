package com.pashaz.tests.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.pashaz.tests.MainActivity;
import com.pashaz.tests.R;


public class GPSTracker extends Service {
  public static final String CHANNEL_ID = "ForegroundServiceChannel";
  private LocationListener listener;
  private LocationManager locationManager;
  int flag;
  SharedPreferences sPref;
  Location startLocation;
  double latitude;
  double latitude_save;
  int Satellites = 0;
  double longitude;
  double longitude_save;


  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    flag=0;
    String input = intent.getStringExtra("inputExtra");
    createNotificationChannel();
    Intent notificationIntent = new Intent(this, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(this,
      0, notificationIntent, 0);

    Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("Foreground Service")
      .setContentText(input)
      .setSmallIcon(R.drawable.location_button)
      .setContentIntent(pendingIntent)
      .build();
    startForeground(1, notification);
    //do heavy work on a background thread
    //stopSelf();
    return START_NOT_STICKY;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @SuppressLint("MissingPermission")
  @Override
  public void onCreate() {
    locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    listener = new LocationListener() {
      @Override
      public void onLocationChanged(Location location) {

        if(flag==0) {
          startLocation=new Location("startloc");
          latitude = location.getLatitude();
          longitude = location.getLongitude();
          startLocation.setLatitude(latitude);
          startLocation.setLongitude(longitude);
          Log.e("flag",Integer.toString(flag));
          flag++;
        }
        latitude_save=location.getLatitude();
        longitude_save=location.getLongitude();

        Intent i = new Intent("location_update");

        i.putExtra("coordinates",location.getLongitude()+" "+location.getLatitude());
        i.putExtra("speed",Float.toString(location.getSpeed()));

        i.putExtra("satelites",Integer.toString(Satellites));
        ////

        Location newLocation=new Location("newloc");
        newLocation.setLatitude(location.getLatitude());
        newLocation.setLongitude(location.getLongitude());

        float distance =startLocation.distanceTo(newLocation);
        i.putExtra("distance",Float.toString( distance));
        ///
        sendBroadcast(i);
      }

      @Override
      public void onStatusChanged(String s, int i, Bundle bundle) {

      }

      @Override
      public void onProviderEnabled(String s) {

      }

      @Override
      public void onProviderDisabled(String s) {
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
      }
    };



    //noinspection MissingPermission
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,listener);
    GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {
      public void onGpsStatusChanged(int event) {

        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS || event == GpsStatus.GPS_EVENT_FIRST_FIX) {
          GpsStatus status = locationManager.getGpsStatus(null);
          Iterable<GpsSatellite> sats = status.getSatellites();
          // Check number of satellites in list to determine fix state
          Satellites=0;
          for (GpsSatellite sat : sats) {
            //if(sat.usedInFix())
            Satellites++;
          }


        }
      }
    };
    locationManager.addGpsStatusListener(gpsStatusListener);

    // Define a listener that responds to location updates

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    saveText( "c1",Double.toString(latitude_save));
    saveText( "c0",Double.toString(longitude_save));
    if(locationManager != null){
      //noinspection MissingPermission
      locationManager.removeUpdates(listener);
    }
  }
  /////////////////////
  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel serviceChannel = new NotificationChannel(
        CHANNEL_ID,
        "Foreground Service Channel",
        NotificationManager.IMPORTANCE_DEFAULT
      );
      NotificationManager manager = getSystemService(NotificationManager.class);
      manager.createNotificationChannel(serviceChannel);
    }
  }

  void saveText( String s1,String s) {
    sPref = this.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
    SharedPreferences.Editor ed = sPref.edit();
    ed.putString(s1, s);

    ed.commit();

  }
  

}
