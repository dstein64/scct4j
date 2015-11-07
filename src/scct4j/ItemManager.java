package scct4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import scct4j.Item.Builder;

public class ItemManager {
    public void addItem(Builder builder, List<PendingFile> pendingFiles) throws IOException, SQLException, GeneralSecurityException {
        BigInteger id;
        synchronized (this) {
            Connection conn = DatabaseManager.theConnection();
            // "ID" has to be upper case or doesn't work
            PreparedStatement ps =
                    conn.prepareStatement("INSERT INTO items VALUES (DEFAULT, ?, ?, ?, ?, ?)", new String[] {"ID"});
            ps.setString(1, builder.name);
            ps.setLong(2, builder.created);
            ps.setLong(3, builder.modified);
            ps.setInt(4, builder.priority);
            ps.setString(5, builder.description);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getBigDecimal(1).toBigInteger();
            } else {
                throw new SQLException();
            }
            
            builder.setId(id);
        }
        
        // save files
        FileManager fileManager = FileManager.theFileManager();
        for (PendingFile file : pendingFiles) {
            BigInteger fid = fileManager.addFile(file, id);
        }
    }
    
    public synchronized void modifyItem(BigInteger id,
            Builder builder,
            List<PendingFile> pendingFiles,
            List<BigInteger> removeFiles) throws SQLException, IOException, GeneralSecurityException {
        Connection conn = DatabaseManager.theConnection();
        PreparedStatement ps =
                conn.prepareStatement("UPDATE items SET name = ?, priority = ?, description = ?, modified = ? WHERE id = ?");
        ps.setString(1, builder.name);
        ps.setInt(2, builder.priority);
        ps.setString(3, builder.description);
        ps.setLong(4, builder.modified);
        ps.setBigDecimal(5, new BigDecimal(id));
        ps.executeUpdate();
        
        FileManager fileManager = FileManager.theFileManager();
        
        for (BigInteger fid : removeFiles) {
            fileManager.delete(fid);
        }
        
        for (PendingFile file : pendingFiles) {
            BigInteger fid = fileManager.addFile(file, id);
        }
    }
    
    public synchronized void deleteItem(BigInteger id) throws SQLException, IOException, GeneralSecurityException {
       // TODO: throw error if problem deleting
        
        FileManager fm = FileManager.theFileManager();
        List<BigInteger> fids = fm.itemFiles(id);
        for (BigInteger fid : fids) {
            fm.delete(fid);
        }
        
        Connection conn = DatabaseManager.theConnection();
        PreparedStatement ps =
                conn.prepareStatement("DELETE FROM items WHERE id = ?");
        ps.setBigDecimal(1, new BigDecimal(id));
        ps.execute();
    }
    
    /**
     * Convert a ResultSet to an item
     * @throws IOException 
     * @throws SQLException 
     */
    private Item rsToItem(ResultSet rs) throws SQLException, IOException {
        // Row: id,name,created,modified,priority,description
        BigInteger id = rs.getBigDecimal("id").toBigInteger();
        Builder builder = new Builder()
                .setId(id)
                .setName(rs.getString("name"))
                .setCreated(rs.getLong("created"))
                .setModified(rs.getLong("modified"))
                .setPriority(rs.getInt("priority"))
                .setDescription(DatabaseManager.clobToString(rs.getClob("description")));
        
        List<BigInteger> fids = FileManager.theFileManager().itemFiles(id);
        for (BigInteger fid : fids) {
            builder.addFile(fid);
        }
        
        return builder.build();
    }
    
    public synchronized Item getItem(BigInteger id) throws SQLException, IOException {
        Connection conn = DatabaseManager.theConnection();
        PreparedStatement ps =
                conn.prepareStatement("SELECT * FROM items WHERE id = ?");
        ps.setBigDecimal(1, new BigDecimal(id));
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return rsToItem(rs);
        } else {
            throw new SQLException();
        }
    }
    
    public synchronized List<Item> getAllItems() throws SQLException, IOException {
        // TODO: return sorted? 
        Connection conn = DatabaseManager.theConnection();
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM items");
        List<Item> items = new ArrayList<>();
        while (rs.next()) {
            items.add(rsToItem(rs));
        }
        return items;
    }
    
    // singleton
    private static ItemManager theItemManager = null;
    public static synchronized ItemManager theItemManager(){
        if (theItemManager == null) {
            theItemManager = new ItemManager();
        }
        return theItemManager;
    }
}
