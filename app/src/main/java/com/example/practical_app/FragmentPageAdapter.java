package com.example.practical_app;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FragmentPageAdapter extends FragmentStatePagerAdapter {
    public FragmentPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        FirstFragment firstFragment=new FirstFragment();
        Bundle bundle=new Bundle();
        position=position+1;
        Integer pos=position;
        bundle.putString("message",pos.toString());
        firstFragment.setArguments(bundle);
        System.out.println(position + " fragment");
        return firstFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        position=position+1;
        if(position==1)
        return "Alarm";
        return "Timer";
    }
}
