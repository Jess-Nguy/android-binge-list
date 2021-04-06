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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class favourites extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "==FAVOURITES ACTIVITY==";

    private DrawerLayout myDrawer;
    NavigationView myNavView;

    ArrayList<RowItem> myRowItems;
    ListView myListView;
    // Create a global instance of our SQL Helper class
    MyDbHelper mydbhelp = new MyDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        navBarView();
        setNavProfileImage();
        getData();

    }

    /**
     * Read the database table for favourite of a movie.
     * Used custom rowlayout to display image and text.
     */
    public void getData(){
        // Get an instance of the database using our helper class
        SQLiteDatabase db = mydbhelp.getReadableDatabase();
        // A projection defines what fields we want to retrieve.
        String[] projection = {MyDbHelper.ID, MyDbHelper.MOVIETITLE, MyDbHelper.YEAR, MyDbHelper.IMAGE, MyDbHelper.RATING};
        // db.query will retreive the data and return a Cursor to access it
        Cursor mycursor = db.query(MyDbHelper.MYFAVOURITESTABLE, projection, null,
                null, null, null, null);
        if (mycursor != null) {
            myRowItems = new ArrayList<RowItem>();
            myListView = findViewById(R.id.favouriteListView);
            // Loop through our returned results from the start
            while (mycursor.moveToNext()) {
                String title = mycursor.getString(
                        mycursor.getColumnIndex(MyDbHelper.MOVIETITLE));
                String year = mycursor.getString(mycursor.getColumnIndex(MyDbHelper.YEAR));
                String poster = mycursor.getString(mycursor.getColumnIndex(MyDbHelper.IMAGE));
                Integer rating = mycursor.getInt(mycursor.getColumnIndex(MyDbHelper.RATING));
                long itemID = mycursor.getLong(
                        mycursor.getColumnIndex(MyDbHelper.ID));
                Log.d(TAG, "Id: " + itemID);
                RowItem rowItem = new RowItem();
                rowItem.setMovieTitle(title);
                rowItem.setYear(year);
                rowItem.setPoster(poster);
                rowItem.setRating(rating);
                myRowItems.add(rowItem);
            }
            // Close the cursor when we're done
            mycursor.close();
        }
        if (myRowItems.isEmpty()) {
            Log.d(TAG, "EMPTY RESULTS");
        } else {
            MySimpleArrayAdapter myAdapter = new MySimpleArrayAdapter (getApplicationContext(), myRowItems);
            myListView.setAdapter(myAdapter);
        }
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
                Intent switchActivityBrowse = new Intent(favourites.this, MainActivity.class);
                startActivity(switchActivityBrowse);
                break;

            case R.id.reviewMenuItem:
                Log.d(TAG, "Review Menu Item");
                Intent switchActivityReviews = new Intent(favourites.this, reviews.class);
                startActivity(switchActivityReviews);
                break;
            case R.id.profileMenuItem:
                Log.d(TAG, "Profile Menu Item");
                Intent switchActivityProfile = new Intent(favourites.this, profile.class);
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