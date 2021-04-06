package jessnguyen.mohawk.bingelist;

public class Movies {
    public String _id;
    public String Year;
    public String Title;
    public String Rated;
    public String Plot;
    public String Actors;
    public String Genre;
    public String Poster;
    public String Error;
    @Override
    public String toString() {
        return "Movie title: " + Title + "\nRated: " + Rated + "    Year: " + Year  + "\nGenre: " + Genre + "\nActors: " + Actors + "\nPlot: " + Plot ;
    }
}
