/**
 * Project: a01063367_assignment02
 * File: MainFrame.java
 * Date: Nov 20, 2018
 * Time: 09:47:54 PM
 */
package a01063367.ui;

import java.awt.Dialog.ModalityType;
import java.awt.Event;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.data.Book;
import a01063367.data.Customer;
import a01063367.data.Purchase;
import a01063367.db.BookDao;
import a01063367.db.CustomerDao;
import a01063367.db.PurchaseDao;

/**
 * @author Andrew Wu, A01063367
 *
 */
public class MainFrame extends JFrame {
    
    private static final long serialVersionUID = -6354785100650428430L;
    private static final Logger LOG = LogManager.getLogger();
    
    public static boolean bookByAuthor = false;
    public static boolean bookByDescending = false;
    public static boolean bookListByAuthor = false;
    
    public static boolean customerByJoinDate = false;
    public static boolean customerIsToList = false;
    
    public static long selectedCustomerID = 0;
    public static boolean purchaseByLastName = false;
    public static boolean purchaseByTitle = false;
    public static boolean purchaseByDescending = false;
    public static boolean purchaseCustomerFiltered = false;
    public static boolean purchaseSortedList = false;
    
    Map<Long, Customer> customerList;
    Map<Long, Customer> customerSortedList;
    
    ArrayList<Customer> customers = new ArrayList<Customer>();
    ArrayList<Book> books = new ArrayList<Book>();
    ArrayList<Purchase> purchases = new ArrayList<Purchase>();
    ArrayList<Purchase> purchasedList = new ArrayList<Purchase>();
    long inputID = 0;
    
    
    
