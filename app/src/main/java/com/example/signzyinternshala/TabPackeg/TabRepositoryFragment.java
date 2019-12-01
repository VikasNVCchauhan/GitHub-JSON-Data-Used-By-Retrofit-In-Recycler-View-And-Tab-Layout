package com.example.signzyinternshala.TabPackeg;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signzyinternshala.InternetChecking.NetworkStateChangeReciever;
import com.example.signzyinternshala.Model.UserModelRepositoryData;
import com.example.signzyinternshala.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabRepositoryFragment extends Fragment {


    //widgets
    private RecyclerView recyclerView;
    private String stringActualRepoPosition;
    private AppCompatActivity appCompatActivity;
    private Map<String, String> mapUserData;
    private LinearLayout linearLayoutProgressBar;

    //variables
    private TabRepositoryRecyclerAdapter tabRepositoryRecyclerAdapter;
    private List<UserModelRepositoryData> listUserModelRepositoryData;
    private String repositoryName, stringLanguage, stringScore, stringWatcherCount, stringOpenIssues, stringForkCount;
    //Json data
    private RequestQueue mRequestQueue;
    private String stringRepoUrl;

    //Internet connectivity
    private NetworkStateChangeReciever networkStateChangeReciever;


    public TabRepositoryFragment() {

    }

    public TabRepositoryFragment(Map<String, String> mapUserData) {


        this.mapUserData = mapUserData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_repository, container, false);
        appCompatActivity = (AppCompatActivity) view.getContext();
        //internet checking
        networkStateChangeReciever = new NetworkStateChangeReciever();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        appCompatActivity.registerReceiver(networkStateChangeReciever, intentFilter);

        setIdForAllWidgets(view);

        listUserModelRepositoryData = new ArrayList<>();
        stringRepoUrl = mapUserData.get("REPO_LINK");
        setDataToList();
        recyclerView.setLayoutManager(new LinearLayoutManager(appCompatActivity));
        return view;
    }

    private void setDataToList() {
        mRequestQueue = Volley.newRequestQueue(appCompatActivity);
        jsonPars();
    }

    private void jsonPars() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stringRepoUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        repositoryName = jsonObject.getString("name");
                        stringLanguage = jsonObject.getString("language");
                        stringScore = jsonObject.getString("stargazers_count");
                        stringWatcherCount = jsonObject.getString("watchers_count");
                        stringOpenIssues = jsonObject.getString("open_issues");
                        stringForkCount = jsonObject.getString("forks_count");

                        listUserModelRepositoryData.add(new UserModelRepositoryData(repositoryName, stringLanguage, stringScore, stringWatcherCount, stringOpenIssues, stringForkCount));
                    }
                    linearLayoutProgressBar.setVisibility(View.INVISIBLE);
                    tabRepositoryRecyclerAdapter = new TabRepositoryRecyclerAdapter(listUserModelRepositoryData, appCompatActivity);
                    recyclerView.setAdapter(tabRepositoryRecyclerAdapter);
                    tabRepositoryRecyclerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    linearLayoutProgressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                linearLayoutProgressBar.setVisibility(View.INVISIBLE);
            }
        });
        mRequestQueue.add(stringRequest);
    }


    private void setIdForAllWidgets(View view) {
        linearLayoutProgressBar = view.findViewById(R.id.linear_layout_progress_bar_repository_fragment);
        recyclerView = view.findViewById(R.id.recycler_view_repository_tab);
    }
}
