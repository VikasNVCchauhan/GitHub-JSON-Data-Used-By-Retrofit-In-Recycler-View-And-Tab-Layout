package com.example.signzyinternshala.TabPackeg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.signzyinternshala.MainActivity;
import com.example.signzyinternshala.R;

public class CustomDialogFilterBaseAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private Context context;
    private String[] stringsArray;

    public CustomDialogFilterBaseAdapter(Context context, String[] stringsArray) {
        this.context = context;
        this.stringsArray = stringsArray;
    }

    @Override
    public int getCount() {
        return stringsArray.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CheckBox checkBox;
        convertView = LayoutInflater.from(context).inflate(R.layout.filter_by_language_dialog_data, parent, false);

        checkBox = convertView.findViewById(R.id.checkbox_filter_by_language);
        checkBox.setTag(position);
        checkBox.setText(stringsArray[position]);

        checkBox.setOnCheckedChangeListener(this);
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int position = (int) buttonView.getTag();
        if (TabRepositoryFragment.arrayListFilterUserSelection.contains(stringsArray[position])) {
            TabRepositoryFragment.arrayListFilterUserSelection.remove(stringsArray[position]);
        } else {
            TabRepositoryFragment.arrayListFilterUserSelection.add(stringsArray[position]);
        }
    }
}
