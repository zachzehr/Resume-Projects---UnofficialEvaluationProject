package com.example.hsport.unofficialevaluationproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
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

public class AddCourse extends AppCompatActivity {
    private LinearLayout linearLayout;
    private Cursor termsCursor, courseCursor, schoolCursor, transcriptsCursor;
    private String schoolID, listItem, termID, schoolName, termName, courseName, IDFilter, courseFilter, courseID,
            addCourseIDFilter;
    String[] schoolIDQuery, IDQuery, courseQuery, addCourseIDQuery;
    private List<String> termList = new ArrayList<String>();
    private List<String> courseList = new ArrayList<String>();
    private ArrayAdapter<String> termAdapter, courseAdapter;
    private AutoCompleteTextView courseSelectCompleteTextView;
    private Button addToTranscriptButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        addToTranscriptButton = new Button(AddCourse.this);
        Intent intent = getIntent();
        courseSelectCompleteTextView = new AutoCompleteTextView(this);
        schoolID = intent.getStringExtra("schoolIDForAddCourseActivity");
        Log.i("addCourseschoolID", schoolID);
        IDQuery = new String[]{schoolID};
        IDFilter = DatabaseHelper.KEY_ID + " = ?";
        schoolCursor = getContentResolver().query(InfoProvider.SCHOOLS_URI, DatabaseHelper.ALL_COLUMNS_SCHOOLS, IDFilter, IDQuery, null, null);
        Boolean result = schoolCursor.moveToFirst();
        Log.i("addCourseSchoolCursRes", result.toString());
        schoolName = schoolCursor.getString(schoolCursor.getColumnIndex(DatabaseHelper.KEY_SCHOOL_NAME));
        AutoCompleteTextView termSelectTextView = new AutoCompleteTextView(this);
        linearLayout =  (LinearLayout)findViewById(R.id.addCourseLayout);
        termSelectTextView.setHint("Enter course term");
        LinearLayout.LayoutParams lParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
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

                courseCursor = getContentResolver().query(InfoProvider.COURSES_URI, DatabaseHelper.ALL_COLUMNS_COURSES,
                        termIDFilter, termIDQuery, null, null);

                if (courseCursor.moveToFirst()) {
                    courseSelectCompleteTextView.setHint("Enter course name");
                    LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    courseSelectCompleteTextView.setLayoutParams(lParams);
                    courseSelectCompleteTextView.setThreshold(0);
                    courseSelectCompleteTextView.addTextChangedListener(courseAutoCompleteViewTextWatcher);
                    courseSelectCompleteTextView.setDropDownWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                    termIDQuery = new String[]{termID};
                    termIDFilter = DatabaseHelper.KEY_TERM_ID + " = ?";
                    courseCursor = getContentResolver().query(InfoProvider.COURSES_URI, DatabaseHelper.ALL_COLUMNS_COURSES, termIDFilter, termIDQuery, null, null);
                    courseCursor.moveToFirst();
                    while (!courseCursor.isAfterLast()) {
                        listItem = courseCursor.getString(courseCursor.getColumnIndex("course_name"));
                        courseList.add(listItem);
                        courseCursor.moveToNext();
                    }
                    courseAdapter = new ArrayAdapter<String>(AddCourse.this, android.R.layout.simple_list_item_1, courseList);
                    courseSelectCompleteTextView.setAdapter(courseAdapter);
                    linearLayout.addView(courseSelectCompleteTextView);

                }

            else

                {
                    if (courseSelectCompleteTextView.isShown()) {
                        linearLayout.removeView(courseSelectCompleteTextView);
                    }


                }
            }

        }


    };

    private final TextWatcher courseAutoCompleteViewTextWatcher = new TextWatcher() {
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
            courseName = s.toString();
            if (courseList.contains(submittedText)){

                addToTranscriptButton.setText("Add to Unofficial Transcript");
                addToTranscriptButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        courseQuery = new String[]{courseName};
                        courseFilter = DatabaseHelper.KEY_DEGREE_OR_COURSE_NAME + " = ?";
                        transcriptsCursor = getContentResolver().query(InfoProvider.TRANSCRIPTS_URI,
                                DatabaseHelper.ALL_COLUMNS_TRANSCRIPTS, courseFilter, courseQuery, null, null);
                        if (transcriptsCursor.moveToFirst()){
                            Toast.makeText(AddCourse.this, "This course has already been added",
                                    Toast.LENGTH_LONG).show();
                            return;

                        }
                        Log.i("addCourseIDLog", "termID:" + termID + " courseName:" +courseName + " schoolID:" +schoolID);
                        addCourseIDQuery = new String[]{termID, courseName, schoolID};
                        addCourseIDFilter = DatabaseHelper.KEY_TERM_ID + " =?" + " AND " + DatabaseHelper.KEY_COURSE_NAME+ " =?"
                        + " AND " + DatabaseHelper.KEY_SCHOOL_ID + " =?";
                        courseCursor = getContentResolver().query(InfoProvider.COURSES_URI, DatabaseHelper.ALL_COLUMNS_COURSES, addCourseIDFilter, addCourseIDQuery, null, null);
                        courseCursor.moveToFirst();
                        courseID = courseCursor.getString(courseCursor.getColumnIndex("_id"));

                        ContentValues values = new ContentValues();
                        values.put(DatabaseHelper.KEY_SCHOOL_NAME, schoolName);
                        values.put(DatabaseHelper.KEY_TERM_NAME, termName);
                        values.put(DatabaseHelper.KEY_DEGREE_OR_COURSE_NAME, courseName);
                        values.put(DatabaseHelper.KEY_TERM_ID, termID);
                        values.put(DatabaseHelper.KEY_COURSE_ID, courseID);
                        values.put(DatabaseHelper.KEY_DEGREE_ID, "0");
                        getContentResolver().insert(InfoProvider.TRANSCRIPTS_URI, values);
                        Intent programSelectionActivityIntent = new Intent(AddCourse.this, ProgramSelection.class);
                        Toast.makeText(AddCourse.this, courseName + " has been added " +
                                "into the unofficial evaluation list", Toast.LENGTH_LONG).show();
                        Log.i("addCourse", courseName + "was added");
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
