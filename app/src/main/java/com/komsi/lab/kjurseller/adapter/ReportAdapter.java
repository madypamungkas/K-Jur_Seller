package com.komsi.lab.kjurseller.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.komsi.lab.kjurseller.ProductLogStockActivity;
import com.komsi.lab.kjurseller.R;
import com.komsi.lab.kjurseller.model.ReportList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.CustomViewHolder>{

    List<ReportList> reportLists;
    List<String> colors;

    public ReportAdapter(List<ReportList> reportLists) {
        this.reportLists = reportLists;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_report, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ReportList reportList = reportLists.get(position);
        holder.tvDate.setText(reportList.getDateTime());
        holder.tvName.setText(reportList.getName());
        holder.tvLocation.setText(reportList.getLocation());
        holder.tvPrice.setText(String.valueOf(reportList.getPrice()));
        holder.tvTotal.setText(String.valueOf(reportList.getTotal()));
        holder.tvSold.setText(reportList.getSold());
        holder.tvRemain.setText(String.valueOf(reportList.getRemain()));
        holder.tvIncome.setText(String.valueOf(reportList.getIncome()));
        holder.id = reportLists.get(position).getId();

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

        holder.viewColor.setBackgroundColor(Color.parseColor(colors.get(i1)));
    }

    @Override
    public int getItemCount() {
        return reportLists.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDate, tvName, tvLocation, tvPrice, tvTotal, tvSold, tvRemain, tvIncome;
        private ImageButton btnNext;
        private View viewColor;
        private String id;

        public CustomViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvName = itemView.findViewById(R.id.tvName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvSold = itemView.findViewById(R.id.tvSold);
            tvRemain = itemView.findViewById(R.id.tvRemain);
            tvIncome = itemView.findViewById(R.id.tvIncome);
            viewColor = itemView.findViewById(R.id.viewColor);
            btnNext = itemView.findViewById(R.id.btnNext);
            btnNext.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , ProductLogStockActivity.class);
            intent.putExtra("productId" , id);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<ReportList> reportLists) {
        this.reportLists.clear();
        this.reportLists.addAll(reportLists);
        notifyDataSetChanged();
    }
}