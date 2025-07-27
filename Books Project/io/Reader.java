/**
 * Project: a01063367_assignment02
 * File: Reader.java
 * Date: Nov 16, 2018
 * Time: 23:38:40 PM
 */

package a01063367.io;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import a01063367.ApplicationException;

/**
 * Reader
 * 
 * @author Sam Cirka, A00123456
 *
 */
public class Reader {
    
    public static final String RECORD_DELIMITER = ":";
    public static final String FIELD_DELIMITER = "\\|";
    private static final Logger LOG = LogManager.getLogger();
    
    
    /**
     * Given a FIELD_DELIMITER delimited input string, parts the string into a String array.
     * 
     * @param row
     *            input string
     * @return the parsed String array
     * @throws ApplicationException
     *             if the element count doesn't match the expected count.
     */
    public static String[] getElements(String row, int attributeCount) throws ApplicationException {
        String[] elements = row.split(FIELD_DELIMITER);
        LOG.trace(elements);
        if (elements.length != attributeCount) {
            throw new ApplicationException(String.format("Expected %d but got %d: %s", attributeCount, elements.length, Arrays.toString(elements)));
        }
        
        return elements;
    }
    
}
