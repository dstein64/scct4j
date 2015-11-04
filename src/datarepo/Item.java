package datarepo;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Item {
    public String name;
    public BigInteger id;
    public long created;
    public long modified;
    public int priority;
    public String description;
    public List<BigInteger> files = new ArrayList<BigInteger>(); // IDs of files
    
    private Item(Builder builder) throws IOException {
        name = builder.name;
        id = builder.id;
        created = System.currentTimeMillis();
        modified = created;
        priority = builder.priority;
        description = builder.description;
        // save files
        FileManager fileManager = FileManager.theFileManager();
        for (PendingFile file : builder.files) {
            BigInteger id = fileManager.addFile(file);
            files.add(id);
        }
    }
    
    /**
     * A PendingFile gets saved by the ItemManager/FileManager
     */
    public static class PendingFile {
        public String name;
        public String contentType;
        public InputStream stream;
        public long size;
    }
    
    public static class Builder {
        private String name;
        private BigInteger id;
        private int priority;
        private String description;
        private List<PendingFile> files;
        
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder setId(BigInteger id) {
            this.id = id;
            return this;
        }
        
        public Builder setPriority(int priority) {
            this.priority = priority;
            return this;
        }
        
        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }
        
        public Builder setFiles(List<PendingFile> files) {
            this.files = files;
            return this;
        }
        
        public Item build() throws IOException {
            return new Item(this);
        }
    }
}
