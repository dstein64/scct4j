package daterepo.servlets;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Endpoint for retrieving files
 */
public class FileServlet extends HttpServlet {
    public static BigInteger getId(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (!pathInfo.isEmpty())
            pathInfo = pathInfo.substring(1);
        return new BigInteger(pathInfo, 10);
    }
    
    // GET retrieves a file
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BigInteger id = getId(req);
        ServletOutputStream out = resp.getOutputStream();
        
        String filename = "GETFILENAME_" + id.toString() + ".txt";
        
        resp.setHeader("Content-Disposition", "attachment; filename=" + filename + ";");
        
        out.write("Hello World!\n".getBytes());
        out.close();
    }
}
