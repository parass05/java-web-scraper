import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * @description - This program prints IMDb's Top 250 movies of all time
 *                by retrieving information from the official IMDb website, using the Jsoup library.
 *
 * @author - Paras Nath Seth
 */

public class IMDb {
    public static void main(String[] args) throws IOException {

        Document doc = (Document) Jsoup.connect("https://www.imdb.com/chart/top/").get();

        // As of 2023-01-14, the DOM of the IMDb website is structured so that the table of elements we want to scrape is contained
        // in an HTML element called '<tbody class="lister-list">'

        //We will 'scrape' all 250 movies contained inside this HTML element and store it in variable 'body'
        Elements body = doc.select("tbody.lister-list");

        //Here, we are iterating through each row of the list of movies we just collected
        for(Element movie : body.select("tr")){

            /* Extracting key details from the website based on the website's DOM structure */

            String movieRank = String.valueOf(movie.select("td.titleColumn").textNodes().get(0));
            System.out.print(movieRank + '\t');

            String movieName = movie.select("td.titleColumn a").text();
            System.out.print(movieName);

            String year = movie.select("td.titleColumn span.secondaryInfo").text().substring(1,5);
            System.out.print('\t' + year + "\t");

            String rating = movie.select("td.ratingColumn.imdbRating").text().trim();
            System.out.println(rating);
        }
    }
}