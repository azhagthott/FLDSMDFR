package cl.zecovery.android.fldsmdfr.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

import cl.zecovery.android.fldsmdfr.R;
import cl.zecovery.android.fldsmdfr.Node.Node;
import cl.zecovery.android.fldsmdfr.Node.NodeAdapter;
import cl.zecovery.android.fldsmdfr.Utils.Constants;
import cl.zecovery.android.fldsmdfr.activity.NodeRegisterMapsActivity;
import cl.zecovery.android.fldsmdfr.activity.ReadNodeActivity;
import cl.zecovery.android.fldsmdfr.com.CustomJsonRequest;
import cl.zecovery.android.fldsmdfr.com.NodeDataRequest;
import cl.zecovery.android.fldsmdfr.data.DatabaseHandler;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private NodeAdapter adapter;

    private CustomJsonRequest request;
    private CustomJsonRequest send;
    private NodeDataRequest nodeDataRequest;

    private TextView textViewNodeCount;

    private ListView listViewNode;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        textViewNodeCount = (TextView) rootView.findViewById(R.id.textViewNodeCount);

        nodeDataRequest = new NodeDataRequest();

        ArrayList<Node> arrayOfNode = new ArrayList<>();
        adapter = new NodeAdapter(getActivity().getApplicationContext(), arrayOfNode);

        listViewNode =(ListView) rootView.findViewById(R.id.listViewNode);
        listViewNode.setAdapter(adapter);

        Button btnNodeList = (Button) rootView.findViewById(R.id.btnNodeList);
        Button btnNodeRegister = (Button) rootView.findViewById(R.id.btnNodeRegister);
        Button btnNodeReader = (Button) rootView.findViewById(R.id.btnNodeReader);
        Button btnSynchronize = (Button) rootView.findViewById(R.id.btnSynchronize);

        btnNodeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapter.clear();

                if (nodeDataRequest.isNetworkAvailable(getActivity().getApplicationContext())){

                    request = new CustomJsonRequest(
                            Request.Method.GET,
                            Constants.URL_GET_POINTS,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    nodeDataRequest = new NodeDataRequest(jsonObject);
                                    nodeDataRequest.getDataForListView(adapter, getActivity().getApplicationContext());
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(LOG_TAG, "ERROR:::: " + error);
                                    error.printStackTrace();
                                }
                            });

                    request.setPriority(Request.Priority.LOW);
                    Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);

                }else{
                    nodeDataRequest.getLocalDataForList(adapter, getActivity().getApplicationContext());
                }
            }
        });

        btnNodeRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NodeRegisterMapsActivity.class);
                startActivity(i);
            }
        });

        btnNodeReader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ReadNodeActivity.class);
                startActivity(i);
            }
        });


        btnSynchronize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nodeDataRequest.isNetworkAvailable(getActivity().getApplicationContext())){

                    send = new CustomJsonRequest(Request.Method.POST, Constants.URL_GET_POINTS, null,

                            new Response.Listener() {
                                @Override
                                public void onResponse(Object response) {

                                }
                            },  new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(LOG_TAG, "ERROR:::: " + error);
                                    error.printStackTrace();
                                }
                            });

                }else{
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Imposible sincronizar sin conexion",
                            Toast.LENGTH_LONG);

                }

            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());
        textViewNodeCount.setText(String.valueOf(db.getNodeCount()));

        adapter.clear();

        if(nodeDataRequest.isNetworkAvailable(getActivity().getApplicationContext())){

            request = new CustomJsonRequest(
                    Request.Method.GET,
                    Constants.URL_GET_POINTS,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            nodeDataRequest = new NodeDataRequest(jsonObject);
                            nodeDataRequest.getDataForListView(adapter, getActivity().getApplicationContext());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(LOG_TAG, "ERROR:::: " + error);
                            error.printStackTrace();
                        }
                    });
            request.setPriority(Request.Priority.LOW);
            Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);
        }else{
            nodeDataRequest.getLocalDataForList(adapter, getActivity().getApplicationContext());
        }

        listViewNode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


    }
}