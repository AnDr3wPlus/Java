/**
 * Project: a01063367_assignment02
 * File: CustomerDao.java
 * Date: Nov 17, 2018
 * Time: 01:09:10 AM
 */
package a01063367.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.ApplicationException;
import a01063367.data.Customer;
import a01063367.io.CustomerReader;

/**
 * Customer data access object (DAO)
 * 
 * @author Andrew Wu, A01063367
 *
 */
public class CustomerDao extends Dao {
    
    public static final String CUSTOMER_TABLE_NAME = DbConstants.TABLE_ROOT + "Customers";
    
    private static Logger LOG = LogManager.getLogger();
    
    
    /**
     * Constructor
     * 
     * @param database
     *             the database to use for CustomerDao
     * @throws ApplicationException 
     */
    public CustomerDao(Database database) throws ApplicationException {
        super(database, CUSTOMER_TABLE_NAME);

        load();
    }
    
    /**
     * @param customerDataFile
     * @throws ApplicationException
     * @throws SQLException
     */
    public void load() throws ApplicationException {
        //File customerDataFile = new File(CUSTOMERS_DATA_FILENAME);
        
        try {
            if (!Database.tableExists(CustomerDao.CUSTOMER_TABLE_NAME) || Database.dbTableDropRequested()) {
                if (Database.tableExists(CustomerDao.CUSTOMER_TABLE_NAME) && Database.dbTableDropRequested()) {
                    drop();
                }
                
                create();
                
                LOG.debug("Inserting the customers");
                /*
                if (!customerDataFile.exists()) {
                    throw new ApplicationException(String.format("Required '%s' is missing.", CUSTOMERS_DATA_FILENAME));
                }
                */
                CustomerReader.read(CustomerReader.CUSTOMERS_FILENAME, this); // read CUSTOMERS_FILENAME and parse it to CustomerDao
                //Map<Long, Customer> customerList = CustomerReader.read();
                //for ( Long customerID : customerList.keySet() )
                //CustomerDao.add(customerList.get(customerID));
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
        LOG.debug("Creating database table " + CUSTOMER_TABLE_NAME);
        
        // With MS SQL Server, JOINED_DATE needs to be a DATETIME type.
        String sqlString = String.format(
                "CREATE TABLE %s(" // CUSTOMER_TABLE_NAME
                + "%s BIGINT, " // 1. ID
                + "%s VARCHAR(%d), " // 2. FIRST_NAME
                + "%s VARCHAR(%d), " // 3. LAST_NAME
                + "%s VARCHAR(%d), " // 4. STREET
                + "%s VARCHAR(%d), " // 5. CITY
                + "%s VARCHAR(%d), " // 6. POSTAL_CODE
                + "%s VARCHAR(%d), " // 7. PHONE
                + "%s VARCHAR(%d), " // 8. EMAIL_ADDRESS
                + "%s DATETIME, " // 9. JOINED_DATE
                //+ "%s DATE, " // JOINED_DATE
                + "PRIMARY KEY (%s))", // Primary key: ID
                CUSTOMER_TABLE_NAME, // CUSTOMER_TABLE_NAME
                Column.ID.name, // 1
                Column.FIRST_NAME.name, Column.FIRST_NAME.length, // 2
                Column.LAST_NAME.name, Column.LAST_NAME.length, // 3
                Column.STREET.name, Column.STREET.length, // 4
                Column.CITY.name, Column.CITY.length, // 5
                Column.POSTAL_CODE.name, Column.POSTAL_CODE.length, // 6
                Column.PHONE.name, Column.PHONE.length, // 7
                Column.EMAIL_ADDRESS.name, Column.EMAIL_ADDRESS.length, // 8
                Column.JOINED_DATE.name, // 9
                Column.ID.name); // Primary key: ID
        
        super.create(sqlString);
    }
    
    /**
     * Add a Customer to the database
     * 
     * @param customer
     *            the customer to be added to the database
     * @throws SQLException
     * 
     */
    public void add(Customer customer) throws SQLException {
        String sqlString = String.format("INSERT INTO %s values(?, ?, ?, ?, ?, ?, ?, ?, ?)", CUSTOMER_TABLE_NAME);
        @SuppressWarnings("unused")
        boolean result = execute(sqlString,
                customer.getId(), // 1
                customer.getFirstName(), // 2
                customer.getLastName(), // 3
                customer.getStreet(), // 4
                customer.getCity(), // 5
                customer.getPostalCode(), // 6
                customer.getPhone(), // 7
                customer.getEmailAddress(), // 8
                toTimestamp(customer.getJoinedDate())); // 9
        
        //LOG.debug(String.format("Adding: %s was %s", customer, result ? "successful" : "unsuccessful"));
        
        LOG.debug(String.format("Adding: %s to the database", customer));
    }
    
    /**
     * Update the customer.
     * 
     * @param customer
     *            the customer to be updated in the database
     * @throws SQLException
     */
    public void update(Customer customer) throws SQLException {
        String sqlString = String.format("UPDATE %s SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?", CUSTOMER_TABLE_NAME,
                Column.FIRST_NAME.name, // 2
                Column.LAST_NAME.name, // 3
                Column.STREET.name, // 4
                Column.CITY.name, // 5
                Column.POSTAL_CODE.name, // 6
                Column.PHONE.name, // 7
                Column.EMAIL_ADDRESS.name, // 8
                Column.JOINED_DATE.name, // 9
                Column.ID.name);
        
        LOG.debug("Update statment: " + sqlString);
        
        @SuppressWarnings("unused")
        boolean result = execute(sqlString,
                customer.getFirstName(), // 2
                customer.getLastName(), // 3
                customer.getStreet(), // 4
                customer.getCity(), // 5
                customer.getPostalCode(), // 6
                customer.getPhone(), // 7
                customer.getEmailAddress(), // 8
                toTimestamp(customer.getJoinedDate()), // 9
                customer.getId());
        
        //LOG.debug(String.format("Updating %s was %s", customer, result ? "successful" : "unsuccessful"));
        LOG.debug(String.format("Updating: %s to the database", customer));
    }
    
    /**
     * Delete the customer from the database.
     * 
     * @param customer
     *            the customer to be deleted in the database
     * @throws SQLException
     */
    public void delete(Customer customer) throws SQLException {
        Connection connection;
        Statement statement = null;
        try {
            connection = Database.getConnection();
            statement = connection.createStatement();

            String sqlString = String.format("DELETE FROM %s WHERE %s='%s'", CUSTOMER_TABLE_NAME, Column.ID.name, customer.getId());
            LOG.debug(sqlString);
            int rowcount = statement.executeUpdate(sqlString);
            LOG.debug(String.format("Deleted %d rows", rowcount));
        } finally {
            close(statement);
        }
    }
    
    /**
     * Retrieve all the customer IDs from the database
     * 
     * @return the list of customer IDs
     * @throws SQLException
     */
    public List<Long> getCustomerIds() throws SQLException {
        List<Long> CustomerIDs = new ArrayList<>();
        
        String selectString = String.format("SELECT %s FROM %s", Column.ID.name, CUSTOMER_TABLE_NAME);
        LOG.debug(selectString);
        
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Connection connection = Database.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectString);

            while (resultSet.next()) {
                CustomerIDs.add(resultSet.getLong(Column.ID.name));
            }
            
        } finally {
            close(statement);
        }
        
        LOG.debug(String.format("Loaded %d customer IDs from the database", CustomerIDs.size()));
        
        return CustomerIDs;
    }
    
