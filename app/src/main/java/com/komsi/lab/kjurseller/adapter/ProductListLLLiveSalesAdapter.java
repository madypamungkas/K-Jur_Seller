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
import com.komsi.lab.kjurseller.model.ProductToday;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductListLLLiveSalesAdapter extends RecyclerView.Adapter<ProductListLLLiveSalesAdapter.CustomViewHolder>{

    List<ProductToday> products;
    List<String> colors;

    public ProductListLLLiveSalesAdapter(List<ProductToday> products) {
        this.products = products;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ProductToday product = products.get(position);
        holder.tvProductName.setText(product.getProductName());
        holder.id = products.get(position).getId();
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

        holder.viewColor.setBackgroundColor(Color.parseColor(colors.get(i1)));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProductName;
        private ImageButton btnNext;
        private View viewColor;
        private String id;

        public CustomViewHolder(View view) {
            super(view);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            btnNext = itemView.findViewById(R.id.btnNext);
            viewColor = itemView.findViewById(R.id.viewColor);
            btnNext.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , ProductLogStockActivity.class);
            intent.putExtra("productId" , id);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<ProductToday> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }
}