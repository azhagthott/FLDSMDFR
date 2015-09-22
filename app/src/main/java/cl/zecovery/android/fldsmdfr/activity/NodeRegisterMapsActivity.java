package cl.zecovery.android.fldsmdfr.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import cl.zecovery.android.fldsmdfr.Node.Node;
import cl.zecovery.android.fldsmdfr.R;
import cl.zecovery.android.fldsmdfr.Utils.Constants;
import cl.zecovery.android.fldsmdfr.com.CustomJsonRequest;
import cl.zecovery.android.fldsmdfr.com.NodeDataRequest;
import cl.zecovery.android.fldsmdfr.data.DatabaseHandler;

public class NodeRegisterMapsActivity
        extends AppCompatActivity
        implements OnMapReadyCallback, LocationListener {

    private static final String LOG_TAG = NodeRegisterMapsActivity.class.getName();

    private LocationManager locationManager;
    private String provider;

    private GoogleMap mMap;
    private LatLng mLocation;

    private CustomJsonRequest request;
    private NodeDataRequest nodeDataRequest;
    private Node node;

    private TextView textViewLat;
    private TextView textViewLng;

    private Button btnViewModeSatellite;
    private Button btnViewModeTerrain;
    private Button btnViewModeNormal;
    private Button btnSaveNode;
    private Button btnViewNode;

    private int nodeCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_register_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        node = new Node();
        nodeDataRequest = new NodeDataRequest();
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

        textViewLat = (TextView) findViewById(R.id.textViewLat);
        textViewLng = (TextView) findViewById(R.id.textViewLng);

        btnViewNode = (Button) findViewById(R.id.btnViewNode);
        btnSaveNode = (Button) findViewById(R.id.btnSaveNode);
        btnViewModeSatellite = (Button) findViewById(R.id.btnViewModeSatellite);
        btnViewModeTerrain = (Button) findViewById(R.id.btnViewModeTerrain);
        btnViewModeNormal = (Button) findViewById(R.id.btnViewModeNormal);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mLocation = new LatLng(node.getLat(), node.getLng());

        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        nodeCount = db.getNodeCount();

        // UI Config
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        int mapType = sharedPreferences.getInt("mapType", 0);

        Log.d(LOG_TAG, "mapType:  " + mapType);

        // Camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(node.getLat(), node.getLng()), 14.0f));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {

                Date date = new Date();
                int unique = date.hashCode();

                node = new Node(unique, "Point" + unique , latLng.latitude, latLng.longitude);
                db.addNode(node);
                db.close();

                textViewLat.setText("LAT: " + String.valueOf(latLng.latitude));
                textViewLng.setText("LNG: " + String.valueOf(latLng.longitude));

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                        .title(Constants.NODO_NO_GUARDADO)
                );
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                textViewLat.setText("LAT: " + String.valueOf(latLng.latitude));
                textViewLng.setText("LNG: " + String.valueOf(latLng.longitude));
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                marker.showInfoWindow();
                if (marker.getTitle().equals(Constants.NODO_NO_GUARDADO)) {
                    marker.remove();
                } else {
                    marker.showInfoWindow();
                }
                //FIXME: WTF???
                return true;
            }
        });

        if (nodeDataRequest.isNetworkAvailable(getApplicationContext())){

            request = new CustomJsonRequest(
                    Request.Method.GET,
                    Constants.URL_GET_POINTS,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            nodeDataRequest = new NodeDataRequest(jsonObject);
                            nodeDataRequest.getDataForMap(mMap);
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(LOG_TAG, "ERROR:::: " + error);
                            error.printStackTrace();
                        }
                    });

            request.setPriority(Request.Priority.HIGH);
            Volley.newRequestQueue(getApplicationContext()).add(request);
        }else{
            nodeDataRequest.getLocalDataForMap(getApplicationContext(),mMap);
        }

        btnViewNode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nodeDataRequest.isNetworkAvailable(getApplicationContext())){
                    request = new CustomJsonRequest(
                            Request.Method.GET,
                            Constants.URL_GET_POINTS,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    nodeDataRequest = new NodeDataRequest(jsonObject);
                                    nodeDataRequest.getDataForMap(mMap);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(LOG_TAG, "ERROR:::: " + error);
                                    error.printStackTrace();
                                }
                            });

                    request.setPriority(Request.Priority.HIGH);
                    Volley.newRequestQueue(getApplicationContext()).add(request);
                }else{
                    nodeDataRequest.getLocalDataForMap(getApplicationContext(),mMap);
                }
            }
        });

        btnViewModeSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        btnViewModeTerrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });

        btnViewModeNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        node.setLat(location.getLatitude());
        node.setLng(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
