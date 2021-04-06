package jessnguyen.mohawk.bingelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class browser extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final String TAG = "==Browser==";

    private DrawerLayout myDrawer;
    NavigationView myNavView;

    String responseTitle;
    String responseYear;
    static String responsePoster;

    // Create a global instance of our SQL Helper class
    MyDbHelper mydbhelp = new MyDbHelper(this);

    Spinner ratingSpinner;
    String uri;
    Integer rating;
    TextView ratingHeading;
    ImageButton favouriteButton;
    EditText commentEditText;
    Button addCommentButton;
    ImageView moviePoster;
    TextView movieDataText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        navBarView();
        setNavProfileImage();
        postURL();
        // Setting all elements for the this page for showing visibility on and off
        ratingSpinner = findViewById(R.id.spinner3);
        ratingSpinner.setSelection(0, false);
        ratingSpinner.setOnItemSelectedListener(this);
        ratingHeading = findViewById(R.id.personalRatingHeading);
        favouriteButton = findViewById(R.id.imageButton2);
        commentEditText = findViewById(R.id.editTextComment2);
        addCommentButton = findViewById(R.id.button3);
    }

    /**
     * Show nav bar view.
     */
    public void navBarView() {
        // Access myDrawer
        myDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Access the ActionBar, enable "home" icon
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.setDisplayHomeAsUpEnabled(true);

        // ActionBarDrawerToggle element
        ActionBarDrawerToggle myactionbartoggle = new
                ActionBarDrawerToggle(this, myDrawer,
                (R.string.open), (R.string.close));
        myDrawer.addDrawerListener(myactionbartoggle);
        myactionbartoggle.syncState();

        // the callback method for Navigation View
        myNavView = (NavigationView)
                findViewById(R.id.nav_view);
        myNavView.setNavigationItemSelectedListener(this);
    }

    /**
     * Get the url ready for retrieving data.
     */
    public void postURL() {
        Intent intent = getIntent();
        String urlTitle = intent.getStringExtra("movieTitle");
        String userInputYear = intent.getStringExtra("movieYear");
        // Build call to Webservice
        uri = "http://www.omdbapi.com/?apikey=a5b24c9";
        Log.d(TAG, "title: " + urlTitle);
        if (!urlTitle.isEmpty()) {
            Log.d(TAG, "user input title: " + urlTitle);
            uri += "&t=" + urlTitle;
        }
        // Cannot search by year without movie title.
        if (!userInputYear.isEmpty() && !urlTitle.isEmpty()) {
            Log.d(TAG, "user input year: " + userInputYear);
            uri += "&y=" + userInputYear;

        }
        getData();
    }

    /**
     * Retrieve data set movie data - picture, movie info and display elements.
     */
    public void getData() {
        StringRequest myRequest = new StringRequest(Request.Method.GET, uri,
                response -> {
                    try {
                        Movies movies = new Movies();
                        if (response == null) {
                            Log.d(TAG, "no results");
                        } else {
                            Gson gson = new Gson();
                            movies = gson.fromJson(response, Movies.class);
                        }
                        String movieString = movies.toString();

                        movieDataText = findViewById(R.id.movieDataTextView);
                        moviePoster = findViewById(R.id.movieHolder);
                        if (movies.Error == null) {
                            responseTitle = movies.Title;
                            responseYear = movies.Year;
                            responsePoster = movies.Poster;
                            try {
                                URL imageurl = new URL(movies.Poster);
                                Picasso.with(this).load(String.valueOf(imageurl)).fit().into(moviePoster);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            movieDataText.setText(movieString);
                            moviePoster.setVisibility(View.VISIBLE);
                            ratingHeading.setVisibility(View.VISIBLE);
                            ratingSpinner.setVisibility(View.VISIBLE);
                            favouriteButton.setVisibility(View.VISIBLE);
                            commentEditText.setVisibility(View.VISIBLE);
                            addCommentButton.setVisibility(View.VISIBLE);
                        } else {
                            movieDataText.setText(movies.Error);
                            moviePoster.setVisibility(View.GONE);
                            ratingHeading.setVisibility(View.GONE);
                            ratingSpinner.setVisibility(View.GONE);
                            favouriteButton.setVisibility(View.GONE);
                            commentEditText.setVisibility(View.GONE);
                            addCommentButton.setVisibility(View.GONE);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, volleyError -> Log.d(TAG, "ERROR VOLLEY")
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(myRequest);
    }

    /**
     * Add the movie searched to favourites database table.
     *
     * @param view activity_browser.xml
     */
    public void favouritesDb(View view) {
        // Get an instance of the database using our helper class
        SQLiteDatabase db = mydbhelp.getWritableDatabase();
        // A ContentValues class provides an easy helper function to add our values
        ContentValues values = new ContentValues();

        // Similar to using a bundle - put values using column name and value
        values.put(MyDbHelper.MOVIETITLE, responseTitle);
        values.put(MyDbHelper.YEAR, responseYear);
        values.put(MyDbHelper.IMAGE, responsePoster);
        values.put(MyDbHelper.RATING, rating);
        Log.d(TAG, "VALUES: " + values);
        // The db.insert command will do a SQL insert on our table, return the new row ID
        long newrowID = db.insert(MyDbHelper.MYFAVOURITESTABLE, null, values);

        Log.d(TAG, "New ID " + newrowID);
    }

    /**
     * Add the movie search to review database table.
     *
     * @param view activity_browser.xml
     */
    public void reviewDb(View view) {
        // Get an instance of the database using our helper class
        SQLiteDatabase db = mydbhelp.getWritableDatabase();
        // A ContentValues class provides an easy helper function to add our values
        ContentValues values = new ContentValues();
        String comment = commentEditText.getText().toString();
        // Similar to using a bundle - put values using column name and value
        values.put(MyDbHelper.MOVIETITLE, responseTitle);
        values.put(MyDbHelper.YEAR, responseYear);
        values.put(MyDbHelper.IMAGE, responsePoster);
        values.put(MyDbHelper.COMMENT, comment);
        values.put(MyDbHelper.RATING, rating);
        Log.d(TAG, "VALUES: " + values);
        // The db.insert command will do a SQL insert on our table, return the new row ID
        long newrowID = db.insert(MyDbHelper.MYREVIEWTABLE, null, values);
        Log.d(TAG, "New ID " + newrowID);
    }

    /**
     * Switch activities when a certain menu item is selected.
     *
     * @param item selected item from nav bar
     * @return false
     */
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        myNavView.setCheckedItem(item);
        // Show visual for selection
        item.setChecked(true);
        // Close the Drawer
        myDrawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.browserMenuItem:
                Log.d(TAG, "Browser Menu Item");
                Intent switchActivityBrowse = new Intent(browser.this, MainActivity.class);
                startActivity(switchActivityBrowse);
                break;
            case R.id.favouriteMenuItem:
                Log.d(TAG, "Favourite Menu Item");
                Intent switchActivityFavourites = new Intent(browser.this, favourites.class);
                startActivity(switchActivityFavourites);
                break;
            case R.id.reviewMenuItem:
                Log.d(TAG, "Review Menu Item");
                Intent switchActivityReviews = new Intent(browser.this, reviews.class);
                startActivity(switchActivityReviews);
                break;
            case R.id.profileMenuItem:
                Log.d(TAG, "Profile Menu Item");
                Intent switchActivityProfile = new Intent(browser.this, profile.class);
                startActivity(switchActivityProfile);
                break;
        }
        return false;
    }

    /**
     * Selecting an item will close the nav bar. Otherwise open the nav bar.
     *
     * @param item selected item from nav bar
     * @return  the parent selected item.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Find out the current state of the drawer (open or closed)
        boolean isOpen = myDrawer.isDrawerOpen(GravityCompat.START);
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // Home button - open or close the drawer
                if (isOpen == true) {
                    myDrawer.closeDrawer(GravityCompat.START);
                } else {
                    myDrawer.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets global variable of the selected rated movie item in the spinner. So that it can be stored.
     *
     * @param parent   The spinner adapted view
     * @param view     activty_browser.xml
     * @param position spinner array index position
     * @param id       spinner position id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Load string array from resources
        String[] ratings = getResources().getStringArray(R.array.array_of_rating);
        // log the spinner's choice
        Log.d(TAG, "@@@ on rating: " + ratings[position]);
        rating = position;
    }

    /**
     * When nothing is selected log it.
     * We set the default value to be 0 already so nothing needs to be done here.
     * This is required with spinner.
     *
     * @param parent The spinner adapted view
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // log the spinner's choice
        Log.d(TAG, "Nothing Selected");
    }

    /**
     * On click method for button click.
     * This is empty because favouriteDb and reviewDb method is set for the buttons.
     * @param v activity_browser.xml
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * Fetch the url from the database and setting the header nav profile image.
     */
    public void setNavProfileImage() {
        // Get an instance of the database using our helper class
        SQLiteDatabase db = mydbhelp.getReadableDatabase();
        // A projection defines what fields we want to retrieve.
        String[] projection = {MyDbHelper.ID, MyDbHelper.PROFILE};
        // db.query will retreive the data and return a Cursor to access it
        Cursor mycursor = db.query(MyDbHelper.MYPROFILETABLE, projection, null,
                null, null, null, null);
        if (mycursor.moveToFirst()) {
            String profileImage = mycursor.getString(
                    mycursor.getColumnIndex(MyDbHelper.PROFILE));
            View vh = myNavView.getHeaderView(0);
            ImageView profileImageView = vh.findViewById(R.id.profileImage);
            try {
                URL imageurl = new URL(profileImage);
                Picasso.with(vh.getContext()).load(String.valueOf(imageurl)).fit().into(profileImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}