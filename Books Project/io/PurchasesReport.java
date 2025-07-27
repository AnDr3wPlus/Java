/**
 * Project: a01063367_assignment02
 * File: PurchasesReport.java
 * Date: Nov 16, 2018
 * Time: 23:36:05 PM
 */

package a01063367.io;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.BookOptions;
import a01063367.data.AllData;
import a01063367.data.Book;
import a01063367.data.Customer;
import a01063367.data.Purchase;
import a01063367.data.util.Common;

/**
 * PurchasesReport
 * 
 * @author Andrew Wu, A01063367
 *
 */
public class PurchasesReport {
    
    public static final String REPORT_FILENAME = "purchases_report.txt";
    public static final String HORIZONTAL_LINE = new String(new char[115]).replace('\0', '-');
    public static final String HEADER_FORMAT = "%-24s %-80s %-8s";
    public static final String ROW_FORMAT = "%-24s %-80s $%.2f";
    
    private static final Logger LOG = LogManager.getLogger();
    
    private static List<Item> items;
    
    
    /**
     * Print the report.
     *
     * @param out
     */
    public static void print(PrintStream out) {
        LOG.debug("Printing the Purchases Report");
        String text = null;
        boolean hasTotal = BookOptions.isTotalOptionSet();
        LOG.debug("isTotalOptionSet = " + hasTotal);
        boolean descending = BookOptions.isDescendingOptionSet();
        LOG.debug("isDescendingOptionSet = " + descending);
        
        out.println("\nPurchases Report");
        
        out.println(HORIZONTAL_LINE);
        text = String.format(HEADER_FORMAT, "Name", "Title", "Price");
        out.println(text);
        out.println(HORIZONTAL_LINE);
        
        Collection<Purchase> purchases = AllData.getPurchases().values();
        Map<Long, Book> books = AllData.getBooks();
        Map<Long, Customer> customers = AllData.getCustomers();
        
        String customerIdString = BookOptions.getCustomerId();
        long optionsCustomerId = customerIdString == null ? -1L : Long.parseLong(customerIdString);
        items = new ArrayList<>();
        for (Purchase purchase : purchases) {
            long customerId = purchase.getCustomerId();
            if (customerIdString != null && customerId != optionsCustomerId) {
                continue;
            }
            
            long bookId = purchase.getBookId();
            Book book = books.get(bookId);
            Customer customer = customers.get(customerId);
            float price = purchase.getPrice();
            Item item = new Item(customer.getFirstName(), customer.getLastName(), book.getTitle(), price);
            items.add(item);
        }
        
        if (BookOptions.isByLastnameOptionSet()) {
            LOG.debug("isByLastnameOptionSet = true");
            if (descending) {
                Collections.sort(items, new CompareByLastNameDescending());
            } else {
                Collections.sort(items, new CompareByLastName());
            }
        }
        
        if (BookOptions.isByTitleOptionSet()) {
            LOG.debug("isByTitleOptionSet = true");
            if (descending) {
                Collections.sort(items, new CompareByTitleDescending());
            } else {
                Collections.sort(items, new CompareByTitle());
            }
        }
        
        float total = 0;
        for (Item item : items) {
            if (hasTotal) {
                total += item.price;
            }
            
            text = String.format(ROW_FORMAT, item.firstName + " " + item.lastName, item.title, item.price);
            out.println(text);
            LOG.trace(text);
        }
        
        if (hasTotal) {
            text = String.format("%nValue of purchases: $%,.2f%n", total);
            out.println(text);
            LOG.trace(text);
        }
        
    }
    
    /**
     * CompareByLastName
     */
    public static class CompareByLastName implements Comparator<Item> {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.lastName.compareTo(item2.lastName);
        }
    }
    
    /**
     * CompareByLastNameDescending
     */
    public static class CompareByLastNameDescending implements Comparator<Item> {
        @Override
        public int compare(Item item1, Item item2) {
            return item2.lastName.compareTo(item1.lastName);
        }
    }
    
    /**
     * CompareByTitle
     */
    public static class CompareByTitle implements Comparator<Item> {
        @Override
        public int compare(Item item1, Item item2) {
            return item1.title.compareToIgnoreCase(item2.title);
        }
    }
    
    /**
     * CompareByTitleDescending
     */
    public static class CompareByTitleDescending implements Comparator<Item> {
        @Override
        public int compare(Item item1, Item item2) {
            return item2.title.compareToIgnoreCase(item1.title);
        }
    }
    
    
    /**
     * Inner class Item used to store purchase information for printing report
     */
    public static class Item {
        
        private String firstName;
        private String lastName;
        private String title;
        private float price;
        
        
        /**
         * Constructor
         * 
         * @param firstName
         * @param lastName
         * @param title
         * @param price
         */
        public Item(String firstName, String lastName, String title, float price) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.title = Common.truncateIfRequired(title, 80);
            this.price = price;
        }
        
        /**
         * @return the firstName
         */
        public String getFirstName() {
            return firstName;
        }
        
        /**
         * @param firstName the firstName to set
         */
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
        
        /**
         * @return the lastName
         */
        public String getLastName() {
            return lastName;
        }
        
        /**
         * @param lastName the lastName to set
         */
        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
        
        /**
         * @return the title
         */
        public String getTitle() {
            return title;
        }
        
        /**
         * @param title the title to set
         */
        public void setTitle(String title) {
            this.title = title;
        }
        
        /**
         * @return the price
         */
        public float getPrice() {
            return price;
        }
        
        /**
         * @param price the price to set
         */
        public void setPrice(float price) {
            this.price = price;
        }
        
        
        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Item [firstName=" + firstName + ", lastName=" + lastName + ", title=" + title + ", price=" + price + "]";
        }
        
        /*
        @Override
        public String toString() {
            return String.format("%-14s    %-14s    %-50s    $%.2f", firstName, lastName, StringUtils.abbreviate(title, 40), price);
        }
        */
    }
}
