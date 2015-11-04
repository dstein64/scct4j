package daterepo.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import datarepo.FileManager;
import datarepo.Item;
import datarepo.Item.Builder;
import datarepo.Item.PendingFile;
import datarepo.ItemManager;

/**
 *  Endpoint for creating/retrieving/updating/deleting a single item
 */
public class ItemServlet extends HttpServlet {
    private ItemManager itemManager = ItemManager.theItemManager();
    private FileManager fileManager = FileManager.theFileManager();
    
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
        
        Item item = itemManager.getItem(id);
        
        JSONObject object = new JSONObject();
        object.put("name", item.name);
        object.put("id", item.id);
        object.put("created", item.created);
        object.put("modified", item.modified);
        object.put("priority", item.priority);
        object.put("description", item.description);
        
        JSONArray array = new JSONArray();
        List<String> files = Arrays.asList("CLIO.Height.Data Doc.Template.xlsx", "CLIO.Height.xlsx", "height.pdf");
        for (BigInteger fileId : item.files) {
            JSONObject file = new JSONObject();
            file.put("name", fileManager.name(fileId));
            file.put("id", fileId);
            array.put(file);
        }
        object.put("files", array);
        
        out.write(object.toString().getBytes());
        out.close();
    }
    
    // POST creates a new item
    // It receives Content-Type: multipart/form-data
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = IOUtils.toString(req.getPart("name").getInputStream());
        String _priority = IOUtils.toString(req.getPart("priority").getInputStream());
        int priority = Integer.parseInt(_priority);
        String description = IOUtils.toString(req.getPart("description").getInputStream());
        
        List<PendingFile> files = new ArrayList<PendingFile>();
        Collection<Part> parts = req.getParts();
        for (Part p : parts) {
            if (p.getName().equals("files[]")) {
                PendingFile file = new PendingFile();
                file.name = p.getSubmittedFileName();
                file.contentType = p.getContentType();
                file.stream = p.getInputStream(); // input stream is closed when copied
                                                  // in FileManager#addFile
                file.size = p.getSize();
                files.add(file);
            }
        }
        
        Builder builder = new Builder()
                .setName(name)
                .setDescription(description)
                .setPriority(priority)
                .setFiles(files);
        
        itemManager.addItem(builder);
    }
    
    // DELETE removes an item
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BigInteger id = getId(req);
        itemManager.deleteItem(id);
    }
    
    // PUT updates an existing item
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BigInteger id = getId(req);
        // TODO: Update item
    }
}
