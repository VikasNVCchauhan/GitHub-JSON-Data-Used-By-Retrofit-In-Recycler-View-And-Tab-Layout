package com.example.signzyinternshala.TabPackeg;


import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signzyinternshala.InternetChecking.NetworkStateChangeReciever;
import com.example.signzyinternshala.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabProfileFragment extends Fragment {


    private AppCompatActivity appCompatActivity;
    private ProgressBar progressBarFollower, progressBarFollowing, progressBarRepoCount;
    private String stringProfileUrl, stringUserName, stringUserPositionInJson, stringFollowerCount, stringFollowingCOunt;
    private CircleImageView circleImageViewUserProfile;
    private Map<String, String> mapUserData;
    private TextView textViewUserName, textViewFollowerCount, textViewFollowingCount, textViewRepositoryCount;

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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_profile, container, false);
        appCompatActivity = (AppCompatActivity) view.getContext();
        //internet checking
        networkStateChangeReciever = new NetworkStateChangeReciever();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        appCompatActivity.registerReceiver(networkStateChangeReciever, intentFilter);

        setIdForAllWidgets(view);


        setData();
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

        CountDownTimer countDownTimer = new CountDownTimer(1000, 100) {
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

            private void getFollowerCount( String stringFollowerLink) {
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

    private void setIdForAllWidgets(View view) {
        circleImageViewUserProfile = view.findViewById(R.id.circle_image_view_profile_tab);
        textViewUserName = view.findViewById(R.id.tv_name_user_profile_tab);
        textViewFollowerCount = view.findViewById(R.id.text_view_follower_count_profile_tab);
        textViewFollowingCount = view.findViewById(R.id.text_view_following_count_profile_tab);
        textViewRepositoryCount = view.findViewById(R.id.text_view_repository_count_profile_tab);

        progressBarFollower = view.findViewById(R.id.progress_bar_follower_fragment_tab_profile);
        progressBarFollowing = view.findViewById(R.id.progress_bar_followings_fragment_tab_profile);
        progressBarRepoCount=view.findViewById(R.id.progress_bar_repo_count_fragment_tab_profile);
    }
}
