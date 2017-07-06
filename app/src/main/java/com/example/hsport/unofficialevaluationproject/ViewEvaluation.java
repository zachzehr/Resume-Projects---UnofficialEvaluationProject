package com.example.hsport.unofficialevaluationproject;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewEvaluation extends AppCompatActivity {
    File fileForPDF;
    Cursor transcriptCursor, academicRecordsCursor;
    private static final String BACHELORS_DEGREE_EVAL_PATH = "bachelors_degree_eval_application.pdf";
    private static final String ENGLISH_COMPOSITION_EVAL_PATH = "english_composition_eval_application.pdf";
    private static final String NO_CREDIT_EVAL_PATH = "no_credit_eval_application.pdf";
    String creditEarned, courseID, degreeID, courseIDFilter, creditAwarded, degreeIDFilter;
    private List<String> degreeList = new ArrayList<String>();
    private List<String> courseList = new ArrayList<String>();
    private List<String> creditAwardedList = new ArrayList<String>();
    String[] IDQuery;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_evaluation);
        copyAssets();
        linearLayout =  (LinearLayout)findViewById(R.id.evaluation_layout);
        LinearLayout.LayoutParams lParams =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView infoTV = new TextView(this);
        infoTV.setLayoutParams(lParams);
        infoTV.setTextSize(24);
        infoTV.setText("Please press back to return to the transcript list");
        linearLayout.addView(infoTV);

        transcriptCursor = getContentResolver().query(InfoProvider.TRANSCRIPTS_URI, DatabaseHelper.ALL_COLUMNS_TRANSCRIPTS,
                null, null, null);
        transcriptCursor.moveToFirst();
        while (!transcriptCursor.isAfterLast()) {
            courseID = "0";
            degreeID = "0";
            courseID = transcriptCursor.getString(transcriptCursor.getColumnIndex("course_id"));
            degreeID = transcriptCursor.getString(transcriptCursor.getColumnIndex("degree_id"));
            if(Integer.parseInt(courseID) > 0){
                courseList.add(courseID);}
            if(Integer.parseInt(degreeID) > 0){
                degreeList.add(degreeID);}
            transcriptCursor.moveToNext();}

        if(!courseList.isEmpty()) {
            courseIDFilter = DatabaseHelper.KEY_COURSE_ID + " = ?";
            for (String id : courseList) {
                IDQuery = new String[]{id};

                academicRecordsCursor = getContentResolver().query(InfoProvider.ACADEMIC_RECORDS_URI, DatabaseHelper.ALL_COLUMNS_ACADEMIC_RECORDS,
                        courseIDFilter, IDQuery, null, null);
                academicRecordsCursor.moveToFirst();
                while (!academicRecordsCursor.isAfterLast()){
                    creditAwarded = academicRecordsCursor.getString(academicRecordsCursor.getColumnIndex(DatabaseHelper.KEY_CREDIT_AWARDED));
                    creditAwardedList.add(creditAwarded);
                    academicRecordsCursor.moveToNext();

                }

            }
        }

        if(!degreeList.isEmpty()) {
            degreeIDFilter = DatabaseHelper.KEY_DEGREE_ID + " = ?";
            for (String id : degreeList) {
                IDQuery = new String[]{id};

                academicRecordsCursor = getContentResolver().query(InfoProvider.ACADEMIC_RECORDS_URI, DatabaseHelper.ALL_COLUMNS_ACADEMIC_RECORDS,
                        degreeIDFilter, IDQuery, null, null);
                academicRecordsCursor.moveToFirst();
                while (!academicRecordsCursor.isAfterLast()){
                    creditAwarded = academicRecordsCursor.getString(academicRecordsCursor.getColumnIndex(DatabaseHelper.KEY_CREDIT_AWARDED));
                    creditAwardedList.add(creditAwarded);
                    academicRecordsCursor.moveToNext();
                }

            }
        }

        if (creditAwardedList.contains("no_credit")){
            creditEarned =NO_CREDIT_EVAL_PATH;
            creditAwardedList.removeAll(Collections.singleton("no_credit"));;
        }

        if (creditAwardedList.contains("english_composition_1")){
            creditEarned =ENGLISH_COMPOSITION_EVAL_PATH;
            creditAwardedList.removeAll(Collections.singleton("english_composition_1"));;
        }


        if (creditAwardedList.contains("general_education")){
            creditEarned =BACHELORS_DEGREE_EVAL_PATH;
            creditAwardedList.removeAll(Collections.singleton("general_education"));;
        }

        fileForPDF = new File(ViewEvaluation.this.getExternalFilesDir(null)+ "/" + creditEarned);


        Uri path = FileProvider.getUriForFile(ViewEvaluation.this,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                fileForPDF);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(path, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PackageManager packageManager = getPackageManager();
        List activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please download a PDF viewer app from the " +
                    "Google Play Store. Once downloaded return to this" +
                    " screen to create the evaluation", Toast.LENGTH_LONG).show();
        }

    }


    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);

        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            Log.i("file check", filename);
            try {
                in = assetManager.open(filename);
                File outFile = new File(getExternalFilesDir(null), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }


}
