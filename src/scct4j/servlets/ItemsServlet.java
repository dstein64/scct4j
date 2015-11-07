package scct4j.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import scct4j.DatabaseManager;
import scct4j.Item;
import scct4j.ItemManager;
import scct4j.MyLogger;

/**
 *  Endpoint for retrieving all items
 *  Perhaps to be RESTful this would go in Item servlet with GET request
 *  that has no ID
 */
public class ItemsServlet extends HttpServlet {
    
    private ItemManager itemManager = ItemManager.theItemManager();
    
    // GET retrieves an existing item
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        try {
            DatabaseManager.theDatabaseManager();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        ServletOutputStream out = resp.getOutputStream();
        
        List<Item> items;
        try {
            items = itemManager.getAllItems();
        } catch (SQLException e) {
            MyLogger.log.log(Level.SEVERE, "SQL Error", e);
            resp.setStatus(500);
            return;
        }
        
        JSONArray array = new JSONArray();
        for (Item item : items) {
            JSONObject object = new JSONObject();
            object.put("name", item.name);
            object.put("id", item.id);
            object.put("created", item.created);
            object.put("modified", item.modified);
            object.put("priority", item.priority);
            object.put("description", item.description);
            array.put(object);
        }
        
        out.write(array.toString().getBytes());
        out.close();
    }
}
