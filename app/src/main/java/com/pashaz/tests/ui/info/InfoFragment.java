package com.pashaz.tests.ui.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.pashaz.tests.R;

public class InfoFragment extends Fragment {
  TextView coord;
  TextView speed;
  TextView satelit;
  TextView distance;
  LatLng loc;
  private BroadcastReceiver broadcastReceiver;
  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_info, container, false);
    coord= root.findViewById(R.id.info_text_coord);
     speed= root.findViewById(R.id.info_text_speed);
    satelit= root.findViewById(R.id.info_text_satelit);
    distance= root.findViewById(R.id.info_text_distance);




    return root;
  }

  @Override
  public void onResume() {
    super.onResume();
    if (broadcastReceiver == null) {
      broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


          coord.setText("Coordinates: "+String.valueOf(intent.getExtras().get("coordinates")));
          speed.setText("Speed: "+String.valueOf(intent.getExtras().get("speed"))+" m/s");
          satelit.setText("Satelites: "+String.valueOf(intent.getExtras().get("satelites")));
          distance.setText("Distance: "+String.valueOf(intent.getExtras().get("distance"))+" m");


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


}
