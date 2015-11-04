package daterepo.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import datarepo.Item;
import datarepo.ItemManager;

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
        ServletOutputStream out = resp.getOutputStream();
        
        List<Item> items = itemManager.getAllItems();
        
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
