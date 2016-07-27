package com.example.cntgfy.radiacia.Determinant.Position;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cntgfy.radiacia.SocketAPI.Radiacia.Debug.Debug;

import java.util.Date;

/**
 * Created by Cntgfy on 28.06.2016.
 * Определение долготы и широты с помощью gps и wi-fi
 * Ссыль на исходный код: http://startandroid.ru/ru/uroki/vse-uroki-spiskom/291-urok-138-opredelenie-mestopolozhenija-gps-koordinaty.html
 */
public class DeterminantOfPositionByStartAndroid implements DeterminantOfPosition {

    private LocationManager locationManager;
    private Activity activity;
    private boolean networkEnabled;
    private boolean gpsEnabled;
    private int gpsStatus;
    private int networkStatus;

    //Последние полученные значения
    private Location lastLocation;

    Debug debug = new Debug("DETERMINANT_OF_POSITION", 1000);

    public DeterminantOfPositionByStartAndroid(Activity activity) {
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
    }

    /**
     * @throws RuntimeException если на устройстве нету одного из датчиков
     */
    @Override
    public void onResume() throws RuntimeException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0,
                locationListener);
        checkEnabled();
    }

    @Override
    public void onPause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        locationManager.removeUpdates(locationListener);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            lastLocation = location;
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }
            lastLocation = locationManager.getLastKnownLocation(provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                gpsStatus = status;
                System.out.println("GPS PROVIDER Status: " + String.valueOf(status));
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                networkStatus = status;
                System.out.println("NETWORK PROVIDER Status: " + String.valueOf(status));
            }
        }
    };

    //Выводит информацию о последних переданных координатах от всех провайдеров
    public void showLocation(TextView tvLocationGPS, TextView tvLocationNet) {
        if (lastLocation == null)
            return;
        if (lastLocation.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            tvLocationGPS.setText(formatLocation(lastLocation));
        } else
        if (lastLocation.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            tvLocationNet.setText(formatLocation(lastLocation));
        }
    }

    @Override
    public void showLocation(TextView textView) {
        if (lastLocation == null) return;
        textView.setText(formatLocation(lastLocation));
    }

    @Override
    public double getLatitude() {
        if (lastLocation == null) {
            return 0;
        }
        else {

            return lastLocation.getLatitude();
        }
    }

    @Override
    public double getLongitude() {
        if (lastLocation == null)
            return 0;
        else
            return lastLocation.getLongitude();
    }

    @Override
    public float getAccuracy() {
        if (lastLocation == null)
            return 0;
        else
            return lastLocation.getAccuracy();
    }

    public Location getLocation() {
        return lastLocation;
    }

    @Override
    public boolean isProviderEnabled() {
        boolean isProviderEnabled = false;

        if ( locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)     ) isProviderEnabled = true;
        if ( locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER) ) isProviderEnabled = true;

        return isProviderEnabled;
    }

    public boolean isGPSEnabled() {
        return gpsEnabled;
    }

    public boolean isNetEnabled() {
        return networkEnabled;
    }

    public int netStatus() {
        return networkStatus;
    }

    public int gpsStatus() {
        return gpsStatus;
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.5f, lon = %2$.5f, accuracy= %3$.1f, time = %4$tF %4$tT",
                location.getLatitude(), location.getLongitude(), location.getAccuracy(), new Date(
                        location.getTime()));
    }

    private void checkEnabled() {
        if (gpsEnabled != locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            gpsEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            System.out.println("GPS PROVIDER Enabled: " + gpsEnabled);
        }

        if (networkEnabled != locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)) {
            networkEnabled = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);
            System.out.println("NETWORK PROVIDER Enabled: " + networkEnabled);
        }
    }
}
