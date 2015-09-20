package cl.zecovery.android.fldsmdfr.activity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import cl.zecovery.android.fldsmdfr.Node.Node;
import cl.zecovery.android.fldsmdfr.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final String LOG_TAG = MapsActivity.class.getName();

    private LocationManager locationManager;
    private Node node;
    private String provider;
    private GoogleMap mMap;
    public LatLng mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        node = new Node();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.d(LOG_TAG, "Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Log.d(LOG_TAG, "No provider has been selected.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mLocation = new LatLng(node.getLat(), node.getLng());

        mMap.setMyLocationEnabled(true);

        // UI Config
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(node.getLat(), node.getLng()), 14.0f));

        // Marker
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_lime_2))
                .position(mLocation)
                .title("COORD: ")
                        .snippet("Lat:" + node.getLat() + ", Lng:" + node.getLng())
        );
    }

    @Override
    public void onLocationChanged(Location location) {

        node.setLat(location.getLatitude());
        node.setLng(location.getLongitude());
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
}
