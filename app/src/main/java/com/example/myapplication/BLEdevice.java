package com.example.myapplication;

class BLEdevice {
    private String name;
    private String UUID;
    private String major;
    private String minor;

    private String distance ="far";
    private int rss;


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


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

    public int getRss() {
        return rss;
    }

    public void setRss(int rss) {
        this.rss = rss;
    }


    @Override
    public String toString() {;
     String a = "uuid: " +UUID +"\n major: "+major +"\n minor: "+minor +"\n rssi: "+rss+"\n"+distance;



     return a;

    }
}
