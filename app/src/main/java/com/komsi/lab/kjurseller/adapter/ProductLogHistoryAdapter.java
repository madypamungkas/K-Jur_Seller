package com.komsi.lab.kjurseller.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.komsi.lab.kjurseller.R;
import com.komsi.lab.kjurseller.model.ProductLogHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductLogHistoryAdapter extends RecyclerView.Adapter<ProductLogHistoryAdapter.CustomViewHolder>{

    List<ProductLogHistory> productLogHistories;
    List<String> colors;

    public ProductLogHistoryAdapter(List<ProductLogHistory> productLogHistories) {
        this.productLogHistories = productLogHistories;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_live_sales, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ProductLogHistory productLogHistory = productLogHistories.get(position);
        holder.tvType.setText(productLogHistory.getType());
        holder.tvDateTime.setText(productLogHistory.getDateCreated());
        holder.tvRef.setText(String.valueOf(productLogHistory.getId()));
        holder.tvOldStock.setText(String.valueOf(productLogHistory.getStockOld()));
        holder.tvChangeStock.setText(String.valueOf(productLogHistory.getStockChange()));
        holder.tvNewStock.setText(String.valueOf(productLogHistory.getStockNew()));

        colors = new ArrayList<String>();

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

        holder.imgLiveSales.setColorFilter(Color.parseColor(colors.get(i1)), PorterDuff.Mode.SRC_IN);
        holder.tvType.setTextColor(Color.parseColor(colors.get(i1)));
        holder.tvDateTime.setTextColor(Color.parseColor(colors.get(i1)));
    }

    @Override
    public int getItemCount() {
        return productLogHistories.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView tvType, tvDateTime, tvRef, tvOldStock, tvChangeStock, tvNewStock;
        private ImageView imgLiveSales;

        public CustomViewHolder(View view) {
            super(view);
            tvType = itemView.findViewById(R.id.tvType);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvRef = itemView.findViewById(R.id.tvRef);
            tvOldStock = itemView.findViewById(R.id.tvOldStock);
            tvChangeStock = itemView.findViewById(R.id.tvChangeStock);
            tvNewStock = itemView.findViewById(R.id.tvNewStock);
            imgLiveSales = itemView.findViewById(R.id.imgLiveSales);
        }
    }
}