package com.example.hsport.unofficialevaluationproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddDegree extends AppCompatActivity {

    private LinearLayout linearLayout;
    private Cursor termsCursor, degreeCursor, schoolCursor, transcriptsCursor;
    private String schoolID, listItem, termID, schoolName, termName, degreeName, IDFilter, degreeFilter, degreeID, addDegreeIDFilter;
    String[] schoolIDQuery, IDQuery, degreeQuery, addDegreeIDQuery;
    private List<String> termList = new ArrayList<String>();
    private List<String> degreeList = new ArrayList<String>();
    private ArrayAdapter<String> termAdapter, degreeAdapter;
    private AutoCompleteTextView degreeSelectCompleteTextView;
    private Button addToTranscriptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_degree);
        addToTranscriptButton = new Button(AddDegree.this);
        Intent intent = getIntent();
        degreeSelectCompleteTextView = new AutoCompleteTextView(this);
        schoolID = intent.getStringExtra("schoolIDForAddDegreeActivity");
        Log.i("addDegreeschoolID", schoolID);
        IDQuery = new String[]{schoolID};
        IDFilter = DatabaseHelper.KEY_ID + " = ?";
        schoolCursor = getContentResolver().query(InfoProvider.SCHOOLS_URI, DatabaseHelper.ALL_COLUMNS_SCHOOLS, IDFilter, IDQuery, null, null);
        Boolean result = schoolCursor.moveToFirst();
        Log.i("addDegreeSchoolCursRes", result.toString());
        schoolName = schoolCursor.getString(schoolCursor.getColumnIndex(DatabaseHelper.KEY_SCHOOL_NAME));
        AutoCompleteTextView termSelectTextView = new AutoCompleteTextView(this);
        linearLayout =  (LinearLayout)findViewById(R.id.addDegreeLayout);
        termSelectTextView.setHint("Enter degree completed term");LinearLayout.LayoutParams lParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        termSelectTextView.setLayoutParams(lParams);
        termSelectTextView.setThreshold(0);
        termSelectTextView.addTextChangedListener(termAutoCompleteTextWatcher);
        termSelectTextView.setDropDownWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        schoolIDQuery = new String[]{schoolID};
        String schoolIDFilter = DatabaseHelper.KEY_SCHOOL_ID + " = ?";

        termsCursor = getContentResolver().query(InfoProvider.TERMS_URI, DatabaseHelper.ALL_COLUMNS_TERMS, schoolIDFilter, schoolIDQuery, null, null);
        termsCursor.moveToFirst();
        while (!termsCursor.isAfterLast()) {
            listItem = termsCursor.getString(termsCursor.getColumnIndex("term_name"));
            termList.add(listItem);
            termsCursor.moveToNext();}
        termAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, termList);
        termSelectTextView.setAdapter(termAdapter);

        linearLayout.addView(termSelectTextView);

    }



    private final TextWatcher termAutoCompleteTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String[] termNameQuery, termIDQuery;
            final String submittedText = s.toString();
            termName =s.toString();
            if (termList.contains(submittedText)) {

                termNameQuery = new String[]{submittedText};
                String termNameFilter = DatabaseHelper.KEY_TERM_NAME + " = ?";
                termsCursor = getContentResolver().query(InfoProvider.TERMS_URI, DatabaseHelper.ALL_COLUMNS_TERMS,
                        termNameFilter, termNameQuery, null, null);
                termsCursor.moveToFirst();
                termID = termsCursor.getString(termsCursor.getColumnIndex(DatabaseHelper.KEY_ID));

                termIDQuery = new String[]{termID};
                String termIDFilter = DatabaseHelper.KEY_TERM_ID + " = ?";

                degreeCursor = getContentResolver().query(InfoProvider.DEGREES_URI, DatabaseHelper.ALL_COLUMNS_DEGREES,
                        termIDFilter, termIDQuery, null, null);

                if (degreeCursor.moveToFirst()) {
                    degreeSelectCompleteTextView.setHint("Enter degree name");
                    LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    degreeSelectCompleteTextView.setLayoutParams(lParams);
                    degreeSelectCompleteTextView.setThreshold(0);
                    degreeSelectCompleteTextView.addTextChangedListener(degreeAutoCompleteViewTextWatcher);
                    degreeSelectCompleteTextView.setDropDownWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                    termIDQuery = new String[]{termID};
                    termIDFilter = DatabaseHelper.KEY_TERM_ID + " = ?";
                    degreeCursor = getContentResolver().query(InfoProvider.DEGREES_URI, DatabaseHelper.ALL_COLUMNS_DEGREES, termIDFilter, termIDQuery, null, null);
                    degreeCursor.moveToFirst();
                    while (!degreeCursor.isAfterLast()) {
                        listItem = degreeCursor.getString(degreeCursor.getColumnIndex("degree_name"));
                        degreeList.add(listItem);
                        degreeCursor.moveToNext();
                    }
                    degreeAdapter = new ArrayAdapter<String>(AddDegree.this, android.R.layout.simple_list_item_1, degreeList);
                    degreeSelectCompleteTextView.setAdapter(degreeAdapter);
                    linearLayout.addView(degreeSelectCompleteTextView);

                }

                else

                {
                    if (degreeSelectCompleteTextView.isShown()) {
                        linearLayout.removeView(degreeSelectCompleteTextView);
                    }


                }
            }

        }


    };

    private final TextWatcher degreeAutoCompleteViewTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String[] schoolNameQuery, schoolIDQuery;
            final String submittedText = s.toString();
            degreeName = s.toString();
            if (degreeList.contains(submittedText)){

                addToTranscriptButton.setText("Add to Unofficial Transcript");
                addToTranscriptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        degreeQuery = new String[]{degreeName};
                        degreeFilter = DatabaseHelper.KEY_DEGREE_OR_COURSE_NAME + " = ?";
                        transcriptsCursor = getContentResolver().query(InfoProvider.TRANSCRIPTS_URI,
                                DatabaseHelper.ALL_COLUMNS_TRANSCRIPTS, degreeFilter, degreeQuery, null, null);
                        if (transcriptsCursor.moveToFirst()){
                            Toast.makeText(AddDegree.this, "This degree has already been added",
                                    Toast.LENGTH_LONG).show();
                            return;

                        }
                        Log.i("addCourseIDLog", "termID:" + termID + " degreeName:" +degreeName + " schoolID:" +schoolID);
                        addDegreeIDQuery = new String[]{termID, degreeName, schoolID};
                        addDegreeIDFilter = DatabaseHelper.KEY_TERM_ID + " =?" + " AND " + DatabaseHelper.KEY_DEGREE_NAME+ " =?"
                                + " AND " + DatabaseHelper.KEY_SCHOOL_ID + " =?";
                        degreeCursor = getContentResolver().query(InfoProvider.DEGREES_URI, DatabaseHelper.ALL_COLUMNS_DEGREES, addDegreeIDFilter, addDegreeIDQuery, null, null);
                        degreeCursor.moveToFirst();
                        degreeID = degreeCursor.getString(degreeCursor.getColumnIndex("_id"));


                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.KEY_SCHOOL_NAME, schoolName);
                        values.put(DatabaseHelper.KEY_TERM_NAME, termName);
                        values.put(DatabaseHelper.KEY_DEGREE_OR_COURSE_NAME, degreeName);
                        values.put(DatabaseHelper.KEY_TERM_ID, termID);
                        values.put(DatabaseHelper.KEY_DEGREE_ID, degreeID);
                        values.put(DatabaseHelper.KEY_COURSE_ID, "0");
                        getContentResolver().insert(InfoProvider.TRANSCRIPTS_URI, values);
                        Intent programSelectionActivityIntent = new Intent(AddDegree.this, ProgramSelection.class);
                        Toast.makeText(AddDegree.this, degreeName + " has been added " +
                                "into the unofficial evaluation list", Toast.LENGTH_LONG).show();
                        Log.i("addDegree", degreeName + "was added");
                        startActivity(programSelectionActivityIntent);
                    }
                });

                linearLayout.addView(addToTranscriptButton);

            }
            else{
                if(addToTranscriptButton.isShown()) {
                    linearLayout.removeView(addToTranscriptButton);
                }

            }

        }
    };

}

