package scct4j.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import scct4j.FileItem;
import scct4j.FileManager;
import scct4j.Utils;

/**
 *  Endpoint for retrieving files
 */
public class FileServlet extends HttpServlet {
    private FileManager fileManager = FileManager.theFileManager();
    
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
        
        String filename;
        File file;
        try {
            FileItem f = fileManager.fidToFileItem(id);
            filename = f.name;
            file = fileManager.getFile(id);
        } catch (SQLException | GeneralSecurityException e) {
            Utils.genericicHandleError(e, resp);
            return;
        }
        
        resp.setHeader("Content-Disposition", "attachment; filename=" + filename + ";");
        
        FileInputStream fileStream = new FileInputStream(file);
        IOUtils.copy(fileStream, out);
        fileStream.close();
        out.close();
    }
}
