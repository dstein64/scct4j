package daterepo.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  Endpoint for retrieving all items
 *  Perhaps to be RESTful this would go in Item servlet with GET request
 *  that has no ID
 */
public class ItemsServlet extends HttpServlet {
    
    private static final int FILEBUFFERSIZE = 1024;
    
    // GET retrieves an existing item
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletOutputStream out = null;
        InputStream in = null;
        try {
            out = resp.getOutputStream();
            in = new FileInputStream(new File("items.json"));
            
            byte[] bytes = new byte[FILEBUFFERSIZE];
            int bytesRead;

            resp.setContentType("application/json");

            while ((bytesRead = in.read(bytes)) != -1) {
                out.write(bytes, 0, bytesRead);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }
}
