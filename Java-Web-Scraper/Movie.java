/**
 * This is a helper class for the IMDb Web Scraper Program (IMDb.java) that consolidates all the
 * ...relevant attributes of movies into one class, just so that we can operate on the movies as objects in the main class.
 *
 * @author - Paras Nath Seth
 */
public class Movie {

    public String rank, name, releaseYear, rating;

    public Movie(String rank, String name, String releaseYear, String rating) {
        this.rank = rank;
        this.name = name;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

}