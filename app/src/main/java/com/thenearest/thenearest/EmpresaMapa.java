package com.thenearest.thenearest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class EmpresaMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Bundle b;

    Location location;
    LocationManager locationManager;
    LocationListener locationListener;
    AlertDialog alert = null;

    Double myLatitud = null;
    Double myLongitud = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empresa_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void AlertNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(alert != null)
        {
            alert.dismiss ();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            } else {
                locationManager.removeUpdates(locationListener);
            }
        } else {
            locationManager.removeUpdates(locationListener);
        }*/


    }
    @Override
    protected void onResume() {
        super.onResume();
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }*/



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        b = getIntent().getExtras();
        double latitud = b.getDouble("latitud");
        double longitud = b.getDouble("longitud");
        String nombre = b.getString("nombre");

        final Marker[] miUbicacion = {null};

        // Add a marker in Sydney and move the camera
        LatLng punto = new LatLng(latitud,longitud);
        mMap.addMarker(new MarkerOptions().position(punto).title(nombre));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(punto));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        /****Mejora****/
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            AlertNoGps();
        }
        /********/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } else {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if(location != null) {
            System.out.println("locacionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn 1" + location.getLatitude() + "sd" + location.getLongitude());

            LatLng myLatLong = new LatLng(location.getLatitude(), location.getLongitude());

            if(miUbicacion[0] ==null) {
                miUbicacion[0] = mMap.addMarker(new MarkerOptions().position(myLatLong).title("Mi Ubicacion"));
            }else{
                miUbicacion[0].remove();
                miUbicacion[0] = mMap.addMarker(new MarkerOptions().position(myLatLong).title("Mi Ubicacion"));
            }

        }
        else{
            System.out.println("nooooooooooooooooooooooooooooooooooooooooooooo 1");
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(location != null) {
                    LatLng myLatLong = new LatLng(location.getLatitude(), location.getLongitude());
                    System.out.println("locacionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn 2" + location.getLatitude() + "sd" + location.getLongitude());
                    if(miUbicacion[0] ==null) {
                        miUbicacion[0] = mMap.addMarker(new MarkerOptions().position(myLatLong).title("Mi Ubicacion"));
                    }else{
                        miUbicacion[0].remove();
                        miUbicacion[0] = mMap.addMarker(new MarkerOptions().position(myLatLong).title("Mi Ubicacion"));
                    }
                }else{
                    System.out.println("nooooooooooooooooooooooooooooooooooooooooooooo 2");
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

    }
}
