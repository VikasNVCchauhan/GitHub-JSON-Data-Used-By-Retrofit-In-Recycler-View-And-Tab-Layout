package com.example.signzyinternshala.TabPackeg;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.Map;

public class TabPagAdapter extends FragmentPagerAdapter {
    private int tabNo;
    private Map<String ,String> mapUserData;

    public TabPagAdapter(@NonNull FragmentManager fm, int tabNo, Map<String ,String> mapUserData) {
        super(fm);
        this.tabNo = tabNo;
        this.mapUserData=mapUserData;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TabProfileFragment(mapUserData);
            case 1:
                return new TabRepositoryFragment(mapUserData);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabNo;
    }
}
