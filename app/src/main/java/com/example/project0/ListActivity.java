package com.example.project0;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> dataList;

    private int stopCounter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.listView);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("dataList")) {
            dataList = intent.getStringArrayListExtra("dataList");
        } else {
            dataList = new ArrayList<>();
        }

        stopCounter = intent.getIntExtra("stopCounter", 0);

        // Set up the adapter
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);

        CustomListAdapter customListAdapter = new CustomListAdapter(this, dataList);

        // Set the adapter to the ListView
        listView.setAdapter(customListAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = listAdapter.getItem(position);
            displayToast(selectedItem);
        });
    }

    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        StopCounter.incrementStopCounter();
        NotificationHelper.displayNotification(this, "Stops", "Number of stops: " + StopCounter.getStopCounter(), StopCounter.getStopCounter());
    }


}

