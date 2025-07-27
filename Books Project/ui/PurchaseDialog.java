/**
 * Project: a01063367_assignment02
 * File: PurchaseDialog.java
 * Date: Nov 21, 2018
 * Time: 22:36:29 PM
 */
package a01063367.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.data.Book;
import a01063367.data.Customer;
import a01063367.data.Purchase;
import a01063367.io.PurchasesReport.CompareByLastName;
import a01063367.io.PurchasesReport.CompareByLastNameDescending;
import a01063367.io.PurchasesReport.CompareByTitle;
import a01063367.io.PurchasesReport.CompareByTitleDescending;
import a01063367.io.PurchasesReport.Item;
import net.miginfocom.swing.MigLayout;

/**
 * Purchase Dialog
 * 
 * @author Andrew Wu, A01063367
 *
 */
@SuppressWarnings("serial")
public class PurchaseDialog extends JDialog {
    
    private static Logger LOG = LogManager.getLogger();
    List<Item> items;
    
    /**
     * Constructor
     */
    public PurchaseDialog(ArrayList<Purchase> purchasedList, ArrayList<Customer> customers, ArrayList<Book> books) {
        
        setTitle("Purchase Dialog");
        LOG.info("Initializing Purchase Dialog");
        getContentPane().setLayout(new MigLayout("", "[155.00,grow]", "[][186.00,grow]"));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(550, 550);
        setLocationRelativeTo(null);
        
        JLabel purchaseDialogLabel = new JLabel(String.format(" %-40s  %-70s  %-10s", "Name", "Title", "Price"));  //" ID       Customer ID    Book ID    Price"
        getContentPane().add(purchaseDialogLabel, "cell 0 0");
        
        
        items = new ArrayList<Item>();
        // Create purchase information
        for (Purchase aPurchase : purchasedList) {
            
            long customerID = aPurchase.getCustomerId();
            // Find customer from purchase
            Customer purchasedCustomer = null;
            for (Customer aCustomer : customers) {
                if (aCustomer.getId() == customerID) {
                    purchasedCustomer = aCustomer;
                }
            }
            // Customer ID
            long bookID = aPurchase.getBookId();
            // Book ID
            Book purchasedBook = null;
            for (Book aBook : books) {
                if (aBook.getId() == bookID) {
                    purchasedBook = aBook;
                }
            }
            // Title
            String bookTitle = purchasedBook.getTitle();
            // Price
            float purchaseAmount = aPurchase.getPrice();
            
            Item item = new Item(purchasedCustomer.getFirstName(), purchasedCustomer.getLastName(), bookTitle, purchaseAmount);
            items.add(item);
        }
        LOG.debug("items: " + items);
        
        
        // Compared by last name
        if( MainFrame.purchaseByLastName ) {
            if( MainFrame.purchaseByDescending ) {
                Collections.sort(items, new CompareByLastNameDescending());
            } else {
                Collections.sort(items, new CompareByLastName());
            }
        }
        // Compared by last name
        if( MainFrame.purchaseByTitle ) {
            if( MainFrame.purchaseByDescending ) {
                Collections.sort(items, new CompareByTitleDescending());
            } else {
                Collections.sort(items, new CompareByTitle());
            }
        }
        
        // JList
        @SuppressWarnings({ "unchecked", "rawtypes" })
        JList<Item> itemsJList = new JList(items.toArray());
        itemsJList.setCellRenderer( new PurchaseRender() );
        
        itemsJList.setSize(550, 550);
        itemsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemsJList.setLayoutOrientation(JList.VERTICAL);
        itemsJList.setVisibleRowCount(-1);
        
        JScrollPane purchaseListScrollPane = new JScrollPane(itemsJList);
        purchaseListScrollPane.setPreferredSize(new Dimension(550, 550));
        purchaseListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(purchaseListScrollPane, "cell 0 1");
        
        // Reset the purchaseCustomerFiltered after display
        if (MainFrame.purchaseCustomerFiltered) {
            MainFrame.purchaseCustomerFiltered = false;
            JOptionPane.showMessageDialog(getContentPane(), "Display selected customer purchases \nCustomer filter is now reset, please select a new customer!", 
                    "Purchase Total", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    
    
    
    /**
     * Custom renderer Class
     *
     * @author 
     */
    class PurchaseRender extends JLabel implements ListCellRenderer<Item>  {
        
        public PurchaseRender() {
            setOpaque(true);
        }
        
        @SuppressWarnings("static-access")
        @Override
        public Component getListCellRendererComponent(JList<? extends Item> list, Item item, int index, boolean isSelected, boolean cellHasFocus) {
            
            setText( String.format("%-12s    %-12s    %-50s    %.2f", item.getFirstName(), item.getLastName(), StringUtils.abbreviate(item.getTitle(), 50), item.getPrice()) );
            
            if (isSelected) {
                setBackground(list.getSelectionBackground().LIGHT_GRAY);
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            
            return this;
        }
    }
    
}
