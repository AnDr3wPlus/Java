/**
 * Project: a01063367_assignment02
 * File: BookDao.java
 * Date: Nov 18, 2018
 * Time: 07:17:28 AM
 */
package a01063367.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.ApplicationException;
import a01063367.data.Book;
import a01063367.io.BookReader;

/**
 * @author Andrew Wu, A01063367
 *
 */
public class BookDao extends Dao {
    
    public static final String BOOK_TABLE_NAME = DbConstants.TABLE_ROOT + "Books";
    
    private static Logger LOG = LogManager.getLogger();
    
    
    /**
     * Constructor
     * 
     * @param database
     *             the database to use for BookDao
     * @throws ApplicationException 
     */
    public BookDao(Database database) throws ApplicationException {
        super(database, BOOK_TABLE_NAME);
        
        this.load();
    }
    
    /**
     * @param customerDataFile
     * @throws ApplicationException
     * @throws SQLException
     */
    public void load() throws ApplicationException {
        //File customerDataFile = new File(BOOOKS_DATA_FILENAME);
        
        try {
            if (!Database.tableExists(BookDao.BOOK_TABLE_NAME) || Database.dbTableDropRequested()) {
                if (Database.tableExists(BookDao.BOOK_TABLE_NAME) && Database.dbTableDropRequested()) {
                    drop();
                }
                
                //BookReader.read();
                
                create();
                
                LOG.debug("Inserting the books");
                /*
                if (!customerDataFile.exists()) {
                    throw new ApplicationException(String.format("Required '%s' is missing.", CUSTOMERS_DATA_FILENAME));
                }
                */
                BookReader.read(BookReader.BOOKS_FILENAME, this);
                //BookReader.read();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationException(e);
        }
    }
    
    /**
     * Create tables
     * 
     * @throws SQLException
     * 
     */
    @Override
    public void create() throws SQLException {
        LOG.debug("Creating database table " + BOOK_TABLE_NAME);
        
        String sqlString = String.format("CREATE TABLE %s(" // BOOK_TABLE
                + "%s BIGINT, " // 1. ID
                + "%s VARCHAR(%d), " // 2. ISBN
                + "%s VARCHAR(%d), " // 3. AUTHORS
                + "%s INTEGER, " // 4. YEARS
                + "%s VARCHAR(%d), " // 5. TITLE
                + "%s FLOAT, " // 6. RATING
                + "%s INTEGER, " // 7. RATING_COUNT
                + "%s VARCHAR(%d), " // 8. IMAGE_URL
                + "PRIMARY KEY (%s))", // Primary key: ID
                BOOK_TABLE_NAME, // BOOK_TABLE
                Column.ID.name, // 1
                Column.ISBN.name, Column.ISBN.length, // 2
                Column.AUTHORS.name, Column.AUTHORS.length, // 3
                Column.YEARS.name, // 4
                Column.TITLE.name, Column.TITLE.length, // 5
                Column.RATING.name, // 6
                Column.RATING_COUNT.name, // 7
                Column.IMAGE_URL.name, Column.IMAGE_URL.length, // 8
                Column.ID.name); // Primary key: ID
        
        super.create(sqlString);
    }
    
    /**
     * Add a Book to the database
     * 
     * @param book
     *            the book to be added to the database
     * @throws SQLException
     * 
     */
    public void add(Book book) throws SQLException {
        String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?)", BOOK_TABLE_NAME);
        LOG.debug(String.format("Adding: %s to the database", book));
        @SuppressWarnings("unused")
        boolean result = execute(sqlString,
                book.getId(), // 1. ID
                book.getIsbn(), // 2. ISBN
                book.getAuthors(), // 3. AUTHORS
                book.getYear(), // 4. YEARS
                book.getTitle(), // 5. TITLE
                book.getRating(), // 6. RATING
                book.getRatingsCount(), // 7. RATING_COUNT
                book.getImageUrl()); // 8. IMAGE_URL
        
        //LOG.debug(String.format("Adding %s was %s", book, result ? "successful" : "unsuccessful"));
        
        LOG.debug(String.format("Adding: %s to the database", book));
    }
    
    /**
     * Update the book.
     * 
     * @param book
     *            the book to be updated in the database
     * @throws SQLException
     */
    public void update(Book book) throws SQLException {
        String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?", BOOK_TABLE_NAME,
                Column.ISBN.name, // 2. ISBN
                Column.AUTHORS.name, // 3. Authors
                Column.YEARS.name, // 4. YEARS
                Column.TITLE.name, // 5. TITLE
                Column.RATING.name, // 6. RATING
                Column.RATING_COUNT.name, // 7. RATING_COUNT
                Column.IMAGE_URL.name, // 8.IMAGE_URL
                Column.ID.name); // ID
                
