package scct4j;

public class FileItem {
    public String name;
    public String type;
    public long size;
    public FileItem() {}
    public FileItem setName(String name) {
        this.name = name;
        return this;
    }
    public FileItem setType(String type) {
        this.type = type;
        return this;
    }
    public FileItem setSize(long size) {
        this.size = size;
        return this;
    }
}
