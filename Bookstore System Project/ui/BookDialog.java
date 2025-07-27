/**
 * Project: a01063367_assignment02
 * File: BookDialog.java
 * Date: Nov 22, 2018
 * Time: 08:27:38 AM
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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.data.Book;
import a01063367.io.BooksReport.CompareByAuthor;
import a01063367.io.BooksReport.CompareByAuthorDescending;
import net.miginfocom.swing.MigLayout;

/**
 * @author Andrew Wu, A01063367
 *
 */
@SuppressWarnings("serial")
public class BookDialog extends JDialog {
    
    private static final Logger LOG = LogManager.getLogger();
    
    
    /**
     * Constructor
     */
    public BookDialog(ArrayList<Book> books) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        setTitle("Book Dialog");
        LOG.info("Initializing Book Dialog");
        getContentPane().setLayout(new MigLayout("", "[155.00,grow]", "[][186.00,grow]"));
        setSize(800, 579);
        setLocationRelativeTo(null);
        
        JLabel bookDialogLabel = new JLabel(String.format(" %-12s  %-90s     %-70s", "Book ID", "Author", "Title"));
        getContentPane().add(bookDialogLabel, "cell 0 0");
        
        
        // Compared by Author
        if( MainFrame.bookByAuthor ) {
            if(MainFrame.bookByDescending) {
                Collections.sort(books, new CompareByAuthorDescending());
            } else {
                Collections.sort(books, new CompareByAuthor());
            }
        }
        LOG.debug(books.toString());
        
        // JList
        @SuppressWarnings({ "unchecked", "rawtypes" })
        JList<Book> bookJList = new JList(books.toArray());
        bookJList.setCellRenderer( new BookRender() );
        
        bookJList.setSize(800, 550);
        bookJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookJList.setLayoutOrientation(JList.VERTICAL);
        bookJList.setVisibleRowCount(-1);
        
        //getContentPane().add(customerShortList, "cell 0 1"); // Add it to the ScrollPanel instead
        JScrollPane bookListScrollPane = new JScrollPane(bookJList);
        bookListScrollPane.setPreferredSize(new Dimension(800, 550));
        bookListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(bookListScrollPane, "cell 0 1");
        
        
    }
    
    
    /**
     * Custom renderer Class
     *
     * @author Andrew Wu, A01063367
     */
    class BookRender extends JLabel implements ListCellRenderer<Book>  {
        
        public BookRender() {
            setOpaque(true);
        }
        
        @SuppressWarnings("static-access")
        @Override
        public Component getListCellRendererComponent(JList<? extends Book> list, Book book, int index, boolean isSelected, boolean cellHasFocus) {
            
            setText(String.format("%03d              %-90s     %-70s", book.getId(), StringUtils.abbreviate(book.getAuthors(), 60), StringUtils.abbreviate(book.getTitle(), 60)));
            //StringUtils.abbreviate(title, 40)
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
