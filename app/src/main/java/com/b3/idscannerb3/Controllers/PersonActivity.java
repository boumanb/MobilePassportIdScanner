package com.b3.idscannerb3.Controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.b3.idscannerb3.R;
import com.microblink.activity.ScanCard;
import com.microblink.recognizers.BaseRecognitionResult;
import com.microblink.recognizers.RecognitionResults;
import com.microblink.recognizers.blinkid.mrtd.MRTDRecognitionResult;
import com.microblink.results.ocr.OcrResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import javax.xml.datatype.Duration;

import Models.Db.DbHelper;
import Models.Entities.MRTDRecord;

import static android.view.View.GONE;

public class PersonActivity extends AppCompatActivity {
    String intentFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        intentFrom = getIntent().getExtras().getString("INTENTFROM");

        if (intentFrom.equals("ScanActivity")) {
            // Get the Intent that started this activity and extract the string
            RecognitionResults results = getIntent().getParcelableExtra(ScanCard.EXTRAS_RECOGNITION_RESULTS);

            BaseRecognitionResult[] resultArray = results.getRecognitionResults();

            for (BaseRecognitionResult baseResult : resultArray) {
                if (baseResult instanceof MRTDRecognitionResult) {
                    MRTDRecognitionResult result = (MRTDRecognitionResult) baseResult;

                    // you can use getters of MRTDRecognitionResult class to
                    // obtain scanned information
                    if (result.isValid() && !result.isEmpty()) {
                        if (result.isMRZParsed()) {
                            String firstName = result.getSecondaryId();
                            String lastName = result.getPrimaryId();
                            String issuer = result.getIssuer();
                            String nationality = result.getNationality();
                            Date dateOfBirth = result.getDateOfBirth();
                            String documentNumber = result.getDocumentNumber();
                            String sex = result.getSex();
                            String documentCode = result.getDocumentCode();
                            Date dateOfExpiry = result.getDateOfExpiry();
                            String opt1 = result.getOpt1();
                            String opt2 = result.getOpt2();
                            String mrtText = result.getMRZText();


                            // Find the textviews to fill those with the object information
                            TextView firstNameTv = (TextView) findViewById(R.id.firstName);
                            TextView lastNameTv = (TextView) findViewById(R.id.lastName);
                            TextView issuerTv = (TextView) findViewById(R.id.issuer);
                            TextView nationalityTv = (TextView) findViewById(R.id.nationality);
                            TextView dateOfBirthTv = (TextView) findViewById(R.id.dateOfBirth);
                            TextView documentNumberTv = (TextView) findViewById(R.id.documentNumber);
                            TextView sexTv = (TextView) findViewById(R.id.sex);
                            TextView documentCodeTv = (TextView) findViewById(R.id.documentCode);
                            TextView dateOfExpiryTv = (TextView) findViewById(R.id.dateOfExpiry);
                            TextView opt1Tv = (TextView) findViewById(R.id.optional1);
                            TextView opt2Tv = (TextView) findViewById(R.id.optional2);
                            TextView mrzTextTv = (TextView) findViewById(R.id.mRZ);
                            TextView dateTv = (TextView) findViewById(R.id.date);


                            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
                            //TEST FOR RECORDS OLDER THAN 18 HOURS date.setTime(date.getTime() - 64764000);

                            //Create a format used for the presentation of a date format in string
                            DateFormat dtStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            DateFormat dStamp = new SimpleDateFormat("dd-MM-yyyy");

                            //Fill the textviews with the content of the selected MRZ scan
                            firstNameTv.setText(firstName);
                            lastNameTv.setText(lastName);
                            issuerTv.setText(issuer);
                            nationalityTv.setText(nationality);
                            dateOfBirthTv.setText(dStamp.format(dateOfBirth));
                            documentNumberTv.setText(documentNumber);
                            sexTv.setText(sex);
                            documentCodeTv.setText(documentCode);
                            dateOfExpiryTv.setText(dStamp.format(dateOfExpiry));
                            opt1Tv.setText(opt1);
                            opt2Tv.setText(opt2);
                            mrzTextTv.setText(mrtText);
                            dateTv.setText(dtStamp.format(date));


                            final DbHelper dbHelper = new DbHelper(this);


                            CharSequence colors[] = new CharSequence[] {"Bekijk gescand paspoort", "Terug"};
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                            builder.setTitle("Opties");
                            builder.setItems(colors, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            Intent x = new Intent(getApplicationContext(), ScannedListActivity.class);
                                            finish();
                                            startActivity(x);
                                            break;
                                        case 1:
                                            Intent i = new Intent(getApplicationContext(), MyScanActivity.class);
                                            startActivity(i);
                                            break;

                                    }
                                }
                            });
                            //builder.show();
                            final MRTDRecord mrtdRecord = new MRTDRecord(firstName, lastName, issuer, nationality, dStamp.format(dateOfBirth), documentNumber, sex, documentCode, dStamp.format(dateOfExpiry), opt1, opt2, mrtText, date);
                            long id = dbHelper.addMrtdRecord(mrtdRecord);
                            mrtdRecord.setId((int) id);
                            Button saveChangesButton = (Button) findViewById(R.id.saveChangesButton);

                            saveChangesButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText noteEditTv = (EditText) findViewById(R.id.note);
                                    String s = String.valueOf(noteEditTv.getText());
                                    mrtdRecord.setNote(s);
                                    dbHelper.updateMrtdRecord(mrtdRecord);
                                    Toast.makeText(PersonActivity.this, "Wijzigingen aantekening succesvol opgeslagen", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            OcrResult rawOcr = result.getOcrResult();
                            // attempt to parse OCR result by yourself
                            // or ask user to try again
                        }
                    } else {
                        // not all relevant data was scanned, ask user
                        // to try again
                    }
                }
            }
        //User came from list of scans, no need to do anything extra apart from showing the details
        } else {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();

            final MRTDRecord mrtdRecord = (MRTDRecord) intent.getSerializableExtra("record");

            final DbHelper db = new DbHelper(getApplicationContext());
            String note = db.getNote(mrtdRecord.getId());

            String firstName = mrtdRecord.getFirstName();
            String lastName = mrtdRecord.getLastName();
            String issuer = mrtdRecord.getIssuer();
            String nationality = mrtdRecord.getNationality();
            String dateOfBirth = mrtdRecord.getDateOfBirth();
            String documentNumber = mrtdRecord.getDocumentNumber();
            String sex = mrtdRecord.getSex();
            String documentCode = mrtdRecord.getDocumentCode();
            String dateOfExpiry = mrtdRecord.getDateOfExpiry();
            String opt1 = mrtdRecord.getOpt1();
            String opt2 = mrtdRecord.getOpt2();
            String mrtText = mrtdRecord.getMrzText();
            Date date = mrtdRecord.getDateTimeStamp();

            // Find the textviews to fill those with the object information
            TextView firstNameTv = (TextView) findViewById(R.id.firstName);
            TextView lastNameTv = (TextView) findViewById(R.id.lastName);
            TextView issuerTv = (TextView) findViewById(R.id.issuer);
            TextView nationalityTv = (TextView) findViewById(R.id.nationality);
            TextView dateOfBirthTv = (TextView) findViewById(R.id.dateOfBirth);
            TextView documentNumberTv = (TextView) findViewById(R.id.documentNumber);
            TextView sexTv = (TextView) findViewById(R.id.sex);
            TextView documentCodeTv = (TextView) findViewById(R.id.documentCode);
            TextView dateOfExpiryTv = (TextView) findViewById(R.id.dateOfExpiry);
            TextView opt1Tv = (TextView) findViewById(R.id.optional1);
            TextView opt2Tv = (TextView) findViewById(R.id.optional2);
            TextView mrzTextTv = (TextView) findViewById(R.id.mRZ);
            TextView dateTv = (TextView) findViewById(R.id.date);
            EditText noteEditTv = (EditText) findViewById(R.id.note);

            //Create a format used for the presentation of a date format in string
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            //Fill the textviews with the content of the selected MRZ scan
            firstNameTv.setText(firstName);
            lastNameTv.setText(lastName);
            issuerTv.setText(issuer);
            nationalityTv.setText(nationality);
            dateOfBirthTv.setText(dateOfBirth);
            documentNumberTv.setText(documentNumber);
            sexTv.setText(sex);
            documentCodeTv.setText(documentCode);
            dateOfExpiryTv.setText(dateOfExpiry);
            opt1Tv.setText(opt1);
            opt2Tv.setText(opt2);
            mrzTextTv.setText(mrtText);
            dateTv.setText(df.format(date));
            noteEditTv.setText(note);



            Button saveChangesButton = (Button) findViewById(R.id.saveChangesButton);

            saveChangesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText noteEditTv = (EditText) findViewById(R.id.note);
                    String s = String.valueOf(noteEditTv.getText());
                    mrtdRecord.setNote(s);
                    db.updateMrtdRecord(mrtdRecord);
                    Toast.makeText(PersonActivity.this, "Wijzigingen aantekening succesvol opgeslagen", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


}