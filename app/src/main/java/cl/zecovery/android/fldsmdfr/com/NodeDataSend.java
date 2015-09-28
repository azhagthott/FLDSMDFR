package cl.zecovery.android.fldsmdfr.com;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cl.zecovery.android.fldsmdfr.Node.Node;

/**
 * Created by fran on 21-09-15.
 */
public class NodeDataSend extends Node{
    private final String LOG_TAG = NodeDataSend.class.getSimpleName();

    private Node node;
    private JSONObject jsonObject;

    public NodeDataSend() {
    }

    public NodeDataSend(Node node) {
        this.node = node;
    }

    public JSONObject makePostRequest(Node node){

        JSONArray jsonArrayAttributes = new JSONArray();

        try{

            JSONObject jsonObjectBody = new JSONObject();

            jsonObjectBody.put("id", String.valueOf(node.getId()));
            jsonObjectBody.put("type", "Point");
            jsonObjectBody.put("attributes", jsonArrayAttributes );

            JSONObject jsonObjectPointId = new JSONObject();
            JSONObject jsonObjectPointName = new JSONObject();
            JSONObject jsonObjectPointLatitude = new JSONObject();
            JSONObject jsonObjectPointLongitude = new JSONObject();
            JSONObject jsonObjectPointTemperature = new JSONObject();

            jsonObjectPointId.put("name", "id");
            jsonObjectPointId.put("type", "string");
            jsonObjectPointId.put("value", String.valueOf(node.getId()));

            jsonObjectPointName.put("name", "name");
            jsonObjectPointName.put("type", "string");
            jsonObjectPointName.put("value", node.getName()+String.valueOf(node.getId()));

            jsonObjectPointLatitude.put("name", "lat");
            jsonObjectPointLatitude.put("type", "float");
            jsonObjectPointLatitude.put("value", String.valueOf(node.getLat()));

            jsonObjectPointLongitude.put("name", "lng");
            jsonObjectPointLongitude.put("type", "float");
            jsonObjectPointLongitude.put("value", String.valueOf(node.getLng()));

            jsonObjectPointTemperature.put("name", "lng");
            jsonObjectPointTemperature.put("type", "float");
            jsonObjectPointTemperature.put("value", String.valueOf(node.getTemperature()));

            jsonArrayAttributes.put(0, jsonObjectPointId);
            jsonArrayAttributes.put(1, jsonObjectPointName);
            jsonArrayAttributes.put(2, jsonObjectPointLatitude);
            jsonArrayAttributes.put(3, jsonObjectPointLongitude);
            jsonArrayAttributes.put(4, jsonObjectPointTemperature);

            jsonObject.put("", jsonObjectBody);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}



