package jessnguyen.mohawk.bingelist;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public class MyDbHelper extends SQLiteOpenHelper {
    public static final String TAG = "==MyDbHelper==";
    /**
     * collection of DataBase field names
     **/
    public static final String DATABASE_FILE_NAME = "MyDatabase.db";
    public static final int DATABASE_VERSION = 1;
    public static final String MYFAVOURITESTABLE = "my_favourites_table";
    public static final String MYREVIEWTABLE = "my_review_table";
    public static final String MYPROFILETABLE = "my_profile_table";

    public static final String ID = "_id";
    public static final String MOVIETITLE = "movie_title";
    public static final String YEAR = "year";
    public static final String IMAGE = "image";
    public static final String COMMENT = "comment";
    public static final String RATING = "rating";
    public static final String PROFILE = "profile_image";
    public static final String DEFAULTPROFILE = "https://barkpost.com/wp-content/uploads/2014/06/DOG-2-superJumbo.jpg";


    // my_favourites_table table create statement
    private static final String SQL_CREATE_FAVOURITES =
            "CREATE TABLE " + MYFAVOURITESTABLE + " ( " + ID + " INTEGER PRIMARY KEY, " +
                    MOVIETITLE + " TEXT, " + YEAR + " TEXT," + IMAGE + " TEXT," + RATING + " INTEGER" + ")";


    // my_review_table table create statement
    private static final String SQL_CREATE_REVIEW =
            "CREATE TABLE " + MYREVIEWTABLE + " ( " + ID + " INTEGER PRIMARY KEY, " +
                    MOVIETITLE + " TEXT, " + YEAR + " TEXT, " + IMAGE + " TEXT," + RATING + " INTEGER," + COMMENT + " TEXT)";

    // my_profile_table table create statement
    private static final String SQL_CREATE_PROFILE =
            "CREATE TABLE " + MYPROFILETABLE + " ( " + ID + " INTEGER PRIMARY KEY, " + PROFILE + " URL)";

    // Insert default profile to profile table
    private static final String SQL_INSERT_DEFAULT_PROFILE =
            "INSERT INTO " + MYPROFILETABLE + " ( " + ID + ", " + PROFILE + " ) VALUES ( 0,'" + DEFAULTPROFILE + "')";

    public MyDbHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "DEAFULT PROFILE: " + DEFAULTPROFILE);
        Log.d(TAG, "onCreate " + SQL_CREATE_FAVOURITES);
        Log.d(TAG, "onCreate " + SQL_CREATE_REVIEW);
        Log.d(TAG, "onCreate " + SQL_CREATE_PROFILE);
        Log.d(TAG, "onCreate " + SQL_INSERT_DEFAULT_PROFILE);
        // creating required tables
        db.execSQL(SQL_CREATE_FAVOURITES);
        db.execSQL(SQL_CREATE_REVIEW);
        db.execSQL(SQL_CREATE_PROFILE);
        db.execSQL(SQL_INSERT_DEFAULT_PROFILE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + MYFAVOURITESTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MYREVIEWTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MYPROFILETABLE);

        // create new tables
        onCreate(db);
    }
}
