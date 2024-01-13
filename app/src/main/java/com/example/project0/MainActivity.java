package com.example.project0;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextArg1, editTextArg2;
    private TextView labelName, labelSum, labelNone, labelZero;
    private Button btnAdd, btnDisplay, btnClear;
    private ArrayList<String> dataList;
    private ArrayAdapter<String> listAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextArg1 = findViewById(R.id.editTextArg1);
        editTextArg2 = findViewById(R.id.editTextArg2);

        labelName = findViewById(R.id.labelName);
        labelSum = findViewById(R.id.labelSum);
        labelNone = findViewById(R.id.labelNone);
        labelZero = findViewById(R.id.labelZero);

        btnAdd = findViewById(R.id.btnAdd);
        btnDisplay = findViewById(R.id.btnDisplay);
        btnClear = findViewById(R.id.btnClear);

        NotificationHelper.createNotificationChannel(this);

        dataList = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);

        loadData();

        // Set up the ListView
        ListView listView = new ListView(this);
        listView.setAdapter(listAdapter);

        // Handle item click on the ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = listAdapter.getItem(position);
                displayToast(selectedItem);
            }
        });

        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the data to the ListActivity
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putStringArrayListExtra("dataList", dataList);

                startActivity(intent);
            }
        });

        // Button click listeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList();
            }
        });


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearList();
            }
        });
    }

    private void addToList() {
        String name = editTextName.getText().toString();
        String arg1 = editTextArg1.getText().toString();
        String arg2 = editTextArg2.getText().toString();

        if (!name.isEmpty() && !arg1.isEmpty() && !arg2.isEmpty()) {
            // Add entry to the list
            double sum = Double.parseDouble(arg1) + Double.parseDouble(arg2);
            String formattedSum = (sum % 1 == 0) ? String.valueOf((int) sum) : String.valueOf(sum);
            String entry = "Name: " + name + ", Sum: " + formattedSum;
            dataList.add(entry);

            // Update ListView
            listAdapter.notifyDataSetChanged();
            String lastItem = dataList.get(dataList.size() - 1);
            displayLastItem(lastItem);

            editTextName.getText().clear();
            editTextArg1.getText().clear();
            editTextArg2.getText().clear();

            saveData();
        } else {
            Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }


    private void displayLastItem(String lastItem) {
        if (dataList.isEmpty()) {
            labelNone.setText("None");
            labelZero.setText("0");
        } else {
            String[] parts = lastItem.split(", ");
            if (parts.length == 2) {
                String[] nameParts = parts[0].split(": ");
                String[] sumParts = parts[1].split(": ");

                if (nameParts.length == 2 && sumParts.length == 2) {
                    labelNone.setText(nameParts[1]);
                    labelZero.setText(sumParts[1]);
                }
            }
        }
    }



    private void clearList() {
        dataList.clear();
        labelNone.setText("None");
        labelZero.setText("0");
        listAdapter.notifyDataSetChanged();
        saveData();
        Toast.makeText(MainActivity.this, "List cleared", Toast.LENGTH_SHORT).show();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> dataSet = new HashSet<>(dataList);
        editor.putStringSet("dataList", dataSet);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        Set<String> dataSet = sharedPreferences.getStringSet("dataList", new HashSet<String>());
        dataList = new ArrayList<>(dataSet);
        if (!dataList.isEmpty()) {
            String lastItem = dataList.get(dataList.size() - 1);
            displayLastItem(lastItem);
        }
        listAdapter.addAll(dataList);
    }

    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        StopCounter.incrementStopCounter();
        NotificationHelper.displayNotification(this, "Stops", "Number of stops: " + StopCounter.getStopCounter(), StopCounter.getStopCounter());

        saveData();
    }

}