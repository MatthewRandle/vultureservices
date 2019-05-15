package utils;

public class Part {
    private String part, available;

    public Part(String part, String available) {
        this.part = part;
        this.available = available;
    }

    public String getPart() {
        return part;
    }
    public void setPart(String part) {
        this.part = part;
    }

    public String getAvailable() { return available; }
    public void setAvailable(String available) { this.available = available; }
}
