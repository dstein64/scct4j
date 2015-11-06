package datarepo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private File DIRECTORY = new File("files");
    
    public FileManager() {
        DIRECTORY.mkdirs();
    }
    
    public synchronized BigInteger addFile(PendingFile file, BigInteger itemId) throws IOException, SQLException, GeneralSecurityException {
        Connection conn = DatabaseManager.theConnection();
        PreparedStatement ps =
                conn.prepareStatement("INSERT INTO files VALUES (DEFAULT, ?, ?, ?)", new String[] {"ID"});
        ps.setString(1, file.name);
        ps.setString(2, file.contentType);
        ps.setLong(3, file.size);
        ps.executeUpdate();
        
        BigInteger fid;
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            fid = rs.getBigDecimal(1).toBigInteger();
        } else {
            throw new SQLException();
        }
        
        Files.copy(file.stream,
                Utils.secureFile(DIRECTORY, fid.toString(10)).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        file.stream.close();
        
        ps = conn.prepareStatement("INSERT INTO itemfiles VALUES (?, ?)");
        ps.setBigDecimal(1, new BigDecimal(itemId));
        ps.setBigDecimal(2, new BigDecimal(fid));
        ps.executeUpdate();
        
        return fid;
    }
    
    public FileItem fidToFileItem(BigInteger fid) throws SQLException, IOException {
        Connection conn = DatabaseManager.theConnection();
        PreparedStatement ps =
                conn.prepareStatement("SELECT * FROM files WHERE id = ?");
        ps.setBigDecimal(1, new BigDecimal(fid));
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            FileItem f = new FileItem()
                    .setName(rs.getString("name"))
                    .setType(rs.getString("type"))
                    .setSize(rs.getLong("size"));
            return f;
        } else {
            throw new SQLException();
        }
    }
    
    public File getFile(BigInteger fid) throws GeneralSecurityException {
        return Utils.secureFile(DIRECTORY, fid.toString());
    }
    
    /**
     * Return file IDs for the specified item ID
     * @throws IOException 
     * @throws SQLException 
     */
    public List<BigInteger> itemFiles(BigInteger id) throws SQLException, IOException {
        List<BigInteger> fids = new ArrayList<BigInteger>();
        Connection conn = DatabaseManager.theConnection();
        PreparedStatement ps =
                conn.prepareStatement("SELECT file FROM itemfiles WHERE item = ?");
        ps.setBigDecimal(1, new BigDecimal(id));
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            fids.add(rs.getBigDecimal("file").toBigInteger());
        }
        return fids;
    }
    
    /**
     * Deletes a file on disk, and removes the file from both the files table and itemfiles table.
     * 
     * @param fid
     * @throws SQLException
     * @throws IOException
     * @throws GeneralSecurityException 
     */
    public synchronized void delete(BigInteger fid) throws SQLException, IOException, GeneralSecurityException {
        // TODO: the following should be done in one transaction
        Connection conn = DatabaseManager.theConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM itemfiles WHERE file = ?");
        ps.setBigDecimal(1, new BigDecimal(fid));
        ps.execute();
        ps = conn.prepareStatement("DELETE FROM files WHERE id = ?");
        ps.setBigDecimal(1, new BigDecimal(fid));
        ps.execute();
        getFile(fid).delete();
    }
    
    // singleton
    private static FileManager theFileManager = null;
    public static synchronized FileManager theFileManager(){
        if (theFileManager == null) {
            theFileManager = new FileManager();
        }
        return theFileManager;
    }
}
