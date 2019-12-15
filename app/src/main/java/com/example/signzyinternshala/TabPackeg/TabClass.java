package com.example.signzyinternshala.TabPackeg;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.signzyinternshala.InternetChecking.NetworkStateChangeReciever;
import com.example.signzyinternshala.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TabClass extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {

    private TabItem tabItemProfile, tabItemRepository;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabPagAdapter pagAdapter;
    private Map<String, String> mapUserData;

    //Variables
    private String stringProfileUrl, stringUserName, stringUserPositionInJson, stringRepoLink,stringRepoCount;

    //Internet connectivity
    private NetworkStateChangeReciever networkStateChangeReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        //internet checking
        networkStateChangeReciever = new NetworkStateChangeReciever();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(networkStateChangeReciever, intentFilter);

        setItemForAllWidgets();
        stringProfileUrl = getIntent().getStringExtra("IMG_URL");
        stringUserName = getIntent().getStringExtra("USER_NAME");
        stringUserPositionInJson = getIntent().getStringExtra("USER_POSITION_IN_JSON");
        stringRepoLink = getIntent().getStringExtra("REPO_LINK");
        stringRepoCount=getIntent().getStringExtra("REPO_COUNT");

        mapUserData = new HashMap<>();
        mapUserData.put("ProfileUrl", stringProfileUrl);
        mapUserData.put("UserName", stringUserName);
        mapUserData.put("ActualPositionInJSONFile", stringUserPositionInJson);
        mapUserData.put("REPO_LINK", stringRepoLink);
        mapUserData.put("REPO_COUNT", stringRepoCount);
        pagAdapter = new TabPagAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), mapUserData);
        viewPager.setAdapter(pagAdapter);

        setData();

        tabLayout.addOnTabSelectedListener(this);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setData() {

    }

    private void setItemForAllWidgets() {
        tabItemProfile = findViewById(R.id.tab_profile);
        tabItemRepository = findViewById(R.id.tab_repository);
        tabLayout = findViewById(R.id.tab_layout_tab_activity);
        viewPager = findViewById(R.id.view_pager_tab_layout);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 0) {
            new TabProfileFragment(mapUserData);
        } else if (tab.getPosition() == 1) {
            new TabRepositoryFragment(mapUserData);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
