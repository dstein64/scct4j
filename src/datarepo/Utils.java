package datarepo;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.GeneralSecurityException;
import java.util.Scanner;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;

public class Utils {
    public static void genericicHandleError(Exception e, HttpServletResponse resp) {
        MyLogger.log.log(Level.SEVERE, "Error", e);
        
        if (Utils.hasCause(e, SizeLimitExceededException.class)) {
            // this doesn't work. Tomcat stops accepting data and connection closes, thus no error message sent
            resp.setStatus(413);
        } else {
            resp.setStatus(500);
        }
    }
    
    /**
     * Return a File object. Throws an exception if the File object is not a child
     * of the parent argument. That is, using ".." to get outside of parent is disabled.
     * @throws GeneralSecurityException 
     * @throws FileNotFoundException 
     */
    public static File secureFile(File parent, String child) throws GeneralSecurityException  {
        File f = new File(parent, child);
        File _parent = f.getParentFile();
        while (_parent != null) {
            if (_parent.equals(parent)) {
                return f;
            } else {
                _parent = _parent.getParentFile();
            }
        }
        throw new GeneralSecurityException();
    }
    
    // singleton
    // don't close because then you can't do subsequent reads from stdin
    private static Scanner inputScanner = null;
    public static synchronized Scanner inputScanner() {
        if (inputScanner == null) {
            inputScanner = new Scanner(System.in);
        }
        return inputScanner;
    }
    
    public static boolean yesOrNo(String question) {
        while (true) {
            System.out.print(question);
            String response = new String(inputScanner().next().toLowerCase());
            switch (response) {
                case "y":
                case "yes:":
                    return true;
                case "n":
                case "no":
                    return false;
                default:
                    break;  
            }
        }
    }
    
    /**
     * Returns true if t is itself or has as an underlying cause, cause
     */
    public static boolean hasCause(Throwable t, Class<? extends Throwable> cause) {
        while (true) {
            if (t.getClass().equals(cause)) {
                return true;
            } else if (t.getCause() != null) {
                t = t.getCause();
            } else {
                return false;
            }
        }
    }
}
