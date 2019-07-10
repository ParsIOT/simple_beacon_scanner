package com.example.myapplication;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {




    private BeaconManager beaconManager = null;
    private Region beaconRegion;

    private ArrayAdapter<BLEdevice> discoveredDevicesAdapter;
    private ArrayList<BLEdevice> discoveredDevices;

    private ListView listView;
    private Button scanButton;
    private TextView textView;
    private  BLEdevice mBle= new BLEdevice();;
    private  MyStructure myStructure = new MyStructure(6);

    private  static  final String ALTBEACON_LAYOUT="m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";
    private static  final  long PERIOD_TIME_BETWEEN = 150l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //list view which shows discovered Bluetooth Devices:
        listView = findViewById(R.id.lv_devices);
        //discoveredDevices is a array which has found bluetooth devices 'name' and 'mac address'.
        discoveredDevices = new ArrayList<>();
        discoveredDevicesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, discoveredDevices);
        listView.setAdapter(discoveredDevicesAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                12);
                    }

                });
                builder.show();
            }
        }
        //setting of beacons Manager
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(ALTBEACON_LAYOUT));
        beaconManager.bind(this);
        //set between scan period
        beaconManager.setForegroundBetweenScanPeriod(PERIOD_TIME_BETWEEN);

        //views
        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMonitoring();
            }
        });


        Button stop = findViewById(R.id.stopButton);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopMonitoring();
            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 12: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("tagRequest", "coarse location permission granted");
                } else {

                }
                return;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {



        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));

            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));

        } catch (RemoteException e) {   }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //stop discover Beacons In Region
    void startMonitoring(){

        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {

                   // Log.e("beacon", "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());


                    //covert collection<beacon> to list<beacon> for access to beacons
                    List<Beacon> list = new ArrayList<Beacon>(beacons);

                    if(list.size()>0){

                     for(int i=0;i<list.size();i++){
                         Beacon beacon = list.get(i);
                         boolean flag =false;

                         for(int j=0;j<discoveredDevices.size();j++){
                             Integer a = beacon.getId3().toInt();
                             Integer b =Integer.valueOf(discoveredDevices.get(j).getMinor());

                             if(a.equals(b)
                                    ){
                                 discoveredDevices.get(j).setRss(beacon.getRssi());

                                 Log.e("beacon","minor: "+discoveredDevices.get(j).getMinor()+" add rssi="+ beacon.getRssi());
                                 flag =true;



                             }
                         }

                         if(!flag){


                             Log.e("beacon","add minor="+ beacon.getId3().toString());
                             BLEdevice blEdevice = new BLEdevice();
                             blEdevice.setUUID(beacon.getId1().toString());
                             blEdevice.setMajor(beacon.getId2().toString());
                             blEdevice.setMinor(beacon.getId3().toString());
                             blEdevice.setRss(beacon.getRssi());

                             discoveredDevices.add(blEdevice);



                         }



                     }
                        sortDiscoveredDevices();
                        discoveredDevicesAdapter.notifyDataSetChanged();




                    }
                }
            }

        };


        try {

            //set uuid of beacons and their major for better discovering
            beaconRegion = new Region("beacon",Identifier.parse("23a01af0-232a-4518-9c0e-323fb773f5ef"),Identifier.parse("1"),null);
            beaconManager.startRangingBeaconsInRegion(beaconRegion);
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(beaconRegion);
            beaconManager.addRangeNotifier(rangeNotifier);

        } catch (RemoteException e) {    }



    }

    private void sortDiscoveredDevices() {

        for(int i=0;i<discoveredDevices.size();i++){
            for(int j=i;j<discoveredDevices.size();j++){
                if(discoveredDevices.get(j).getRss()>discoveredDevices.get(i).getRss()){
                    BLEdevice a = discoveredDevices.get(i);
                    discoveredDevices.set(i,discoveredDevices.get(j));
                    discoveredDevices.set(j,a);
                }
            }

        }
    }


    //stop discovering Beacons In Region
    void stopMonitoring(){


        try {

            beaconRegion = new Region("beacon",Identifier.parse("23a01af0-232a-4518-9c0e-323fb773f5ef"),Identifier.parse("1"),null);
            beaconManager.stopRangingBeaconsInRegion(beaconRegion);

            beaconManager.stopRangingBeaconsInRegion(beaconRegion);

        } catch (RemoteException e) {    }



    }
}