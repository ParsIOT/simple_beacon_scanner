package ir.parsiot.simplebeaconscanner;

import java.util.ArrayList;
public class Settings {
    // General Settings
    public static final ArrayList<String> VALID_LAYOUTS = new ArrayList<String>(){
        {
            add("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24");
            add("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
            add("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
            add("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19");
            add("x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15");
            add("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-21v");
            add("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19");
            add("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v");
        }
    };
    public static final long PERIOD_TIME_BETWEEN = 150L;

    // Moving Average algorithm settings
    public static final int MOVING_SLIDE_MAX_LENGTH = 6;

    public static final ArrayList<Integer> bounds = new ArrayList<Integer>(){ // Note: Its size must be exactly one less than size of boundNames
        {
            // Note: Enter sorted values, e.g. Enter -60 then -70 [dbm]
            add(-60);
        }
    };
    public static final ArrayList<String> boundNames = new ArrayList<String>(){
        {
            add("Near");
            add("Far");
        }
    };
}
