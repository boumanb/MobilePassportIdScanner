package com.b3.idscannerb3.Controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.b3.idscannerb3.R;

import java.util.ArrayList;

import Models.Adapters.CustomScannedListAdapter;
import Models.Db.DbHelper;
import Models.Entities.MRTDRecord;

public class ScannedListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView mScannedListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mScannedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_list);

        DbHelper dbHelper = new DbHelper(this);

        ArrayList<MRTDRecord> scannedList = dbHelper.getAllMrtdRecords();

        for (MRTDRecord record : scannedList){
            Log.i("SCANNED LIST", record.getFirstName());
        }

        int count = dbHelper.getMrtdRecordsCount();

        ListView mScannedListView = (ListView) findViewById(R.id.scannedListView);

        TextView mScannedCountTextView = (TextView) findViewById(R.id.scannedCount);

        mScannedCountTextView.setText("Aantal gescand: " + count);

        //mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, scannedList);
        mArrayAdapter = new CustomScannedListAdapter(this, R.layout.scanned_list_row, scannedList);
        mScannedListView.setAdapter(mArrayAdapter);

        mArrayAdapter.notifyDataSetChanged();

        mScannedListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
        final Intent i = new Intent(this, PersonActivity.class);

        final MRTDRecord mrtdRecord = (MRTDRecord) parent.getItemAtPosition(position);

        CharSequence colors[] = new CharSequence[] {"Bekijken", "Verwijderen"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opties");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        i.putExtra("record", mrtdRecord);
                        i.putExtra("INTENTFROM", "ScannedListActivity");

                        startActivity(i);
                        break;
                    case 1:
                        DbHelper dbHelper = new DbHelper(getApplicationContext());

                        dbHelper.deleteMrtdRecord(mrtdRecord);
                        int count = dbHelper.getMrtdRecordsCount();
                        mArrayAdapter.remove(mrtdRecord);
                        mArrayAdapter.notifyDataSetChanged();
                        TextView mScannedCountTextView = (TextView) findViewById(R.id.scannedCount);
                        mScannedCountTextView.setText("Gescande paspoorten: " + count);
                        break;
                }
            }
        });
        builder.show();



    }
}
