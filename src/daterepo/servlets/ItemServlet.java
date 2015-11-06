package daterepo.servlets;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.json.JSONException;
import org.json.JSONObject;

import datarepo.FileItem;
import datarepo.FileManager;
import datarepo.Item;
import datarepo.Item.Builder;
import datarepo.ItemManager;
import datarepo.PendingFile;
import datarepo.Utils;

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
        
        Item item;
        try {
            item = itemManager.getItem(id);
        } catch (SQLException e) {
            Utils.genericicHandleError(e, resp);
            return;
        }
        
        JSONObject object = new JSONObject();
        object.put("name", item.name);
        object.put("id", item.id);
        object.put("created", item.created);
        object.put("modified", item.modified);
        object.put("priority", item.priority);
        object.put("description", item.description);
        
        JSONArray array = new JSONArray();
        for (BigInteger fileId : item.files) {
            JSONObject file = new JSONObject();
            try {
                FileItem f = fileManager.fidToFileItem(fileId);
                file.put("name", f.name);
                file.put("size", f.size);
            } catch (JSONException | SQLException e) {
                Utils.genericicHandleError(e, resp);
                return;
            }
            file.put("id", fileId);
            array.put(file);
        }
        object.put("files", array);
        
        out.write(object.toString().getBytes());
        out.close();
    }
    
    private String partToString(Part p, HttpServletRequest req) throws IOException, ServletException {
        return IOUtils.toString(p.getInputStream());
    }
    
    /**
     * Given a POST/PUT request, returns the corresponding Builder object.
     * Doesn't set every field. For example, created/modified should be set by the caller
     * @throws ServletException 
     * @throws IOException 
     */
    private Builder requestToBuilder(HttpServletRequest req) throws IOException, ServletException {
        String name = partToString(req.getPart("name"), req);
        String _priority = partToString(req.getPart("priority"), req);
        int priority = Integer.parseInt(_priority);
        String description = partToString(req.getPart("description"), req);
        
        Builder builder = new Builder()
                .setName(name)
                .setDescription(description)
                .setPriority(priority);
        return builder;
    }
    
    private List<PendingFile> requestToPendingFiles(HttpServletRequest req) throws IOException, ServletException {
        List<PendingFile> files = new ArrayList<PendingFile>();
        for (Part p : req.getParts()) {
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
        return files;
    }
    
    // POST creates a new item
    // It receives Content-Type: multipart/form-data
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long time = System.currentTimeMillis();
        Builder builder = requestToBuilder(req)
                .setCreated(time)
                .setModified(time);
        List<PendingFile> files = requestToPendingFiles(req);
        try {
            itemManager.addItem(builder, files);
        } catch (SQLException e) {
            Utils.genericicHandleError(e, resp);
            return;
        }
    }
    
    // DELETE removes an item
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BigInteger id = getId(req);
        try {
            itemManager.deleteItem(id);
        } catch (SQLException e) {
            Utils.genericicHandleError(e, resp);
            return;
        }
    }
    
    // PUT updates an existing item
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BigInteger id = getId(req);
        long time = System.currentTimeMillis();
        // Don't need to set "created". We keep the original "created" date
        Builder builder = requestToBuilder(req)
                .setModified(time);
        List<PendingFile> files = requestToPendingFiles(req);
        
        List<BigInteger> removeFiles = new ArrayList<BigInteger>();
        for (Part p : req.getParts()) {
            if (p.getName().equals("removefiles[]")) {
                removeFiles.add(new BigInteger(partToString(p, req)));
            }
        }
        
        try {
            // TODO: have to get the list of files to remove
            itemManager.modifyItem(id, builder, files, removeFiles);
        } catch (SQLException e) {
            Utils.genericicHandleError(e, resp);
            return;
        }
    }
}