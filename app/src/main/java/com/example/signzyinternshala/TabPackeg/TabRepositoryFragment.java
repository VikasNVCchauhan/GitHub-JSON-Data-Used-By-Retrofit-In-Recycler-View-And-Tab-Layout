package com.example.signzyinternshala.TabPackeg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TabRepositoryFragment extends Fragment implements View.OnClickListener {


    //widgets
    private RecyclerView recyclerView;
    private AppCompatActivity appCompatActivity;
    private LinearLayout linearLayoutProgressBar, linearLayoutFloatingButtonFilter, linearLayoutFloatingButtonRemoveFilter, linearLayoutListViewFilterDialog;
    private ListView listView;
    private TextView textViewCancelDialog, textViewContinueFilter, textViewOkLanguageNotAvailable;
    //variables
    private Map<String, String> mapUserData;
    private TabRepositoryRecyclerAdapter tabRepositoryRecyclerAdapter;
    private List<UserModelRepositoryData> listUserModelRepositoryData;
    private String repositoryName, stringLanguage, stringScore, stringWatcherCount, stringOpenIssues, stringForkCount;
    //filter Variable
    private AlertDialog alertDialog;
    private String[] stringArray;
    private String[] languageFilter;
    private String[] languageStringArray;
    private ArrayList<String> languageStringArrayList;
    private List<UserModelRepositoryData> listUserModelRepositoryDataLanguageFilter;
    public static ArrayList<String> arrayListFilterUserSelection;
    private boolean filterSelected = false;

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

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_repository, container, false);
        appCompatActivity = (AppCompatActivity) view.getContext();

        arrayListFilterUserSelection = new ArrayList<>();
        languageStringArrayList = new ArrayList<>();
        listUserModelRepositoryDataLanguageFilter = new ArrayList<>();

        stringArray = appCompatActivity.getResources().getStringArray(R.array.customDialogFilterArry);
        languageFilter = appCompatActivity.getResources().getStringArray(R.array.filterLanguageArray);
        languageStringArray = appCompatActivity.getResources().getStringArray(R.array.languagesArray); //only languages here
        for (int i = 0; i < languageStringArray.length; i++) {
            languageStringArrayList.add(languageStringArray[i]);
        }
        //internet checking
        networkStateChangeReciever = new NetworkStateChangeReciever();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        appCompatActivity.registerReceiver(networkStateChangeReciever, intentFilter);

        setIdForAllWidgets(view);

        checkRemovalFloatingButtonVisibility();
        listUserModelRepositoryData = new ArrayList<>();
        stringRepoUrl = mapUserData.get("REPO_LINK");
        setDataToList();
        recyclerView.setLayoutManager(new LinearLayoutManager(appCompatActivity));

        linearLayoutFloatingButtonFilter.setOnClickListener(this);
        linearLayoutFloatingButtonRemoveFilter.setOnClickListener(this);
        return view;
    }

    private void checkRemovalFloatingButtonVisibility() {
        if (filterSelected) {
            linearLayoutFloatingButtonRemoveFilter.setVisibility(View.VISIBLE);
        } else {
            linearLayoutFloatingButtonRemoveFilter.setVisibility(View.INVISIBLE);
        }
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
                    setDataToRecyclerView(listUserModelRepositoryData);
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

    private void setDataToRecyclerView(List<UserModelRepositoryData> listUserModelRepositoryData) {
        tabRepositoryRecyclerAdapter = new TabRepositoryRecyclerAdapter(listUserModelRepositoryData, appCompatActivity);
        recyclerView.setAdapter(tabRepositoryRecyclerAdapter);
        tabRepositoryRecyclerAdapter.notifyDataSetChanged();
    }


    private void setIdForAllWidgets(View view) {

        linearLayoutFloatingButtonFilter = view.findViewById(R.id.linear_layout_floating_button_filter);
        linearLayoutFloatingButtonRemoveFilter = view.findViewById(R.id.linear_layout_floating_button_remove_filter);
        linearLayoutProgressBar = view.findViewById(R.id.linear_layout_progress_bar_repository_fragment);
        recyclerView = view.findViewById(R.id.recycler_view_repository_tab);
    }

    @Override
    public void onClick(View v) {
        if (v == linearLayoutFloatingButtonFilter) {
            View view = LayoutInflater.from(appCompatActivity).inflate(R.layout.custom_filter_dialog, null);

            setIdForAllWidgetsOfDialog(view);
            AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
            builder.setView(view);

            alertDialog = builder.show();

            alertDialog.setCancelable(false);
            setDialogBackgroundTranperent();


            callBaseAdapter(stringArray);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            textViewCancelDialog.setOnClickListener(this);
            textViewContinueFilter.setOnClickListener(this);

        } else if (v == textViewCancelDialog) {
            arrayListFilterUserSelection.clear();
            filterSelected = false;
            checkRemovalFloatingButtonVisibility();
            alertDialog.dismiss();

        } else if (v == textViewContinueFilter) {

            textViewContinueFilter.setTextColor(Color.WHITE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewContinueFilter.setTextColor(getResources().getColor(R.color.orange));
                    onClickContinueActions();
                }
            }, 200);
        } else if (v == linearLayoutFloatingButtonRemoveFilter) {
            filterSelected = false;
            checkRemovalFloatingButtonVisibility();
            setDataToRecyclerView(listUserModelRepositoryData);
        } else if (v == textViewOkLanguageNotAvailable) {
            alertDialog.dismiss();
        }

    }

    private void onClickContinueActions() {
        if (arrayListFilterUserSelection.size() != 0) {
            filterSelected = true;
            checkRemovalFloatingButtonVisibility();
            if (arrayListFilterUserSelection.size() > 1) {
                setBottomMargin();
                arrayListFilterUserSelection.remove(stringArray[0]);
                allFilterApplied();
            } else {
                if (arrayListFilterUserSelection.contains(stringArray[0])) {
                    setBottomMargin();
                    arrayListFilterUserSelection.remove(stringArray[0]);
                    callBaseAdapter(languageFilter);
                    setFilterOFLanguagesOnly();
                } else {
                    arrayListFilterUserSelection.clear();
                    setFilterAction();
                }
            }
        } else {
            Toast.makeText(appCompatActivity, "Please select filter to continue", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBottomMargin() {
        ViewGroup.LayoutParams params = linearLayoutListViewFilterDialog.getLayoutParams();
        params.height = 1000;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        ;
        listView.setLayoutParams(params);
    }

    private void setFilterOFLanguagesOnly() {
        textViewContinueFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (arrayListFilterUserSelection.size() != 0) {
                    setAllFilterWithLanguages();
                    setDataToRecyclerView(listUserModelRepositoryDataLanguageFilter);
                    arrayListFilterUserSelection.clear();
                } else {
                    alertDialog.dismiss();
                    filterSelected = false;
                    checkRemovalFloatingButtonVisibility();
                    customDialogLanguageNotAvailable();
                }
            }
        });
    }

    private void applyAscendingOrderFilter() {
        if (listUserModelRepositoryDataLanguageFilter.size() != 0) {
            Comparator<UserModelRepositoryData> compareById = new Comparator<UserModelRepositoryData>() {
                @Override
                public int compare(UserModelRepositoryData o1, UserModelRepositoryData o2) {
                    return Integer.valueOf(o1.getStringScore()).compareTo(Integer.valueOf(o2.getStringScore()));
                }
            };
            Collections.sort(listUserModelRepositoryDataLanguageFilter, compareById);
            alertDialog.dismiss();
            setDataToRecyclerView(listUserModelRepositoryDataLanguageFilter);
        } else {
            alertDialog.dismiss();
            filterSelected = false;
            checkRemovalFloatingButtonVisibility();
            customDialogLanguageNotAvailable();
        }
    }

    private void customDialogLanguageNotAvailable() {
        View view = LayoutInflater.from(appCompatActivity).inflate(R.layout.custom_dialog_filter_null_response, null);
        textViewOkLanguageNotAvailable = view.findViewById(R.id.text_view_ok_alert_dialog_language_not_available);

        AlertDialog.Builder builder = new AlertDialog.Builder(appCompatActivity);
        builder.setView(view);

        alertDialog = builder.show();
        alertDialog.setCancelable(false);
        setDialogBackgroundTranperent();

        textViewOkLanguageNotAvailable.setOnClickListener(this);
    }

    private void setDialogBackgroundTranperent() {
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 40);
        alertDialog.getWindow().setBackgroundDrawable(inset);

    }

    private void allFilterApplied() {
        callBaseAdapter(languageFilter);
        List<UserModelRepositoryData> tempListUserModelRepositoryData;
        textViewContinueFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (arrayListFilterUserSelection.size() != 1) {
                    setAllFilterWithLanguages();
                    applyAscendingOrderFilter();
                    arrayListFilterUserSelection.clear();
                } else {
                    setFilterAction();
                }
            }
        });
    }

    private void setAllFilterWithLanguages() {
        ArrayList<String> languages = new ArrayList<>();
        for (int i = 0; i < languageFilter.length; i++) {
            if (arrayListFilterUserSelection.contains(languageFilter[i])) {
                languages.add(languageStringArrayList.get(i));
            }
        }
        for (int i = 0; i < listUserModelRepositoryData.size(); i++) {
            for (int j = 0; j < languages.size(); j++) {
                if (listUserModelRepositoryData.get(i).getStringLanguage().toLowerCase().contains(languages.get(j).toLowerCase())) {
                    setDataToLanguageList(i);
                    break;
                }
            }
        }
    }

    private void setDataToLanguageList(int i) {
        UserModelRepositoryData userModelRepositoryData = new UserModelRepositoryData();
        userModelRepositoryData.setRepositoryName(listUserModelRepositoryData.get(i).getRepositoryName());
        userModelRepositoryData.setStringLanguage(listUserModelRepositoryData.get(i).getStringLanguage());
        userModelRepositoryData.setStringScore(listUserModelRepositoryData.get(i).getStringScore());
        userModelRepositoryData.setStringWatcherCount(listUserModelRepositoryData.get(i).getStringWatcherCount());
        userModelRepositoryData.setStringForkCount(listUserModelRepositoryData.get(i).getStringForkCount());
        userModelRepositoryData.setStringOpenIssues(listUserModelRepositoryData.get(i).getStringOpenIssues());

        listUserModelRepositoryDataLanguageFilter.add(userModelRepositoryData);
    }

    private void setFilterAction() {
        Comparator<UserModelRepositoryData> compareById = new Comparator<UserModelRepositoryData>() {
            @Override
            public int compare(UserModelRepositoryData o1, UserModelRepositoryData o2) {
                return Integer.valueOf(o1.getStringScore()).compareTo(Integer.valueOf(o2.getStringScore()));
            }
        };
        Collections.sort(listUserModelRepositoryData, compareById);
        alertDialog.dismiss();
        setDataToRecyclerView(listUserModelRepositoryData);
    }

    private void callBaseAdapter(String[] filterArray) {
        CustomDialogFilterBaseAdapter customDialogFilterBaseAdapter = new CustomDialogFilterBaseAdapter(appCompatActivity, filterArray);
        listView.setAdapter(customDialogFilterBaseAdapter);
    }

    private void setIdForAllWidgetsOfDialog(View view) {

        linearLayoutListViewFilterDialog = view.findViewById(R.id.linear_layout_list_view_filter_custom_dialog);
        listView = view.findViewById(R.id.list_view_custom_filter_dialog);
        textViewCancelDialog = view.findViewById(R.id.text_view_cancel_custom_dialog_filter_repo);
        textViewContinueFilter = view.findViewById(R.id.text_view_continue_custom_dialog_filter_repo);
    }
}
