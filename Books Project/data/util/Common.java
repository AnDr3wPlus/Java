/**
 * Project: a01063367_assignment02
 * File: Common.java
 * Date: Nov 15, 2018
 * Time: 22:45:36 PM
 */

package a01063367.data.util;

/**
 * Common
 * 
 * @author Sam Cirka, A00123456
 *
 */
public class Common {
    
    /**
     * If the input string exceeds the length then truncate the string to the length - 3 characters and add "..."
     * 
     * @param title
     * @param length
     * @return a string
     */
    public static String truncateIfRequired(String title, int length) {
        if (title.length() > length) {
            title = title.substring(0, length - 3) + "...";
        }
        
        return title;
    }
    
}
