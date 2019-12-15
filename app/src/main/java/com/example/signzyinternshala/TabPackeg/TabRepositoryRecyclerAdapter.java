package com.example.signzyinternshala.TabPackeg;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signzyinternshala.Model.UserModelRepositoryData;
import com.example.signzyinternshala.R;

import java.util.List;

public class TabRepositoryRecyclerAdapter extends RecyclerView.Adapter<TabRepositoryRecyclerAdapter.TabRepositoryHolder> {

    private List<UserModelRepositoryData> listUserModelRepository;
    private Context context;

    public TabRepositoryRecyclerAdapter(List<UserModelRepositoryData> listUserModelRepository, Context context) {
        this.listUserModelRepository = listUserModelRepository;
        this.context = context;
    }

    @NonNull
    @Override
    public TabRepositoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_repository_tab, parent, false);
        return new TabRepositoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TabRepositoryHolder holder, int position) {
        holder.setData(listUserModelRepository.get(position).getRepositoryName(), listUserModelRepository.get(position).getStringLanguage(),
                listUserModelRepository.get(position).getStringScore(), listUserModelRepository.get(position).getStringWatcherCount(),
                listUserModelRepository.get(position).getStringOpenIssues(), listUserModelRepository.get(position).getStringForkCount());
    }

    @Override
    public int getItemCount() {
        int size;
        if (listUserModelRepository.size() < 6 || listUserModelRepository.size() > 6) {
            size = listUserModelRepository.size();
        } else {
            size = 6;
        }
        return size;
    }

    class TabRepositoryHolder extends RecyclerView.ViewHolder {

        public TextView textViewRepoName, textViewLanguage, textViewScore, textViewWatcherCount, textViewOpenIssue, textViewForks;

        public TabRepositoryHolder(@NonNull View itemView) {
            super(itemView);
            setIdForAllWidgets(itemView);
            if(getItemCount()==6){
                textViewRepoName.setTextSize(14);
            }

        }

        private void setIdForAllWidgets(View itemView) {
            textViewRepoName = itemView.findViewById(R.id.text_view_repository_name_item_recycler_repository_tab);
            textViewLanguage = itemView.findViewById(R.id.text_view_language_item_recycler_repository_tab);
            textViewScore = itemView.findViewById(R.id.text_view_score_count_item_recycler_repository_tab);
            textViewWatcherCount = itemView.findViewById(R.id.text_view_watcher_count_item_recycler_repository_tab);
            textViewOpenIssue = itemView.findViewById(R.id.text_view_open_issue_item_recycler_repository_tab);
            textViewForks = itemView.findViewById(R.id.text_view_forks_item_recycler_repository_tab);
        }

        public void setData(String repositoryName, String stringLanguage, String stringScore, String stringWatcherCount, String stringOpenIssues, String stringForkCount) {
            textViewRepoName.setText(repositoryName);
            textViewLanguage.setText(stringLanguage);
            textViewScore.setText(stringScore);
            textViewWatcherCount.setText(stringWatcherCount);
            textViewOpenIssue.setText(stringOpenIssues);
            textViewForks.setText(stringForkCount);
        }
    }
}
