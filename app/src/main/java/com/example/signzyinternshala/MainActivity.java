package com.example.signzyinternshala;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signzyinternshala.InternetChecking.NetworkStateChangeReciever;
import com.example.signzyinternshala.Model.UserModelProfileData;
import com.example.signzyinternshala.TabPackeg.TabClass;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, RecylerAdapterMain.onItemClickListener {

    //Widgets
    private RecyclerView recyclerViewUserDetail;
    private LinearLayout linearLayoutSearch;
    private RecylerAdapterMain recylerAdapterMain;
    private EditText editTextSearch;
    private LinearLayout linearLayoutProgressBar;
    //Variables
    private int ITEM_SEARCH = 0;
    private static final String TAG = "MainActivity";
    private List<UserModelProfileData> list;
    private List<UserModelProfileData> listUserModelProfileData;
    private List<UserModelProfileData> listUserModelProfileDataNameAndProfileUrl;
    //Json Query
    private RequestQueue mRequestQueue;
    private static final String jsonUrl = "https://api.github.com/search/users?q=language:java+location:lagos";
    private String stringUserName, stringRepositoryCount, stringUserProfileURl, stringActualCount;
    //Internet connectivity
    private NetworkStateChangeReciever networkStateChangeReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //internet checking
        networkStateChangeReciever = new NetworkStateChangeReciever();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(networkStateChangeReciever, intentFilter);
        //Id are set here
        setIdForAllWidgets();
        linearLayoutProgressBar.setVisibility(View.VISIBLE);
        listUserModelProfileData = new ArrayList<>();
        listUserModelProfileDataNameAndProfileUrl = new ArrayList<>();
        list = new ArrayList<>();
        recyclerViewUserDetail.setLayoutManager(new LinearLayoutManager(this));
        setDataInList();
        //Listeners are set here
        linearLayoutSearch.setOnClickListener(this);
        recyclerViewUserDetail.setOnClickListener(this);
        editTextSearch.addTextChangedListener(this);
    }

    private void setAlertDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Please check your Internet connection")
                .setIcon(R.drawable.ic_signal_cellular_connected_no_internet_0_bar_black_24dp)
                .setTitle("Internet Alert")
                .setPositiveButton("Retry ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .show();

    }

    private void setDataInList() {
        mRequestQueue = Volley.newRequestQueue(this);
        jsonPars();
    }

    private void jsonPars() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    String[] arr = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        stringUserName = object.getString("login");
                        stringActualCount = String.valueOf(i);
                        stringUserProfileURl = object.getString("avatar_url");
                        arr[i] = object.getString("repos_url");

                        listUserModelProfileDataNameAndProfileUrl.add(new UserModelProfileData(stringUserName, stringUserProfileURl, stringActualCount, arr[i]));
                    }


                    for (int i = 0; i < arr.length; i++) {

                        final int position = i;
                        RequestQueue requestQueue;
                        requestQueue = Volley.newRequestQueue(MainActivity.this);
                        StringRequest request = new StringRequest(Request.Method.GET, arr[position], new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray jArray = new JSONArray(response);
                                    stringRepositoryCount = String.valueOf(jArray.length());
                                    listUserModelProfileData.add(new UserModelProfileData(listUserModelProfileDataNameAndProfileUrl.get(position).getStringUserName(),
                                            stringRepositoryCount,
                                            listUserModelProfileDataNameAndProfileUrl.get(position).getStringUserProfileURl(),
                                            listUserModelProfileDataNameAndProfileUrl.get(position).getStringActualPosition(),
                                            listUserModelProfileDataNameAndProfileUrl.get(position).getStringRepoLink()));
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
                        requestQueue.add(request);
                    }
                    CountDownTimer countDownTimer = new CountDownTimer(1000, 100) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            System.out.println();
                            linearLayoutProgressBar.setVisibility(View.INVISIBLE);
                            recylerAdapterMain = new RecylerAdapterMain(listUserModelProfileData, MainActivity.this);
                            recyclerViewUserDetail.setAdapter(recylerAdapterMain);
                            recylerAdapterMain.notifyDataSetChanged();
                            recylerAdapterMain.setOnItemClickListener(MainActivity.this);
                        }
                    };
                    countDownTimer.start();

                } catch (JSONException e) {
                    linearLayoutProgressBar.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error in Json Data fetching ");
            }
        });
        mRequestQueue.add(stringRequest);
    }

    private void setIdForAllWidgets() {
        recyclerViewUserDetail = findViewById(R.id.recycler_view_git_all_user_activity_main);
        linearLayoutSearch = findViewById(R.id.linear_layout_search_item_customized_search_bar);
        editTextSearch = findViewById(R.id.searchBar_search_activity);
        linearLayoutProgressBar = findViewById(R.id.progress_bar_activity_main);
    }

    @Override
    public void onClick(View view) {
        if (view == linearLayoutSearch) {
            linearLayoutSearch.setVisibility(View.INVISIBLE);
            editTextSearch.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!editTextSearch.getText().equals("")) {
            ITEM_SEARCH = 1;
            editTextSearch.setVisibility(View.VISIBLE);
        } else {
            ITEM_SEARCH = 0;
            editTextSearch.setVisibility(View.INVISIBLE);
        }
        filter(editable.toString());
    }

    private void filter(String toString) {
        ArrayList<UserModelProfileData> arrayList = new ArrayList<>();
        for (UserModelProfileData userModelData : listUserModelProfileData) {
            if (checkDataInRecycle(userModelData, toString)) {
                arrayList.add(userModelData);
            }
        }
        if (!editTextSearch.getText().equals("")) {
            list = arrayList;
            ITEM_SEARCH = 1;
        }
        recylerAdapterMain.filteredList(arrayList);
    }

    private boolean checkDataInRecycle(UserModelProfileData userModelData, String toString) {
        boolean returnType;
        if (userModelData.getStringUserName().toLowerCase().contains(toString.toLowerCase())) {
            returnType = true;
        } else {
            returnType = false;
        }
        return returnType;
    }

    @Override
    public void onItemClick(int position) {

        String ActualPosition;
        if (ITEM_SEARCH == 0) {
            list = listUserModelProfileData;
            ActualPosition = listUserModelProfileData.get(position).getStringActualPosition();
        } else {
            ActualPosition = list.get(position).getStringActualPosition();
        }

        listUserModelProfileData.get(position).getStringUserName();
        Intent intent = new Intent(MainActivity.this, TabClass.class);
        intent.putExtra("IMG_URL", list.get(position).getStringUserProfileURl());
        intent.putExtra("USER_NAME", list.get(position).getStringUserName());
        intent.putExtra("USER_POSITION_IN_JSON", ActualPosition);
        intent.putExtra("REPO_LINK", list.get(position).getStringRepoLink());
        intent.putExtra("REPO_COUNT", list.get(position).getStringRepositoryCount());
        startActivity(intent);

    }
}
