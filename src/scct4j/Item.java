package scct4j;

import java.io.IOException;
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
    public List<BigInteger> files; // IDs of files
    
    private Item(Builder builder) throws IOException {
        name = builder.name;
        id = builder.id;
        created = builder.created;
        modified = builder.modified;
        priority = builder.priority;
        description = builder.description;
        files = builder.files;
    }
    
    public static class Builder {
        public String name;
        public BigInteger id;
        public long created;
        public long modified;
        public int priority;
        public String description;
        public List<BigInteger> files = new ArrayList<BigInteger>();
        
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder setId(BigInteger id) {
            this.id = id;
            return this;
        }
        
        public Builder setCreated(long created) {
            this.created = created;
            return this;
        }
        
        public Builder setModified(long modified) {
            this.modified = modified;
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
        
        public Builder addFile(BigInteger file) {
            this.files.add(file);
            return this;
        }
        
        public Item build() throws IOException {
            return new Item(this);
        }
    }
}
