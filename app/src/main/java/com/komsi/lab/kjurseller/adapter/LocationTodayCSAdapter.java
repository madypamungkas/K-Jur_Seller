package com.komsi.lab.kjurseller.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.komsi.lab.kjurseller.ProductCloseStoreActivity;
import com.komsi.lab.kjurseller.R;
import com.komsi.lab.kjurseller.model.CloseStoreResponse;
import com.komsi.lab.kjurseller.model.LocationToday;
import com.komsi.lab.kjurseller.model.User;
import com.komsi.lab.kjurseller.api.RetrofitClient;
import com.komsi.lab.kjurseller.storage.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationTodayCSAdapter extends RecyclerView.Adapter<LocationTodayCSAdapter.CustomViewHolder>{

    List<LocationToday> locationTodays;
    List<String> colors;
    Context mContext;

    public LocationTodayCSAdapter(List<LocationToday> locationTodays, Context mContext) {
        this.locationTodays = locationTodays;
        this.mContext = mContext;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_loc_today, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        LocationToday locationToday = locationTodays.get(position);
        holder.tvLocName.setText(locationToday.getNama());
        holder.id = locationTodays.get(position).getId();
        holder.circleText.setText(locationToday.getNama().substring(0, 1));

        colors=new ArrayList<String>();

        colors.add("#e51c23");
        colors.add("#e91e63");
        colors.add("#9c27b0");
        colors.add("#673ab7");
        colors.add("#3f51b5");
        colors.add("#5677fc");
        colors.add("#03a9f4");
        colors.add("#00bcd4");
        colors.add("#009688");
        colors.add("#259b24");
        colors.add("#8bc34a");
        colors.add("#cddc39");
        colors.add("#ffeb3b");
        colors.add("#ff9800");
        colors.add("#ff5722");
        colors.add("#795548");
        colors.add("#9e9e9e");
        colors.add("#607d8b");

        Random r = new Random();
        int i1 = r.nextInt(17- 0) + 0;

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.OVAL);
        draw.setColor(Color.parseColor(colors.get(i1)));
        holder.circleText.setBackground(draw);
    }

    @Override
    public int getItemCount() {
        return locationTodays.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvLocName, circleText;
        private int id;

        public CustomViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            tvLocName = itemView.findViewById(R.id.tvLocName);
            circleText = itemView.findViewById(R.id.circleText);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , ProductCloseStoreActivity.class);
            intent.putExtra("locTodayId" , id);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<LocationToday> locationTodays) {
        this.locationTodays.clear();
        this.locationTodays.addAll(locationTodays);
        notifyDataSetChanged();
    }
}