/**
 * Project: a01063367_assignment02
 * File: BookStore.java
 * Date: Nov 16, 2018
 * Time: 23:47:00 PM
 */

package a01063367;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import a01063367.db.BookDao;
import a01063367.db.CustomerDao;
import a01063367.db.Database;
import a01063367.db.PurchaseDao;
import a01063367.io.BooksReport;
import a01063367.io.CustomersReport;
import a01063367.io.PurchasesReport;
import a01063367.ui.MainFrame;

/**
 * @author Andrew Wu, A01063367
 *
 */
public class Books2 {
    
    public static final String DB_PROPERTIES_FILENAME = "db.properties";
    private static final String DROP_OPTION = "-drop";
    //private Database database;
    
    
    /**
     * Logging Startup
     */
    private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
    static {
        configureLogging();
    }
    private static final Logger LOG = LogManager.getLogger();
    
    /**
     * Configures log4j2 from the external configuration file specified in LOG4J_CONFIG_FILENAME.
     * If the configuration file isn't found then log4j2's DefaultConfiguration is used.
     */
    private static void configureLogging() {
        ConfigurationSource source;
        try {
            source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
            Configurator.initialize(null, source);
        } catch (IOException e) {
            System.out.println(String.format("WARNING! Can't find the log4j logging configuration file %s; using DefaultConfiguration for logging.",
                    LOG4J_CONFIG_FILENAME));
            Configurator.initialize(new DefaultConfiguration());
        }
    }
    
    /**
     * Book2 Constructor.
     * 
     * @throws ApplicationException
     * @throws IOException 
     * @throws ParseException
     */
    public Books2() throws IOException {
        //LOG.debug("Input args: " + Arrays.toString(args));
        //BookOptions.process(args);
        
        //database = new Database();
    }
    
    /**
     * Entry point to GIS
     * 
     * @param args
     * @throws ApplicationException 
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, ApplicationException {
        LOG.info("Starting Books2");
        Instant startTime = Instant.now();
        LOG.info(startTime);
        
        if (args.length == 1 && args[0].equalsIgnoreCase(DROP_OPTION)) {
            Database.requestDbTableDrop();
        }
        
        // start the Bookstore System
        
        //BookStore bookStore = null;
        try {
            //bookStore = new BookStore();
            new Books2().run();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        
        LOG.debug("main thread exiting");
        /*
        try {
            BookStore bookStore = new BookStore(args);
            if (BookOptions.isHelpOptionSet()) {
                BookOptions.Value[] values = BookOptions.Value.values();
                System.out.format("%-5s %-15s %-10s %s%n", "Option", "Long Option", "Has Value", "Description");
                for (BookOptions.Value value : values) {
                    System.out.format("-%-5s %-15s %-10s %s%n", value.getOption(), ("-" + value.getLongOption()), value.isHasArg(),
                            value.getDescription());
                }
                
                System.out.println("\nex. -inventory -make=honda -by_count -desc -total -service");
                
                return;
            }
            
            bookStore.run();
        } catch (ApplicationException | FileNotFoundException e) {
            // e.printStackTrace();
            LOG.debug(e.getMessage());
        }
        */
        
