import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @description - This program prints IMDb's Top 250 movies of all time
 * ...by retrieving information from the official IMDb website, using the Jsoup library.
 *
 * @author - Paras Nath Seth
 */

public class IMDb {
    public static void main(String[] args) throws IOException, FileNotFoundException {

        Document doc = (Document) Jsoup.connect("https://www.imdb.com/chart/top/").get();

        //Initializing an ArrayList which can hold 'Movie' objects of the 'Movie' class
        ArrayList<Movie> movies = new ArrayList<Movie>();

        // As of 2023-01-14, the DOM of the IMDb website is structured so that the table of elements we want to scrape is contained
        // in an HTML element called '<tbody class="lister-list">'

        //We will 'scrape' all 250 movies contained inside this HTML element and store it in variable 'body'
        Elements body = doc.select("tbody.lister-list");

        //Here, we are iterating through each row of the list of movies we just collected
        for(Element movie : body.select("tr")) {

            /* Extracting key details from the website based on the website's DOM structure */

            String movieRank = String.valueOf(movie.select("td.titleColumn").textNodes().get(0));
            System.out.print(movieRank + '\t');

            String movieName = movie.select("td.titleColumn a").text();
            System.out.print(movieName);

            String year = movie.select("td.titleColumn span.secondaryInfo").text().substring(1, 5);
            System.out.print('\t' + year + "\t");

            String rating = movie.select("td.ratingColumn.imdbRating").text().trim();
            System.out.println(rating);

            movies.add(new Movie(movieRank, movieName, year, rating));
        }

        //Creating a Blank Excel Sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("IMDb Top 250");

        String[] columnHeaders = {"Rank", "Film Name", "Year of Release", "IMDb Rank"};

        //Here we are defining the font style for the header
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLUE1.getIndex());

        //Defining the style for the header cells
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        //Creating a new row at the 0th index for the Headers
        Row headerRow = sheet.createRow(0);

        for(int i=0; i < columnHeaders.length; i++){

            //Setting the value of each header cell with the respective heading for each column
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnHeaders[i]);
            cell.setCellStyle(headerCellStyle);
        }

        int rowIndex = 1;

        for(Movie movie : movies){

            //Creating a new record (row) which can be populated with data from the 'movie' variable
            Row row = sheet.createRow(rowIndex);
            rowIndex++;

            //Creating a new cell for each attribute and setting the value of that cell to the appropriate value
            row.createCell(0).setCellValue(movie.rank);
            row.createCell(1).setCellValue(movie.name);
            row.createCell(2).setCellValue(movie.releaseYear);
            row.createCell(3).setCellValue(movie.rating);

        }

        //After all the data has been entered into the appropriate cells, we can automatically resize the relevant columns
        for(int i=0; i < columnHeaders.length; i++){
            sheet.autoSizeColumn(i);
        }

        //Creating a FileOutputStream instance to generate the .xlsx file
        FileOutputStream fileOut = new FileOutputStream("IMDb Top 250 Data.xlsx");

        workbook.write(fileOut);

        fileOut.close();
        workbook.close();

    }
}