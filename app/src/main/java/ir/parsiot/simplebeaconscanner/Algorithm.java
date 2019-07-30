package ir.parsiot.simplebeaconscanner;

import java.util.ArrayList;
import java.util.List;

public class Algorithm {
    private List<Integer> rssi = new ArrayList<>();
    private int count = 0;

    public Algorithm(List<Integer> rssi) {
        this.rssi = rssi;
        count = 0;
    }

    public Algorithm() {
        count = 0;
    }

    public void insert(int value) {
        int c = count % Settings.MOVING_SLIDE_MAX_LENGTH;
        if (count < Settings.MOVING_SLIDE_MAX_LENGTH) {
            rssi.add(value);
        } else {
            rssi.set(c, value);
        }
//        Log.d("beacon", "insert:c= "+c+" rssi.size()= "+rssi.size());
        count++;
    }

    public int get_val() {
        return average();
    }

    private int average() {
        int sum = 0;
        for (int i = 0; i < rssi.size(); i++) {
            sum += rssi.get(i);
        }
        if (rssi.size() < Settings.MOVING_SLIDE_MAX_LENGTH) {
            return sum / rssi.size();
        }
        return sum / Settings.MOVING_SLIDE_MAX_LENGTH;
    }

    @Override
    public String toString() {
        return String.valueOf(average());
    }
}
