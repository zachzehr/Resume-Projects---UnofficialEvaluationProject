package com.example.hsport.unofficialevaluationproject;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramSelection extends AppCompatActivity implements View.OnTouchListener {
    private String[] PROGRAM;
    private SimpleCursorAdapter sca;
    private ArrayAdapter<String> schoolAdapter;
    private int position, selectedColumn;
    private Cursor cursor, schoolCursor,courseCursor, degreeCursor;
    private String listItem, schoolID;
    private LinearLayout linearLayout;
    private List<String> schoolList = new ArrayList<String>();
    private Button addCourseButton, addDegreeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Spinner programView = (Spinner) findViewById(R.id.programSelectSpinner);
        addCourseButton = new Button(ProgramSelection.this);
        addDegreeButton = new Button(ProgramSelection.this);

        String[] fromCols=new String[]{"program_name"};
//        String[] adapterCols=new String[]{"sampletext"};
        int[] adapterRowViews=new int[]{android.R.id.text1};
        cursor = getContentResolver().query(InfoProvider.PROGRAM_URI, DatabaseHelper.ALL_COLUMNS_PROGRAM,
                null, null, null, null);

        sca=new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, fromCols, adapterRowViews,0);

        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        programView.setAdapter(sca);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String selectedLog = (cursor.getString(cursor.getColumnIndex("selected")));
            Log.i("SelectedColumninOrder", selectedLog);
            cursor.moveToNext();

        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if ("1".equals(cursor.getString(cursor.getColumnIndex("selected")))){
                selectedColumn = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
                position = selectedColumn - 1;
                Log.i("position log", Integer.toString(position));
                cursor.moveToNext();
                break;
            }
            cursor.moveToNext();
        }
        if(selectedColumn > 1){
            Log.i("ProgramSelection", "entered text view creation");
//            AutoCompleteTextView completeTextView = (AutoCompleteTextView) findViewById(R.id.schoolField);

            AutoCompleteTextView completeTextView = new AutoCompleteTextView(this);
            linearLayout =  (LinearLayout)findViewById(R.id.programSelectLayout);

            completeTextView.setHint("Enter school name");
            LinearLayout.LayoutParams lParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            completeTextView.setLayoutParams(lParams);
            completeTextView.setThreshold(0);
            completeTextView.addTextChangedListener(schoolTextWatcher);
            completeTextView.setDropDownWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            linearLayout.addView(completeTextView);

            TextView infoClickTextView = new TextView(this);
            infoClickTextView.setLayoutParams(lParams);
            infoClickTextView.setTextColor(Color.RED);
            infoClickTextView.setText("My school is not listed, or I can't find my course or degree");
            infoClickTextView.setTextSize(20);
            infoClickTextView.setOnTouchListener(this);
            final CharSequence text = infoClickTextView.getText();
            final SpannableString spannableString = new SpannableString( text );
            spannableString.setSpan(new URLSpan(""), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            infoClickTextView.setText(spannableString, TextView.BufferType.SPANNABLE);
            linearLayout.addView(infoClickTextView, 2);



            Button myButton = new Button(this);
            myButton.setText("Unofficial Transcripts Added");
            myButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent unofficialTranscriptsActivityIntent = new Intent(ProgramSelection.this, UnofficialTranscript.class);
                    startActivity(unofficialTranscriptsActivityIntent);
                }
            });
            linearLayout.addView(myButton, 3);

            schoolCursor = getContentResolver().query(InfoProvider.SCHOOLS_URI, DatabaseHelper.ALL_COLUMNS_SCHOOLS, null, null, null);
            schoolCursor.moveToFirst();
            while (!schoolCursor.isAfterLast()) {
                listItem = schoolCursor.getString(schoolCursor.getColumnIndex("school_name"));
                        schoolList.add(listItem);
            schoolCursor.moveToNext();}
            schoolAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, schoolList);
            completeTextView.setAdapter(schoolAdapter);
        }


        programView.setSelection(position, false);

        programView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Long selectedProgramID = arg0.getSelectedItemId();
                String programID = Long.toString(selectedProgramID);
                Log.i("on item selected id", programID);

                ContentValues CV = new ContentValues();
                int value = 1;
                CV.put(DatabaseHelper.KEY_SELECTED, value);

                getContentResolver().update(InfoProvider.PROGRAM_URI, CV,
                        "_id =" + programID, null);
                cursor.moveToPosition(Integer.parseInt(programID) - 1);
                while (!cursor.isBeforeFirst()){
                    if ("1".equals(cursor.getString(cursor.getColumnIndex("selected")))){
                        String currentRow = (cursor.getString(cursor.getColumnIndex("_id")));
                        ContentValues CV2 = new ContentValues();
                        value = 0;
                        CV2.put(DatabaseHelper.KEY_SELECTED, value);

                        getContentResolver().update(InfoProvider.PROGRAM_URI, CV2,
                                "_id =" + currentRow, null);
                    }
                    cursor.moveToPrevious();

                }
                cursor.moveToPosition(Integer.parseInt(programID) - 1);
                while (!cursor.isAfterLast()){
                    if ("1".equals(cursor.getString(cursor.getColumnIndex("selected")))){
                        String currentRow = (cursor.getString(cursor.getColumnIndex("_id")));
                        ContentValues CV3 = new ContentValues();
                        value = 0;
                        CV3.put(DatabaseHelper.KEY_SELECTED, value);

                        getContentResolver().update(InfoProvider.PROGRAM_URI, CV3,
                                "_id =" + currentRow, null);
                    }
                    cursor.moveToNext();

                }
                Intent intent = new Intent(ProgramSelection.this,
                        ProgramSelection.class);

                startActivity(intent);
            }



            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

