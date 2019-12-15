package com.example.signzyinternshala.TabPackeg;


import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signzyinternshala.InternetChecking.NetworkStateChangeReciever;
import com.example.signzyinternshala.Model.UserModelRepositoryData;
import com.example.signzyinternshala.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabProfileFragment extends Fragment implements View.OnClickListener {


    private AppCompatActivity appCompatActivity;
    private ProgressBar progressBarFollower, progressBarFollowing, progressBarRepoCount;
    private CircleImageView circleImageViewUserProfile;
    private TextView textViewUserName, textViewFollowerCount, textViewFollowingCount, textViewRepositoryCount,textViewViewMore;
    private RecyclerView recyclerView;
    private LinearLayout linearLayoutProgressBar;

    //Variables
    private Map<String, String> mapUserData;
    private String repositoryName, stringLanguage, stringScore, stringWatcherCount, stringOpenIssues, stringForkCount;
    private List<UserModelRepositoryData> listUserModelRepositoryData;
    private String stringFollowerCount, stringFollowingCOunt;
    private String stringRepoLink;
    private int size;
    //User Defined Variables
    private TabRepositoryRecyclerAdapter tabRepositoryRecyclerAdapter;
    //Json data
    private RequestQueue mRequestQueue;
    private String stringJsonUrl = "https://api.github.com/search/users?q=language:java+location:lagos";

    //Internet connectivity
    private NetworkStateChangeReciever networkStateChangeReciever;


    public TabProfileFragment() {
        // Required empty public constructor
    }

    public TabProfileFragment(Map<String, String> mapUserData) {
        this.mapUserData = mapUserData;
        stringRepoLink = mapUserData.get("REPO_LINK");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_profile, container, false);
        appCompatActivity = (AppCompatActivity) view.getContext();
        listUserModelRepositoryData = new ArrayList<>();
        //internet checking
        networkStateChangeReciever = new NetworkStateChangeReciever();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        appCompatActivity.registerReceiver(networkStateChangeReciever, intentFilter);

        setIdForAllWidgets(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(appCompatActivity));

        setData();

        textViewViewMore.setOnClickListener(this);
        return view;
    }

    private void setData() {
        Picasso.with(appCompatActivity).load(mapUserData.get("ProfileUrl"))
                .centerCrop()
                .fit()
                .placeholder(R.drawable.button_background)
                .into(circleImageViewUserProfile);
        textViewUserName.setText(mapUserData.get("UserName"));
        mRequestQueue = Volley.newRequestQueue(appCompatActivity);

        jsonParser();
        getRepositoyData();
        CountDownTimer countDownTimer = new CountDownTimer(1800, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                textViewFollowingCount.setVisibility(View.VISIBLE);
                textViewFollowerCount.setVisibility(View.VISIBLE);
                progressBarFollower.setVisibility(View.GONE);
                progressBarFollowing.setVisibility(View.GONE);
                progressBarRepoCount.setVisibility(View.GONE);
                textViewRepositoryCount.setVisibility(View.VISIBLE);

                textViewFollowerCount.setText(stringFollowerCount);
                textViewFollowingCount.setText(stringFollowingCOunt);
                textViewRepositoryCount.setText(mapUserData.get("REPO_COUNT"));
            }
        };
        countDownTimer.start();
    }

    private void getRepositoyData() {
        setDataToList();
    }

    private void setDataToList() {
        mRequestQueue = Volley.newRequestQueue(appCompatActivity);
        jsonParsLoadRepo();
    }

    private void jsonParsLoadRepo() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, stringRepoLink, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray;
                    jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 6) {
                        size = 6;
                    } else {
                        size = jsonArray.length();
                    }
                    for (int i = 0; i < size; i++) {
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

    private void jsonParser() {

        final String[] stringFollowerUrl = new String[1];
        final String[] stringFollowingUrl = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.GET, stringJsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    JSONObject object = jsonArray.getJSONObject(Integer.valueOf(mapUserData.get("ActualPositionInJSONFile")));
                    stringFollowerUrl[0] = object.getString("followers_url");
                    stringFollowingUrl[0] = object.getString("subscriptions_url");
                    getFollowAndFollowerCount(stringFollowerUrl[0], stringFollowingUrl[0]);
                } catch (JSONException e) {
                    progressBarFollower.setVisibility(View.GONE);
                    progressBarFollowing.setVisibility(View.GONE);
                    progressBarRepoCount.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }

            private void getFollowAndFollowerCount(String stringFollowerLink, String stringFollowingLink) {

                getFollowerCount(stringFollowerLink);
                getFollowingCount(stringFollowingLink);


            }

            private void getFollowerCount(String stringFollowerLink) {
                final String[] size = new String[1];
                StringRequest stringRequest1Follower = new StringRequest(Request.Method.GET, stringFollowerLink, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            size[0] = String.valueOf(jsonArray.length());

                            stringFollowerCount = size[0];
                            textViewFollowerCount.setText(size[0]);

                        } catch (JSONException e) {
                            progressBarFollower.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarFollower.setVisibility(View.GONE);
                    }
                });
                mRequestQueue.add(stringRequest1Follower);
            }


            private void getFollowingCount(String stringFollowingLink) {
                final String[] size = new String[1];
                StringRequest stringRequest1Following = new StringRequest(Request.Method.GET, stringFollowingLink, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            size[0] = String.valueOf(jsonArray.length());
                            stringFollowingCOunt = size[0];

                            textViewFollowingCount.setText(size[0]);

                        } catch (JSONException e) {
                            progressBarFollower.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarFollower.setVisibility(View.GONE);
                    }
                });
                mRequestQueue.add(stringRequest1Following);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarFollower.setVisibility(View.GONE);
                progressBarFollowing.setVisibility(View.GONE);
                progressBarRepoCount.setVisibility(View.GONE);
            }
        });
        mRequestQueue.add(stringRequest);
    }

    //id for all the widgets are set here
    private void setIdForAllWidgets(View view) {

        circleImageViewUserProfile = view.findViewById(R.id.circle_image_view_profile_tab);
        textViewUserName = view.findViewById(R.id.tv_name_user_profile_tab);
        textViewFollowerCount = view.findViewById(R.id.text_view_follower_count_profile_tab);
        textViewFollowingCount = view.findViewById(R.id.text_view_following_count_profile_tab);
        textViewRepositoryCount = view.findViewById(R.id.text_view_repository_count_profile_tab);
        textViewViewMore= view.findViewById(R.id.text_view_view_more_profile_tab_fragment);

        progressBarFollower = view.findViewById(R.id.progress_bar_follower_fragment_tab_profile);
        progressBarFollowing = view.findViewById(R.id.progress_bar_followings_fragment_tab_profile);
        progressBarRepoCount = view.findViewById(R.id.progress_bar_repo_count_fragment_tab_profile);

        recyclerView = view.findViewById(R.id.recycler_view_profile_tab_fragment);

        linearLayoutProgressBar = view.findViewById(R.id.linear_layout_progress_bar_tab_profile_fragment);
    }

    @Override
    public void onClick(View v) {
        new TabRepositoryFragment(mapUserData);
    }
}
