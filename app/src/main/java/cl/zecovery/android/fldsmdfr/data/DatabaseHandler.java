package cl.zecovery.android.fldsmdfr.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cl.zecovery.android.fldsmdfr.Node.Node;
import cl.zecovery.android.fldsmdfr.Node.NodeCrud;
import cl.zecovery.android.fldsmdfr.Utils.Constants;

/**
 * Created by fran on 15-09-15.
 */
public class DatabaseHandler extends SQLiteOpenHelper implements NodeCrud{

    private final String LOG_TAG = DatabaseHandler.class.getSimpleName();

    public DatabaseHandler(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_NODES =
                "CREATE TABLE " + Constants.TABLE_NODES
                        + "("
                        + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                        + Constants.KEY_NAME + " TEXT,"
                        + Constants.KEY_LAT + " TEXT,"
                        + Constants.KEY_LNG + " TEXT"
                        + ");";

        db.execSQL(CREATE_TABLE_NODES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(Constants.DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void addNode(Node node) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();

            values.put(Constants.KEY_ID, node.getId());
            values.put(Constants.KEY_NAME, node.getName());
            values.put(Constants.KEY_LAT, node.getLat());
            values.put(Constants.KEY_LNG, node.getLng());

            db.insert(Constants.TABLE_NODES, null, values);
            db.close();

        }catch (Exception e){
            Log.d(LOG_TAG, "Exception: " + e);
        }
    }

    @Override
    public Node getNode(int nodeId) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(
                Constants.TABLE_NODES,

                new String[] {
                        Constants.KEY_NAME,
                        Constants.KEY_LAT,
                        Constants.KEY_LNG
                    },
                Constants.KEY_ID + "=?",

                new String[] {String.valueOf(nodeId)},

                null,
                null,
                null
        );

        if(cursor!=null)
            cursor.moveToFirst();

        return new Node(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getDouble(2),
                cursor.getDouble(3)
        );
    }

    @Override
    public List<Node> getAllNodes() {

        List<Node> nodeList = new ArrayList<>();

        String selectAllQuery = Constants.SELECT_ALL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAllQuery, null);

        if(cursor.moveToFirst()){
            do {Node node = new Node();
            node.setId(Integer.parseInt(cursor.getString(0)));
            node.setName(cursor.getString(1));
            node.setLat(Double.parseDouble(cursor.getString(2)));
            node.setLng(Double.parseDouble(cursor.getString(3)));
            nodeList.add(node);
            }
            while (cursor.moveToNext());
        }
        return nodeList;
    }

    @Override
    public int getNodeCount(int result) {

        String countQuery = Constants.SELECT_ALL;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }


    @Override
    public int updateNode(Node node) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.KEY_NAME, node.getName());
        values.put(Constants.KEY_LAT, node.getLat());
        values.put(Constants.KEY_LNG, node.getLng());

        int i = db.update(Constants.TABLE_NODES, values, Constants.KEY_ID + "=?", new String[]{
                String.valueOf(node.getId())
        });

        return i;
    }

    @Override
    public void deleteNode(Node node) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NODES, Constants.KEY_ID + "=?", new String[]{
           String.valueOf(node.getId())
        });
        db.close();
    }
}
