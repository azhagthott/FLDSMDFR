package cl.zecovery.android.fldsmdfr.com;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cl.zecovery.android.fldsmdfr.Node.Node;
import cl.zecovery.android.fldsmdfr.Node.NodeAdapter;
import cl.zecovery.android.fldsmdfr.data.DatabaseHandler;

/**
 * Created by fran on 16-09-15.
 */
public class NodeDataRequest {

    private final String LOG_TAG = NodeDataRequest.class.getSimpleName();

    private JSONObject jsonObject;
    private Context mContext;

    public NodeDataRequest() {

    }

    public NodeDataRequest(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void getLocalDataForList(NodeAdapter adapter, Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        adapter.addAll(db.getAllNodes());
        db.close();
    }

    public void getLocalDataForMap(Context context, GoogleMap mMap){
        DatabaseHandler db = new DatabaseHandler(context);
        db.getAllNodes();

        try {

            for(int i = 0; i < db.getAllNodes().size(); i++){
                Node node = db.getAllNodes().get(i);

                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(node.getLat(), node.getLng()))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .title(node.getName())
                );
            }
        db.close();

        }catch (Exception e){
            Log.d(LOG_TAG, "Exception :  " + e);
        }
    }

    public void getDataForListView(NodeAdapter adapter, Context context) {

        adapter.clear();

        DatabaseHandler db = new DatabaseHandler(context);

        try {
            JSONArray contextResponses = jsonObject.getJSONArray("contextResponses");

            for (int i = 0; i < contextResponses.length(); i++){

                Node node = new Node();

                JSONObject contextElement = contextResponses.getJSONObject(i);
                JSONObject element = contextElement.getJSONObject("contextElement");
                JSONArray attributes = element.getJSONArray("attributes");

                if (attributes.length() == 4) {

                    JSONObject pointId = attributes.getJSONObject(0);
                    JSONObject pointName = attributes.getJSONObject(1);
                    JSONObject pointLatitude = attributes.getJSONObject(2);
                    JSONObject pointLongitude = attributes.getJSONObject(3);

                    int id = pointId.getInt("value");
                    String name = pointName.getString("value");
                    double lat = pointLatitude.getDouble("value");
                    double lng = pointLongitude.getDouble("value");

                    node.setId(id);
                    node.setName(name);
                    node.setLat(lat);
                    node.setLng(lng);

                    if(!db.findNode(id)){
                        db.addNode(new Node(id, name, lat, lng));
                    }

                }else {
                    continue;
                }
                adapter.addAll(node);
            }
            db.close();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDataForMap(GoogleMap mMap) {

        try {
            JSONArray contextResponses = jsonObject.getJSONArray("contextResponses");

            for (int i = 0; i < contextResponses.length(); i++){

                Node node = new Node();

                JSONObject contextElement = contextResponses.getJSONObject(i);
                JSONObject element = contextElement.getJSONObject("contextElement");
                JSONArray attributes = element.getJSONArray("attributes");

                if (attributes.length() == 4) {

                    JSONObject pointId = attributes.getJSONObject(0);
                    JSONObject pointName = attributes.getJSONObject(1);
                    JSONObject pointLatitude = attributes.getJSONObject(2);
                    JSONObject pointLongitude = attributes.getJSONObject(3);

                    int id = pointId.getInt("value");
                    String name = pointName.getString("value");
                    double lat = pointLatitude.getDouble("value");
                    double lng = pointLongitude.getDouble("value");

                    node.setId(id);
                    node.setName(name);
                    node.setLat(lat);
                    node.setLng(lng);

                    mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(node.getLat(), node.getLng()))
                                    .flat(true)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                    .title(node.getName())
                    );

                }else {
                    continue;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean isNetworkAvailable (Context context){
        mContext = context;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
