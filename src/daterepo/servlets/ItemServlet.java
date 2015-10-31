package daterepo.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *  Endpoint for creating/retrieving/updating/deleting a single item
 */
public class ItemServlet extends HttpServlet {
    public static BigInteger getId(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (!pathInfo.isEmpty())
            pathInfo = pathInfo.substring(1);
        return new BigInteger(pathInfo, 10);
    }
    
    // GET retrieves an existing item
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BigInteger id = getId(req);
        ServletOutputStream out = resp.getOutputStream();
        
        JSONObject object = new JSONObject();
        object.put("name", "Item " + id.toString());
        object.put("id", id);
        object.put("user", "dan");
        object.put("created", System.currentTimeMillis());
        object.put("modified", System.currentTimeMillis());
        object.put("priority", 75);
        object.put("description", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
        JSONArray array = new JSONArray();
        List<String> files = Arrays.asList("CLIO.Height.Data Doc.Template.xlsx", "CLIO.Height.xlsx", "height.pdf");
        for (String file : files) {
            array.put(file);
        }
        object.put("files", array);
        
        out.write(object.toString().getBytes());
        out.close();
    }
    
    // POST creates a new item
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        System.out.println("HELLO");
    }
    
    // DELETE removes an item
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BigInteger id = getId(req);
        // TODO: Delete item
    }
    
    // PUT updates an existing item
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BigInteger id = getId(req);
        // TODO: Update item
    }
}
