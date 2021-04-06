package jessnguyen.mohawk.bingelist;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {

    private final ArrayList<RowItem> singleRow;
    private LayoutInflater thisInflater;

    public static final String TAG = "==ARRAY ADAPTER==";

    /**
     * Constructor for the custom array apdater.
     *
     * @param context   surrounding information of where the adapter is begin called. Reviews or favourites.
     * @param singleRow Array list of the row. Review or Favourites.
     */
    public MySimpleArrayAdapter(Context context, ArrayList singleRow) {
        super(context, R.layout.rowlayout, singleRow);
        this.singleRow = singleRow;
        thisInflater = (LayoutInflater.from(context));
    }

    /**
     * Get the rowlayout view
     *
     * @param position    current index of listView
     * @param convertView the view of the listView
     * @param parent      the group view of what is already showing
     * @return rowlayout view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = thisInflater.inflate(R.layout.rowlayout, parent, false);

            ImageView thePoster = (ImageView) convertView.findViewById(R.id.rowPoster);
            TextView theMovieTitle = (TextView) convertView.findViewById(R.id.rowMovieTitle);
            TextView theYear = (TextView) convertView.findViewById(R.id.rowYear);
            TextView theComment = (TextView) convertView.findViewById(R.id.rowComment);
            TextView theRating = (TextView) convertView.findViewById(R.id.rowRating);
            RowItem currentRow = singleRow.get(position);

            String title = currentRow.getMovieTitle();
            String year = currentRow.getYear();
            String comment = currentRow.getComment();
            String rating = currentRow.getRating().toString();

            theMovieTitle.setText(title);
            theYear.setText(year);
            if (rating != null) {
                theRating.setText("Rating: " + rating);
            } else if (rating == null) {
                theRating.setText("");
            }
            Log.d(TAG, "COMMENT: " + comment);
            if (comment != null) {
                theComment.setText("Comment: " + comment);
            } else if (comment == null) {
                theComment.setText("");
            }
            try {
                URL imageurl = new URL(currentRow.getPoster());
                Picasso.with(getContext()).load(String.valueOf(imageurl)).fit().into(thePoster);
            } catch (Exception e) {
                Log.d(TAG, "CAUGHT ERROR: " + e);
            }
        }
        return convertView;
    }

    /**
     * @return size length of the array
     */
    public int getCount() {
        return singleRow.size();
    }

    /**
     * @param position the index of the array
     * @return index of array
     */
    public long getItemId(int position) {
        return position;
    }
}