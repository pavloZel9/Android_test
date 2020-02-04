package com.pashaz.tests.ui.list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.pashaz.tests.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter extends
  RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
  Context mContext;
  LayoutInflater layoutInflater;
  private ArrayList<List_pojo> list_pojos;

  /**
   * View holder class
   * */
  public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView id;
    public TextView title;
    public ImageView img;
    public MyViewHolder(View view) {
      super(view);
      id = (TextView) view.findViewById(R.id.item_id);
      title = (TextView) view.findViewById(R.id.item_title);
      img=(ImageView)view.findViewById(R.id.item_image);
    }
  }

  public RecyclerAdapter(Context mContext, ArrayList<List_pojo> list_pojos) {
    this.list_pojos = list_pojos;
    this.mContext=mContext;
    layoutInflater = LayoutInflater.from(mContext);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, int position) {
    List_pojo c = list_pojos.get(position);
    String id_text=Integer.toString(c.getId());
    holder.id.setText(id_text);
    holder.title.setText(String.valueOf(c.getTitle()));
    Log.e("u",Integer.toString(position));
    Picasso.get()
      .load(c.getThumbnailUrl())
      .placeholder(R.drawable.taxi_pic)
      .error(R.drawable.location_button)
      .into(holder.img);
  }

  @Override
  public int getItemCount() {
    return list_pojos.size();
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = layoutInflater.inflate(R.layout.item_list,parent, false);
    return new MyViewHolder(v);
  }
}
