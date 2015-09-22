package cl.zecovery.android.fldsmdfr.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.zxing.Result;

import cl.zecovery.android.fldsmdfr.Node.Node;
import cl.zecovery.android.fldsmdfr.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReadNodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final String LOG_TAG = ReadNodeActivity.class.getName();

    // {"id":"point1","data":[{"id":"1","name":"point1","lat":"-33.50283562","lng":"-70.65061558",}]}

    private TextView textViewQrResult;
    private TextView textViewQrLatitude;
    private TextView textViewQrLongitude;
    private ZXingScannerView qrScanner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_node);

        textViewQrResult = (TextView) findViewById(R.id.textViewQrResult);
        textViewQrLatitude = (TextView) findViewById(R.id.textViewQrLatitude);
        textViewQrLongitude = (TextView) findViewById(R.id.textViewQrLongitude);
        qrScanner = (ZXingScannerView) findViewById(R.id.qrScanner);
    }

    @Override
    public void onResume() {
        super.onResume();
        qrScanner.setResultHandler(this);
        qrScanner.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        qrScanner.stopCamera();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void handleResult(Result rawResult) {

        try {

            if(rawResult.getText()!= null){
                Node node = new Node();
                node.setName(rawResult.getText());
                textViewQrResult.setText(rawResult.getText());
            }

        }catch (Exception e){
            Log.v(LOG_TAG, e.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        /*
        if (id == R.id.action_map) {
            Intent i = new Intent(ReadNodeActivity.this, MapsActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_settings) {
            Intent i = new Intent(ReadNodeActivity.this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

}
