package com.pashaz.tests;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class LoadingScreen extends AppCompatActivity {
  SharedPreferences prefs = null;
  SharedPreferences sPref;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loading_screen);
    prefs = getSharedPreferences("com.pashaz.tests", MODE_PRIVATE);
    //Сохраняем первие координаты для презапуска карты
    if (prefs.getBoolean("firstrun", true)) {

      saveText( "c1","50.45");
      saveText( "c0","30.5233");

      prefs.edit().putBoolean("firstrun", false).commit();
    }



    //Получаем права ,я использую библиотеку https://github.com/ParkSangGwon/TedPermission
    PermissionListener permissionlistener = new PermissionListener() {
      @Override
      public void onPermissionGranted() {
        //Cделал задержу чтобы наш логотип показывало,
        // если все права уже есть, в последующих загрузках
        try {
          Thread.sleep(1000); //задержка 1 сек
        } catch (Exception e) {

        }
        //Переходим в наше главное активити,для его создания использовал
        //Navigation Drawarer activity

        Intent intent = new Intent(LoadingScreen.this, MainActivity.class);
        startActivity(intent);
      }

      @Override
      public void onPermissionDenied(List<String> deniedPermissions) {
        Toast.makeText(LoadingScreen.this, "Для доступа к программе нужно дать разрешения" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
      }


    };

    TedPermission.with(this)
      .setPermissionListener(permissionlistener)
      .setDeniedMessage("")
      .setPermissions(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.FOREGROUND_SERVICE

      ).check();

    ///

  }
  //функция для сохранения координат
  void saveText( String s1,String s) {
    sPref = this.getSharedPreferences("preferences", Activity.MODE_PRIVATE);
    SharedPreferences.Editor ed = sPref.edit();
    ed.putString(s1, s);

    ed.commit();

  }

}
