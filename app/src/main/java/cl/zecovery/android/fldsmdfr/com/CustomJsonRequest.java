package cl.zecovery.android.fldsmdfr.com;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fran on 10-09-15.
 */
public class CustomJsonRequest extends JsonObjectRequest{

    public Request.Priority mPriority;

    public CustomJsonRequest(int method,
                             String url,
                             JSONObject jsonRequest,
                             Response.Listener listener,
                             Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public Request.Priority getPriority(){
        return mPriority == null ? Priority.NORMAL : mPriority;
    }

    public void setPriority(Request.Priority priority){
        mPriority = priority;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        return headers;
    }
}