    /**
     * Constructor for the main frame
     * 
     * @param customersList
     *             the list of customers
     */
    public MainFrame(CustomerDao customerDao, BookDao bookDao, PurchaseDao purchaseDao) {
        
        setTitle("Books2");
        setBounds(100, 100, 500, 300);
        //setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        customerList = new HashMap<Long, Customer>();
        customerSortedList = new HashMap<Long, Customer>();
        
        createUIMenu(customerDao, bookDao, purchaseDao);
    }
    
    
    /**
     * Create UI menu
     * 
     * @param customersList
     *             the list of customers
     */
    private void createUIMenu(CustomerDao customerDao, BookDao bookDao, PurchaseDao purchaseDao) {
        
        // Retrieve information from database
        try {
            for (long id : customerDao.getCustomerIds()) { //   AllData.customers.keySet()
                //customerList.put(oneID, customerDao.getCustomer(oneID));
                customers.add(customerDao.getCustomer(id));
                
            }
            
            for (long id : purchaseDao.getPurchaseIds()) { //   AllData.purchases.keySet()
                //customerList.put(oneID, customerDao.getCustomer(oneID));
                purchases.add(purchaseDao.getPurchase(id));
                
            }
            
            for (long id : bookDao.getBookIds()) { //   AllData.books.keySet()
                //customerList.put(oneID, customerDao.getCustomer(oneID));
                books.add(bookDao.getBook(id));
                
            }
        } catch (Exception e1) {
            LOG.error(e1.getMessage());
        }
        
        List<Long> customerIDs  = new ArrayList<Long>();
        List<Long> purchaseIDs  = new ArrayList<Long>();
        
        // Add customer IDs from customerDao and purchaseDao to match
        for (Customer aCustomer : customers) {
            customerIDs.add(aCustomer.getId());
        }
        for (Purchase aPurchase : purchases) {
            purchaseIDs.add(aPurchase.getCustomerId());
        } 
        //System.out.println(customerIDs.toString());
        //System.out.println(purchaseIDs.toString());
        
        /** Main Menu */
        JMenuBar mainMenuBar = new JMenuBar();
        setJMenuBar(mainMenuBar);
        
        /* 1. File Menu */
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        mainMenuBar.add(fileMenu);
        
        /* 2. Books Menu */
        JMenu booksMenu = new JMenu("Books");
        fileMenu.setMnemonic('B');
        mainMenuBar.add(booksMenu);
        
        /* 3. Customer Menu */
        JMenu customersMenu = new JMenu("Customers");
        fileMenu.setMnemonic('C');
        mainMenuBar.add(customersMenu);
        
        /* 4. Purchase Menu */
        JMenu purchasesMenu = new JMenu("Purchase");
        fileMenu.setMnemonic('P');
        mainMenuBar.add(purchasesMenu);
        
        /* 5. Help Menu */
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        mainMenuBar.add(helpMenu);
        
        
        /** Sub-Menu */
        /** 1. "File" */
        /* 1.1 Sub-menu "Drop" */
        JMenuItem dropSubMenu = new JMenuItem("Drop");
        dropSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply = JOptionPane.showConfirmDialog(getContentPane(), "Delete all tables?", "Drop Tables", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    try {
                        customerDao.drop();
                        LOG.info("Table Dropped: " + CustomerDao.CUSTOMER_TABLE_NAME);
                        bookDao.drop();
                        LOG.info("Table Dropped: " + BookDao.BOOK_TABLE_NAME);
                        purchaseDao.drop();
                        LOG.info("Table Dropped: " + PurchaseDao.PURCHASE_TABLE_NAME);
                    } catch (SQLException e1) {
                        LOG.error(e1.getMessage());
                    }
                }
                System.exit(0);
            }
        });
        fileMenu.add(dropSubMenu);
        
        
        /* 1.2 Sub-menu "Quit" */
        JMenuItem quitSubMenu = new JMenuItem("Quit");
        quitSubMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.ALT_MASK));
        // Implicit event handler
        quitSubMenu.addActionListener(e -> {System.out.println("I'm out of here!");
                                     System.exit(0);});
        // Separator between Customer and Exit buttons on the sub-menu
        fileMenu.addSeparator();
        fileMenu.add(quitSubMenu);
        
        
        /** 2. "Books" */
        /* 2.1 Sub-menu "Count" */
        JMenuItem countBooksSubMenu = new JMenuItem("Count");
        countBooksSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int bookCount;
                try {
                    bookCount = bookDao.countAllBooks();
                    JOptionPane.showMessageDialog(getContentPane(), "Total Books: " + bookCount + " books", "Book Count", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(getContentPane(), "No Database Available", "Book Count", JOptionPane.INFORMATION_MESSAGE);
                    LOG.error(e1.getMessage());
                }
            }
        });
        booksMenu.add(countBooksSubMenu);
        
        
        /* 2.2 Sub-menu "By Author" */
        JCheckBox byAuthorBox = new JCheckBox("Author");
        
        byAuthorBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookByAuthor = byAuthorBox.isSelected();
                LOG.info(String.format("bookByAuthor = %s", bookByAuthor));
            }
        });
        
        byAuthorBox.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
        booksMenu.addSeparator();
        booksMenu.add(byAuthorBox);
        
        
        /* 2.3 Sub-menu "Descending" */
        JCheckBox descendingBookBox = new JCheckBox("Descending");
        
        descendingBookBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookByDescending = descendingBookBox.isSelected();
                LOG.info(String.format("bookByDescending = %s", bookByDescending));
            }
        });
        
        descendingBookBox.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
        booksMenu.addSeparator();
        booksMenu.add(descendingBookBox);
        
        
        /* 2.4 Sub-menu "List" */
        JMenuItem listBooksSubMenu = new JMenuItem("List");
        
        listBooksSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (bookByAuthor) {
                        bookByDescending = descendingBookBox.isSelected();
                    } else {
                        bookByDescending = false;
                    }
                    bookListByAuthor = listBooksSubMenu.isSelected();
                    LOG.info(String.format("bookListByAuthor = %s", bookListByAuthor));
                    
                    BookDialog bookDialog = new BookDialog(books);
                    bookDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    bookDialog.setModalityType(ModalityType.APPLICATION_MODAL);
                    bookDialog.setVisible(true);
                    
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        } ) ;
        
        booksMenu.addSeparator();
        booksMenu.add(listBooksSubMenu);
        
        
        /** 3. "Customers" */
        /* 3.1 Sub-menu "Count" */
        JMenuItem countCustomersSubMenu = new JMenuItem("Count");
        countCustomersSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int customerCount;
                try {
                    customerCount = customerDao.countAllCustomers();
                    JOptionPane.showMessageDialog(getContentPane(), "Total Customers: " + customerCount + " customers", "Customer Count", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(getContentPane(), "No Database Available", "Customer Count", JOptionPane.INFORMATION_MESSAGE);
                    LOG.error(e1.getMessage());
                }
            }
        });
        customersMenu.add(countCustomersSubMenu);
        
        
        /* 3.2 Sub-menu "By Join Date" */
        JCheckBox byJoinDateBox = new JCheckBox("By Join Date");
        
        byJoinDateBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerByJoinDate = byJoinDateBox.isSelected();
                LOG.info(String.format("customerByJoinDate = %s", customerByJoinDate));
            }
        });
        
        byJoinDateBox.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
        customersMenu.addSeparator();
        customersMenu.add(byJoinDateBox);
        
        
        /* 3.3 Sub-menu "List" */
        JMenuItem listCustomersSubMenu = new JMenuItem("List");
        
        listCustomersSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    /*
                    if (!listCustomersSubMenu.isSelected()) {
                        customerIsToList = false;
                    }
                    */
                    customerIsToList = true;
                    //customerIsToList = customerByJoinDate;
                    
                    //LOG.info(String.format("customerByJoinDate = %s", customerByJoinDate));
                    
                    CustomerDialog customerDialog = new CustomerDialog(customers);
                    customerDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    customerDialog.setModalityType(ModalityType.APPLICATION_MODAL);
                    customerDialog.setVisible(true);
                    
                    customerIsToList = false;
                    customerByJoinDate = false;
                    
                } catch (Exception e1) {
                    LOG.error(e1.getMessage());
                }
            }
        } ) ;
        
        customersMenu.addSeparator();
        customersMenu.add(listCustomersSubMenu);
        
        
        /** 4. "Purchases" */
        /* 4.1 Sub-menu "Total" */
        JMenuItem totalSubMenu = new JMenuItem("Total");
        totalSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double totalPurchaseAmount = 0;
                
                if (purchaseCustomerFiltered) {
                    try {
                        
                        // Sum the selected customer's purchases
                        for ( Purchase aPurchase : purchasedList ) {
                            totalPurchaseAmount += aPurchase.getPrice();
                        }
                        String total = String.format("$%.2f", totalPurchaseAmount);
                        JOptionPane.showMessageDialog(getContentPane(), 
                                "Purchases: " + total + ", for customer: " + selectedCustomerID + "\nCustomer filter is now reset, please select a new customer!" ,
                                "Purchase Total", JOptionPane.INFORMATION_MESSAGE);
                        purchaseCustomerFiltered = false;
                    } catch (Exception e1) {
                        LOG.error(e1.getMessage());
                    }
                } else {
                    try {
                        totalPurchaseAmount = purchaseDao.sumPurchasesAmount(); // Total Purchase amount for all purchases
                        String total = String.format("$%.2f", totalPurchaseAmount);
                        JOptionPane.showMessageDialog(getContentPane(), "Purchases: " + total +  " in total", "Purchase Total", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(getContentPane(), "No Database Available", "Purchase Total", JOptionPane.INFORMATION_MESSAGE);
                        LOG.error(e1.getMessage());
                    }
                }
            }
        });
        purchasesMenu.add(totalSubMenu);
        
        /* 4.2 Sub-menu "By Last Name" */
        JCheckBox byLastNameSubMenu = new JCheckBox("By Last Name");
        
        byLastNameSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseByLastName = byLastNameSubMenu.isSelected();
                LOG.info(String.format("purchaseByLastName = %s", purchaseByLastName));
            }
        });
        
        byLastNameSubMenu.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
        purchasesMenu.addSeparator();
        purchasesMenu.add(byLastNameSubMenu);
        
        /* 4.3 Sub-menu "By Title" */
        JCheckBox byTitleSubMenu = new JCheckBox("By Title");
        
        byTitleSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseByTitle = byTitleSubMenu.isSelected();
                LOG.info(String.format("purchaseByTitle = %s", purchaseByTitle));
            }
        });
        
        byTitleSubMenu.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
        purchasesMenu.addSeparator();
        purchasesMenu.add(byTitleSubMenu);
        
        /* 4.4 Sub-menu "Descending" */
        JCheckBox descendingPurchaseSubMenu = new JCheckBox("Descending");
        
        descendingPurchaseSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseByDescending = descendingPurchaseSubMenu.isSelected();
                LOG.info(String.format("purchaseByDescending = %s", purchaseByDescending));
            }
        });
        
        descendingPurchaseSubMenu.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
        purchasesMenu.addSeparator();
        purchasesMenu.add(descendingPurchaseSubMenu);
        
        /* 4.5 Sub-menu "Filter by Customer ID" */
        JMenuItem customerIDSubMenu = new JMenuItem("Filter by Customer ID");
        
        customerIDSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show Error Message if customer IDs are not available
                if (customerIDs.isEmpty()) {
                    JOptionPane.showMessageDialog(getContentPane(), "No Database Available", "Search Customers", JOptionPane.INFORMATION_MESSAGE);
                }
                
                // Get input ID String and convert it to Long
                String inputIDString = (String) JOptionPane.showInputDialog(getContentPane(), "Please Enter Customer ID: ", "Select Customer", JOptionPane.PLAIN_MESSAGE, null, null, null);
                
                // (inputIDString != null) to avoid NullPointerException
                if ( inputIDString != null && !inputIDString.equals("") && inputIDString.length() == 4) {
                    inputID = Long.parseLong(inputIDString);
                    System.out.println(inputID);
                } else {
                    JOptionPane.showMessageDialog(getContentPane(), "Invalid Input", "Warning", JOptionPane.INFORMATION_MESSAGE);
                    purchaseCustomerFiltered = false;  // Filter is removed if no customer ID is entered
                    return;
                }
                // Match IDs
                if ( customerIDs.contains(inputID) ) {
                    purchaseCustomerFiltered = false;
                    LOG.info("Customer: " + inputID + " is there");
                    
                    if (purchaseIDs.contains(inputID)) {
                        selectedCustomerID = inputID;
                        purchaseCustomerFiltered = true;
                        
                        LOG.info("Customer: " + inputID + " purchases found");
                        
                    }
                } else {
                    JOptionPane.showMessageDialog(getContentPane(), "Cannot Find Customer: " + inputID, "Warning", JOptionPane.INFORMATION_MESSAGE);
                    purchaseCustomerFiltered = false;
                    return;
                }
                
                // Collect the selected customer's purchases
                try {
                    if (purchaseCustomerFiltered) {
                        for ( Purchase aPurchase : purchases ) {
                            if (aPurchase.getCustomerId() == selectedCustomerID) {
                                purchasedList.add(aPurchase);
                            }
                        }
                    }
                } catch (Exception e1) {
                    LOG.error(e1.getMessage());
                }
                
            }
        } ) ;
        
        purchasesMenu.addSeparator();
        purchasesMenu.add(customerIDSubMenu);
        
        /* 4.6 Sub-menu "List" */
        JMenuItem listPurchasesSubMenu = new JMenuItem("List");
        
        listPurchasesSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if ( !purchaseByLastName && !purchaseByTitle) {
                        purchaseByDescending = false;
                    }
                    if (purchaseByLastName || purchaseByTitle) {
                        purchaseSortedList = true;
                    }
                    
                    LOG.info(String.format("purchaseSortedList = %s", purchaseSortedList));
                    
                    if (!purchaseCustomerFiltered) {
                        purchasedList = purchases;
                    }
                    
                    PurchaseDialog purchaseDialog = new PurchaseDialog(purchasedList, customers, books);
                    purchaseDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    purchaseDialog.setModalityType(ModalityType.APPLICATION_MODAL);
                    purchaseDialog.setVisible(true);
                    
                } catch (Exception e1) {
                    LOG.error(e1.getMessage());
                }
            }
        } ) ;
        
        purchasesMenu.addSeparator();
        purchasesMenu.add(listPurchasesSubMenu);
        
        
        /** 5. "About" */
        JMenuItem aboutSubMenu = new JMenuItem("About");
        aboutSubMenu.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0) );
        helpMenu.add(aboutSubMenu);
        aboutSubMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainFrame.this, "Books2\nBy Andrew Wu A01063367", "About Books2", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        
    }
    
    
    
    
    
    
    
}

