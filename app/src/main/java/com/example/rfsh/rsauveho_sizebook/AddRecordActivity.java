package com.example.rfsh.rsauveho_sizebook;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddRecordActivity extends AppCompatActivity {

    private static final String FILENAME = "size_book_saves.sav";

    Record currRecord;

    EditText[] fields;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_record);

        fields = new EditText[]{
                (EditText) findViewById(R.id.name_field),
                (EditText) findViewById(R.id.date_field),
                (EditText) findViewById(R.id.neck_field),
                (EditText) findViewById(R.id.bust_field),
                (EditText) findViewById(R.id.chest_field),
                (EditText) findViewById(R.id.waist_field),
                (EditText) findViewById(R.id.hip_field),
                (EditText) findViewById(R.id.inseam_field),
                (EditText) findViewById(R.id.comment_field)
        };

        EditTextDatePicker date = new EditTextDatePicker(this, R.id.date_field);


    }

    @Override
    public void onStart(){
        super.onStart();

        currRecord =  (Record) getIntent().getSerializableExtra("SELECTED_RECORD");

        String mode = getIntent().getStringExtra("EXTRA_MODE");

        // Apply Date filter to date_field
//        if (fields[1] != null) {
//            fields[1].setFilters(new InputFilter[]{new DateFilter()});
//        }
        /**
         * Apply decimal filter to number fields,
         * digitsBeforeZero set to 4, since realistically no size should need even close
         *   to this many digits before for any measurement
         * digitsAfterZero set to 1
         */
        for (int i = 2; i < 8; i++) {
            if (fields[i] != null) {
                fields[i].setFilters(new InputFilter[]{new DecimalInputFilter(4, 1)});
            }
        }

        if (mode.equals("v") || mode.equals("e")) {
            fields[0].setText(currRecord.getName());
            fields[1].setText(currRecord.getDate() == null ? "" : simpleDateFormat.format(currRecord.getDate()));
            fields[2].setText(currRecord.getNeck() < 0 ? "" : String.format("%d", currRecord.getNeck()));
            fields[3].setText(currRecord.getBust() < 0 ? "" : String.format("%d", currRecord.getBust()));
            fields[4].setText(currRecord.getChest() < 0 ? "" : String.format("%d", currRecord.getChest()));
            fields[5].setText(currRecord.getWaist() < 0 ? "" : String.format("%d", currRecord.getWaist()));
            fields[6].setText(currRecord.getHip() < 0 ? "" : String.format("%d", currRecord.getHip()));
            fields[7].setText(currRecord.getInseam() < 0 ? "" :String.format("%d", currRecord.getInseam()));
            fields[8].setText(currRecord.getComment());
        }
        else if (mode.equals("v")) {
            for (EditText E : fields) {
                if (E != null) {
                    E.setEnabled(false);
                    E.setFocusable(false);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            for (EditText E : fields) {
                if (E != null) {
                    E.setEnabled(true);
                    E.setFocusable(true);
                }
            }
        }
        else if (id == R.id.action_save){
            Record record = new Record( fields[0].getText().toString());
            String date_str = fields[1].getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                record.setDate(date_str.equals("") ? new Date() : sdf.parse(date_str));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String neck = fields[2].getText().toString();
            record.setNeck(neck.equals("") ? -1 : Integer.parseInt(neck));
            String bust = fields[3].getText().toString();
            record.setBust(bust.equals("") ? -1 : Integer.parseInt(bust));
            String chest = fields[4].getText().toString();
            record.setChest(chest.equals("") ? -1 : Integer.parseInt(chest));
            String waist = fields[5].getText().toString();
            record.setWaist(waist.equals("") ? -1 : Integer.parseInt(waist));
            String hip = fields[6].getText().toString();
            record.setHip(hip.equals("") ? -1 :Integer.parseInt(hip));
            String inseam = fields[7].getText().toString();
            record.setInseam(inseam.equals("") ? -1 :Integer.parseInt(inseam));
            record.setComment(fields[8].getText().toString());

            Intent intent = new Intent(AddRecordActivity.this, MainActivity.class);

            intent.putExtra("RETURN_RECORD", record);
            if (getIntent().getStringExtra("EXTRA_MODE").equals("a")) {
                intent.putExtra("ACTION", "add");
            }
            else {
                intent.putExtra("ACTION", "replace");
                intent.putExtra("RETURN_RECORD_POS", getIntent().getIntExtra("SELECTED_RECORD_POS", 0));
            }
            setResult(RESULT_OK, intent);
            finish();

        }
        else if (id == R.id.action_delete) {
            Intent intent = new Intent(AddRecordActivity.this, MainActivity.class);

            intent.putExtra("ACTION", "delete");
            setResult(RESULT_OK, intent);
            finish();
        }
        // might not want to use this button
        else if (id == R.id.action_delete_all) {
            ArrayList<Record> recordList;
            try {
                FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

                Gson gson = new Gson();
                recordList = new ArrayList<Record>();
                gson.toJson(recordList, bufferedWriter);
                bufferedWriter.flush();

                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                // This is an exception that will not happen, given that recorList is initialized within the method
            } catch (IOException e) {
                throw new RuntimeException();
            }

            Intent intent = new Intent(AddRecordActivity.this, MainActivity.class);
            intent.putExtra("ACTION", "delete");
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
