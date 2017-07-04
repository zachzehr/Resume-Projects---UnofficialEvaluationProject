package com.example.hsport.unofficialevaluationproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by zachzehr on 6/24/17.
 */

public class TranscriptListAdapter extends CursorAdapter {
    public TranscriptListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.listview_transcript_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String transcriptInformation = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_SCHOOL_NAME)) + "\n"
                + cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DEGREE_OR_COURSE_NAME)) + "\n"
                + cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_TERM_NAME));
        TextView tv = (TextView) view.findViewById(R.id.tvBody);
        tv.setText(transcriptInformation);

    }

}
