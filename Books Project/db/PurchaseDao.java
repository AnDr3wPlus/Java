/**
 * Project: a01063367_assignment02
 * File: PurchaseDao.java
 * Date: Nov 20, 2018
 * Time: 04:55:41 AM
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
import a01063367.data.Purchase;
import a01063367.io.PurchaseReader;

/**
 * @author Andrew Wu, A01063367
 *
 */
public class PurchaseDao extends Dao {

public static final String PURCHASE_TABLE_NAME = DbConstants.TABLE_ROOT + "Purchases";
    
    private static Logger LOG = LogManager.getLogger();
    
    
    /**
     * Constructor
     * 
     * @param database
     *             the database to use for PurchaseDao
     * @throws ApplicationException 
     */
    public PurchaseDao(Database database) throws ApplicationException {
        super(database, PURCHASE_TABLE_NAME);
        
        this.load();
    }
    
    /**
     * 
     * 
     * @throws ApplicationException
     * @throws SQLException
     */
    public void load() throws ApplicationException {
        //File customerDataFile = new File(BOOOKS_DATA_FILENAME);
        
        try {
            if (!Database.tableExists(PurchaseDao.PURCHASE_TABLE_NAME) || Database.dbTableDropRequested()) {
                if (Database.tableExists(PurchaseDao.PURCHASE_TABLE_NAME) && Database.dbTableDropRequested()) {
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
                PurchaseReader.read(PurchaseReader.PURCHASES_FILENAME, this);
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
        LOG.debug("Creating database table " + PURCHASE_TABLE_NAME);
        
        String sqlString = String.format("CREATE TABLE %s(" // PURCHASES_TABLE
                + "%s BIGINT, " // 1. ID
                + "%s BIGINT, " // 2. CUSTOMER ID
                + "%s BIGINT, " // 3. BOOK ID
                + "%s FLOAT, " // 4. PRICE
                + "PRIMARY KEY (%s))", // Primary key: ID
                PURCHASE_TABLE_NAME, // PURCHASES_TABLE
                Column.ID.name, // 1
                Column.CUSTOMER_ID.name, // 2
                Column.BOOK_ID.name, // 3
                Column.PRICE.name, // 4
                Column.ID.name); // Primary key: ID
        
        super.create(sqlString);
    }
    
    /**
     * Add a Purchase to the database
     * 
     * @param purchase
     *            the purchase to be added to the database
     * @throws SQLException
     * 
     */
    public void add(Purchase purchase) throws SQLException {
        String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?)", PURCHASE_TABLE_NAME);
        LOG.debug(String.format("Adding: %s to the database", purchase));
        @SuppressWarnings("unused")
        boolean result = execute(sqlString,
                purchase.getId(), // 1. ID
                purchase.getCustomerId(), // 2. CUSTOMER ID
                purchase.getBookId(), // 3. BOOK ID
                purchase.getPrice()); // 4. PRICE
        
        //LOG.debug(String.format("Adding %s was %s", purchase, result ? "successful" : "unsuccessful"));
        
        LOG.debug(String.format("Adding: %s to the database", purchase));
    }
    
    /**
     * Update the book.
     * 
     * @param book
     *            the book to be updated in the database
     * @throws SQLException
     */
    public void update(Purchase pruchase) throws SQLException {
        String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=? WHERE %s=?", PURCHASE_TABLE_NAME,
                Column.PRICE.name, // 2. PRICE
                Column.CUSTOMER_ID.name, // 3. CUSTOMER ID
                Column.BOOK_ID.name, // 4. BOOK ID
                Column.ID.name); // ID
                
        LOG.debug("Update statment: " + sqlString);
        
        boolean result = execute(sqlString, pruchase.getId(), sqlString,
                pruchase.getCustomerId(), // 2
                pruchase.getBookId(), // 3
                pruchase.getPrice(), // 4
                pruchase.getId());
        LOG.debug(String.format("Updating %s was %s", pruchase, result ? "successful" : "unsuccessful"));
    }
    
    /**
     * Delete the purchase from the database.
     * 
     * @param purchase
     *            the purchase to be deleted in the database
     * @throws SQLException
     */
    public void delete(Purchase purchase) throws SQLException {
        Connection connection;
        Statement statement = null;
        try {
            connection = Database.getConnection();
            statement = connection.createStatement();

            String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", PURCHASE_TABLE_NAME, Column.ID.name, purchase.getId());
            LOG.debug(sqlString);
            int rowcount = statement.executeUpdate(sqlString);
            LOG.debug(String.format("Deleted %d rows", rowcount));
        } finally {
            close(statement);
        }
    }
    
    /**
     * Retrieve all the purchase IDs from the database
     * 
     * @return the list of purchase IDs
     * @throws SQLException
     */
    public List<Long> getPurchaseIds() throws SQLException {
        List<Long> purchaseIDs = new ArrayList<>();
        
        String selectString = String.format("SELECT %s FROM %s", Column.ID.name, PURCHASE_TABLE_NAME);
        LOG.debug(selectString);
        
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Connection connection = Database.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectString);

            while (resultSet.next()) {
                purchaseIDs.add(resultSet.getLong(Column.ID.name));
            }
            
        } finally {
            close(statement);
        }
        
        LOG.debug(String.format("Loaded %d purchase IDs from the database", purchaseIDs.size()));
        
        return purchaseIDs;
    }
    
    /**
     * Retrieve a purchase from database
     * 
     * @param bookId
     *            the id of the book to be retrieved
     * @return book
     *         the book object
     * @throws Exception
     */
    public Purchase getPurchase(Long purchaseId) throws Exception {
        String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", PURCHASE_TABLE_NAME, Column.ID.name, purchaseId);
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
                
                Purchase purchase = new Purchase.Builder(resultSet.getInt(Column.ID.name), // ID
                        resultSet.getInt(Column.CUSTOMER_ID.name), // CUSTOMER ID
                        resultSet.getInt(Column.BOOK_ID.name), // BOOK ID
                        resultSet.getFloat(Column.PRICE.name) // PRICE
                        ).build();
                        
                
                return purchase;
            }
        } finally {
            close(statement);
        }
        
        return null;
    }
    
    /**
     * Count the number of the purchases in the database
     * 
     * @return count
     *         the count of the purchases in the database
     * @throws Exception
     */
    public int countAllPurchases() throws Exception {
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
     * Count the total of the purchases
     * 
     * @return
     *         amount of purchases as an int
     * @throws Exception
     *             if data is invalid
     */
    public double sumPurchasesAmount() throws Exception {
        Statement statement = null;
        double totalAmount = 0;
        try {
            Connection connection = Database.getConnection();
            statement = connection.createStatement();
            String sqlString = String.format("SELECT SUM(PRICE) AS total FROM %s", tableName);
            ResultSet resultSet = statement.executeQuery(sqlString);
            if (resultSet.next()) {
                totalAmount = resultSet.getFloat("total");
            }
        } finally {
            close(statement);
        }
        return totalAmount;
    }
    
    
    
    
    
    /**
     * Enum declaration for the column of Book DAO
     */
    public enum Column {
        ID("id", 10),
        CUSTOMER_ID("customerID", 10),
        BOOK_ID("bookID", 10),
        PRICE("price", 10);
        
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
