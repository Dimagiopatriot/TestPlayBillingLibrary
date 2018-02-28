package com.appcreate.testplaybillinglibrary.adapter;

import android.support.v7.widget.RecyclerView;

import com.appcreate.testplaybillinglibrary.MainViewController;

import java.util.List;

/**
 * Created by dmitriysmishnyi on 28.02.18.
 */

public abstract class CommonRecyclerViewAdapter <T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public abstract void addItems(List<T> items);
    public abstract void clearItems();
    public abstract void setMainViewController(MainViewController controller);
}
