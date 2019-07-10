package com.example.myapplication;

class BLEdevice {
    private String name;
    private String UUID;
    private String major;
    private String minor;
    private MyStructure data = new MyStructure(6);


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
        return data.average();
    }

    public void setRss(int rss) {
       data.insert(rss);
    }


    @Override
    public String toString() {
        String a;
    if(getRss()>-60){
        a = "uuid: " +UUID +"\n major: "+major +"\n minor: "+minor +"\n avg of rssi: "+getRss()+"\n"+"near";
    }else{
        a = "uuid: " +UUID +"\n major: "+major +"\n minor: "+minor +"\n avg of rssi: "+getRss()+"\n"+"far";
    }



     return a;

    }
}
