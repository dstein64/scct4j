package datarepo;

import java.io.InputStream;

/**
 * A PendingFile gets saved by the ItemManager/FileManager
 */
public class PendingFile {
    public String name;
    public String contentType;
    public InputStream stream;
    public long size;
}
