package cl.zecovery.android.fldsmdfr.Node;

import java.util.UUID;

/**
 * Created by fran on 13-09-15.
 */
public class Node{

    int id;
    public String name;
    public double lat;
    public double lng;

    public Node() {
    }

    public Node(int id) {
        this.id = id;
    }

    public Node(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public Node(int id, String name, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


}