        LOG.debug("Update statment: " + sqlString);
        
        boolean result = execute(sqlString, book.getId(), sqlString, book.getIsbn(), book.getYear(), book.getTitle(),
                book.getRating(), book.getRatingsCount(), book.getImageUrl());
        LOG.debug(String.format("Updating %s was %s", book, result ? "successful" : "unsuccessful"));
    }
    
    /**
     * Delete the book from the database.
     * 
     * @param book
     *            the book to be deleted in the database
     * @throws SQLException
     */
    public void delete(Book book) throws SQLException {
        Connection connection;
        Statement statement = null;
        try {
            connection = Database.getConnection();
            statement = connection.createStatement();

            String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", BOOK_TABLE_NAME, Column.ID.name, book.getId());
            LOG.debug(sqlString);
            int rowcount = statement.executeUpdate(sqlString);
            LOG.debug(String.format("Deleted %d rows", rowcount));
        } finally {
            close(statement);
        }
    }
    
    /**
     * Retrieve all the book IDs from the database
     * 
     * @return the list of book IDs
     * @throws SQLException
     */
    public List<Long> getBookIds() throws SQLException {
        List<Long> bookIDs = new ArrayList<>();
        
        String selectString = String.format("SELECT %s FROM %s", Column.ID.name, BOOK_TABLE_NAME);
        LOG.debug(selectString);
        
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Connection connection = Database.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectString);

            while (resultSet.next()) {
                bookIDs.add(resultSet.getLong(Column.ID.name));
            }
            
        } finally {
            close(statement);
        }
        
        LOG.debug(String.format("Loaded %d book IDs from the database", bookIDs.size()));
        
        return bookIDs;
    }
    
    /**
     * Retrieve a book from database
     * 
     * @param bookId
     *            the id of the book to be retrieved
     * @return book
     *         the book object
     * @throws Exception
     */
    public Book getBook(Long bookId) throws Exception {
        String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", BOOK_TABLE_NAME, Column.ID.name, bookId);
        LOG.debug(sqlString);
        
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Connection connection = Database.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlString);
            
            int count = 0;
            while (resultSet.next()) {
                count++;
                if (count > 1) {
                    throw new ApplicationException(String.format("Expected one result, got %d", count));
                }
                
                Book book = new Book.Builder(resultSet.getInt(Column.ID.name)) //
                        .setIsbn(resultSet.getString(Column.ISBN.name)) //
                        .setAuthors(resultSet.getString(Column.AUTHORS.name)) //
                        .setYear(resultSet.getInt(Column.YEARS.name)) //
                        .setTitle(resultSet.getString(Column.TITLE.name)) //
                        .setRating(resultSet.getFloat(Column.RATING.name)) //
                        .setRatingsCount(resultSet.getInt(Column.RATING_COUNT.name))
                        .setImageUrl(resultSet.getString(Column.IMAGE_URL.name)).build(); //
                        
                
                return book;
            }
        } finally {
            close(statement);
        }
        
        return null;
    }
    
    /**
     * Count the number of the customers in the database
     * 
     * @return count
     *         the count of the customers in the database
     * @throws Exception
     */
    public int countAllBooks() throws Exception {
        Statement statement = null;
        int count = 0;
        try {
            Connection connection = Database.getConnection();
            statement = connection.createStatement();
            // Execute a statement
            String sqlString = String.format("SELECT COUNT(*) AS total FROM %s", tableName);
            ResultSet resultSet = statement.executeQuery(sqlString);
            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }
        } finally {
            close(statement);
        }
        return count;
    }
    
    /**
     * Enum declaration for the column of Book DAO
     */
    public enum Column {
        ID("id", 10),
        ISBN("isbn", 20),
        AUTHORS("authors", 90),
        YEARS("years", 10),
        TITLE("title", 90),
        RATING("rating", 10),
        RATING_COUNT("ratingCount", 10),
        IMAGE_URL("imageURL", 90);
        
        String name;
        int length;
        
        /**
         * Private Constructor for the column of Customer DAO
         */
        private Column(String name, int length) {
            this.name = name;
            this.length = length;
        }
    }
    
}
