package com.pashaz.tests.ui.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pashaz.tests.R;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
  Context ctx;
  LayoutInflater lInflater;
  ArrayList<List_pojo> objects;

  ListAdapter(Context context, ArrayList<List_pojo> list_pojos) {
    ctx = context;
    objects = list_pojos;
    lInflater = (LayoutInflater) ctx
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  // кол-во элементов
  @Override
  public int getCount() {
    return objects.size();
  }

  // элемент по позиции
  @Override
  public Object getItem(int position) {
    return objects.get(position);
  }

  // id по позиции
  @Override
  public long getItemId(int position) {
    return position;
  }

  // пункт списка
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // используем созданные, но не используемые view
    View view = convertView;
    if (view == null) {
      view = lInflater.inflate(R.layout.item_list, parent, false);
    }

    ((TextView) view.findViewById(R.id.item_id)).setText(" "+objects.get(position).getId());
    ((TextView) view.findViewById(R.id.item_title)).setText(" "+objects.get(position).getTitle());
     ImageView img= view.findViewById(R.id.item_image);
    /* Glide.with(view.getContext())
      .load(objects.get(position).getThumbnailUrl())
      .placeholder(R.drawable.taxi_pic)
      .error(R.drawable.common_google_signin_btn_text_dark)
      .into(img);*/

    return view;
  }
  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public int getItemViewType(int position) {
    return position == 0 ? 0 : 1;
  }



}
