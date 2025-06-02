package objects;

public class Jewelry extends ItemBase {
    private int valueEstimate;
    private String fileName;
    private String type;

    public int getValueEstimate() {
        return valueEstimate;
    }

    public void setValueEstimate(int valueEstimate) {
        this.valueEstimate = valueEstimate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
