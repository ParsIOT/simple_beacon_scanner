package com.example.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyStructure {
    private  int MAX_STRUCTURE_SIZE =6;
    private List<Integer> rssi=new ArrayList<>();
    private int count =0;

    public MyStructure(List<Integer> rssi) {
        this.rssi = rssi;
        count =0;
    }
    public MyStructure(int MAX_STRUCTURE_SIZE){
        this.MAX_STRUCTURE_SIZE =MAX_STRUCTURE_SIZE;
        count =0;
    }
    public void insert (int value){
            int c =count% MAX_STRUCTURE_SIZE;
        if(count<MAX_STRUCTURE_SIZE){
            rssi.add(value);
        }else {
            rssi.set(c,value);
        }

//        Log.e("beacon", "insert:c= "+c+" rssi.size()= "+rssi.size());

        count++;
    }
    public int average(){
        int sum=0;
        for(int i=0;i<rssi.size();i++){
            sum +=rssi.get(i);
        }
        if(rssi.size()<MAX_STRUCTURE_SIZE){
            return sum/rssi.size();
        }
        return sum/MAX_STRUCTURE_SIZE;
    }


    @Override
    public String toString() {
        return String.valueOf(average());
    }
}
