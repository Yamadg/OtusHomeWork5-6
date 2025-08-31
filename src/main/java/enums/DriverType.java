package enums;

public enum DriverType {
    CHROME,
    FIREFOX;


    public static DriverType fromString(String driverName) {
        for (DriverType type : DriverType.values()) {
            if (type.name().equalsIgnoreCase(driverName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported WebDriver: " + driverName + ". Supported types are: " + java.util.Arrays.toString(DriverType.values()));
    }
}