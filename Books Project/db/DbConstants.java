/**
 * Project: a01063367_assignment02
 * File: DbConstants.java
 * Date: Nov 16, 2018
 * Time: 23:13:17 PM
 */

package a01063367.db;

/**
 * DbConstants class contains the constants to use in database
 * 
 * @author scirka
 *
 */
public interface DbConstants {
    
    String DB_PROPERTIES_FILENAME = "db.properties";
    String DB_DRIVER_KEY = "db.driver";
    String DB_URL_KEY = "db.url";
    String DB_USER_KEY = "db.user";
    String DB_PASSWORD_KEY = "db.password";
    String TABLE_ROOT = "A01063367_";
    String STUDENT_TABLE_NAME = TABLE_ROOT + "Student";
    String COURSE_TABLE_NAME = TABLE_ROOT + "Course";
    String ENROLLMENT_TABLE_NAME = TABLE_ROOT + "Enrollment";
    
}
