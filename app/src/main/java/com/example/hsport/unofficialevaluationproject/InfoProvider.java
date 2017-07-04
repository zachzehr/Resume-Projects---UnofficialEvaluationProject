package com.example.hsport.unofficialevaluationproject;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by zachzehr on 6/14/17.
 */

public class InfoProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.unofficialevaluationproject.infoprovider";
    private static final String PROGRAM_PATH = "program";
    private static final String SCHOOLS_PATH = "schools";
    private static final String COURSES_PATH = "courses";
    private static final String DEGREES_PATH = "degrees";
    private static final String TERMS_PATH = "terms";
    private static final String TRANSCRIPTS_PATH = "transcripts";
    private static final String ACADEMIC_RECORDS_PATH = "academic_recrods";



    public static final Uri PROGRAM_URI = Uri.parse("content://" + AUTHORITY + "/" + PROGRAM_PATH );
    public static final Uri SCHOOLS_URI = Uri.parse("content://" + AUTHORITY + "/" + SCHOOLS_PATH );
    public static final Uri COURSES_URI = Uri.parse("content://" + AUTHORITY + "/" + COURSES_PATH );
    public static final Uri DEGREES_URI = Uri.parse("content://" + AUTHORITY + "/" + DEGREES_PATH );
    public static final Uri TERMS_URI = Uri.parse("content://" + AUTHORITY + "/" + TERMS_PATH );
    public static final Uri TRANSCRIPTS_URI = Uri.parse("content://" + AUTHORITY + "/" + TRANSCRIPTS_PATH );
    public static final Uri ACADEMIC_RECORDS_URI = Uri.parse("content://" + AUTHORITY + "/" + ACADEMIC_RECORDS_PATH );


    // Constant to identify the requested operation
    private static final int PROGRAM = 10;
    private static final int PROGRAM_ID = 20;
    private static final int SCHOOLS = 30;
    private static final int SCHOOLS_ID = 40;
    private static final int COURSES = 50;
    private static final int COURSES_ID = 60;
    private static final int DEGREES = 70;
    private static final int DEGREES_ID = 80;
    private static final int TERMS = 90;
    private static final int TERMS_ID = 100;
    private static final int TRANSCRIPTS = 110;
    private static final int TRANSCRIPTS_ID = 120;
    private static final int ACADEMIC_RECORDS = 130;
    private static final int ACADEMIC_RECORDS_ID = 140;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String TERM_ITEM_TYPE = "term";
    public static final String TERM_FROM_TERM_EDITOR_ITEM_TYPE = "termFromTermEditor";
    public static final String ID_ITEM_TYPE_COURSE_ACTIVITY = "idFromCourseActivity";
    public static final String ID_ITEM_TYPE_NOTIFICATION_COURSE_EDITOR = "idFromCourseEditorForStartDateNotification";
    public static final String ID_ITEM_TYPE_COURSE_TO_CM_ACTIVITY = "idForCMActivity";
    public static final String ID_ITEM_TYPE_COURSE_TO_CM_EDITOR = "idForCMEditor";
    public static final String ID_ITEM_TYPE_COURSE_TO_ASSESSMENTS_ACTIVITY = "idForAssessmentsActivity" ;
    public static final String ID_ITEM_TYPE_ASSESSMENT_TO_ASSESSMENT_EDITOR = "idForAssessmentsEditorActivity";


    static{
        uriMatcher.addURI(AUTHORITY, PROGRAM_PATH, PROGRAM);
        uriMatcher.addURI(AUTHORITY, PROGRAM_PATH + "/#", PROGRAM);
        uriMatcher.addURI(AUTHORITY, SCHOOLS_PATH, SCHOOLS);
        uriMatcher.addURI(AUTHORITY, SCHOOLS_PATH + "/#", SCHOOLS);
        uriMatcher.addURI(AUTHORITY, COURSES_PATH, COURSES);
        uriMatcher.addURI(AUTHORITY, COURSES_PATH + "/#", COURSES);
        uriMatcher.addURI(AUTHORITY, DEGREES_PATH, DEGREES);
        uriMatcher.addURI(AUTHORITY, DEGREES_PATH + "/#", DEGREES);
        uriMatcher.addURI(AUTHORITY, TERMS_PATH, TERMS);
        uriMatcher.addURI(AUTHORITY, TERMS_PATH + "/#", TERMS);
        uriMatcher.addURI(AUTHORITY, TRANSCRIPTS_PATH, TRANSCRIPTS);
        uriMatcher.addURI(AUTHORITY, TRANSCRIPTS_PATH + "/#", TRANSCRIPTS);
        uriMatcher.addURI(AUTHORITY, ACADEMIC_RECORDS_PATH, ACADEMIC_RECORDS);
        uriMatcher.addURI(AUTHORITY, ACADEMIC_RECORDS_PATH + "/#", ACADEMIC_RECORDS);


    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        DatabaseHelper helper = new DatabaseHelper(getContext());
        database = helper.getWritableDatabase();

        try {

            helper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");


        }
//        try {
//
//            helper.openDataBase();
//
//        }catch(SQLException sqle){
//
//            throw sqle;
//
//        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        Cursor c = null;

        switch (uriMatcher.match(uri)) {

            case PROGRAM:
            case PROGRAM_ID:
                c = database.query(DatabaseHelper.TABLE_PROGRAM, DatabaseHelper.ALL_COLUMNS_PROGRAM, selection,
                        selectionArgs, null, null, null);
                break;

            case SCHOOLS:
            case SCHOOLS_ID:
                c = database.query(DatabaseHelper.TABLE_SCHOOLS, DatabaseHelper.ALL_COLUMNS_SCHOOLS, selection,
                        selectionArgs, null, null, null);
                break;

            case COURSES:
            case COURSES_ID:
                c = database.query(DatabaseHelper.TABLE_COURSES, DatabaseHelper.ALL_COLUMNS_COURSES, selection,
                        selectionArgs, null, null, null);
                break;


            case DEGREES:
            case DEGREES_ID:
                c = database.query(DatabaseHelper.TABLE_DEGREES, DatabaseHelper.ALL_COLUMNS_DEGREES, selection,
                        selectionArgs, null, null, null);
                break;

            case TERMS:
            case TERMS_ID:
                c = database.query(DatabaseHelper.TABLE_TERMS, DatabaseHelper.ALL_COLUMNS_TERMS, selection,
                        selectionArgs, null, null, null);
                break;

            case TRANSCRIPTS:
            case TRANSCRIPTS_ID:
                c = database.query(DatabaseHelper.TABLE_TRANSCRIPTS, DatabaseHelper.ALL_COLUMNS_TRANSCRIPTS, selection,
                        selectionArgs, null, null, null);
                break;

            case ACADEMIC_RECORDS:
            case ACADEMIC_RECORDS_ID:
                c = database.query(DatabaseHelper.TABLE_ACADEMIC_RECORDS, DatabaseHelper.ALL_COLUMNS_ACADEMIC_RECORDS, selection,
                        selectionArgs, null, null, null);
                break;



        }
        return c;



    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Uri u = null;

        switch (uriMatcher.match(uri)) {
            case PROGRAM:

                long id = database.insert(DatabaseHelper.TABLE_PROGRAM, null, values
                );

                u = Uri.parse(PROGRAM_PATH + "/" + id);
                break;

            case COURSES:

                long id2 = database.insert(DatabaseHelper.TABLE_COURSES, null, values
                );

                u = Uri.parse(COURSES_PATH + "/" + id2);
                break;

            case SCHOOLS:

                long id3 = database.insert(DatabaseHelper.TABLE_SCHOOLS, null, values
                );

                u = Uri.parse(SCHOOLS_PATH+ "/" + id3);
                break;

            case DEGREES:

                long id4 = database.insert(DatabaseHelper.TABLE_DEGREES, null, values
                );

                u = Uri.parse(DEGREES_PATH+ "/" + id4);
                break;

            case TERMS:

                long id5 = database.insert(DatabaseHelper.TABLE_TERMS, null, values
                );

                u = Uri.parse(TERMS_PATH+ "/" + id5);
                break;

            case TRANSCRIPTS:

                long id6 = database.insert(DatabaseHelper.TABLE_TRANSCRIPTS, null, values
                );

                u = Uri.parse(TRANSCRIPTS_PATH+ "/" + id6);
                break;

            case ACADEMIC_RECORDS:

                long id7 = database.insert(DatabaseHelper.TABLE_ACADEMIC_RECORDS, null, values
                );

                u = Uri.parse(TRANSCRIPTS_PATH+ "/" + id7);
                break;


        }
        return u;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int i = 0;

        switch (uriMatcher.match(uri)) {
            case PROGRAM:
                i = database.delete(DatabaseHelper.TABLE_PROGRAM, selection, selectionArgs);
                break;

            case TRANSCRIPTS:
            case TRANSCRIPTS_ID:
                i = database.delete(DatabaseHelper.TABLE_TRANSCRIPTS, selection, selectionArgs);
                break;
//
//            case COURSE_MENTORS:
//                i = database.delete(DBOpenHelper.TABLE_COURSE_MENTORS, selection, selectionArgs);
//                break;
//
//            case ASSESSMENTS:
//                i = database.delete(DBOpenHelper.TABLE_ASSESSMENTS, selection, selectionArgs);
//                break;
//
//
//
        }
        return i;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int i = 0;

        switch (uriMatcher.match(uri)) {
            case PROGRAM:
                i = database.update(DatabaseHelper.TABLE_PROGRAM, values, selection, selectionArgs);
                break;

            case COURSES:
                i = database.update(DatabaseHelper.TABLE_COURSES, values,  selection, selectionArgs);
                break;

            case SCHOOLS:
                i = database.update(DatabaseHelper.TABLE_SCHOOLS, values, selection, selectionArgs);
                break;

            case DEGREES:
                i = database.update(DatabaseHelper.TABLE_DEGREES, values, selection, selectionArgs);
                break;

            case TERMS:
                i = database.update(DatabaseHelper.TABLE_TERMS, values, selection, selectionArgs);
                break;

            case TRANSCRIPTS:
                i = database.update(DatabaseHelper.TABLE_TRANSCRIPTS, values, selection, selectionArgs);
                break;

            case ACADEMIC_RECORDS:
                i = database.update(DatabaseHelper.TABLE_ACADEMIC_RECORDS, values, selection, selectionArgs);
                break;


        }
        return i;
    }
}
