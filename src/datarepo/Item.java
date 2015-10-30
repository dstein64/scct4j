package datarepo;

import java.math.BigInteger;
import java.util.List;

public class Item {
    public String name;
    public BigInteger id;
    public String user;
    public long created;
    public long modified;
    public int priority;
    public String description;
    public List<String> files;
    
    public Item() {
        // TODO Auto-generated constructor stub
    }
}
