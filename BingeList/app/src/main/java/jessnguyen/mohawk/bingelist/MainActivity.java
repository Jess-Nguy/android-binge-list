package jessnguyen.mohawk.bingelist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.net.URL;

/*
I, Jess Nguyen, 000747411 certify that this material is my original work. No
other person's work has been used without due acknowledgement.

Video - https://youtu.be/kdZN74BiZVE
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "==MainActivity==";
    private DrawerLayout myDrawer;
    NavigationView myNavView;
    // Create a global instance of our SQL Helper class
    MyDbHelper mydbhelp = new MyDbHelper(this);

    EditText editTextTitle;
    String userInputTitle;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navBarView();
        setNavProfileImage();
        editTextTitle = findViewById(R.id.editTextMovie);
        searchButton = findViewById(R.id.searchButton);

        editTextTitle.addTextChangedListener(disableSearch);
        editTextTitle.setText("");
        Log.d(TAG, "onCreate");
    }

    /**
     * Send and change user input to url params for next activity.
     * @param view activity_main.xml
     */
    public void onClick(View view) {
        Log.d(TAG, "onClick ");
        userInputTitle = editTextTitle.getText().toString().trim();
        EditText editTextYear = findViewById(R.id.editTextYear);

        String urlTitle = userInputTitle.replaceAll(" ", "+");
        String userInputYear = editTextYear.getText().toString();

        Intent switchActivity = new Intent(MainActivity.this, browser.class);
        switchActivity.putExtra("movieTitle", urlTitle);
        switchActivity.putExtra("movieYear", userInputYear);
        startActivity(switchActivity);
    }

    /**
     * Watch for text change to disable search button.
     */
    private TextWatcher disableSearch = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userInputTitle = editTextTitle.getText().toString().trim();
            Log.d(TAG, "on text change: " + userInputTitle);
            searchButton.setEnabled(!userInputTitle.isEmpty());
            if (!userInputTitle.isEmpty()) {
                Log.d(TAG, "Is not empty: " + userInputTitle);
                searchButton.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * Switch activities when a certain menu item is selected.
     *
     * @param item selected item from nav bar
     * @return false
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        myNavView.setCheckedItem(item);
        // Show visual for selection
        item.setChecked(true);
        // Close the Drawer
        myDrawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.favouriteMenuItem:
                // switch fragment
                Log.d(TAG, "Favourite Menu Item");
                Intent switchActivityFavourites = new Intent(MainActivity.this, favourites.class);
                startActivity(switchActivityFavourites);
                break;
            case R.id.reviewMenuItem:
                // switch fragment
                Log.d(TAG, "Review Menu Item");
                Intent switchActivityReviews = new Intent(MainActivity.this, reviews.class);
                startActivity(switchActivityReviews);
                break;
            case R.id.profileMenuItem:
                Log.d(TAG, "Profile Menu Item");
                Intent switchActivityProfile = new Intent(MainActivity.this, profile.class);
                startActivity(switchActivityProfile);
                break;
        }
        return false;
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
     * Selecting an item will close the nav bar. Otherwise open the nav bar.
     *
     * @param item selected item from nav bar
     * @return the parent selected item.
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