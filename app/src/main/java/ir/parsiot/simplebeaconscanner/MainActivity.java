package ir.parsiot.simplebeaconscanner;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import ir.parsiot.simplebeaconscanner.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //listview which shows discovered Bluetooth Devices:
        listView = findViewById(R.id.lv_devices);

        //discoveredDevices is an array which has found bluetooth devices 'name' and 'mac address'.
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
        for (String layout : Settings.VALID_LAYOUTS){
            beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(layout));
        }
        beaconManager.bind(this);
        //set between scan period
        beaconManager.setForegroundBetweenScanPeriod(Settings.PERIOD_TIME_BETWEEN);

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

        } catch (RemoteException e) {
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //stop discover Beacons In Region
    void startMonitoring() {

        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //covert collection<beacon> to beaconList<beacon> for access to beacons
                    List<Beacon> beaconList = new ArrayList<Beacon>(beacons);

                    if (beaconList.size() > 0) { // Avoid empty beacon list
                        for (Beacon beacon : beaconList) {
                            boolean flag = false;

                            for (BLEdevice bleDevice : discoveredDevices) {
                                String aID = beacon.getBluetoothAddress();
                                String bID = bleDevice.getMac();
                                if (aID.equals(bID)) { // Check the beacon mac address
                                    bleDevice.setRss(beacon.getRssi());
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                BLEdevice bleDevice = new BLEdevice();
                                bleDevice.setUUID(beacon.getId1().toString());
                                bleDevice.setMajor(beacon.getId2().toString());
                                bleDevice.setMinor(beacon.getId3().toString());
                                bleDevice.setMac(beacon.getBluetoothAddress());
                                bleDevice.setRss(beacon.getRssi());
                                discoveredDevices.add(bleDevice);

                                Log.d("Beacon:", bleDevice.toString());
                            }
                        }
                        sortDiscoveredDevices();
                        discoveredDevicesAdapter.notifyDataSetChanged();
                    }
                }
            }

        };

        try {
            //Set available beacon UUIDs of beacons
            beaconManager.startRangingBeaconsInRegion(new Region("BeaconScanner", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {
            Log.e("StartingScanProblem", e.getMessage());
            e.printStackTrace();
        }
    }

    private void sortDiscoveredDevices() {
        for (int i = 0; i < discoveredDevices.size(); i++) {
            for (int j = i; j < discoveredDevices.size(); j++) {
                if (discoveredDevices.get(j).getRss() > discoveredDevices.get(i).getRss()) {
                    BLEdevice a = discoveredDevices.get(i);
                    discoveredDevices.set(i, discoveredDevices.get(j));
                    discoveredDevices.set(j, a);
                }
            }
        }
    }

    // Stop discovering Beacons In Region
    void stopMonitoring() {
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("BeaconScanner", null, null, null));
            beaconManager.stopRangingBeaconsInRegion(beaconRegion);
        } catch (RemoteException e) {
            Log.e("StoppingScanProblem", e.getMessage());
            e.printStackTrace();
        }
    }
}