package datarepo;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ItemManager {
    private ItemManager() {}
    
    private Map<BigInteger, Item> items = new HashMap<BigInteger, Item>();
    
    private BigInteger counter = BigInteger.ZERO;
    
    public synchronized void addItem(Item.Builder builder) throws IOException {
        builder.setId(counter);
        counter = counter.add(BigInteger.ONE);
        Item item = builder.build();
        items.put(item.id, item);
    }
    
    public synchronized void deleteItem(BigInteger id) {
        Item item = items.get(id);
        for (BigInteger fileId : item.files) {
            FileManager.theFileManager().delete(fileId);
        }
        items.remove(id);
    }
    
    public synchronized Item getItem(BigInteger id) {
        // in items, index matches id
        return items.get(id);
    }
    
    public synchronized List<Item> getAllItems() {
        // TODO: return sorted? 
        List<Item> _items = new ArrayList<>();
        for (Entry<BigInteger, Item> e : items.entrySet()) {
            _items.add(e.getValue());
        }
        return _items;
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
