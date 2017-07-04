package com.example.hsport.unofficialevaluationproject;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zachzehr on 6/14/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.hsport.unofficialevaluationproject/databases/";

    private static String DB_NAME = "eval_app_database.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    // Table names
    public static final String TABLE_PROGRAM = "program";
    public static final String TABLE_SCHOOLS = "schools";
    public static final String TABLE_COURSES = "courses";
    public static final String TABLE_DEGREES = "degrees";
    public static final String TABLE_TERMS = "terms";
    public static final String TABLE_TRANSCRIPTS = "transcripts";
    public static final String TABLE_ACADEMIC_RECORDS = "academic_records";



    // Common column name
    public static final String KEY_ID = "_id";
    public static final String KEY_SCHOOL_ID = "school_id";
    public static final String KEY_TERM_ID = "term_id";


    // PROGRAM Table - column names
    public static final String KEY_PROGRAM_NAME = "program_name";
    public static final String KEY_SELECTED = "selected";

    // SCHOOLS Table - column names
    public static final String KEY_SCHOOL_NAME = "school_name";

    // COURSES Table - column names
    public static final String KEY_COURSE_NAME = "course_name";

    // DEGREES Table - column names
    public static final String KEY_DEGREE_NAME = "degree_name";

    // TERMS Table - column names
    public static final String KEY_TERM_NAME = "term_name";

    // TRANSCRIPTS Table - column names
    public static final String KEY_ACADEMIC_RECORD_ID = "academic_record_id";
    public static final String KEY_DEGREE_OR_COURSE_NAME = "degree_or_course_name";

    // ACADEMIC_RECORDS Table - column names
    public static final String KEY_PROGRAM_ID = "program_id";
    public static final String KEY_COURSE_ID = "course_id";
    public static final String KEY_DEGREE_ID = "degree_id";
    public static final String KEY_CREDIT_AWARDED = "credit_awarded";



    // PROGRAM Table - column selectors
    public static final String[] ALL_COLUMNS_PROGRAM = {KEY_ID, KEY_PROGRAM_NAME, KEY_SELECTED};
    public static final String[] ALL_COLUMNS_SCHOOLS = {KEY_ID, KEY_SCHOOL_NAME};
    public static final String[] ALL_COLUMNS_COURSES = {KEY_ID, KEY_COURSE_NAME, KEY_SCHOOL_ID, KEY_TERM_ID};
    public static final String[] ALL_COLUMNS_DEGREES = {KEY_ID, KEY_DEGREE_NAME, KEY_SCHOOL_ID, KEY_TERM_ID};
    public static final String[] ALL_COLUMNS_TERMS = {KEY_ID, KEY_TERM_NAME, KEY_SCHOOL_ID};
    public static final String[] ALL_COLUMNS_TRANSCRIPTS = {KEY_ID, KEY_SCHOOL_NAME,
            KEY_TERM_NAME, KEY_DEGREE_OR_COURSE_NAME, KEY_TERM_ID, KEY_DEGREE_ID, KEY_COURSE_ID};
    public static final String[] ALL_COLUMNS_ACADEMIC_RECORDS = {KEY_ID, KEY_PROGRAM_ID, KEY_SCHOOL_ID,
            KEY_TERM_ID, KEY_COURSE_ID, KEY_DEGREE_ID, KEY_CREDIT_AWARDED};




    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        try {
            this.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

// Add your public helper methods to access and get content from the database.
// You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
// to you to create adapters for your views.






}

