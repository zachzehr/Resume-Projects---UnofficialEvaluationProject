package com.example.hsport.unofficialevaluationproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TranscriptEntry extends AppCompatActivity {

    String transcriptEntryID, IDFilter, schoolName, termName, degreeOrCourseName;
    Cursor transcriptCursor;
    String [] IDQuery;
    LinearLayout linearLayout;
    Button deleteTranscript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transcript_entry);
        Intent intent = getIntent();
        transcriptEntryID = Long.toString(intent.getLongExtra(UnofficialTranscript.TRANSCRIPT_ENTRY_ID, 0));
        IDFilter = DatabaseHelper.KEY_ID + " = ?";
        IDQuery = new String[]{transcriptEntryID};
        transcriptCursor = getContentResolver().query(InfoProvider.TRANSCRIPTS_URI, DatabaseHelper.ALL_COLUMNS_TRANSCRIPTS,
                IDFilter, IDQuery, null, null);
        transcriptCursor.moveToFirst();
        schoolName = transcriptCursor.getString(transcriptCursor.getColumnIndex(DatabaseHelper.KEY_SCHOOL_NAME));
        termName = transcriptCursor.getString(transcriptCursor.getColumnIndex(DatabaseHelper.KEY_TERM_NAME));
        degreeOrCourseName = transcriptCursor.getString(transcriptCursor.getColumnIndex(DatabaseHelper.KEY_DEGREE_OR_COURSE_NAME));
        linearLayout =  (LinearLayout)findViewById(R.id.transcriptEntryLayout);
        LinearLayout.LayoutParams lParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView schoolNameTV = new TextView(this);
        TextView termNameTV = new TextView(this);
        TextView degreeOrCourseNameTV = new TextView(this);
        schoolNameTV.setLayoutParams(lParams);
        degreeOrCourseNameTV.setLayoutParams(lParams);
        termNameTV.setLayoutParams(lParams);
        schoolNameTV.setText(schoolName);
        degreeOrCourseNameTV.setText(degreeOrCourseName);
        termNameTV.setText(termName);
        schoolNameTV.setTextSize(24);
        degreeOrCourseNameTV.setTextSize(24);
        termNameTV.setTextSize(24);
        linearLayout.addView(schoolNameTV);
        linearLayout.addView(degreeOrCourseNameTV);
        linearLayout.addView(termNameTV);

        deleteTranscript = new Button(TranscriptEntry.this);
        deleteTranscript.setText("Remove Transcript");
        deleteTranscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(InfoProvider.TRANSCRIPTS_URI, IDFilter, IDQuery);

                Intent intent = new Intent(TranscriptEntry.this, UnofficialTranscript.class);
                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);



            }
        });
        linearLayout.addView(deleteTranscript);

    }
}
