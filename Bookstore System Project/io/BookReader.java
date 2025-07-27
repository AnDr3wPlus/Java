/**
 * Project: a01063367_assignment02
 * File: BookReader.java
 * Date: Nov 16, 2018
 * Time: 23:24:47 PM
 */

package a01063367.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.ApplicationException;
import a01063367.data.AllData;
import a01063367.data.Book;
import a01063367.db.BookDao;

/**
 * BookReader reads the book data file
 * 
 * @author Andrew Wu, A01063367
 *
 */
public class BookReader extends Reader {
    
    public static final String BOOKS_FILENAME = "books500.csv";
    
    private static final Logger LOG = LogManager.getLogger();
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private BookReader() {
    }
    
    /**
     * Read the book input data.
     * 
     * @return A collection of books.
     * @throws ApplicationException
     * @throws IOException
     */
    public static void read( String fileName, BookDao bookDao ) throws ApplicationException {
        File bookDataFile = new File(BOOKS_FILENAME);
        if (!bookDataFile.exists()) {
            throw new ApplicationException(String.format("Required '%s' is missing.", BOOKS_FILENAME));
        }
        
        FileReader in;
        Iterable<CSVRecord> records;
        try {
            in = new FileReader(bookDataFile);
            // Defining a header
            records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
        
        //Map<Long, Book> books = new HashMap<Long, Book>();
        
        LOG.debug("Reading: " + bookDataFile.getAbsolutePath());
        for (CSVRecord record : records) {
            String id = record.get("book_id");
            String isbn = record.get("isbn");
            String authors = record.get("authors");
            String original_publication_year = record.get("original_publication_year");
            String original_title = record.get("original_title");
            String average_rating = record.get("average_rating");
            float avgRating = Float.parseFloat(average_rating);
            String ratings_count = record.get("ratings_count");
            int ratingsCount = Integer.parseInt(ratings_count);
            String image_url = record.get("image_url");
            
            Book book = new Book.Builder(Long.parseLong(id)).
                    setIsbn(isbn).
                    setAuthors(authors).
                    setYear(Integer.parseInt(original_publication_year)).
                    setTitle(original_title).
                    setRating(avgRating).
                    setRatingsCount(ratingsCount).
                    setImageUrl(image_url).
                    build();
            //LOG.debug("Adding: " + book);
            try {
                bookDao.add(book);
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
            
            //books.put(book.getId(), book);
            AllData.books.put(book.getId(), book);
            
            LOG.debug("Added " + book.toString() + " as " + id);
            
        }
        
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                throw new ApplicationException(e);
            }
        }
        
        //AllData.books = books;
        //return books;
    }
    
}

/*
 * public static Map<Long, Book> read() throws ApplicationException {
 * File file = new File(FILENAME);
 * FileReader in;
 * Iterable<CSVRecord> records;
 * FileWriter out;
 * CSVPrinter printer;
 * try {
 * in = new FileReader(file);
 * records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
 * out = new FileWriter(new File("books500.csv"));
 * printer = new CSVPrinter(out, CSVFormat.DEFAULT);// CSVFormat.DEFAULT.print(new File("cleanbooks.csv"), charset);
 * } catch (IOException e) {
 * throw new ApplicationException(e);
 * }
 * for (CSVRecord record : records) {
 * String book_id = record.get("book_id");
 * String goodreads_book_id = record.get("goodreads_book_id");
 * String best_book_id = record.get("best_book_id");
 * String work_id = record.get("work_id");
 * String books_count = record.get("books_count");
 * String isbn = record.get("isbn");
 * String isbn13 = record.get("isbn13");
 * String authors = record.get("authors");
 * String original_publication_year = record.get("original_publication_year");
 * String original_title = record.get("original_title");
 * String title = record.get("title");
 * String language_code = record.get("language_code");
 * String average_rating = record.get("average_rating");
 * String ratings_count = record.get("ratings_count");
 * String work_ratings_count = record.get("work_ratings_count");
 * String work_text_reviews_count = record.get("work_text_reviews_count");
 * String ratings_1 = record.get("ratings_1");
 * String ratings_2 = record.get("ratings_2");
 * String ratings_3 = record.get("ratings_3");
 * String ratings_4 = record.get("ratings_4");
 * String ratings_5 = record.get("ratings_5");
 * String image_url = record.get("image_url");
 * String small_image_url = record.get("small_image_url");
 * if (original_publication_year.length() < 4) {
 * continue;
 * }
 * Object[] o = new Object[] { book_id, isbn, authors, original_publication_year.substring(0, 4), original_title, average_rating,
 * ratings_count, image_url };
 * try {
 * printer.printRecord(o);
 * } catch (IOException e) {
 * // TODO Auto-generated catch block
 * e.printStackTrace();
 * }
 * }
 */
