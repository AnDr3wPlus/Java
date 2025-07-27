/**
 * Project: a01063367_assignment02
 * File: ApplicationException.java
 * Date: Nov 16, 2018
 * Time: 23:39:08 PM
 */

package a01063367;

/**
 * ApplicationException
 * 
 * @author Sam Cirka, A00123456
 *
 */
@SuppressWarnings("serial")
public class ApplicationException extends Exception {
    
    /**
     * Construct an ApplicationException
     * 
     * @param message
     *            the exception message.
     */
    public ApplicationException(String message) {
        super(message);
    }
    
    /**
     * Construct an ApplicationException
     * 
     * @param throwable
     *            a Throwable.
     */
    public ApplicationException(Throwable throwable) {
        super(throwable);
    }
    
}
