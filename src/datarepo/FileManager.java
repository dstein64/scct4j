package datarepo;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private String DIRECTORY = "files";
    
    public FileManager() {
        new File(DIRECTORY).mkdirs();
    }
    
    private Map<BigInteger, String> file2name = new HashMap<BigInteger, String>();
    
    private BigInteger counter = BigInteger.ZERO;
    
    public synchronized BigInteger addFile(PendingFile file) throws IOException {
        BigInteger id = counter;
        Files.copy(file.stream,
                Paths.get(DIRECTORY, id.toString(10)),
                StandardCopyOption.REPLACE_EXISTING);
        file.stream.close();
        file2name.put(counter, file.name);
        counter = counter.add(BigInteger.ONE);
        return id;
    }
    
    public String name(BigInteger id) {
        return file2name.get(id);
    }
    
    public File getFile(BigInteger id) {
        return new File(DIRECTORY, id.toString());
    }
    
    public synchronized void delete(BigInteger id) {
        getFile(id).delete();
        file2name.remove(id);
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