    /**
     * Retrieve a customer from database
     * 
     * @param customerId
     *            the id of the customer to be retrieved
     * @return customer
     *         the customer object
     * @throws Exception
     */
    public Customer getCustomer(Long customerId) throws Exception {
        String sqlString = String.format("SELECT * FROM %s WHERE %s = %d", CUSTOMER_TABLE_NAME, Column.ID.name, customerId);
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
                
                Timestamp timestamp = resultSet.getTimestamp(Column.JOINED_DATE.name);
                LocalDate date = timestamp.toLocalDateTime().toLocalDate();
                
                Customer customer = new Customer.Builder(resultSet.getInt(Column.ID.name), resultSet.getString(Column.PHONE.name)) //
                        .setFirstName(resultSet.getString(Column.FIRST_NAME.name)) //
                        .setLastName(resultSet.getString(Column.LAST_NAME.name)) //
                        .setStreet(resultSet.getString(Column.STREET.name)) //
                        .setCity(resultSet.getString(Column.CITY.name)) //
                        .setPostalCode(resultSet.getString(Column.POSTAL_CODE.name)) //
                        .setEmailAddress(resultSet.getString(Column.EMAIL_ADDRESS.name)) //
                        .setJoinedDate(date).build();
                
                return customer;
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
    public int countAllCustomers() throws Exception {
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
     * Enum declaration for the column of Customer DAO
     */
    public enum Column {
        ID("id", 6),
        FIRST_NAME("firstName", 20),
        LAST_NAME("lastName", 20),
        STREET("street", 40),
        CITY("city", 30),
        POSTAL_CODE("postalCode", 16),
        PHONE("phone", 16),
        EMAIL_ADDRESS("emailAddress", 40),
        JOINED_DATE("joinedDate", -1);
        
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
