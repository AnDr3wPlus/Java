/**
 * Project: a01063367_assignment02
 * File: CustomerDetailsDialog.java
 * Date: Nov 21, 2018
 * Time: 17:40:29 PM
 */
package a01063367.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.ApplicationException;
import a01063367.data.Customer;
import a01063367.db.CustomerDao;
import a01063367.db.Database;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author Andrew Wu, A01063367
 *
 */
@SuppressWarnings("serial")
public class CustomerDetailsDialog extends JDialog {
    
    private static final Logger LOG = LogManager.getLogger();
    
    private final JPanel contentPanel;
    
    private JTextField textField_ID;
    private JTextField textField_FirstName;
    private JTextField textField_LastName;
    private JTextField textField_Street;
    private JTextField textField_City;
    private JTextField textField_PostalCode;
    private JTextField textField_Phone;
    private JTextField textField_Email;
    private JTextField textField_JoinedDate;
    private JPanel buttonPanel;
    private JButton OK_Button;
    private JButton Cancel_Button;
        
    /**
     * Constructor for the Dialog 
     */
    public CustomerDetailsDialog(Customer aCustomer) {
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        getRootPane().setBorder( BorderFactory.createLineBorder(Color.GRAY,1) );
        //getRootPane().setWindowDecorationStyle(JRootPane.NONE); // Use to get rid of the boarder
        setForeground(SystemColor.activeCaptionBorder);
        setBackground(SystemColor.activeCaptionBorder);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        setModalityType(ModalityType.DOCUMENT_MODAL);
        LOG.debug(aCustomer);
        
        
        /** Top Panel */
        contentPanel = new JPanel();
        contentPanel.setBackground(UIManager.getColor("menu"));
        //setBounds(300, 300, 400, 339);
        setSize(400, 340);
        setLocationRelativeTo(null);//BorderFactory.createLineBorder(Color.red,3) CompoundBorder
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        //contentPanel.setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.gray,1), new EmptyBorder(5, 5, 5, 5)));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new MigLayout("", "[shrink 10,right][350px,left]", "[][][][][][][][][]"));
        
        /* ID */
        JLabel lblId = new JLabel("ID");
        contentPanel.add(lblId, "cell 0 0,alignx trailing");
        
        textField_ID = new JTextField();
        textField_ID.setBackground(UIManager.getColor("menu"));
        textField_ID.setForeground(Color.GRAY);
        textField_ID.setEnabled(false);
        textField_ID.setEditable(false);
        textField_ID.setText( String.valueOf(aCustomer.getId()) );
        contentPanel.add(textField_ID, "cell 1 0,growx");
        textField_ID.setColumns(10);
        
        /* First Name */
        JLabel lblNewLabel = new JLabel("First Name");
        contentPanel.add(lblNewLabel, "cell 0 1,alignx trailing");
        
        textField_FirstName = new JTextField();
        textField_FirstName.setBackground(Color.WHITE);
        textField_FirstName.setText( aCustomer.getFirstName() );
        contentPanel.add(textField_FirstName, "cell 1 1,growx");
        textField_FirstName.setColumns(10);
        
        /* Last Name */
        JLabel lblLastName = new JLabel("Last Name");
        contentPanel.add(lblLastName, "cell 0 2,alignx trailing");
        
        textField_LastName = new JTextField();
        textField_LastName.setBackground(Color.WHITE);
        textField_LastName.setText( aCustomer.getLastName() );
        contentPanel.add(textField_LastName, "cell 1 2,growx");
        textField_LastName.setColumns(10);
        
        /* Street */
        JLabel lblStreet = new JLabel("Street");
        contentPanel.add(lblStreet, "cell 0 3,alignx trailing");
        
        textField_Street = new JTextField();
        textField_Street.setBackground(Color.WHITE);
        textField_Street.setText( aCustomer.getStreet() );
        contentPanel.add(textField_Street, "cell 1 3,growx");
        textField_Street.setColumns(10);
        
        /* City */
        JLabel lblCity = new JLabel("City");
        contentPanel.add(lblCity, "cell 0 4,alignx trailing");
        
        textField_City = new JTextField();
        textField_City.setBackground(Color.WHITE);
        textField_City.setText( aCustomer.getCity() );
        contentPanel.add(textField_City, "cell 1 4,growx");
        textField_City.setColumns(10);
        
        /* Postal Code */
        JLabel lblPostalCode = new JLabel("Postal Code");
        contentPanel.add(lblPostalCode, "cell 0 5,alignx trailing");
        
        textField_PostalCode = new JTextField();
        textField_PostalCode.setBackground(Color.WHITE);
        textField_PostalCode.setText( aCustomer.getPostalCode() );
        contentPanel.add(textField_PostalCode, "cell 1 5,growx");
        textField_PostalCode.setColumns(10);
        
        /* Phone */
        JLabel lblPhone = new JLabel("Phone");
        contentPanel.add(lblPhone, "cell 0 6,alignx trailing");
        
        textField_Phone = new JTextField();
        textField_Phone.setBackground(Color.WHITE);
        textField_Phone.setText( aCustomer.getPhone() );
        contentPanel.add(textField_Phone, "cell 1 6,growx");
        textField_Phone.setColumns(10);
        
        /* Email */
        JLabel lblEmail = new JLabel("Email");
        contentPanel.add(lblEmail, "cell 0 7,alignx trailing");
        
        textField_Email = new JTextField();
        textField_Email.setBackground(Color.WHITE);
        textField_Email.setText( aCustomer.getEmailAddress() );
        contentPanel.add(textField_Email, "cell 1 7,growx");
        textField_Email.setColumns(10);
        
        /* Joined Date */
        JLabel label = new JLabel("Joined Date");
        contentPanel.add(label, "cell 0 8,alignx trailing");
        
        textField_JoinedDate = new JTextField();
        textField_JoinedDate.setBackground(Color.WHITE);
        textField_JoinedDate.setText( String.valueOf(aCustomer.getJoinedDate()) );
        contentPanel.add(textField_JoinedDate, "cell 1 8,growx");
        textField_JoinedDate.setColumns(10);
        
        
        /** Button Panel */
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Buttons align to the right
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        /* OK Button */
        OK_Button = new JButton("OK");
        OK_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Connect to database and update Customer information change
                try {
                    updateCustomer(aCustomer);
                    LOG.debug(aCustomer);
                    Database database = new Database();
                    CustomerDao customerDao = new CustomerDao(database);
                    customerDao.update(aCustomer);  // Update customer information to the database
                } catch (ApplicationException | IOException | SQLException e1) {
                    LOG.error(e1.getMessage());
                }
                
                CustomerDetailsDialog.this.dispose();
            }
        });
        buttonPanel.add(OK_Button);
        
        /* Cancel Button */
        Cancel_Button = new JButton("Cancel");
        Cancel_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomerDetailsDialog.this.dispose();
            }
        });
        buttonPanel.add(Cancel_Button);
        
    }
    
    /**
     * Update Customer Information
     */
    public void updateCustomer(Customer customer) {
        customer.setFirstName(textField_FirstName.getText());
        customer.setLastName(textField_LastName.getText());
        customer.setStreet(textField_Street.getText());
        customer.setCity(textField_City.getText());
        customer.setPostalCode(textField_PostalCode.getText());
        customer.setPhone(textField_Phone.getText());
        customer.setEmailAddress(textField_Email.getText());
        String dateString = textField_JoinedDate.getText();  // 2008-02-17
        LOG.debug(dateString);
        int year = Integer.parseInt(dateString.substring(0, 4));
        int month = Integer.parseInt(dateString.substring(5, 7));
        int day = Integer.parseInt(dateString.substring(8));
        customer.setJoinedDate(year, month, day);
    }
    
}
