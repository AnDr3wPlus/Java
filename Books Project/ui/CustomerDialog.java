/**
 * Project: a01063367_assignment02
 * File: CustomerDialog.java
 * Date: Nov 21, 2018
 * Time: 01:09:30 AM
 */
package a01063367.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.data.Customer;
import a01063367.io.CustomersReport.CompareByJoinedDate;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author Andrew Wu, A01063367
 *
 */
@SuppressWarnings("serial")
public class CustomerDialog extends JDialog {
    
    private static final Logger LOG = LogManager.getLogger();
    private static Customer selectedCustomer;
    ArrayList<Customer> customersUnsorted;
    ArrayList<Customer> customersSorted;
    ArrayList<Customer> customersDisplayList;
    
    /**
     * Constructor
     */
    public CustomerDialog(ArrayList<Customer> customers) {
        
        setTitle("Customer Dialog");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        LOG.info("Initializing Customer Dialog");
        getContentPane().setLayout(new MigLayout("", "[155.00,grow]", "[][186.00,grow]"));
        setSize(231, 579);
        setLocationRelativeTo(null);
        
        JLabel customerDialogLabel = new JLabel("  ID         Name");
        getContentPane().add(customerDialogLabel, "cell 0 0");
        
        
        // Compared by join date
        if( MainFrame.customerByJoinDate ) {
            Collections.sort(customers, new CompareByJoinedDate());
            MainFrame.customerByJoinDate = false;
        }
        customersUnsorted = customers;
        customersSorted = customers;
        /*
        if (MainFrame.customerIsToList && MainFrame.customerByJoinDate) {
            LOG.debug("customerIsToList: " + MainFrame.customerIsToList);
            LOG.debug("customerByJoinDate: " + MainFrame.customerByJoinDate);
            Collections.sort(customersSorted, new CompareByJoinedDate());
            //customersDisplayList = customersSorted;
            LOG.debug("customersSorted" + customersSorted);
        } else {
            //customersDisplayList = customersUnsorted;
            LOG.debug("customerIsToList: " + MainFrame.customerIsToList);
            LOG.debug("customerByJoinDate: " + MainFrame.customerByJoinDate);
            LOG.debug("customersUnsorted" + customersUnsorted);
        }
        */
        
        /*
        if( !MainFrame.customerToList ) {
            MainFrame.customerByJoinDate = false;
        } else if (MainFrame.customerToList && MainFrame.customerByJoinDate){
            Collections.sort(customers, new CompareByJoinedDate());
        }
        
        
        if( MainFrame.customerByJoinDate ) {
            Collections.sort(customersSorted, new CompareByJoinedDate());
            customersDisplayList = customersSorted;
        } else {
            customersDisplayList = customers;
        }
        */
        
        
        
        //LOG.debug(customers.toString());
        
        // JList
        @SuppressWarnings({ "unchecked", "rawtypes" })
        /*
        JList<Customer> customerJList;
        if (MainFrame.customerIsToList && MainFrame.customerByJoinDate) {
            LOG.debug("customerIsToList: " + MainFrame.customerIsToList);
            LOG.debug("customerByJoinDate: " + MainFrame.customerByJoinDate);
            Collections.sort(customersSorted, new CompareByJoinedDate());
            
            LOG.debug("customersSorted" + customersSorted);
            customerJList = new JList(customersSorted.toArray());
        } else {
            LOG.debug("customerIsToList: " + MainFrame.customerIsToList);
            LOG.debug("customerByJoinDate: " + MainFrame.customerByJoinDate);
            
            LOG.debug("customersUnsorted" + customersUnsorted);
            customerJList = new JList(customersUnsorted.toArray());
        }
        */
        JList<Customer> customerJList = new JList(customers.toArray());
        customerJList.setCellRenderer( new CustomerRender() );  // Render the list to display id and name only
        
        customerJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectedCustomer = customerJList.getSelectedValue();
                    System.out.println(customerJList.getSelectedValue());
                    
                    CustomerDetailsDialog customerDetails = new CustomerDetailsDialog(selectedCustomer);
                    customerDetails.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    customerDetails.setModalityType(ModalityType.APPLICATION_MODAL);
                    customerDetails.setVisible(true);
                }
            }
        });
        
        customerJList.setSize(200, 500);
        customerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerJList.setLayoutOrientation(JList.VERTICAL);
        customerJList.setVisibleRowCount(-1);
        
        //getContentPane().add(customerShortList, "cell 0 1"); // Add it to the ScrollPanel instead
        JScrollPane customerListScrollPane = new JScrollPane(customerJList);
        customerListScrollPane.setPreferredSize(new Dimension(200, 500));
        customerListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(customerListScrollPane, "cell 0 1");
        
        MainFrame.customerIsToList = false;
    }
    
    
    /**
     * Custom renderer Class
     *
     * @author Andrew Wu, A01063367
     */
    class CustomerRender extends JLabel implements ListCellRenderer<Customer>  {
        
        public CustomerRender() {
            setOpaque(true);
        }
        
        @SuppressWarnings("static-access")
        @Override
        public Component getListCellRendererComponent(JList<? extends Customer> list, Customer customer, int index, boolean isSelected, boolean cellHasFocus) {
            
            setText( customer.getId() + "   " + customer.getFirstName() + " " + customer.getLastName() );
            
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
