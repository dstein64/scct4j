package datarepo;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;

public class Utils {
    public static void genericicHandleError(Exception e, HttpServletResponse resp) {
        MyLogger.log.log(Level.SEVERE, "Error", e);
        resp.setStatus(500);
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
}
