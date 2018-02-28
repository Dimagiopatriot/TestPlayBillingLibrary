package com.appcreate.testplaybillinglibrary.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.billingclient.api.SkuDetails;
import com.appcreate.testplaybillinglibrary.MainViewController;
import com.appcreate.testplaybillinglibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitriysmishnyi on 28.02.18.
 */

public class SkuDetailsAdapter extends CommonRecyclerViewAdapter<SkuDetails, SkuDetailsAdapter.Holder> {

    private List<SkuDetails> skuDetailsList = new ArrayList<>();
    private MainViewController mainViewController;

    @Override
    public void addItems(List<SkuDetails> skuDetailsList){
        this.skuDetailsList.addAll(skuDetailsList);
        notifyDataSetChanged();
    }

    @Override
    public void clearItems(){
        skuDetailsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void setMainViewController(MainViewController controller) {
        mainViewController = controller;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        return new Holder(row);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        SkuDetails skuDetails = skuDetailsList.get(position);
        holder.skuDetails = skuDetails;
        holder.titleTextView.setText(skuDetails.getTitle());
        holder.priceTextView.setText(skuDetails.getPrice());
        holder.descriptionTextView.setText(skuDetails.getDescription());
    }

    @Override
    public int getItemCount() {
        return skuDetailsList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SkuDetails skuDetails;

        TextView titleTextView, priceTextView, descriptionTextView;
        Button button;

        public Holder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            button = itemView.findViewById(R.id.subscribeButton);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mainViewController.subscribe(skuDetails.getSku());
        }
    }
}
