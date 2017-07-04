package com.example.hsport.unofficialevaluationproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class UnofficialTranscript extends AppCompatActivity {

    private CursorAdapter cursorAdapter;
    private Cursor transcriptCursor;
    private LinearLayout linearLayout;
    private ListView listView;
    private static final int MENU_ITEM_ITEM1 = 1;
    public static final String TRANSCRIPT_ENTRY_ID = "transcript_entry_id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unofficial_transcript);
        linearLayout =  (LinearLayout)findViewById(R.id.transcriptLayout);
        LinearLayout.LayoutParams lParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        listView = new ListView(this);
        listView.setLayoutParams(lParams);

        transcriptCursor = getContentResolver().query(InfoProvider.TRANSCRIPTS_URI, DatabaseHelper.ALL_COLUMNS_TRANSCRIPTS,
                null, null, null);
        if (transcriptCursor.moveToFirst()){
        Boolean check = transcriptCursor.moveToFirst();
        Log.i("transcriptcursorcheck", check.toString());


        cursorAdapter = new TranscriptListAdapter(this, transcriptCursor, 0);
        listView.setAdapter(cursorAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(UnofficialTranscript.this, TranscriptEntry.class);
                    long itemId = cursorAdapter.getItemId(position);
                    intent.putExtra(UnofficialTranscript.TRANSCRIPT_ENTRY_ID, itemId);
                    startActivity(intent);
                }
            });
        linearLayout.addView(listView);

        }
        else{
            TextView textView = new TextView(this);
            textView.setLayoutParams(lParams);
            textView.setText("Please add a course or degree from the previous screen and return to here to create the" +
                    " evaluation.");
            textView.setTextSize(25);
            linearLayout.addView(textView);
        }
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(TermActivity.this, TermEditor.class);
//                long itemId = cursorAdapter.getItemId(position);
//                intent.putExtra(TermProvider.TERM_ITEM_TYPE, itemId);
//                startActivityForResult(intent, EDITOR_REQUEST_CODE);
//            }
//
//        };
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_create_evaluation:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This is an unofficial evaluation and has no bearing on the final official evaluation." +
                        " All official transcripts must be sent to your enrollment counselor for an official evaluation" +
                        " to be completed. Official transcripts must be received in order to enroll at WGU and the unofficial" +
                        " transcript may not stand in for the official evaluation.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                createEvaluation();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
        }

        return true;


    }
//    public boolean onPrepareOptionsMenu(Menu menu)
//    {
//        MenuItem register = menu.findItem(R.id.transcripts_menu);
//        if(userRegistered)
//        {
//            register.setVisible(false);
//        }
//        else
//        {
//            register.setVisible(true);
//        }
//        return true;
//    }

    private void createEvaluation() {
        Intent intent = new Intent(this, ViewEvaluation.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(transcriptCursor.moveToFirst()) {
            getMenuInflater().inflate(R.menu.transcripts_menu, menu);
        }
            return true;
    }

}