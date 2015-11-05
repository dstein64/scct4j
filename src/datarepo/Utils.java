package datarepo;

import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;

public class Utils {
    public static void genericicHandleError(Exception e, HttpServletResponse resp) {
        MyLogger.log.log(Level.SEVERE, "Error", e);
        resp.setStatus(500);
    }
}
