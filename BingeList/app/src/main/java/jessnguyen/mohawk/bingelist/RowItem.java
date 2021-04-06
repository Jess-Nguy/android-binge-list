package jessnguyen.mohawk.bingelist;


public class RowItem {
    private String movieTitle;
    private String year;
    private String poster;
    private String comment;
    private int rating;

    /**
     * Set the movie title, used in browser.java for favouriteDb and reviewDb methods for inserting into database table.
     * @param theMovieTitle
     */
    public void setMovieTitle(String theMovieTitle){
        this.movieTitle = theMovieTitle;
    }

    /**
     * Get the movie title, used in browser.java for favouriteDb and reviewDb methods for inserting into database table.
     * @return
     */
    public String getMovieTitle(){
        return this.movieTitle;
    }

    /**
     * Set the year, used in browser.java for favouriteDb and reviewDb methods for inserting into database table.
     * @param theYear
     */
    public void setYear(String theYear){
        this.year = theYear;
    }

    /**
     * Get the year, used in browser.java for favouriteDb and reviewDb methods for inserting into database table.
     * @return
     */
    public String getYear(){
        return this.year;
    }

    /**
     * Set the poster, used in browser.java for favouriteDb and reviewDb methods for inserting into database table.
     * @param thePoster
     */
    public void setPoster(String thePoster){
        this.poster = thePoster;
    }

    /**
     * Get the poster , used in browser.java for favouriteDb and reviewDb methods for inserting into database table.
     * @return
     */
    public String getPoster(){
        return this.poster;
    }

    /**
     * Set the comment, used in browser.java for favouriteDb and reviewDb methods for inserting into database table.
     * @param theComment
     */
    public void setComment(String theComment){
        this.comment = theComment;
    }

    /**
     * Get the comment, used in browser.java for favouriteDb and reviewDb methods for inserting into database table.
     * @return
     */
    public String getComment(){
        return this.comment;
    }

    /**
     * Set the rating, used in browser.java for favouriteDb and reviewDb methods for inserting into database table.
     * @param theRating
     */
    public void setRating(Integer theRating){
        this.rating = theRating;
    }

    /**
     * Get the rating, used in browser.java for favouriteDb and reviewDb methods for inserting into database table.
     * @return
     */
    public Integer getRating(){
        return this.rating;
    }
}
