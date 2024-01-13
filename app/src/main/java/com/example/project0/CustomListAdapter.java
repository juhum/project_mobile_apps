package com.example.project0;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> {

    public CustomListAdapter(Context context, List<String> dataList) {
        super(context, 0, dataList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_item, parent, false);
        }

        String data = getItem(position);

        if (data != null) {
            TextView textViewName = convertView.findViewById(R.id.textViewName);
            TextView textViewSum = convertView.findViewById(R.id.textViewSum);

            String[] parts = data.split(" ");

            if (parts.length >= 2) {
                String nameWithoutLastChar = parts[1].substring(0, parts[1].length() - 1);
                textViewName.setText(nameWithoutLastChar);
                textViewSum.setText(parts[3]);
            }
        }

        return convertView;
    }
}
