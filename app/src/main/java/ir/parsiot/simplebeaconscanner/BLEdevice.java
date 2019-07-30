package ir.parsiot.simplebeaconscanner;

class BLEdevice {
    private String name;
    private String UUID;
    private String major;
    private String minor;
    private String mac;
    private int rawRss;
    private Algorithm algorithm = new Algorithm();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getRss() {
        return algorithm.get_val();
    }

    public int getRawRss() {
        return rawRss;
    }

    public void setRss(int rss) {
        algorithm.insert(rss);
        rawRss = rss;
    }

    public String select_bounds() {
        // Showed each item template in the list
        String tempStr = "uuid: " + UUID + "\nmajor: " + major + ", minor: " + minor + "\nmac: " + mac + "\n      Algorithm: " + getRss() + " , RawRss: " + getRawRss() + "\n";
        Integer algoResRSS = getRss();
        int boundIndex = 0;
        for (; boundIndex < Settings.bounds.size(); boundIndex++) {
            if (algoResRSS > Settings.bounds.get(boundIndex)) {
                tempStr += Settings.boundNames.get(boundIndex);
            }
        }
        if (boundIndex == Settings.bounds.size() + 1) {
            tempStr += Settings.boundNames.get(boundIndex); // Set last boundNames
        }
        return tempStr;
    }

    @Override
    public String toString() {
        return select_bounds();
    }
}
