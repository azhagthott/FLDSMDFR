package cl.zecovery.android.fldsmdfr.Node;

import com.google.android.gms.maps.model.LatLng;


/**
 * Created by fran on 14-09-15.
 */
public class NodeCluster{

    private final LatLng mPosition;

    public NodeCluster(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }


}
