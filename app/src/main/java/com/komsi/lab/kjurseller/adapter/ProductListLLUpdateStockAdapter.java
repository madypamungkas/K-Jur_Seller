package com.komsi.lab.kjurseller.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.komsi.lab.kjurseller.ProductUpdateStockActivity;
import com.komsi.lab.kjurseller.R;
import com.komsi.lab.kjurseller.model.ProductToday;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductListLLUpdateStockAdapter extends RecyclerView.Adapter<ProductListLLUpdateStockAdapter.CustomViewHolder>{

    List<ProductToday> products;
    List<String> colors;

    public ProductListLLUpdateStockAdapter(List<ProductToday> products) {
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
        holder.productName = products.get(position).getProductName();
        holder.productStock = products.get(position).getProductStockNow();

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
        /*
        holder.productPic = products.get(position).getProductPic();

        if(product.get() != null && !product.getProductPic().isEmpty()){
            Picasso.get()
                    .load(product.getProductPic())
                    .placeholder(R.drawable.ic_snack)
                    .error(R.drawable.ic_close)
                    // To fit image into imageView
                    //.fit()
                    .resize(500, 500)
                    .centerInside()
                    // To prevent fade animation
                    .noFade()
                    .into(holder.ivProduct);
        } else{
            holder.ivProduct.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_snack));
        }*/
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvProductName;
        private ImageButton btnNext;
        private View viewColor;
        private ImageView ivProduct;
        private int productStock;
        private String id, productName, productPic;

        public CustomViewHolder(View view) {
            super(view);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            btnNext = itemView.findViewById(R.id.btnNext);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            viewColor = itemView.findViewById(R.id.viewColor);
            btnNext.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            Intent intent = new Intent(itemView.getContext() , ProductUpdateStockActivity.class);
            intent.putExtra("productId" , id);
            intent.putExtra("productName" , productName);
            intent.putExtra("productStock" , productStock);
            itemView.getContext().startActivity(intent);
        }
    }

    public void refreshEvents(List<ProductToday> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }
}