        Instant endTime = Instant.now();
        LOG.info(endTime);
        LOG.info(String.format("Duration: %d ms", Duration.between(startTime, endTime).toMillis()));
        LOG.info("Books2 has stopped");
    }
    
    
    /**
     * Load the files, connect to database and initialize UI.
     * 
     * @throws ApplicationException
     * @throws FileNotFoundException
     * 
     */
    private void run() throws ApplicationException, FileNotFoundException {
        LOG.debug("run()");
        
        try {
            //Database database = connect();
            Database database = new Database();
            //CustomerDao customerDao = loadCustomers(database);
            
            CustomerDao customerDao = new CustomerDao(database);
            BookDao bookDao = new BookDao(database);
            PurchaseDao purchaseDao = new PurchaseDao(database);
            
            
            
            
            //purchaseDao.getPurchaseIds();
            //bookDao.getBookIds();
            
            /*
            ArrayList<Book> bookList = new ArrayList<Book>();
            for (long oneID : bookDao.getBookIds()) {
                System.out.println(bookDao.getBookIds().toString() +  "============");
                bookList.add(bookDao.getBook(oneID));
                System.out.println(bookList.toString() +  "============");
                System.out.println(bookDao.getBook(oneID));
            }
            
            */
            
            
            //customerDao.getCustomerIds();
            /*
            //ArrayList<Customer> customerList = new ArrayList<Customer>();
            for (long oneID : customerDao.getCustomerIds()) {
                //customerList.add(customerDao.getCustomer(oneID));
                
                System.out.println(customerDao.getCustomer(oneID).toString());
            }
            
            */
            
            
            
            EventQueue.invokeLater( new Runnable() {
                @Override
                public void run() {
                    try {
                        
                        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                            if ("Nimbus".equals(info.getName())) {
                                LOG.info(info.getName() + " LookAndFeel set.");
                                UIManager.setLookAndFeel(info.getClassName());
                                break;
                            }
                        }
                        
                        MainFrame frame = new MainFrame(customerDao, bookDao, purchaseDao);
                        frame.setVisible(true);
                        
                    } catch (Exception e) {
                        LOG.error(e.getMessage());
                    }
                }
            } ) ;
            LOG.info("Frame created");
            /*
            */
            
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        
        //AllData.loadData();
        //generateReports();
    }
    
    /**
     * Generate the reports from the input data
     * 
     * @throws FileNotFoundException
     */
    @SuppressWarnings("unused")
    private void generateReports() throws FileNotFoundException {
        LOG.debug("generating the reports");
        
        PrintStream out = null;
        
        if (BookOptions.isCustomersOptionSet()) {
            LOG.debug("generating the customer report");
            CustomersReport.print(System.out);
            out = getOutputStream(CustomersReport.REPORT_FILENAME);
            CustomersReport.print(out);
            out.close();
        }
        
        if (BookOptions.isBooksOptionSet()) {
            LOG.debug("generating the book report");
            BooksReport.print(System.out);
            out = getOutputStream(BooksReport.REPORT_FILENAME);
            BooksReport.print(out);
            out.close();
        }
        
        if (BookOptions.isPurchasesOptionSet()) {
            LOG.debug("generating the purchase report");
            PurchasesReport.print(System.out);
            out = getOutputStream(PurchasesReport.REPORT_FILENAME);
            PurchasesReport.print(out);
            out.close();
        }
        
    }
    
    /**
     * @param reportFilename
     * @return
     * @throws ApplicationException
     * @throws FileNotFoundException
     */
    private PrintStream getOutputStream(String reportFilename) throws FileNotFoundException {
        PrintStream out = null;
        try {
            out = new PrintStream(new File(reportFilename));
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage());
            throw e;
        }
        
        return out;
    }
    
    /**
     * Load the customers.
     * 
     * @TODO this method has much to much knowledge of the DB and should be refactored.
     * 
     * @throws IOException
     * @throws SQLException
     * @throws ApplicationException
     */
    @SuppressWarnings("unused")
    private CustomerDao loadCustomers(Database database) throws ApplicationException {
        CustomerDao customerDao = new CustomerDao(database);
        return customerDao;
    }
    
    /**
     * Connect to the database
     * 
     * @return database
     *             the database t connect to
     * 
     * @throws IOException
     * @throws SQLException
     * @throws ApplicationException
     */
    @SuppressWarnings("unused")
    private Database connect() throws IOException, SQLException, ApplicationException {
        Properties dbProperties = new Properties();
        dbProperties.load(new FileInputStream(DB_PROPERTIES_FILENAME));
        Database database = new Database();
        
        return database;
    }
    
}
