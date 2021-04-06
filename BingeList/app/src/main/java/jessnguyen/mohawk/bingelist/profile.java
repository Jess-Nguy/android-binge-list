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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "==PROFILE ACTIVITY==";

    private DrawerLayout myDrawer;
    NavigationView myNavView;

    // Create a global instance of our SQL Helper class
    MyDbHelper mydbhelp = new MyDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        navBarView();
        setNavProfileImage();
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
                Intent switchActivityBrowse = new Intent(profile.this, MainActivity.class);
                startActivity(switchActivityBrowse);
                break;
            case R.id.favouriteMenuItem:
                // switch fragment
                Log.d(TAG, "Favourite Menu Item");
                Intent switchActivityFavourites = new Intent(profile.this, favourites.class);
                startActivity(switchActivityFavourites);
                break;
            case R.id.reviewMenuItem:
                // switch fragment
                Log.d(TAG, "Review Menu Item");
                Intent switchActivityReviews = new Intent(profile.this, reviews.class);
                startActivity(switchActivityReviews);
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
     * When a button is clicked check what was click and do the following.
     * searchImageButton - Show the url image
     * setProfileButton - Update the url string that is stored in the database
     * @param view activity_profile.xml
     */
    public void onClick(View view) {
        EditText profileURL = findViewById(R.id.editTextProfileURL);
        String url = profileURL.getText().toString();
        switch (view.getId()) {
            case R.id.searchImageButton:
                ImageView setProfileImage = findViewById(R.id.setProfileImage);
                try {
                    URL imageurl = new URL(url);
                    Picasso.with(view.getContext()).load(String.valueOf(imageurl)).fit().into(setProfileImage);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.setProfileButton:
                Log.d(TAG, "SET IMAGE");
                // Get an instance of the database using our helper class
                SQLiteDatabase db = mydbhelp.getWritableDatabase();
                // A projection defines what fields we want to retrieve.
                String[] projection = {MyDbHelper.ID, MyDbHelper.PROFILE};
                // db.query will retreive the data and return a Cursor to access it
                Cursor mycursor = db.query(MyDbHelper.MYPROFILETABLE, projection, null,
                        null, null, null, null);
                if (mycursor.moveToFirst()) {
                    // A ContentValues class provides an easy helper function to add our values
                    ContentValues values = new ContentValues();

                    // Similar to using a bundle - put values using column name and value
                    values.put(MyDbHelper.PROFILE, url);
                    // Get the id for update
                    String id = mycursor.getString(
                            mycursor.getColumnIndex(MyDbHelper.ID));

                    // The db.update will updated the url for the image
                    long newrowID = db.update(MyDbHelper.MYPROFILETABLE, values, "_id = ?",new String[] {id} );
                    Log.d(TAG, "New ID " + newrowID);
                    setNavProfileImage();
                }

                break;
        }
    }


    /**
     * Fetch the url from the database and setting the header nav profile image.
     */
    public void setNavProfileImage (){
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

