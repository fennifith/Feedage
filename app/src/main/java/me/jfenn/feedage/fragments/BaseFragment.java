package me.jfenn.feedage.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import me.jfenn.feedage.Feedage;

public abstract class BaseFragment extends Fragment {

    private Feedage feedage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedage = (Feedage) getContext().getApplicationContext();
    }

    Feedage getFeedage() {
        return feedage;
    }

    public void notifyDataSetChanged() {
    }
}
