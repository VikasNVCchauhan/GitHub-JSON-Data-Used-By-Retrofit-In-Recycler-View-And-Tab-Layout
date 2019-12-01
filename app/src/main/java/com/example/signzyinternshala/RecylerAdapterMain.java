package com.example.signzyinternshala;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signzyinternshala.Model.UserModelProfileData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecylerAdapterMain extends RecyclerView.Adapter<RecylerAdapterMain.RecyclerHolder> {

    private onItemClickListener onItemClickListener;
    private List<UserModelProfileData> listUserModelProfileData;
    private Context context;

    public RecylerAdapterMain(List<UserModelProfileData> listUserModelProfileData, Context context) {
        this.listUserModelProfileData = listUserModelProfileData;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_acticity_main, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.setData(listUserModelProfileData.get(position).getStringUserProfileURl(), listUserModelProfileData.get(position).getStringUserName(), listUserModelProfileData.get(position).getStringRepositoryCount());
    }

    @Override
    public int getItemCount() {
        return listUserModelProfileData.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView circleImageView;
        TextView textViewName, textViewRepoCount;
        LinearLayout linearLayout;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            setIdForAllWidgets(itemView);
        }

        private void setIdForAllWidgets(View itemView) {
            circleImageView = itemView.findViewById(R.id.circle_image_view_user_profile_item_recycler_main);
            textViewName = itemView.findViewById(R.id.tv_user_name_item_recycler_view_activity);
            textViewRepoCount = itemView.findViewById(R.id.tv_repo_number_item_recycler_view_activity);
            linearLayout = itemView.findViewById(R.id.linear_layout_recycler_view_item_activity_main);

            linearLayout.setOnClickListener(this);
        }

        private void setData(String url, String userName, String repoCount) {
            Picasso.with(context).load(url).placeholder(R.drawable.button_background)
                    .centerCrop()
                    .fit()
                    .into(circleImageView);
            textViewName.setText(userName);
            textViewRepoCount.setText(repoCount);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(position);
            }
        }
    }

    public void filteredList(ArrayList<UserModelProfileData> arrayList) {
        listUserModelProfileData = arrayList;
        notifyDataSetChanged();
    }

    public interface onItemClickListener {
        public void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