//        DatabaseHelper myDbHelper = new DatabaseHelper(this);
//
//        try {
//
//            myDbHelper.createDataBase();
//
//        } catch (IOException ioe) {
//
//            throw new Error("Unable to create database");
//
//        }
//
//        try {
//
//            myDbHelper.openDataBase();
//
//        }catch(SQLException sqle){
//
//            throw sqle;
//
//        }
    }

    private final TextWatcher schoolTextWatcher = new TextWatcher() {
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
            if (schoolList.contains(submittedText)){

                schoolNameQuery = new String[]{submittedText};
                String schoolNameFilter = DatabaseHelper.KEY_SCHOOL_NAME + " = ?";
                schoolCursor = getContentResolver().query(InfoProvider.SCHOOLS_URI, DatabaseHelper.ALL_COLUMNS_SCHOOLS,
                        schoolNameFilter, schoolNameQuery, null, null);
                schoolCursor.moveToFirst();
                schoolID = schoolCursor.getString(schoolCursor.getColumnIndex(DatabaseHelper.KEY_ID));

                schoolIDQuery = new String[]{schoolID};
                String schoolIDFilter = DatabaseHelper.KEY_SCHOOL_ID + " = ?";

                courseCursor = getContentResolver().query(InfoProvider.COURSES_URI, DatabaseHelper.ALL_COLUMNS_COURSES,
                        schoolIDFilter, schoolIDQuery, null, null);

                if (courseCursor.moveToFirst()) {
                    addCourseButton.setText("Add Course To Transcript");
                    addCourseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent addCourseActivityIntent = new Intent(ProgramSelection.this, AddCourse.class);
                            addCourseActivityIntent.putExtra("schoolIDForAddCourseActivity", schoolID);
                            startActivity(addCourseActivityIntent);
                        }
                    });
                    linearLayout.addView(addCourseButton, 3);
                }

                degreeCursor = getContentResolver().query(InfoProvider.DEGREES_URI, DatabaseHelper.ALL_COLUMNS_DEGREES,
                        schoolIDFilter, schoolIDQuery, null, null);

                if (degreeCursor.moveToFirst()) {
                    addDegreeButton.setText("Add Degree To Transcript");
                    addDegreeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent addDegreeActivityIntent = new Intent(ProgramSelection.this, AddDegree.class);
                            addDegreeActivityIntent.putExtra("schoolIDForAddDegreeActivity", schoolID);
                            startActivity(addDegreeActivityIntent);
                        }
                    });
                    linearLayout.addView(addDegreeButton, 3);
                }

            }
            else{
                if(addCourseButton.isShown()) {
                    linearLayout.removeView(addCourseButton);
            }

                if(addDegreeButton.isShown()) {
                    linearLayout.removeView(addDegreeButton);
                }

            }

        }
    };


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Unofficial evaluations are based upon previously completed student evaluations. " +
                "If you do not see your school, course or degree, there may not be enough exisitng data to complete " +
                "an evaluation. Please contact your enrollment counselor or log " +
                "into your enrollment portal to request official transcripts for an evaluation. ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        return false;
    }
}
