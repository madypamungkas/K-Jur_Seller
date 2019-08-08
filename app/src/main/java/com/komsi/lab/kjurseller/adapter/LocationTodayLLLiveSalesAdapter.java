package com.komsi.lab.kjurseller.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.komsi.lab.kjurseller.ProductLiveReportLSActivity;
import com.komsi.lab.kjurseller.R;
import com.komsi.lab.kjurseller.model.LocationToday;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocationTodayLLLiveSalesAdapter extends RecyclerView.Adapter<LocationTodayLLLiveSalesAdapter.CustomViewHolder>{

    List<LocationToday> locationTodays;
    List<String> colors;

    public LocationTodayLLLiveSalesAdapter(List<LocationToday> locationTodays) {
        this.locationTodays = locationTodays;
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
        private TextView tvLocName, circleText;
        private int id;

        public CustomViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            tvLocName = itemView.findViewById(R.id.tvLocName);
            circleText = itemView.findViewById(R.id.circleText);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , ProductLiveReportLSActivity.class);
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