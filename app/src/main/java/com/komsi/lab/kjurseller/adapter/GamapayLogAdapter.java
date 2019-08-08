package com.komsi.lab.kjurseller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.komsi.lab.kjurseller.R;
import com.komsi.lab.kjurseller.model.GamapayLog;

import java.util.List;

public class GamapayLogAdapter extends RecyclerView.Adapter<GamapayLogAdapter.CustomViewHolder>{

    List<GamapayLog> gamapayLogs;

    public GamapayLogAdapter(List<GamapayLog> gamapayLogs) {
        this.gamapayLogs = gamapayLogs;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_log_balance, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        GamapayLog gamapayLog = gamapayLogs.get(position);
        holder.tvRef.setText(String.valueOf(gamapayLog.getReferenceId()));
        holder.tvDateTime.setText(gamapayLog.getDateTime());
        holder.tvTotal.setText(String.valueOf(gamapayLog.getTotal()));
        holder.tvType.setText(gamapayLog.getType());
        holder.tvBalance.setText(String.valueOf(gamapayLog.getGrandTotal()));
    }

    @Override
    public int getItemCount() {
        return gamapayLogs.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private TextView tvRef, tvDateTime, tvTotal, tvType, tvBalance;

        public CustomViewHolder(View view) {
            super(view);
            tvRef = itemView.findViewById(R.id.tvRef);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvType = itemView.findViewById(R.id.tvType);
            tvBalance = itemView.findViewById(R.id.tvBalance);
        }
    }
}