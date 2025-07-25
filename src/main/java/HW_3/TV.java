package HW_3;

public class TV {
    private String brand;
    private int screenSize;
    private boolean smartTV;

    public TV(String brand, int screenSize, boolean smartTV) {
        this.brand = brand;
        this.screenSize = screenSize;
        this.smartTV = smartTV;
    }

    public String getBrand() {
        return brand;
    }

    public int getScreenSize() {
        return screenSize;
    }

    public boolean isSmartTV() {
        return smartTV;
    }

    public void displayInfo() {
        System.out.println("Brand: " + getBrand());
        System.out.println("Screen Size: " + getScreenSize() + " inches");
        System.out.println("Smart TV: " + (isSmartTV() ? "Yes" : "No"));
    }

}
