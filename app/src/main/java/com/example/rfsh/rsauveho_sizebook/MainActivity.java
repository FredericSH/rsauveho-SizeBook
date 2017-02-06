package com.example.rfsh.rsauveho_sizebook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "size_book_saves.sav";
    static final int REQUEST_CODE = 1;

    private ListView storedRecordList;

    private ArrayList<Record> recordList;
    private ArrayAdapter<Record> adapter;

    private TextView counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        counter = (TextView) findViewById(R.id.counter);

        storedRecordList = (ListView) findViewById(R.id.recordList);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        /**
         * On item click, go into view mode for specific Record
         */
        storedRecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Record selected = (Record) (storedRecordList.getItemAtPosition(position));

                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);

                // mode of the addRecordActivity e: edit record a: add new record v: view record
                Log.i("GOT TO ", "ON CLICK");
                intent.putExtra("EXTRA_MODE", "v");
                intent.putExtra("SELECTED_RECORD_POS", position);
                intent.putExtra("SELECTED_RECORD", selected);

                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        /**
         * On item long click go into edit mode for specific Record
         */
        storedRecordList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Record selected = (Record) (storedRecordList.getItemAtPosition(position));

                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);

                Log.i("GOT TO ", "ON LONG CLICK");
                intent.putExtra("EXTRA_MODE", "e");
                intent.putExtra("SELECTED_RECORD_POS", position);
                intent.putExtra("SELECTED_RECORD", selected);

                startActivityForResult(intent, REQUEST_CODE);
                return true;
            }
        });

        /**
         * Floating action button adds a new Record
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);

                intent.putExtra("EXTRA_MODE", "a");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        loadRecords();

        adapter = new ArrayAdapter<Record>(this, R.layout.list_item, recordList);
        storedRecordList.setAdapter(adapter);

        updateCounter();
    }

    /**
     * Called when Activity is returned to with a result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Record passedRecord = (Record) data.getSerializableExtra("RETURN_RECORD");

                if (data.getStringExtra("ACTION").equals("add")){
                    recordList.add(passedRecord);
                }
                else if (data.getStringExtra("ACTION").equals("replace")){
                    recordList.set(data.getIntExtra("RETURN_RECORD_POS", 0), passedRecord);
                }
                else if (data.getStringExtra("ACTION").equals("delete")){
                    recordList.remove(data.getIntExtra("RETURN_RECORD_POS", 0));
                }
                adapter.notifyDataSetChanged();
                updateCounter();

                saveRecords();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * updates the counter for number of records
     */
    private void updateCounter() {
        counter.setText(getResources().getString(R.string.counter_text) + " " + recordList.size());

    }

    /**
     * Loads records from file
     */
    private void loadRecords() {
        try {
            FileInputStream fileInputStream = openFileInput(FILENAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Record>>(){}.getType();
            recordList = gson.fromJson(bufferedReader, listType);

        }
        catch (FileNotFoundException e) {
            recordList = new ArrayList<Record>();
        }
    }

    /**
     * Save records to file
     */
    private void saveRecords() {
        try {
            FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            Gson gson = new Gson();
            gson.toJson(recordList, bufferedWriter);
            bufferedWriter.flush();

            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            recordList = new ArrayList<Record>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
