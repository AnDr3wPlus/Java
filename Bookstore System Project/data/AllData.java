/**
 * Project: a01063367_assignment02
 * File: AllData.java
 * Date: Nov 16, 2018
 * Time: 22:47:24 PM
 */

package a01063367.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.ApplicationException;

/**
 * AllData stores the collections of data
 * 
 * @author scirka
 *
 */
public class AllData {
    
    private static final Logger LOG = LogManager.getLogger();
    
    public static Map<Long, Customer> customers = new HashMap<Long, Customer>();
    public static Map<Long, Book> books = new HashMap<Long, Book>();
    public static Map<Long, Purchase> purchases = new HashMap<Long, Purchase>();
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private AllData() {
        //customers = new HashMap<Long, Customer>();
        //books = new HashMap<Long, Book>();
        //purchases = new HashMap<Long, Purchase>();
    }
    
    /**
     * @throws ApplicationException
     * 
     */
    public static void loadData() throws ApplicationException {
        LOG.debug("loading the data");
        //customers = CustomerReader.read();
        //books = BookReader.read();
        //purchases = PurchaseReader.read();
        LOG.debug("successfully loaded the data");
    }
    
    /**
     * @return the customers
     */
    public static Map<Long, Customer> getCustomers() {
        return customers;
    }
    
    /**
     * @return the books
     */
    public static Map<Long, Book> getBooks() {
        return books;
    }
    
    /**
     * @return the purchases
     */
    public static Map<Long, Purchase> getPurchases() {
        return purchases;
    }
    
}
