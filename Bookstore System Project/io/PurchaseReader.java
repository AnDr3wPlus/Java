/**
 * Project: a01063367_assignment02
 * File: PurchaseReader.java
 * Date: Nov 16, 2018
 * Time: 23:35:14 PM
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
import a01063367.data.Purchase;
import a01063367.db.PurchaseDao;

/**
 * PurchaseReader reads the purchase data file
 * 
 * @author Andrew Wu, A01063367
 *
 */
public class PurchaseReader extends Reader {
    
    public static final String PURCHASES_FILENAME = "purchases.csv";
    
    private static final Logger LOG = LogManager.getLogger();
    
    //private static Set<Long> customerIds = AllData.customers.keySet();
    //private static Set<Long> bookIds = AllData.books.keySet();
    
    //private static Set<Long> customerIds = AllData.getCustomers().keySet();
    //private static Long[] customerIdArray = customerIds.toArray(new Long[0]);
    //private static int customerIdCount = customerIdArray.length;
    //private static Set<Long> bookIds = AllData.getBooks().keySet();
    //private static Long[] bookIdArray = bookIds.toArray(new Long[0]);
    //private static int bookIdCount = bookIdArray.length;
    
    
    /**
     * Private constructor to prevent instantiation
     */
    private PurchaseReader() {
        
    }
    
    /**
     * Read the inventory input data.
     * 
     * @return the inventory.
     * @throws ApplicationException
     */
    public static void read( String fileName, PurchaseDao purchaseDao ) throws ApplicationException {
        File purchaseDataFile = new File(PURCHASES_FILENAME);
        if (!purchaseDataFile.exists()) {
            throw new ApplicationException(String.format("Required '%s' is missing.", PURCHASES_FILENAME));
        }
        
        FileReader in;
        Iterable<CSVRecord> records;
        try {
            in = new FileReader(purchaseDataFile);
            records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
        
        //Map<Long, Purchase> purchases = new HashMap<Long, Purchase>();
        
        LOG.debug("Reading" + purchaseDataFile.getAbsolutePath());
        
        for (CSVRecord record : records) {
            long id = Long.parseLong(record.get("id"));
            long customerId = Long.parseLong(record.get("customer_id"));
            long bookId = Long.parseLong(record.get("book_id"));
            float price = Float.parseFloat(record.get("price"));
            
            Purchase purchase = new Purchase.Builder(id, customerId, bookId, price).build();
            
            try {
                purchaseDao.add(purchase);
            } catch (SQLException e) {
                LOG.error(e.getMessage());
            }
            
            //purchases.put(purchase.getId(), purchase);
            AllData.purchases.put(purchase.getId(), purchase);
            
            LOG.debug("Added " + purchase.toString() + " as " + purchase.getId());
            
            /*
            if (!customerIds.contains(customerId)) {
                customerId = customerIdArray[(int) (Math.random() * customerIdCount)];
            }
            if (!bookIds.contains(bookId)) {
                bookId = bookIdArray[(int) (Math.random() * bookIdCount)];
            }
            
            */
        }
        
        //AllData.purchases = purchases;
        //return purchases;
    }
    
}
