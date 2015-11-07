package datarepo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.derby.tools.ij;

public class DatabaseManager {
    private String protocol = "jdbc:derby:";
    private String dbName = "db/derby";
    
    private Connection conn;
    
    /**
     * Initialize the database
     * @throws IOException 
     */
    public void init(OutputStream os) throws IOException {
        InputStream init = new FileInputStream(new File("db/init.sql"));
        
        ij.runScript(conn, init, "ASCII", os, "ASCII");
        
        if (init != null)
            init.close();
    }
    
    private DatabaseManager() throws SQLException, IOException {
        Properties derbyProps = new Properties();
        InputStream is = new FileInputStream(new File("db/derby.properties"));
        derbyProps.load(is);
        is.close();
        for (Entry<Object, Object> e : derbyProps.entrySet()) {
            System.setProperty(e.getKey().toString(), e.getValue().toString());
        }
        
        Properties connProps = new Properties();
        //connProps.put("user", "user1");
        conn = DriverManager.getConnection(protocol + dbName + ";create=true", connProps);
        
        // Be sure to shutdown embedded database. (prevents a recovery operation
        // on subsequent boot
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                try {
                    // This style shutdown always throws an exception
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException e) {
                    MyLogger.log.log(Level.INFO, "Derby shutting down", e);
                }
            }
        }, "Derby Shutdown Thread"));
    }
    
    // singleton
    private static DatabaseManager theDatabaseManager = null;
    public static synchronized DatabaseManager theDatabaseManager() throws SQLException, IOException {
        if (theDatabaseManager == null) {
            theDatabaseManager = new DatabaseManager();
        }
        return theDatabaseManager;
    }
    
    public static synchronized Connection theConnection() throws SQLException, IOException {
        return theDatabaseManager().conn;
    }
    
    public static String clobToString(Clob clob) throws IOException, SQLException {
        InputStream in = clob.getAsciiStream();
        StringWriter w = new StringWriter();
        IOUtils.copy(in, w);
        in.close();
        w.close();
        return w.toString();
    }
    
    /**
     * Database admin (e.g., initialize tables, update schemas, etc.)
     * @throws SQLException 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException, SQLException {
        if (args.length > 0) {
            String arg = args[0];
            switch (arg) {
                case "init":
                    if (Utils.yesOrNo("This will wipe existing data. Continue? [y/n] ")) {
                        System.out.println("Initializing Database");
                        // this wipes any existing database, and initializes a new one
                        //OutputStream os = new NullOutputStream();
                        FileUtils.deleteDirectory(new File("db/derby"));
                        theDatabaseManager().init(System.out);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid argument");
            }
        } else {
            throw new IllegalArgumentException("Argument required");
        }
    }
}
